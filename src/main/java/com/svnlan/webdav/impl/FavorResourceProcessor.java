package com.svnlan.webdav.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ithit.webdav.server.exceptions.ServerException;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.dao.UserFavDao;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service("favorResourceProcessor")
public class FavorResourceProcessor extends AbstractResourceProcessor {

    @Resource
    private UserFavDao userFavDao;

    @Resource
    private IoSourceDao ioSourceDao;

    @Resource
    private IoFileDao ioFileDao;

    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    private static final Integer perMaxQuery = 500;


    List<JSONObject> getIoSourceVoList(Long userId, Long rootId) {
        List<JSONObject> jsonObjList = userFavDao.selectFavorSourceId(WebDavEngine.userVoThreadLocal.get().getUserID(), null);
        log.info("getIoSourceVoList jsonObjList => {}", jsonObjList);
        Map<Long, String> sourceIdNameMap = jsonObjList.stream().collect(Collectors.toMap(it -> it.getLong("sourceId"), it -> it.getString("name")));
        List<JSONObject> resultJsonObjectList = querySourceBySourceIds(new ArrayList<>(sourceIdNameMap.keySet()));
        // 替换收藏夹里对应的名称
        for (JSONObject item : resultJsonObjectList) {
            Optional.ofNullable(sourceIdNameMap.get(item.getLong("sourceID")))
                    .ifPresent(it -> item.put("name", it));
        }
        log.info("getIoSourceVoList resultJsonObjectList => {}", resultJsonObjectList);
        return resultJsonObjectList;
    }

