package com.svnlan.home.utils;

public class WebChatApi {

    public static final String REQUEST_HOST = "https://qyapi.weixin.qq.com";
    /**
     * 获取子部门ID列表 ?access_token=ACCESS_TOKEN&id=ID
     */
    public static final String DEPT_ID_LIST = REQUEST_HOST + "/cgi-bin/department/simplelist";
    /**
     * 获取单个部门详情 ?access_token=ACCESS_TOKEN&id=ID
     */
    public static final String DEPT_INFO = REQUEST_HOST + "/cgi-bin/department/get";
    /**
     * 获取部门列表 ?access_token=ACCESS_TOKEN&id=ID
     */
    public static final String DEPT_LIST = REQUEST_HOST + "/cgi-bin/department/list?access_token={accessToken}";
    public static final String DEPT_SUB_LIST = REQUEST_HOST + "/cgi-bin/department/list?access_token={accessToken}&id={id}";
    /**
     * 获取部门成员详情 ?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID
     */
    public static final String DEPT_USER_LIST = REQUEST_HOST + "/cgi-bin/user/list?access_token={accessToken}&department_id={deptId}";

}
