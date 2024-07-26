/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.Share;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record18;
import org.jooq.Row18;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 分享数据表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ShareRecord extends UpdatableRecordImpl<ShareRecord> implements Record18<Long, String, String, Long, Long, String, String, Integer, Integer, Integer, String, Long, Integer, Integer, String, LocalDateTime, LocalDateTime, Long> {

    private static final long serialVersionUID = 550439154;

    /**
     * Setter for <code>cloud_disk.share.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.share.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>cloud_disk.share.title</code>. 分享标题
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.share.title</code>. 分享标题
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>cloud_disk.share.share_hash</code>. shareid
     */
    public void setShareHash(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.share.share_hash</code>. shareid
     */
    public String getShareHash() {
        return (String) get(2);
    }

    /**
     * Setter for <code>cloud_disk.share.user_id</code>. 分享用户id
     */
    public void setUserId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.share.user_id</code>. 分享用户id
     */
    public Long getUserId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>cloud_disk.share.source_id</code>. 用户数据id
     */
    public void setSourceId(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.share.source_id</code>. 用户数据id
     */
    public Long getSourceId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>cloud_disk.share.source_path</code>. 分享文档路径
     */
    public void setSourcePath(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.share.source_path</code>. 分享文档路径
     */
    public String getSourcePath() {
        return (String) get(5);
    }

    /**
     * Setter for <code>cloud_disk.share.url</code>. 分享别名,替代shareHash
     */
    public void setUrl(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>cloud_disk.share.url</code>. 分享别名,替代shareHash
     */
    public String getUrl() {
        return (String) get(6);
    }

    /**
     * Setter for <code>cloud_disk.share.is_link</code>. 是否外链分享；默认为0
     */
    public void setIsLink(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>cloud_disk.share.is_link</code>. 是否外链分享；默认为0
     */
    public Integer getIsLink() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>cloud_disk.share.status</code>. 状态 1 正常 3 禁止分享 4 取消分享
     */
    public void setStatus(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>cloud_disk.share.status</code>. 状态 1 正常 3 禁止分享 4 取消分享
     */
    public Integer getStatus() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>cloud_disk.share.is_share_to</code>. 是否为内部分享；默认为0
     */
    public void setIsShareTo(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>cloud_disk.share.is_share_to</code>. 是否为内部分享；默认为0
     */
    public Integer getIsShareTo() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>cloud_disk.share.password</code>. 访问密码,为空则无密码
     */
    public void setPassword(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>cloud_disk.share.password</code>. 访问密码,为空则无密码
     */
    public String getPassword() {
        return (String) get(10);
    }

    /**
     * Setter for <code>cloud_disk.share.time_to</code>.
     */
    public void setTimeTo(Long value) {
        set(11, value);
    }

    /**
     * Getter for <code>cloud_disk.share.time_to</code>.
     */
    public Long getTimeTo() {
        return (Long) get(11);
    }

    /**
     * Setter for <code>cloud_disk.share.num_view</code>. 预览次数
     */
    public void setNumView(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>cloud_disk.share.num_view</code>. 预览次数
     */
    public Integer getNumView() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>cloud_disk.share.num_download</code>. 下载次数
     */
    public void setNumDownload(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>cloud_disk.share.num_download</code>. 下载次数
     */
    public Integer getNumDownload() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>cloud_disk.share.options</code>. json 配置信息;是否可以下载,是否可以上传等
     */
    public void setOptions(String value) {
        set(14, value);
    }

    /**
     * Getter for <code>cloud_disk.share.options</code>. json 配置信息;是否可以下载,是否可以上传等
     */
    public String getOptions() {
        return (String) get(14);
    }

    /**
     * Setter for <code>cloud_disk.share.create_time</code>. 创建时间
     */
    public void setCreateTime(LocalDateTime value) {
        set(15, value);
    }

    /**
     * Getter for <code>cloud_disk.share.create_time</code>. 创建时间
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>cloud_disk.share.modify_time</code>. 最后修改时间
     */
    public void setModifyTime(LocalDateTime value) {
        set(16, value);
    }

    /**
     * Getter for <code>cloud_disk.share.modify_time</code>. 最后修改时间
     */
    public LocalDateTime getModifyTime() {
        return (LocalDateTime) get(16);
    }

    /**
     * Setter for <code>cloud_disk.share.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(17, value);
    }

    /**
     * Getter for <code>cloud_disk.share.tenant_id</code>. 租户id
     */
    public Long getTenantId() {
        return (Long) get(17);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record18 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row18<Long, String, String, Long, Long, String, String, Integer, Integer, Integer, String, Long, Integer, Integer, String, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    @Override
    public Row18<Long, String, String, Long, Long, String, String, Integer, Integer, Integer, String, Long, Integer, Integer, String, LocalDateTime, LocalDateTime, Long> valuesRow() {
        return (Row18) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Share.SHARE.ID;
    }

    @Override
    public Field<String> field2() {
        return Share.SHARE.TITLE;
    }

    @Override
    public Field<String> field3() {
        return Share.SHARE.SHARE_HASH;
    }

    @Override
    public Field<Long> field4() {
        return Share.SHARE.USER_ID;
    }

    @Override
    public Field<Long> field5() {
        return Share.SHARE.SOURCE_ID;
    }

    @Override
    public Field<String> field6() {
        return Share.SHARE.SOURCE_PATH;
    }

    @Override
    public Field<String> field7() {
        return Share.SHARE.URL;
    }

    @Override
    public Field<Integer> field8() {
        return Share.SHARE.IS_LINK;
    }

    @Override
    public Field<Integer> field9() {
        return Share.SHARE.STATUS;
    }

    @Override
    public Field<Integer> field10() {
        return Share.SHARE.IS_SHARE_TO;
    }

    @Override
    public Field<String> field11() {
        return Share.SHARE.PASSWORD;
    }

    @Override
    public Field<Long> field12() {
        return Share.SHARE.TIME_TO;
    }

    @Override
    public Field<Integer> field13() {
        return Share.SHARE.NUM_VIEW;
    }

    @Override
    public Field<Integer> field14() {
        return Share.SHARE.NUM_DOWNLOAD;
    }

    @Override
    public Field<String> field15() {
        return Share.SHARE.OPTIONS;
    }

    @Override
    public Field<LocalDateTime> field16() {
        return Share.SHARE.CREATE_TIME;
    }

    @Override
    public Field<LocalDateTime> field17() {
        return Share.SHARE.MODIFY_TIME;
    }

    @Override
    public Field<Long> field18() {
        return Share.SHARE.TENANT_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public String component3() {
        return getShareHash();
    }

    @Override
    public Long component4() {
        return getUserId();
    }

    @Override
    public Long component5() {
        return getSourceId();
    }

    @Override
    public String component6() {
        return getSourcePath();
    }

    @Override
    public String component7() {
        return getUrl();
    }

    @Override
    public Integer component8() {
        return getIsLink();
    }

    @Override
    public Integer component9() {
        return getStatus();
    }

    @Override
    public Integer component10() {
        return getIsShareTo();
    }

    @Override
    public String component11() {
        return getPassword();
    }

    @Override
    public Long component12() {
        return getTimeTo();
    }

    @Override
    public Integer component13() {
        return getNumView();
    }

    @Override
    public Integer component14() {
        return getNumDownload();
    }

    @Override
    public String component15() {
        return getOptions();
    }

    @Override
    public LocalDateTime component16() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime component17() {
        return getModifyTime();
    }

    @Override
    public Long component18() {
        return getTenantId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public String value3() {
        return getShareHash();
    }

    @Override
    public Long value4() {
        return getUserId();
    }

    @Override
    public Long value5() {
        return getSourceId();
    }

    @Override
    public String value6() {
        return getSourcePath();
    }

    @Override
    public String value7() {
        return getUrl();
    }

    @Override
    public Integer value8() {
        return getIsLink();
    }

    @Override
    public Integer value9() {
        return getStatus();
    }

    @Override
    public Integer value10() {
        return getIsShareTo();
    }

    @Override
    public String value11() {
        return getPassword();
    }

    @Override
    public Long value12() {
        return getTimeTo();
    }

    @Override
    public Integer value13() {
        return getNumView();
    }

    @Override
    public Integer value14() {
        return getNumDownload();
    }

    @Override
    public String value15() {
        return getOptions();
    }

    @Override
    public LocalDateTime value16() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime value17() {
        return getModifyTime();
    }

    @Override
    public Long value18() {
        return getTenantId();
    }

    @Override
    public ShareRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ShareRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public ShareRecord value3(String value) {
        setShareHash(value);
        return this;
    }

    @Override
    public ShareRecord value4(Long value) {
        setUserId(value);
        return this;
    }

    @Override
    public ShareRecord value5(Long value) {
        setSourceId(value);
        return this;
    }

    @Override
    public ShareRecord value6(String value) {
        setSourcePath(value);
        return this;
    }

    @Override
    public ShareRecord value7(String value) {
        setUrl(value);
        return this;
    }

    @Override
    public ShareRecord value8(Integer value) {
        setIsLink(value);
        return this;
    }

    @Override
    public ShareRecord value9(Integer value) {
        setStatus(value);
        return this;
    }

    @Override
    public ShareRecord value10(Integer value) {
        setIsShareTo(value);
        return this;
    }

    @Override
    public ShareRecord value11(String value) {
        setPassword(value);
        return this;
    }

    @Override
    public ShareRecord value12(Long value) {
        setTimeTo(value);
        return this;
    }

    @Override
    public ShareRecord value13(Integer value) {
        setNumView(value);
        return this;
    }

    @Override
    public ShareRecord value14(Integer value) {
        setNumDownload(value);
        return this;
    }

    @Override
    public ShareRecord value15(String value) {
        setOptions(value);
        return this;
    }

    @Override
    public ShareRecord value16(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public ShareRecord value17(LocalDateTime value) {
        setModifyTime(value);
        return this;
    }

    @Override
    public ShareRecord value18(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public ShareRecord values(Long value1, String value2, String value3, Long value4, Long value5, String value6, String value7, Integer value8, Integer value9, Integer value10, String value11, Long value12, Integer value13, Integer value14, String value15, LocalDateTime value16, LocalDateTime value17, Long value18) {
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
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ShareRecord
     */
    public ShareRecord() {
        super(Share.SHARE);
    }

    /**
     * Create a detached, initialised ShareRecord
     */
    public ShareRecord(Long id, String title, String shareHash, Long userId, Long sourceId, String sourcePath, String url, Integer isLink, Integer status, Integer isShareTo, String password, Long timeTo, Integer numView, Integer numDownload, String options, LocalDateTime createTime, LocalDateTime modifyTime, Long tenantId) {
        super(Share.SHARE);

        set(0, id);
        set(1, title);
        set(2, shareHash);
        set(3, userId);
        set(4, sourceId);
        set(5, sourcePath);
        set(6, url);
        set(7, isLink);
        set(8, status);
        set(9, isShareTo);
        set(10, password);
        set(11, timeTo);
        set(12, numView);
        set(13, numDownload);
        set(14, options);
        set(15, createTime);
        set(16, modifyTime);
        set(17, tenantId);
    }
}