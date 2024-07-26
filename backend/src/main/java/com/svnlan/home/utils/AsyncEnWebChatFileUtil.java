package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Component
public class AsyncEnWebChatFileUtil {
    @Resource
    RestTemplate restTemplate;

    public List<JSONObject> getDepartmentList(Long deptId, String accessToken) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = EnWebChatUtil.getDepartmentList(restTemplate, accessToken, deptId);
        }catch (Exception e){
            LogUtil.error(e, "获取部门列表失败 deptId=" + deptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("获取部门列表getDepartmentList" + jsonObj);
            String listStr = jsonObj.getString("department");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }


    public List<JSONObject> getDepartmentUserList(Long deptId, String accessToken) {
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        try {
            jsonObj = EnWebChatUtil.getDepartmentUserList(restTemplate, accessToken, deptId);
        }catch (Exception e){
            LogUtil.error(e, "获取部门成员详情列表 deptId=" + deptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("获取部门成员详情列表  getDepartmentUserList" + jsonObj);
            String listStr = jsonObj.getString("userlist");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return list;
    }

}
