/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


/**
 * 分享给指定用户(协作)
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ShareToModel implements Serializable {

    private static final long serialVersionUID = -952174833;

    protected Long          id;
    protected Long          shareId;
    protected Integer       targetType;
    protected Long          targetId;
    protected Integer       authId;
    protected Integer       authDefine;
    protected LocalDateTime createTime;
    protected LocalDateTime modifyTime;
    protected Long          tenantId;

    public ShareToModel() {}

    public ShareToModel(ShareToModel value) {
        this.id = value.id;
        this.shareId = value.shareId;
        this.targetType = value.targetType;
        this.targetId = value.targetId;
        this.authId = value.authId;
        this.authDefine = value.authDefine;
        this.createTime = value.createTime;
        this.modifyTime = value.modifyTime;
        this.tenantId = value.tenantId;
    }

    public ShareToModel(
        Long          id,
        Long          shareId,
        Integer       targetType,
        Long          targetId,
        Integer       authId,
        Integer       authDefine,
        LocalDateTime createTime,
        LocalDateTime modifyTime,
        Long          tenantId
    ) {
        this.id = id;
        this.shareId = shareId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.authId = authId;
        this.authDefine = authDefine;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.tenantId = tenantId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShareId() {
        return this.shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Integer getTargetType() {
        return this.targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Integer getAuthId() {
        return this.authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }

    public Integer getAuthDefine() {
        return this.authDefine;
    }

    public void setAuthDefine(Integer authDefine) {
        this.authDefine = authDefine;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ShareToModel (");

        sb.append(id);
        sb.append(", ").append(shareId);
        sb.append(", ").append(targetType);
        sb.append(", ").append(targetId);
        sb.append(", ").append(authId);
        sb.append(", ").append(authDefine);
        sb.append(", ").append(createTime);
        sb.append(", ").append(modifyTime);
        sb.append(", ").append(tenantId);

        sb.append(")");
        return sb.toString();
    }
}