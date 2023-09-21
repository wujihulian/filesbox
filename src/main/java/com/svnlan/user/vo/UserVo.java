package com.svnlan.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:22
 */
@Data
public class UserVo {

    private Long userID;
    /**
     * 登陆用户名
     */
    private String name;
    /**
     * 用户角色
     */
    private Integer roleID;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String phone;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别 (0女1男)
     */
    private Integer sex;
    /**
     * 密码
     */
    private String password;
    /**
     * 群组存储空间大小(GB) 0-不限制
     */
    private Double sizeMax;
    /**
     * 已使用大小(byte)
     */
    private Long sizeUse;
    /**
     * 用户启用状态 0-未启用 1-启用
     */
    private Integer status;
    /**
     * 最后登录时间
     */
    private Long lastLogin;
    /**
     *
     */
    private Long modifyTime;
    /**
     *
     */
    private Long createTime;

    /** -----------角色相关------------ */

    /**
     * 角色名称
     */
    private String roleName;
    private String code;
    /**
     * 描述
     */
    private String description;
    private String label;
    private String auth;
    /**
     * 是否系统配置
     */
    private Integer isSystem;
    /**
     * 是否系统管理员
     */
    private Integer administrator;
    /**
     * 上传文件大小限制 单位 GB
     */
    private Double ignoreFileSize;
    /**
     * 钉钉
     */
    private String dingOpenId;
    /**
     * 微信
     */
    private String wechatOpenId;
    /**
     * 企业微信
     */
    private String enWechatOpenId;
    /**
     * 支付宝
     */
    private String alipayOpenId;

    private List<UserGroupVo> groupInfo;
}
