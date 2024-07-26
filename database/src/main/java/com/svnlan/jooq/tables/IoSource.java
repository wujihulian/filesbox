/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables;


import com.svnlan.jooq.CloudDisk;
import com.svnlan.jooq.Indexes;
import com.svnlan.jooq.Keys;
import com.svnlan.jooq.tables.records.IoSourceRecord;

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
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 文档数据表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoSource extends TableImpl<IoSourceRecord> {

    private static final long serialVersionUID = -842151576;

    /**
     * The reference instance of <code>cloud_disk.io_source</code>
     */
    public static final IoSource IO_SOURCE = new IoSource();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<IoSourceRecord> getRecordType() {
        return IoSourceRecord.class;
    }

    /**
     * The column <code>cloud_disk.io_source.id</code>.
     */
    public final TableField<IoSourceRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>cloud_disk.io_source.source_hash</code>. id的hash
     */
    public final TableField<IoSourceRecord, String> SOURCE_HASH = createField(DSL.name("source_hash"), org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false), this, "id的hash");

    /**
     * The column <code>cloud_disk.io_source.type</code>. 文档类型 1 文档 2 图片 3 音乐 4 视频 5 压缩包 6 其他
     */
    public final TableField<IoSourceRecord, Integer> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.INTEGER, this, "文档类型 1 文档 2 图片 3 音乐 4 视频 5 压缩包 6 其他");

    /**
     * The column <code>cloud_disk.io_source.target_type</code>. 文档所属类型 (0-sys,1-user,2-group)
     */
    public final TableField<IoSourceRecord, Integer> TARGET_TYPE = createField(DSL.name("target_type"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "文档所属类型 (0-sys,1-user,2-group)");

    /**
     * The column <code>cloud_disk.io_source.target_id</code>. 拥有者对象id
     */
    public final TableField<IoSourceRecord, Long> TARGET_ID = createField(DSL.name("target_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "拥有者对象id");

    /**
     * The column <code>cloud_disk.io_source.create_user</code>. 创建者id
     */
    public final TableField<IoSourceRecord, Long> CREATE_USER = createField(DSL.name("create_user"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "创建者id");

    /**
     * The column <code>cloud_disk.io_source.modify_user</code>. 最后修改者
     */
    public final TableField<IoSourceRecord, Long> MODIFY_USER = createField(DSL.name("modify_user"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "最后修改者");

    /**
     * The column <code>cloud_disk.io_source.is_folder</code>. 是否为文件夹(0否,1是)
     */
    public final TableField<IoSourceRecord, Integer> IS_FOLDER = createField(DSL.name("is_folder"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "是否为文件夹(0否,1是)");

    /**
     * The column <code>cloud_disk.io_source.name</code>. 文件名
     */
    public final TableField<IoSourceRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "文件名");

    /**
     * The column <code>cloud_disk.io_source.file_type</code>. 文件扩展名，文件夹则为空
     */
    public final TableField<IoSourceRecord, String> FILE_TYPE = createField(DSL.name("file_type"), org.jooq.impl.SQLDataType.VARCHAR(10).nullable(false), this, "文件扩展名，文件夹则为空");

    /**
     * The column <code>cloud_disk.io_source.parent_id</code>. 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建
     */
    public final TableField<IoSourceRecord, Long> PARENT_ID = createField(DSL.name("parent_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建");

    /**
     * The column <code>cloud_disk.io_source.parent_level</code>. 父路径id; 例如:  ,2,5,10,
     */
    public final TableField<IoSourceRecord, String> PARENT_LEVEL = createField(DSL.name("parent_level"), org.jooq.impl.SQLDataType.VARCHAR(2000).nullable(false), this, "父路径id; 例如:  ,2,5,10,");

    /**
     * The column <code>cloud_disk.io_source.file_id</code>. 对应存储资源id,文件夹则该处为0
     */
    public final TableField<IoSourceRecord, Long> FILE_ID = createField(DSL.name("file_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "对应存储资源id,文件夹则该处为0");

    /**
     * The column <code>cloud_disk.io_source.is_delete</code>. 是否删除(0-正常 1-已删除)
     */
    public final TableField<IoSourceRecord, Integer> IS_DELETE = createField(DSL.name("is_delete"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "是否删除(0-正常 1-已删除)");

    /**
     * The column <code>cloud_disk.io_source.size</code>. 占用空间大小
     */
    public final TableField<IoSourceRecord, Long> SIZE = createField(DSL.name("size"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "占用空间大小");

    /**
     * The column <code>cloud_disk.io_source.sort</code>. 排序/置顶
     */
    public final TableField<IoSourceRecord, Integer> SORT = createField(DSL.name("sort"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "排序/置顶");

    /**
     * The column <code>cloud_disk.io_source.can_share</code>. 是否可以分享 1 正常 0 禁止分享
     */
    public final TableField<IoSourceRecord, Integer> CAN_SHARE = createField(DSL.name("can_share"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.inline("1", org.jooq.impl.SQLDataType.INTEGER)), this, "是否可以分享 1 正常 0 禁止分享");

    /**
     * The column <code>cloud_disk.io_source.convert_size</code>. 转码文件占用空间大小
     */
    public final TableField<IoSourceRecord, Long> CONVERT_SIZE = createField(DSL.name("convert_size"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "转码文件占用空间大小");

    /**
     * The column <code>cloud_disk.io_source.thumb_size</code>. 缩略图占用空间
     */
    public final TableField<IoSourceRecord, Long> THUMB_SIZE = createField(DSL.name("thumb_size"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "缩略图占用空间");

    /**
     * The column <code>cloud_disk.io_source.storage_id</code>. 磁盘ID
     */
    public final TableField<IoSourceRecord, Integer> STORAGE_ID = createField(DSL.name("storage_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "磁盘ID");

    /**
     * The column <code>cloud_disk.io_source.create_time</code>. 创建时间
     */
    public final TableField<IoSourceRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "创建时间");

    /**
     * The column <code>cloud_disk.io_source.modify_time</code>. 最后修改时间
     */
    public final TableField<IoSourceRecord, LocalDateTime> MODIFY_TIME = createField(DSL.name("modify_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "最后修改时间");

    /**
     * The column <code>cloud_disk.io_source.view_time</code>. 最后访问时间
     */
    public final TableField<IoSourceRecord, LocalDateTime> VIEW_TIME = createField(DSL.name("view_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "最后访问时间");

    /**
     * The column <code>cloud_disk.io_source.tenant_id</code>. 租户id
     */
    public final TableField<IoSourceRecord, Long> TENANT_ID = createField(DSL.name("tenant_id"), org.jooq.impl.SQLDataType.BIGINT, this, "租户id");

    /**
     * The column <code>cloud_disk.io_source.name_pinyin</code>. 拼音全称
     */
    public final TableField<IoSourceRecord, String> NAME_PINYIN = createField(DSL.name("name_pinyin"), org.jooq.impl.SQLDataType.VARCHAR(2048), this, "拼音全称");

    /**
     * The column <code>cloud_disk.io_source.name_pinyin_simple</code>. 拼音首字母
     */
    public final TableField<IoSourceRecord, String> NAME_PINYIN_SIMPLE = createField(DSL.name("name_pinyin_simple"), org.jooq.impl.SQLDataType.VARCHAR(256), this, "拼音首字母");

    /**
     * The column <code>cloud_disk.io_source.description</code>. 描述说明
     */
    public final TableField<IoSourceRecord, String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR(1024), this, "描述说明");

    /**
     * Create a <code>cloud_disk.io_source</code> table reference
     */
    public IoSource() {
        this(DSL.name("io_source"), null);
    }

    /**
     * Create an aliased <code>cloud_disk.io_source</code> table reference
     */
    public IoSource(String alias) {
        this(DSL.name(alias), IO_SOURCE);
    }

    /**
     * Create an aliased <code>cloud_disk.io_source</code> table reference
     */
    public IoSource(Name alias) {
        this(alias, IO_SOURCE);
    }

    private IoSource(Name alias, Table<IoSourceRecord> aliased) {
        this(alias, aliased, null);
    }

    private IoSource(Name alias, Table<IoSourceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("文档数据表"));
    }

    public <O extends Record> IoSource(Table<O> child, ForeignKey<O, IoSourceRecord> key) {
        super(child, key, IO_SOURCE);
    }

    @Override
    public Schema getSchema() {
        return CloudDisk.CLOUD_DISK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.IO_SOURCE_IO_SOURCE_SIZE, Indexes.IO_SOURCE_PRIMARY);
    }

    @Override
    public Identity<IoSourceRecord, Long> getIdentity() {
        return Keys.IDENTITY_IO_SOURCE;
    }

    @Override
    public UniqueKey<IoSourceRecord> getPrimaryKey() {
        return Keys.KEY_IO_SOURCE_PRIMARY;
    }

    @Override
    public List<UniqueKey<IoSourceRecord>> getKeys() {
        return Arrays.<UniqueKey<IoSourceRecord>>asList(Keys.KEY_IO_SOURCE_PRIMARY);
    }

    @Override
    public IoSource as(String alias) {
        return new IoSource(DSL.name(alias), this);
    }

    @Override
    public IoSource as(Name alias) {
        return new IoSource(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public IoSource rename(String name) {
        return new IoSource(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public IoSource rename(Name name) {
        return new IoSource(name, null);
    }
}