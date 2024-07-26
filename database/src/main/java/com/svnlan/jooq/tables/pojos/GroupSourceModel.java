/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


/**
 * 群组、文件关系表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GroupSourceModel implements Serializable {

    private static final long serialVersionUID = 1081582791;

    protected Long          groupId;
    protected Long          sourceId;
    protected LocalDateTime createTime;
    protected Long          tenantId;

    public GroupSourceModel() {}

    public GroupSourceModel(GroupSourceModel value) {
        this.groupId = value.groupId;
        this.sourceId = value.sourceId;
        this.createTime = value.createTime;
        this.tenantId = value.tenantId;
    }

    public GroupSourceModel(
        Long          groupId,
        Long          sourceId,
        LocalDateTime createTime,
        Long          tenantId
    ) {
        this.groupId = groupId;
        this.sourceId = sourceId;
        this.createTime = createTime;
        this.tenantId = tenantId;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GroupSourceModel (");

        sb.append(groupId);
        sb.append(", ").append(sourceId);
        sb.append(", ").append(createTime);
        sb.append(", ").append(tenantId);

        sb.append(")");
        return sb.toString();
    }
}