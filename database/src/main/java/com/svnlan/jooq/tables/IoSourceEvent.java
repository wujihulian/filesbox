/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables;


import com.svnlan.jooq.CloudDisk;
import com.svnlan.jooq.Indexes;
import com.svnlan.jooq.Keys;
import com.svnlan.jooq.tables.records.IoSourceEventRecord;

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
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 文档事件表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoSourceEvent extends TableImpl<IoSourceEventRecord> {

    private static final long serialVersionUID = -1039541134;

    /**
     * The reference instance of <code>cloud_disk.io_source_event</code>
     */
    public static final IoSourceEvent IO_SOURCE_EVENT = new IoSourceEvent();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<IoSourceEventRecord> getRecordType() {
        return IoSourceEventRecord.class;
    }

    /**
     * The column <code>cloud_disk.io_source_event.id</code>.
     */
    public final TableField<IoSourceEventRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>cloud_disk.io_source_event.source_id</code>. 文档id
     */
    public final TableField<IoSourceEventRecord, Long> SOURCE_ID = createField(DSL.name("source_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "文档id");

    /**
     * The column <code>cloud_disk.io_source_event.source_parent</code>. 文档父文件夹id
     */
    public final TableField<IoSourceEventRecord, Long> SOURCE_PARENT = createField(DSL.name("source_parent"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "文档父文件夹id");

    /**
     * The column <code>cloud_disk.io_source_event.user_id</code>. 操作者id
     */
    public final TableField<IoSourceEventRecord, Long> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "操作者id");

    /**
     * The column <code>cloud_disk.io_source_event.type</code>. 事件类型
     */
    public final TableField<IoSourceEventRecord, String> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "事件类型");

    /**
     * The column <code>cloud_disk.io_source_event.detail</code>. 数据详情，根据type内容意义不同
     */
    public final TableField<IoSourceEventRecord, String> DETAIL = createField(DSL.name("detail"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "数据详情，根据type内容意义不同");

    /**
     * The column <code>cloud_disk.io_source_event.create_time</code>. 创建时间
     */
    public final TableField<IoSourceEventRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "创建时间");

    /**
     * The column <code>cloud_disk.io_source_event.tenant_id</code>. 租户id
     */
    public final TableField<IoSourceEventRecord, Long> TENANT_ID = createField(DSL.name("tenant_id"), org.jooq.impl.SQLDataType.BIGINT, this, "租户id");

    /**
     * Create a <code>cloud_disk.io_source_event</code> table reference
     */
    public IoSourceEvent() {
        this(DSL.name("io_source_event"), null);
    }

    /**
     * Create an aliased <code>cloud_disk.io_source_event</code> table reference
     */
    public IoSourceEvent(String alias) {
        this(DSL.name(alias), IO_SOURCE_EVENT);
    }

    /**
     * Create an aliased <code>cloud_disk.io_source_event</code> table reference
     */
    public IoSourceEvent(Name alias) {
        this(alias, IO_SOURCE_EVENT);
    }

    private IoSourceEvent(Name alias, Table<IoSourceEventRecord> aliased) {
        this(alias, aliased, null);
    }

    private IoSourceEvent(Name alias, Table<IoSourceEventRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("文档事件表"));
    }

    public <O extends Record> IoSourceEvent(Table<O> child, ForeignKey<O, IoSourceEventRecord> key) {
        super(child, key, IO_SOURCE_EVENT);
    }

    @Override
    public Schema getSchema() {
        return CloudDisk.CLOUD_DISK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.IO_SOURCE_EVENT_PRIMARY);
    }

    @Override
    public Identity<IoSourceEventRecord, Long> getIdentity() {
        return Keys.IDENTITY_IO_SOURCE_EVENT;
    }

    @Override
    public UniqueKey<IoSourceEventRecord> getPrimaryKey() {
        return Keys.KEY_IO_SOURCE_EVENT_PRIMARY;
    }

    @Override
    public List<UniqueKey<IoSourceEventRecord>> getKeys() {
        return Arrays.<UniqueKey<IoSourceEventRecord>>asList(Keys.KEY_IO_SOURCE_EVENT_PRIMARY);
    }

    @Override
    public IoSourceEvent as(String alias) {
        return new IoSourceEvent(DSL.name(alias), this);
    }

    @Override
    public IoSourceEvent as(Name alias) {
        return new IoSourceEvent(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public IoSourceEvent rename(String name) {
        return new IoSourceEvent(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public IoSourceEvent rename(Name name) {
        return new IoSourceEvent(name, null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<Long, Long, Long, Long, String, String, LocalDateTime, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }
}