/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


/**
 * 文档历史记录表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoSourceHistoryModel implements Serializable {

    private static final long serialVersionUID = 1485932702;

    protected Long          id;
    protected Long          sourceId;
    protected Long          userId;
    protected Long          fileId;
    protected Long          size;
    protected String        detail;
    protected LocalDateTime createTime;
    protected LocalDateTime modifyTime;
    protected Long          tenantId;

    public IoSourceHistoryModel() {}

    public IoSourceHistoryModel(IoSourceHistoryModel value) {
        this.id = value.id;
        this.sourceId = value.sourceId;
        this.userId = value.userId;
        this.fileId = value.fileId;
        this.size = value.size;
        this.detail = value.detail;
        this.createTime = value.createTime;
        this.modifyTime = value.modifyTime;
        this.tenantId = value.tenantId;
    }

    public IoSourceHistoryModel(
        Long          id,
        Long          sourceId,
        Long          userId,
        Long          fileId,
        Long          size,
        String        detail,
        LocalDateTime createTime,
        LocalDateTime modifyTime,
        Long          tenantId
    ) {
        this.id = id;
        this.sourceId = sourceId;
        this.userId = userId;
        this.fileId = fileId;
        this.size = size;
        this.detail = detail;
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

    public Long getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFileId() {
        return this.fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        StringBuilder sb = new StringBuilder("IoSourceHistoryModel (");

        sb.append(id);
        sb.append(", ").append(sourceId);
        sb.append(", ").append(userId);
        sb.append(", ").append(fileId);
        sb.append(", ").append(size);
        sb.append(", ").append(detail);
        sb.append(", ").append(createTime);
        sb.append(", ").append(modifyTime);
        sb.append(", ").append(tenantId);

        sb.append(")");
        return sb.toString();
    }
}