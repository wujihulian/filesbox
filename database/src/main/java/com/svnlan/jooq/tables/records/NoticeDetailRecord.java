/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.NoticeDetail;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 通知详情表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NoticeDetailRecord extends UpdatableRecordImpl<NoticeDetailRecord> implements Record12<Long, Long, String, Integer, JSON, JSON, JSON, Long, LocalDateTime, LocalDateTime, Integer, Long> {

    private static final long serialVersionUID = -2036310590;

    /**
     * Setter for <code>cloud_disk.notice_detail.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.notice_id</code>. 通知id
     */
    public void setNoticeId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.notice_id</code>. 通知id
     */
    public Long getNoticeId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.content</code>. 消息内容
     */
    public void setContent(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.content</code>. 消息内容
     */
    public String getContent() {
        return (String) get(2);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.is_all</code>. 是否为所有用户 1 是 0 否
     */
    public void setIsAll(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.is_all</code>. 是否为所有用户 1 是 0 否
     */
    public Integer getIsAll() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.target_user_ids</code>. 目标用户id集合
     */
    public void setTargetUserIds(JSON value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.target_user_ids</code>. 目标用户id集合
     */
    public JSON getTargetUserIds() {
        return (JSON) get(4);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.target_dept_ids</code>. 目标部门id集合
     */
    public void setTargetDeptIds(JSON value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.target_dept_ids</code>. 目标部门id集合
     */
    public JSON getTargetDeptIds() {
        return (JSON) get(5);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.target_role_ids</code>. 目标角色id集合
     */
    public void setTargetRoleIds(JSON value) {
        set(6, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.target_role_ids</code>. 目标角色id集合
     */
    public JSON getTargetRoleIds() {
        return (JSON) get(6);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.notice_detail_id</code>. 通知详情表id
     */
    public void setNoticeDetailId(Long value) {
        set(7, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.notice_detail_id</code>. 通知详情表id
     */
    public Long getNoticeDetailId() {
        return (Long) get(7);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.create_time</code>. 创建时间
     */
    public void setCreateTime(LocalDateTime value) {
        set(8, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.create_time</code>. 创建时间
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(8);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.modify_time</code>. 更新时间
     */
    public void setModifyTime(LocalDateTime value) {
        set(9, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.modify_time</code>. 更新时间
     */
    public LocalDateTime getModifyTime() {
        return (LocalDateTime) get(9);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.dr</code>. 逻辑删除 0 未删除 1 已删除
     */
    public void setDr(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.dr</code>. 逻辑删除 0 未删除 1 已删除
     */
    public Integer getDr() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>cloud_disk.notice_detail.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(11, value);
    }

    /**
     * Getter for <code>cloud_disk.notice_detail.tenant_id</code>. 租户id
     */
    public Long getTenantId() {
        return (Long) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, Long, String, Integer, JSON, JSON, JSON, Long, LocalDateTime, LocalDateTime, Integer, Long> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    @Override
    public Row12<Long, Long, String, Integer, JSON, JSON, JSON, Long, LocalDateTime, LocalDateTime, Integer, Long> valuesRow() {
        return (Row12) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return NoticeDetail.NOTICE_DETAIL.ID;
    }

    @Override
    public Field<Long> field2() {
        return NoticeDetail.NOTICE_DETAIL.NOTICE_ID;
    }

    @Override
    public Field<String> field3() {
        return NoticeDetail.NOTICE_DETAIL.CONTENT;
    }

    @Override
    public Field<Integer> field4() {
        return NoticeDetail.NOTICE_DETAIL.IS_ALL;
    }

    @Override
    public Field<JSON> field5() {
        return NoticeDetail.NOTICE_DETAIL.TARGET_USER_IDS;
    }

    @Override
    public Field<JSON> field6() {
        return NoticeDetail.NOTICE_DETAIL.TARGET_DEPT_IDS;
    }

    @Override
    public Field<JSON> field7() {
        return NoticeDetail.NOTICE_DETAIL.TARGET_ROLE_IDS;
    }

    @Override
    public Field<Long> field8() {
        return NoticeDetail.NOTICE_DETAIL.NOTICE_DETAIL_ID;
    }

    @Override
    public Field<LocalDateTime> field9() {
        return NoticeDetail.NOTICE_DETAIL.CREATE_TIME;
    }

    @Override
    public Field<LocalDateTime> field10() {
        return NoticeDetail.NOTICE_DETAIL.MODIFY_TIME;
    }

    @Override
    public Field<Integer> field11() {
        return NoticeDetail.NOTICE_DETAIL.DR;
    }

    @Override
    public Field<Long> field12() {
        return NoticeDetail.NOTICE_DETAIL.TENANT_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getNoticeId();
    }

    @Override
    public String component3() {
        return getContent();
    }

    @Override
    public Integer component4() {
        return getIsAll();
    }

    @Override
    public JSON component5() {
        return getTargetUserIds();
    }

    @Override
    public JSON component6() {
        return getTargetDeptIds();
    }

    @Override
    public JSON component7() {
        return getTargetRoleIds();
    }

    @Override
    public Long component8() {
        return getNoticeDetailId();
    }

    @Override
    public LocalDateTime component9() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime component10() {
        return getModifyTime();
    }

    @Override
    public Integer component11() {
        return getDr();
    }

    @Override
    public Long component12() {
        return getTenantId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getNoticeId();
    }

    @Override
    public String value3() {
        return getContent();
    }

    @Override
    public Integer value4() {
        return getIsAll();
    }

    @Override
    public JSON value5() {
        return getTargetUserIds();
    }

    @Override
    public JSON value6() {
        return getTargetDeptIds();
    }

    @Override
    public JSON value7() {
        return getTargetRoleIds();
    }

    @Override
    public Long value8() {
        return getNoticeDetailId();
    }

    @Override
    public LocalDateTime value9() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime value10() {
        return getModifyTime();
    }

    @Override
    public Integer value11() {
        return getDr();
    }

    @Override
    public Long value12() {
        return getTenantId();
    }

    @Override
    public NoticeDetailRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value2(Long value) {
        setNoticeId(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value3(String value) {
        setContent(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value4(Integer value) {
        setIsAll(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value5(JSON value) {
        setTargetUserIds(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value6(JSON value) {
        setTargetDeptIds(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value7(JSON value) {
        setTargetRoleIds(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value8(Long value) {
        setNoticeDetailId(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value9(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value10(LocalDateTime value) {
        setModifyTime(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value11(Integer value) {
        setDr(value);
        return this;
    }

    @Override
    public NoticeDetailRecord value12(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public NoticeDetailRecord values(Long value1, Long value2, String value3, Integer value4, JSON value5, JSON value6, JSON value7, Long value8, LocalDateTime value9, LocalDateTime value10, Integer value11, Long value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NoticeDetailRecord
     */
    public NoticeDetailRecord() {
        super(NoticeDetail.NOTICE_DETAIL);
    }

    /**
     * Create a detached, initialised NoticeDetailRecord
     */
    public NoticeDetailRecord(Long id, Long noticeId, String content, Integer isAll, JSON targetUserIds, JSON targetDeptIds, JSON targetRoleIds, Long noticeDetailId, LocalDateTime createTime, LocalDateTime modifyTime, Integer dr, Long tenantId) {
        super(NoticeDetail.NOTICE_DETAIL);

        set(0, id);
        set(1, noticeId);
        set(2, content);
        set(3, isAll);
        set(4, targetUserIds);
        set(5, targetDeptIds);
        set(6, targetRoleIds);
        set(7, noticeDetailId);
        set(8, createTime);
        set(9, modifyTime);
        set(10, dr);
        set(11, tenantId);
    }
}