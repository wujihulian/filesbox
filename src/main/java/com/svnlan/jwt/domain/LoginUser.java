package com.svnlan.jwt.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:用户信息表的对象
 */
@Data
public class LoginUser implements Serializable {

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
     * 用户类型 1 系统管理员 2 管理员 3 普通用户
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

    private String serverName;
    private String email;

}