    private List<JSONObject> querySourceBySourceIds(List<Long> sourceIdList) {
        if (CollectionUtils.isEmpty(sourceIdList)) {
            return Collections.emptyList();
        }
// s.sourceID, s.type, s.isFolder, s.name, s.parentLevel, s.fileID, i.path
        List<JSONObject> resultList = new ArrayList<>();

        Map<String, IOSource> resolvingMap = new HashMap<>();
        if (sourceIdList.size() <= perMaxQuery) {
            List<IOSource> ioSources = ioSourceDao.selectList(
                    new LambdaQueryWrapper<IOSource>()
                            .select(IOSource::getSourceID, IOSource::getType,
                                    IOSource::getIsFolder, IOSource::getName,
                                    IOSource::getParentLevel, IOSource::getFileID)
                            .in(IOSource::getSourceID, sourceIdList)
            );
            Map<String, IOSourceVo> stringIoSourceMap = WebDavEngine.favorRootSourceMap.get();
            if (MapUtils.isEmpty(stringIoSourceMap)) {
                stringIoSourceMap = new HashMap<>();
                WebDavEngine.favorRootSourceMap.set(stringIoSourceMap);
            }
            // 找出是文件的资源
            List<Long> fileIdList = new ArrayList<>();
            for (IOSource ioSource : ioSources) {
//                stringLongMap.put(ioSource.getName(), ioSource);
                // 这里有可能有同名的资源， 但是在不同的文件夹中
                IOSourceVo cachedIoSourceVo = stringIoSourceMap.get(ioSource.getName());
                if (Objects.nonNull(cachedIoSourceVo)) {
                    stringIoSourceMap.remove(ioSource.getName());
                    stringIoSourceMap.put(cachedIoSourceVo.getParentLevel() + "_" + ioSource.getName(), cachedIoSourceVo);
                    stringIoSourceMap.put(ioSource.getParentLevel() + "_" + ioSource.getName(), new IOSourceVo().copyFromIoSource(ioSource));
                }
                if (Objects.equals(ioSource.getIsFolder(), 0)) {
                    fileIdList.add(ioSource.getFileID());
                }
            }

            // 将 ioFileList 转化为 key 为 fileId value 为 path 的 map
            Map<Long, String> filePathMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(fileIdList)) {
                // 查询文件资源对应的 path 即磁盘上的绝对路径
                List<IOFile> ioFileList = ioFileDao.selectList(
                        new LambdaQueryWrapper<IOFile>()
                                .select(IOFile::getPath, IOFile::getFileID)
                                .in(IOFile::getFileID, fileIdList)
                );
                for (IOFile ioFile : ioFileList) {
                    filePathMap.put(ioFile.getFileID(), ioFile.getPath());
                }
            }

            // 将 ioSources 转化为 jsonObject
            for (IOSource ioSource : ioSources) {
                IOSource resolvedIoSource = resolvingMap.get(ioSource.getName());
                if (Objects.nonNull(resolvedIoSource)) {
                    synchronized (this) {
                        IOSource resolvedIoSource2 = resolvingMap.get(ioSource.getName());
                        if (Objects.nonNull(resolvedIoSource2)) {
                            // 表示出现了同名的资源
                            resultList.stream()
                                    .filter(it -> Objects.equals(it.getString("name"), resolvedIoSource2.getName()))
                                    .findFirst()
                                    .ifPresent(it -> it.fluentPut("name", it.getString("parentLevel") + "_" + it.getString("name")));
                            resolvingMap.remove(ioSource.getName());
                        }
                    }
                }

                JSONObject jsonObject = new JSONObject().fluentPut("sourceID", ioSource.getSourceID())
                        .fluentPut("type", ioSource.getType())
                        .fluentPut("isFolder", ioSource.getIsFolder())
                        .fluentPut("name", ioSource.getName())
                        .fluentPut("parentLevel", ioSource.getParentLevel())
                        .fluentPut("fileID", ioSource.getFileID());
                // 如果是文件资源，则加入 path
                if (Objects.equals(ioSource.getIsFolder(), 0)) {
                    jsonObject.fluentPut("path", filePathMap.get(ioSource.getFileID()));
                }
                resultList.add(jsonObject);
            }
        } else {

            // 如果 sourceIdList 过大，则需要切分，分批次查询
            List<Future<List<JSONObject>>> futureList = new ArrayList<>();
            int start = 0;
            do {
                // 切分集合
                List<Long> currentSourceIds = sourceIdList.subList(start, perMaxQuery);
                // 提交给线程池执行
                Future<List<JSONObject>> future = asyncTaskExecutor.submit(() -> querySourceBySourceIds(currentSourceIds));
                // 加入到 future 集合中
                futureList.add(future);
                start += perMaxQuery;
            } while (start <= sourceIdList.size());
            // 任务全部提交后，需要从 future 集合中获取执行结果
            deriveFromFuture(futureList, resultList, 1);
        }
        return resultList;
    }

    /**
     * 从 future 集合中获取执行结果
     */
    @SneakyThrows
    private void deriveFromFuture(List<Future<List<JSONObject>>> futureList, List<JSONObject> resultList, int deriveCount) {
        List<Future<List<JSONObject>>> unCompletedFutureList = new ArrayList<>();
        for (Future<List<JSONObject>> future : futureList) {
            try {
                // 最多等待 2 秒 获取结果
                List<JSONObject> jsonObjectList = future.get(2, TimeUnit.SECONDS);
                resultList.addAll(jsonObjectList);
            } catch (TimeoutException e) {
                // 超时还未完成的，先存起来
                unCompletedFutureList.add(future);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServerException("从 future 中获取 jsonObjectList 失败");
            }

        }
        if (!CollectionUtils.isEmpty(unCompletedFutureList)) {
            if (deriveCount == 5) {
                // 最多 5次，防止无限递归下去导致栈溢出，5次都还没有获取到结果，肯定是代码或者环境有问题
                throw new ServerException("第 " + deriveCount + " 次获取 future 执行结果，请检查代码");
            }
            // 将未获取到结果的 future 集合 重新尝试获取执行结果
            deriveFromFuture(unCompletedFutureList, resultList, ++deriveCount);
        }
    }

}















