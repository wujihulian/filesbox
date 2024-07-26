package com.svnlan.webdav.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.HMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service("privateResourceProcessor")
public class PrivateResourceProcessor extends AbstractResourceProcessor {


//    @Override
//    public WebDavEngine resolveExecuteEngine(String contextRequestUrl) {
//
//        IOSourceVo ioSource = diskSourceUtil.getIoSource(decodeAndConvertToPath(contextRequestUrl));
//        log.info("performDavRequest ioSource => {}", ioSource);
//
//        WebDavEngine engine = null;
//        if (Objects.nonNull(ioSource)) {
//            Integer storageId = ioSource.getStorageID();
//            engine = engineMap.get(storageId);
//        } else {
//            // 返回默认的
//            engine = super.resolveExecuteEngine(contextRequestUrl);
//        }
//        return engine;
//    }

    @Resource
    private IoSourceDao ioSourceDao;

    @Override
    List<JSONObject> getIoSourceVoList(Long userId, Long rootId) {
        return ioSourceDao.getUserDirectoryAndFile(userId, rootId);
    }

//    @Override
//    public Pair<List<HierarchyItemImpl>, Long> getRootChildrenResource(Long rootId, Function<JSONObject, HierarchyItemImpl> func) {
//        long total;
//        List<HierarchyItemImpl> children = new ArrayList<>();
//        UserVo userVo = WebDavEngine.userVoThreadLocal.get();
//        List<JSONObject> ioSourceVoList = ioSourceDao.getUserDirectoryAndFile(userVo.getUserID(), rootId);
//        for (JSONObject jsonObject : ioSourceVoList) {
////            String childPath = path + encode(jsonObject.getString("name"));
//            HierarchyItemImpl item = func.apply(jsonObject);
//            if (Objects.nonNull(item)) {
//                children.add(item);
//            }
//        }
//        total = ioSourceVoList.size();
//        return Pair.of(children, total);
//    }

}
