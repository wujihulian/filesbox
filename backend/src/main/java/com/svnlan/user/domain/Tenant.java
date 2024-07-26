package com.svnlan.user.domain;

import com.svnlan.user.dto.TenantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@EqualsAndHashCode(of = "id")
public class Tenant {

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
     * 状态，0停用，1启用，2删除
     */
    private Integer status;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 已使用大小(byte)
     */
    private Long sizeUse;

    /**
     * 组织数
     */
    private Long groupCount;

    /**
     * 用户数
     */
    private Long userCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改
     */
    private LocalDateTime modifyTime;

    private Integer dr;


    public void populateData(TenantDTO dto) {
        Optional.ofNullable(dto.getId()).ifPresent(this::setId);
        Optional.ofNullable(dto.getTenantName()).ifPresent(this::setTenantName);
        Optional.ofNullable(dto.getSecondLevelDomain()).ifPresent(this::setSecondLevelDomain);
        Optional.ofNullable(dto.getUserId()).ifPresent(this::setUserId);
        Optional.ofNullable(dto.getStatus()).ifPresent(this::setStatus);
        Optional.ofNullable(dto.getStartTime()).ifPresent(this::setStartTime);
        Optional.ofNullable(dto.getExpireTime()).ifPresent(this::setExpireTime);
        Optional.ofNullable(dto.getRemark()).ifPresent(this::setRemark);
        this.setModifyTime(LocalDateTime.now());
    }
}