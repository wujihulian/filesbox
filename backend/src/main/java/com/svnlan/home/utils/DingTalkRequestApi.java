package com.svnlan.home.utils;

/**
 * 钉钉请求的链接
 *
 */
public class DingTalkRequestApi {

    public static final String REQUEST_HOST = "https://oapi.dingtalk.com/topapi";
    /**
     * 部门列表
     */
    public static final String DEPT_LIST = REQUEST_HOST + "/v2/department/listsub";
    /**
     * 部门详情
     */
    public static final String DEPT_DETAIL = REQUEST_HOST + "/v2/department/get";
    /**
     * 获取分支组织列表
     */
    public static final String UNION_BRANCH = REQUEST_HOST + "/org/union/branch/get";
    /**
     * 获取加入或申请加入上下游组织的组织和个人信息
     */
    public static final String COOPERATE_INFO_LIST = REQUEST_HOST + "/union/cooperate/info/list";
    /**
     * 获取主干组织列表
     */
    public static final String TRUNK_UNION = REQUEST_HOST + "/org/union/trunk/get";
    /**
     * 查询用户详情
     */
    public static final String USER_INFO = REQUEST_HOST + "/v2/user/get";
    /**
     * 查询部门userId列表
     */
    public static final String UNION_USER_ID_LIST = REQUEST_HOST + "/user/listid";
    /**
     * 查询部门user列表
     */
    public static final String UNION_USER_LIST = REQUEST_HOST + "/v2/user/list";
    /**
     * 获取登录用户 userId
     */
    public static final String USER_ID = REQUEST_HOST + "/v2/user/getuserinfo";
    /**
     * 根据部门id查询其所有父部门id
     */
    public static final String PARENT_DEPT_ID = REQUEST_HOST + "/v2/department/listparentbydept";
    /**
     * 家校通部门列表
     */
    public static final String SCHOOL_DEPT_LIST = REQUEST_HOST + "/edu/dept/list";
    /**
     * 家校通用户列表
     */
    public static final String SCHOOL_USER_LIST = REQUEST_HOST + "/edu/user/list";
    /**
     * 家校通用户详情
     */
    public static final String SCHOOL_USER_GET = REQUEST_HOST + "/edu/user/get";

}
