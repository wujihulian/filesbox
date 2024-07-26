package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.gson.JsonObject;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Component;

@Component
public class DingCubeUtil {

    public static JSONObject departmentList(String access_token) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/industry/department/list");
        OapiIndustryDepartmentListRequest req = new OapiIndustryDepartmentListRequest();
        req.setDeptId(1L);
        req.setSize(10L);
        req.setCursor(1L);
        OapiIndustryDepartmentListResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }

    public static JSONObject eduDeptList(String access_token, Long deptId) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/edu/dept/list");
        OapiEduDeptListRequest req = new OapiEduDeptListRequest();
        req.setPageSize(30L);
        req.setPageNo(1L);
        req.setSuperId(deptId);
        OapiEduDeptListResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }
    public static JSONObject eduDepartmentList(String access_token, Long deptId) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsub");
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = client.execute(req, access_token);
        System.out.println(rsp.getBody());
        return JSONObject.parseObject(rsp.getBody());
    }



    public static JSONObject eduDeptGet(String access_token, Long deptId) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/edu/dept/get");
        OapiEduDeptGetRequest req = new OapiEduDeptGetRequest();
        req.setDeptId(deptId);
        OapiEduDeptGetResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }

    public static JSONObject eduUserList(String access_token, Long deptId) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/edu/user/list");
        OapiEduUserListRequest req = new OapiEduUserListRequest();
        req.setPageSize(30L);
        req.setPageNo(1L);
        req.setRole("teacher");
        req.setClassId(deptId);
        OapiEduUserListResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }

    public static JSONObject userIdListByDeptId(String access_token, Long deptId) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listid");
        OapiUserListidRequest req = new OapiUserListidRequest();
        req.setDeptId(deptId);
        OapiUserListidResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }


    public static JSONObject userGet(String access_token, Long deptId, String userId) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/industry/user/get");
        OapiIndustryUserGetRequest req = new OapiIndustryUserGetRequest();
        req.setDeptId(deptId);
        req.setUserid(userId);
        OapiIndustryUserGetResponse rsp = client.execute(req, access_token);
        return JSONObject.parseObject(rsp.getBody());
    }

}
