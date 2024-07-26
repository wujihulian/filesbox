/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.IoSourceHistory;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


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
public class IoSourceHistoryRecord extends UpdatableRecordImpl<IoSourceHistoryRecord> implements Record9<Long, Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, Long> {

    private static final long serialVersionUID = 1033777458;

    /**
     * Setter for <code>cloud_disk.io_source_history.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.source_id</code>. 文档资源id
     */
    public void setSourceId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.source_id</code>. 文档资源id
     */
    public Long getSourceId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.user_id</code>. 用户id, 对部门时此id为0
     */
    public void setUserId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.user_id</code>. 用户id, 对部门时此id为0
     */
    public Long getUserId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.file_id</code>. 当前版本对应存储资源id
     */
    public void setFileId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.file_id</code>. 当前版本对应存储资源id
     */
    public Long getFileId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.size</code>. 文件大小
     */
    public void setSize(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.size</code>. 文件大小
     */
    public Long getSize() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.detail</code>. 版本描述
     */
    public void setDetail(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.detail</code>. 版本描述
     */
    public String getDetail() {
        return (String) get(5);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.create_time</code>. 创建时间
     */
    public void setCreateTime(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.create_time</code>. 创建时间
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.modify_time</code>. 最后修改时间
     */
    public void setModifyTime(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.modify_time</code>. 最后修改时间
     */
    public LocalDateTime getModifyTime() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>cloud_disk.io_source_history.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(8, value);
    }

    /**
     * Getter for <code>cloud_disk.io_source_history.tenant_id</code>. 租户id
     */
    public Long getTenantId() {
        return (Long) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Long, Long, Long, Long, Long, String, LocalDateTime, LocalDateTime, Long> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return IoSourceHistory.IO_SOURCE_HISTORY.ID;
    }

    @Override
    public Field<Long> field2() {
        return IoSourceHistory.IO_SOURCE_HISTORY.SOURCE_ID;
    }

    @Override
    public Field<Long> field3() {
        return IoSourceHistory.IO_SOURCE_HISTORY.USER_ID;
    }

    @Override
    public Field<Long> field4() {
        return IoSourceHistory.IO_SOURCE_HISTORY.FILE_ID;
    }

    @Override
    public Field<Long> field5() {
        return IoSourceHistory.IO_SOURCE_HISTORY.SIZE;
    }

    @Override
    public Field<String> field6() {
        return IoSourceHistory.IO_SOURCE_HISTORY.DETAIL;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return IoSourceHistory.IO_SOURCE_HISTORY.CREATE_TIME;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return IoSourceHistory.IO_SOURCE_HISTORY.MODIFY_TIME;
    }

    @Override
    public Field<Long> field9() {
        return IoSourceHistory.IO_SOURCE_HISTORY.TENANT_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getSourceId();
    }

    @Override
    public Long component3() {
        return getUserId();
    }

    @Override
    public Long component4() {
        return getFileId();
    }

    @Override
    public Long component5() {
        return getSize();
    }

    @Override
    public String component6() {
        return getDetail();
    }

    @Override
    public LocalDateTime component7() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime component8() {
        return getModifyTime();
    }

    @Override
    public Long component9() {
        return getTenantId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getSourceId();
    }

    @Override
    public Long value3() {
        return getUserId();
    }

    @Override
    public Long value4() {
        return getFileId();
    }

    @Override
    public Long value5() {
        return getSize();
    }

    @Override
    public String value6() {
        return getDetail();
    }

    @Override
    public LocalDateTime value7() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime value8() {
        return getModifyTime();
    }

    @Override
    public Long value9() {
        return getTenantId();
    }

    @Override
    public IoSourceHistoryRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value2(Long value) {
        setSourceId(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value3(Long value) {
        setUserId(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value4(Long value) {
        setFileId(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value5(Long value) {
        setSize(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value6(String value) {
        setDetail(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value7(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value8(LocalDateTime value) {
        setModifyTime(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord value9(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public IoSourceHistoryRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, String value6, LocalDateTime value7, LocalDateTime value8, Long value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached IoSourceHistoryRecord
     */
    public IoSourceHistoryRecord() {
        super(IoSourceHistory.IO_SOURCE_HISTORY);
    }

    /**
     * Create a detached, initialised IoSourceHistoryRecord
     */
    public IoSourceHistoryRecord(Long id, Long sourceId, Long userId, Long fileId, Long size, String detail, LocalDateTime createTime, LocalDateTime modifyTime, Long tenantId) {
        super(IoSourceHistory.IO_SOURCE_HISTORY);

        set(0, id);
        set(1, sourceId);
        set(2, userId);
        set(3, fileId);
        set(4, size);
        set(5, detail);
        set(6, createTime);
        set(7, modifyTime);
        set(8, tenantId);
    }
}