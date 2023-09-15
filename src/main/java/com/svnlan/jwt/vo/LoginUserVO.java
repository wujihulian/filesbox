package com.svnlan.jwt.vo;

import lombok.Data;


/**
 * @author
 * @Description:
 */
@Data
public class LoginUserVO {

    /**
     * 用户id
     */
    private Long userID;

    /**
     * 用户登录名
     */
    private String name;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 登录时间
     */
    private Long lastLogin;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 性别
     */
    private Integer sex;

    private String ip;

    private String avatar;

    private String userAgent;

    private String languageVersion;

    private String errorCode;

    private String errorMsg;

    private String nickname;

    private String phone;

    private Boolean saveCookie;

    private String requestUri;

    private Integer loginType;
    /**
     * 是否系统管理员
     */
    private Integer administrator;

    private Integer roleID;

}
