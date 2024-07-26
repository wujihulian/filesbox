/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.ShareTo;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ShareToRecord extends UpdatableRecordImpl<ShareToRecord> implements Record9<Long, Long, Integer, Long, Integer, Integer, LocalDateTime, LocalDateTime, Long> {

    private static final long serialVersionUID = 1414858417;

    /**
     * Setter for <code>cloud_disk.share_to.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>cloud_disk.share_to.share_id</code>. 分享id
     */
    public void setShareId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.share_id</code>. 分享id
     */
    public Long getShareId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>cloud_disk.share_to.target_type</code>. 分享给的对象,1用户,2部门
     */
    public void setTargetType(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.target_type</code>. 分享给的对象,1用户,2部门
     */
    public Integer getTargetType() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>cloud_disk.share_to.target_id</code>. 所属对象id
     */
    public void setTargetId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.target_id</code>. 所属对象id
     */
    public Long getTargetId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>cloud_disk.share_to.auth_id</code>. 权限组id；自定义权限则为0
     */
    public void setAuthId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.auth_id</code>. 权限组id；自定义权限则为0
     */
    public Integer getAuthId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>cloud_disk.share_to.auth_define</code>. 自定义权限，4字节占位
     */
    public void setAuthDefine(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.auth_define</code>. 自定义权限，4字节占位
     */
    public Integer getAuthDefine() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>cloud_disk.share_to.create_time</code>. 创建时间
     */
    public void setCreateTime(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.create_time</code>. 创建时间
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>cloud_disk.share_to.modify_time</code>. 最后修改时间
     */
    public void setModifyTime(LocalDateTime value) {
        set(7, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.modify_time</code>. 最后修改时间
     */
    public LocalDateTime getModifyTime() {
        return (LocalDateTime) get(7);
    }

    /**
     * Setter for <code>cloud_disk.share_to.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(8, value);
    }

    /**
     * Getter for <code>cloud_disk.share_to.tenant_id</code>. 租户id
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
    public Row9<Long, Long, Integer, Long, Integer, Integer, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Long, Long, Integer, Long, Integer, Integer, LocalDateTime, LocalDateTime, Long> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return ShareTo.SHARE_TO.ID;
    }

    @Override
    public Field<Long> field2() {
        return ShareTo.SHARE_TO.SHARE_ID;
    }

    @Override
    public Field<Integer> field3() {
        return ShareTo.SHARE_TO.TARGET_TYPE;
    }

    @Override
    public Field<Long> field4() {
        return ShareTo.SHARE_TO.TARGET_ID;
    }

    @Override
    public Field<Integer> field5() {
        return ShareTo.SHARE_TO.AUTH_ID;
    }

    @Override
    public Field<Integer> field6() {
        return ShareTo.SHARE_TO.AUTH_DEFINE;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return ShareTo.SHARE_TO.CREATE_TIME;
    }

    @Override
    public Field<LocalDateTime> field8() {
        return ShareTo.SHARE_TO.MODIFY_TIME;
    }

    @Override
    public Field<Long> field9() {
        return ShareTo.SHARE_TO.TENANT_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getShareId();
    }

    @Override
    public Integer component3() {
        return getTargetType();
    }

    @Override
    public Long component4() {
        return getTargetId();
    }

    @Override
    public Integer component5() {
        return getAuthId();
    }

    @Override
    public Integer component6() {
        return getAuthDefine();
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
        return getShareId();
    }

    @Override
    public Integer value3() {
        return getTargetType();
    }

    @Override
    public Long value4() {
        return getTargetId();
    }

    @Override
    public Integer value5() {
        return getAuthId();
    }

    @Override
    public Integer value6() {
        return getAuthDefine();
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
    public ShareToRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ShareToRecord value2(Long value) {
        setShareId(value);
        return this;
    }

    @Override
    public ShareToRecord value3(Integer value) {
        setTargetType(value);
        return this;
    }

    @Override
    public ShareToRecord value4(Long value) {
        setTargetId(value);
        return this;
    }

    @Override
    public ShareToRecord value5(Integer value) {
        setAuthId(value);
        return this;
    }

    @Override
    public ShareToRecord value6(Integer value) {
        setAuthDefine(value);
        return this;
    }

    @Override
    public ShareToRecord value7(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public ShareToRecord value8(LocalDateTime value) {
        setModifyTime(value);
        return this;
    }

    @Override
    public ShareToRecord value9(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public ShareToRecord values(Long value1, Long value2, Integer value3, Long value4, Integer value5, Integer value6, LocalDateTime value7, LocalDateTime value8, Long value9) {
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
     * Create a detached ShareToRecord
     */
    public ShareToRecord() {
        super(ShareTo.SHARE_TO);
    }

    /**
     * Create a detached, initialised ShareToRecord
     */
    public ShareToRecord(Long id, Long shareId, Integer targetType, Long targetId, Integer authId, Integer authDefine, LocalDateTime createTime, LocalDateTime modifyTime, Long tenantId) {
        super(ShareTo.SHARE_TO);

        set(0, id);
        set(1, shareId);
        set(2, targetType);
        set(3, targetId);
        set(4, authId);
        set(5, authDefine);
        set(6, createTime);
        set(7, modifyTime);
        set(8, tenantId);
    }
}