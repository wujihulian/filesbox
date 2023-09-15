package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.svnlan.enums.UserMetaEnum;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceEventDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.*;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.dao.CommonConvertDao;
import com.svnlan.user.service.IoSourceService;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.ChinesUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.svnlan.utils.VisitRecordExecutor.convertAndPopulateNullData;

/**
 * 资源服务 实现
 *
 * @author lingxu 2023/04/10 15:26
 */
@Slf4j
@Service
public class IoSourceServiceImpl extends ServiceImpl<IoSourceDao, IOSource> implements IoSourceService {

    @Resource
    private IoSourceDao sourceDao;

    @Resource
    private IoSourceEventDao sourceEventDao;

    @Resource
    private IoSourceMetaDao sourceMetaDao;

    @Resource
    private CommonConvertDao commonConvertDao;
    @Resource
    StorageService storageService;

    /**
     * 文件信息概览
     */
    @Override
    public JSONObject fileInfoOverview() {
        // 文件数 目录数 视频数
        List<JSONObject> list = sourceDao.fileCount();
        // 当前剩余转码异常数
        Long exceptionCount = commonConvertDao.queryConvertExceptionCountInstant();
        // 计算14天前的时间戳和前一天的时间戳
        Integer daysBefore = 14;
        Pair<Long, Long> timeRange = calculateTimestampRange(daysBefore);
        // 文件操作数
        List<JSONObject> operateFileCountList = sourceEventDao.queryFileOperateCount(timeRange);
        // 视频操作数
        List<JSONObject> operateVideoFileCountList = sourceEventDao.queryVideoFileOperateCount(timeRange);

        JSONObject jsonObject = new JSONObject();
        for (JSONObject item : list) {
            jsonObject.fluentPut(item.getString("type"), item.get("count"));
        }
        return jsonObject
                .fluentPut("errorCount", exceptionCount)
                .fluentPut("fileOperateCount", convertAndPopulateNullData(operateFileCountList, daysBefore))
                .fluentPut("videoOperateCount", convertAndPopulateNullData(operateVideoFileCountList, daysBefore));
    }

    @Override
    public List<JSONObject> getFileTypeProportion() {
        return sourceDao.getFileTypeProportion();
    }

    @Override
    public IOSourceVo getUserRootDirectory(Long userId) {
        IOSourceVo rootSourceVo = CaffeineUtil.USER_ROOT_DIRECTORY_CACHE.getIfPresent(userId);
        if (Objects.isNull(rootSourceVo)) {
            synchronized (IoSourceServiceImpl.class) {
                rootSourceVo = CaffeineUtil.USER_ROOT_DIRECTORY_CACHE.getIfPresent(userId);
                if (Objects.isNull(rootSourceVo)) {
                    rootSourceVo = sourceDao.getUserRootDirectory(userId);
                    Assert.notNull(rootSourceVo, "没有根目录，请联系管理员");
                    CaffeineUtil.USER_ROOT_DIRECTORY_CACHE.put(userId, rootSourceVo);
                }
            }
        }
        return rootSourceVo;
    }

    /**
     * ",0,586,11340,11592,",11591
     * ",0,1,60296,",90580
     * ",0,586,",10630
     *
     * @param pathList
     * @param userId
     * @param pairList
     * @param rootId
     * @param isFolder
     * @return
     */
    @Override
    public List<Long> getSourceIdByNameAndUserId(List<String> pathList, Long userId, List<Pair<IOSourceVo, List<Long>>> pairList, Long rootId, int isFolder) {
        String lastPathName = pathList.get(pathList.size() - 2);
        List<IOSourceVo> sourceVo = sourceDao.getSourceByNameAndUserId(lastPathName, userId, isFolder);
        sourceVo = sourceVo.stream()
                .filter(it -> it.getParentLevel().chars().filter(c -> c == ',').count() - 2 - 1 == pathList.size() - 2)
                .filter(it -> it.getParentLevel().startsWith(",0," + rootId + ","))
                .collect(Collectors.toList());
        if (sourceVo.size() == 1) {
            return Collections.singletonList(sourceVo.get(0).getSourceID());
        }
        List<Long> result = new ArrayList<>();
        for (Pair<IOSourceVo, List<Long>> item : pairList) {
            IOSourceVo itemSource = item.getFirst();
            for (IOSourceVo ioSourceVo : sourceVo) {
                if (itemSource.getParentLevel().equals(ioSourceVo.getParentLevel() + ioSourceVo.getSourceID() + ",")) {
                    result.add(itemSource.getSourceID());
                    break;
                }
            }
        }
        return result;
    }


