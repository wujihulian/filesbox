package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 13:25
 */
@Data
public class User {

    private Long userID;
    /** 登陆用户名*/
    private String name;
    /** 用户角色 */
    private Integer roleID;
    /** 邮箱 */
    private String email;
    /** 手机 */
    private String phone;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 性别 (0女1男) */
    private Integer sex;
    /** 密码 */
    private String password;
    /** 群组存储空间大小(GB) 0-不限制 */
    private Double sizeMax;
    /** 已使用大小(byte) */
    private Long sizeUse;
    /** 用户启用状态 0-未启用 1-启用 */
    private Integer status;
    /** 最后登录时间 */
    private Long lastLogin;
    /**  */
    private Long modifyTime;
    /**  */
    private Long createTime;
    /**
     * 钉钉 openId
     */
    private String dingOpenId;
    /**
     * 微信 openId
     */
    private String wechatOpenId;

    private String enWechatOpenId;
    /**
     * 支付宝 openId
     */
    private String alipayOpenId;
    private Long tenantId;
    private String dingUnionId;
    private String thirdOpenId;
    private String dingUserId;

}
