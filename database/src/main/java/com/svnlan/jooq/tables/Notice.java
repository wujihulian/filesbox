/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables;


import com.svnlan.jooq.CloudDisk;
import com.svnlan.jooq.Indexes;
import com.svnlan.jooq.Keys;
import com.svnlan.jooq.tables.records.NoticeRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row14;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 通知表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Notice extends TableImpl<NoticeRecord> {

    private static final long serialVersionUID = 842458429;

    /**
     * The reference instance of <code>cloud_disk.notice</code>
     */
    public static final Notice NOTICE = new Notice();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<NoticeRecord> getRecordType() {
        return NoticeRecord.class;
    }

    /**
     * The column <code>cloud_disk.notice.id</code>.
     */
    public final TableField<NoticeRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>cloud_disk.notice.title</code>. 标题
     */
    public final TableField<NoticeRecord, String> TITLE = createField(DSL.name("title"), org.jooq.impl.SQLDataType.VARCHAR(128).nullable(false), this, "标题");

    /**
     * The column <code>cloud_disk.notice.level</code>. 0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。
     */
    public final TableField<NoticeRecord, Integer> LEVEL = createField(DSL.name("level"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "0 弱提示：左下角通知栏显示红点；1 强提示：用户登录后直接弹出通知。");

    /**
     * The column <code>cloud_disk.notice.status</code>. 状态，0暂存，1已发送，2已删除
     */
    public final TableField<NoticeRecord, Integer> STATUS = createField(DSL.name("status"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "状态，0暂存，1已发送，2已删除");

    /**
     * The column <code>cloud_disk.notice.enable</code>. 是否启用，0未启用，1启用
     */
    public final TableField<NoticeRecord, Integer> ENABLE = createField(DSL.name("enable"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "是否启用，0未启用，1启用");

    /**
     * The column <code>cloud_disk.notice.send_type</code>. 推送方式 1 立即推送 2 计划推送
     */
    public final TableField<NoticeRecord, Integer> SEND_TYPE = createField(DSL.name("send_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("1", org.jooq.impl.SQLDataType.INTEGER)), this, "推送方式 1 立即推送 2 计划推送");

    /**
     * The column <code>cloud_disk.notice.send_time</code>. 通知发送时间
     */
    public final TableField<NoticeRecord, LocalDateTime> SEND_TIME = createField(DSL.name("send_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "通知发送时间");

    /**
     * The column <code>cloud_disk.notice.sort</code>. 排序 越大越靠前
     */
    public final TableField<NoticeRecord, Long> SORT = createField(DSL.name("sort"), org.jooq.impl.SQLDataType.BIGINT.defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "排序 越大越靠前");

    /**
     * The column <code>cloud_disk.notice.sender_id</code>. 通知发送者id
     */
    public final TableField<NoticeRecord, Long> SENDER_ID = createField(DSL.name("sender_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "通知发送者id");

    /**
     * The column <code>cloud_disk.notice.sender_ip</code>. 发送通知的IP地址，json(222.22.22.22,杭州)
     */
    public final TableField<NoticeRecord, String> SENDER_IP = createField(DSL.name("sender_ip"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "发送通知的IP地址，json(222.22.22.22,杭州)");

    /**
     * The column <code>cloud_disk.notice.notice_type</code>. 消息类型，1通知2消息3私信
     */
    public final TableField<NoticeRecord, Integer> NOTICE_TYPE = createField(DSL.name("notice_type"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("1", org.jooq.impl.SQLDataType.INTEGER)), this, "消息类型，1通知2消息3私信");

    /**
     * The column <code>cloud_disk.notice.create_time</code>. 创建时间
     */
    public final TableField<NoticeRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "创建时间");

    /**
     * The column <code>cloud_disk.notice.modify_time</code>. 更新时间
     */
    public final TableField<NoticeRecord, LocalDateTime> MODIFY_TIME = createField(DSL.name("modify_time"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "更新时间");

    /**
     * The column <code>cloud_disk.notice.tenant_id</code>. 租户id
     */
    public final TableField<NoticeRecord, Long> TENANT_ID = createField(DSL.name("tenant_id"), org.jooq.impl.SQLDataType.BIGINT, this, "租户id");

    /**
     * Create a <code>cloud_disk.notice</code> table reference
     */
    public Notice() {
        this(DSL.name("notice"), null);
    }

    /**
     * Create an aliased <code>cloud_disk.notice</code> table reference
     */
    public Notice(String alias) {
        this(DSL.name(alias), NOTICE);
    }

    /**
     * Create an aliased <code>cloud_disk.notice</code> table reference
     */
    public Notice(Name alias) {
        this(alias, NOTICE);
    }

    private Notice(Name alias, Table<NoticeRecord> aliased) {
        this(alias, aliased, null);
    }

    private Notice(Name alias, Table<NoticeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("通知表"));
    }

    public <O extends Record> Notice(Table<O> child, ForeignKey<O, NoticeRecord> key) {
        super(child, key, NOTICE);
    }

    @Override
    public Schema getSchema() {
        return CloudDisk.CLOUD_DISK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.NOTICE_PRIMARY);
    }

    @Override
    public Identity<NoticeRecord, Long> getIdentity() {
        return Keys.IDENTITY_NOTICE;
    }

    @Override
    public UniqueKey<NoticeRecord> getPrimaryKey() {
        return Keys.KEY_NOTICE_PRIMARY;
    }

    @Override
    public List<UniqueKey<NoticeRecord>> getKeys() {
        return Arrays.<UniqueKey<NoticeRecord>>asList(Keys.KEY_NOTICE_PRIMARY);
    }

    @Override
    public Notice as(String alias) {
        return new Notice(DSL.name(alias), this);
    }

    @Override
    public Notice as(Name alias) {
        return new Notice(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Notice rename(String name) {
        return new Notice(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Notice rename(Name name) {
        return new Notice(name, null);
    }

    // -------------------------------------------------------------------------
    // Row14 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row14<Long, String, Integer, Integer, Integer, Integer, LocalDateTime, Long, Long, String, Integer, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row14) super.fieldsRow();
    }
}