    /**
     * 计算 daysBefore 天前的时间戳和前一天的时间戳
     */
    private Pair<Long, Long> calculateTimestampRange(Integer daysBefore) {
        LocalDateTime now = LocalDateTime.now();
        // 前一天23点59分59秒
        LocalDateTime endDateTime = now.minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(0);
        // daysBefore 前的时间
        LocalDateTime startDateTime = now.minusDays(14).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Pair.of(startDateTime.toEpochSecond(ZoneOffset.ofHours(8)), endDateTime.toEpochSecond(ZoneOffset.ofHours(8)));
    }

    @Override
    public List<JSONObject> getFileTypeProportionByUserId(Long userID) {
        return sourceDao.getFileTypeProportionByUserId(userID);
    }

    @Override
    public IOFile getFileContentByNameAndUserId(String fileName, Long userId, Boolean isVideoFile) {
        IOFile ioFile = sourceDao.getFileContentByNameAndUserId(fileName, userId, isVideoFile);
        if (Objects.isNull(ioFile)) {
            return null;
        }
        // 是否需要转码
        Integer isM3u8 = ioFile.getIsM3u8();
        // zhy-do 视频文件 doc ppt 相关文件的处理

        return ioFile;
    }

    public static final Pattern pattern = Pattern.compile("^(?<baseName>\\w+)(?<serial>\\(\\d+\\))?$");

    @Override
    public String deriveProperName(String parentLevel, String lastPath) {
        Matcher mat = pattern.matcher(lastPath);
        String baseName = lastPath;
        if (mat.find()) {
            baseName = mat.group("baseName");
        }
        List<String> nameList = sourceDao.getDirectoryByParentLevelAndName(parentLevel, lastPath);
        if (CollectionUtils.isEmpty(nameList)) {
            return baseName;
        }
        Integer currentMaxSerial = nameList.stream().map(it -> {
            Matcher matcher = pattern.matcher(it);
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group("serial"));
            }
            return null;
        }).filter(Objects::nonNull).max(Integer::compareTo).orElse(0);
        return String.format("%s(%d)", baseName, currentMaxSerial + 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createDirectory(Long userId, String name, String parentLevel, Long parentId) {
        // 写入 io_source 表
        IOSource ioSourceInsert = new IOSource(userId, 1, name, parentId, parentLevel);
        // 用户级别 1   群组  2
        ioSourceInsert.setTargetType(1);

        ioSourceInsert.setNamePinyin(ChinesUtil.getPingYin(ioSourceInsert.getName()));
        ioSourceInsert.setNamePinyinSimple(ChinesUtil.getFirstSpell(ioSourceInsert.getName()));
        ioSourceInsert.setStorageID(storageService.getDefaultStorageDeviceId());
        Assert.isTrue(Objects.equals(sourceDao.insert(ioSourceInsert), 1), "保存 io_source 数据失败");
        // 写入 io_source_meta 表
        ArrayList<IOSourceMeta> ioSourceMetas = Lists.newArrayList(
                new IOSourceMeta(ioSourceInsert.getSourceID(), UserMetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(name)),
                new IOSourceMeta(ioSourceInsert.getSourceID(), UserMetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(name))
        );
        Assert.isTrue(Objects.equals(sourceMetaDao.batchInsert(ioSourceMetas), 2), "保存 io_source_meta 数据失败");
        // 写入 io_source_event 表
        IoSourceEvent ioSourceEventInsert = new IoSourceEvent(ioSourceInsert.getSourceID(), parentId, userId, "create",
                new JSONObject().fluentPut("name", name)
                        .fluentPut("createType", "mkfile").toJSONString()
        );
        ioSourceEventInsert.setCreateTime(System.currentTimeMillis());
        Assert.isTrue(Objects.equals(sourceEventDao.insert(ioSourceEventInsert), 1), "保存 io_source_event 数据失败");
        log.info("sourceId => {} ioSourceEvent => {}", ioSourceInsert.getSourceID(), ioSourceEventInsert.getId());
        return true;
    }
}
