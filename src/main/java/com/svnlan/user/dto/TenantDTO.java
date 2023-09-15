package com.svnlan.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.svnlan.utils.DateDUtil;
import lombok.Data;

import java.util.Date;

/**
 * 租户dto
 */
@Data
public class TenantDTO {
    /**
     * 租户id
     */
    private Long id;

    /**
     * 名称
     */
    private String tenantName;

    /**
     * 二级域名
     */
    private String secondLevelDomain;

    /**
     * 超级管理员，用户id
     */
    private Long userId;
    /**
     * 管理员名称
     */
    private String adminUserName;

    /**
     * 状态，0停用，1启用，2删除
     */
    private Integer status;

    /**
     * 生效时间
     */
    @JsonFormat(pattern = DateDUtil.YYYY_MM_DD_HH_MM_SS)
    private Date startTime;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = DateDUtil.YYYY_MM_DD_HH_MM_SS)
    private Date expireTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 管理员密码
     */
    private String password;


}