package com.svnlan.webdav.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.vo.UserVo;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractResourceProcessor {

    @Resource
    protected StorageService storageService;

    @Resource
    protected Environment environment;


    protected Pair<List<HierarchyItemImpl>, Long> getRootChildrenResource(Long rootId, Function<JSONObject, HierarchyItemImpl> func) {
        long total;
        List<HierarchyItemImpl> children = new ArrayList<>();
        UserVo userVo = WebDavEngine.userVoThreadLocal.get();
        List<JSONObject> ioSourceVoList = getIoSourceVoList(userVo.getUserID(), rootId);
        for (JSONObject jsonObject : ioSourceVoList) {
//            String childPath = path + encode(jsonObject.getString("name"));
            HierarchyItemImpl item = func.apply(jsonObject);
            if (Objects.nonNull(item)) {
                children.add(item);
            }
        }
        total = ioSourceVoList.size();
        return Pair.of(children, total);
    }

    abstract List<JSONObject>  getIoSourceVoList(Long userId, Long rootId);

    protected static String decodeAndConvertToPath(String url) {
        String path = decode(url);
        return path.replace("/", File.separator);
    }

    static String decode(String url) {
        try {
            return URLDecoder.decode(url.replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(url.replace("+", "%2B"));
        }
    }

}
