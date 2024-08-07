/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables;


import com.svnlan.jooq.CloudDisk;
import com.svnlan.jooq.Indexes;
import com.svnlan.jooq.Keys;
import com.svnlan.jooq.tables.records.CommonConvertRecord;

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
import org.jooq.Row13;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * 转码表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CommonConvert extends TableImpl<CommonConvertRecord> {

    private static final long serialVersionUID = -1523964753;

    /**
     * The reference instance of <code>cloud_disk.common_convert</code>
     */
    public static final CommonConvert COMMON_CONVERT = new CommonConvert();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CommonConvertRecord> getRecordType() {
        return CommonConvertRecord.class;
    }

    /**
     * The column <code>cloud_disk.common_convert.id</code>. 转码主键
     */
    public final TableField<CommonConvertRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "转码主键");

    /**
     * The column <code>cloud_disk.common_convert.source_id</code>. 对应存储资源id
     */
    public final TableField<CommonConvertRecord, Long> SOURCE_ID = createField(DSL.name("source_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "对应存储资源id");

    /**
     * The column <code>cloud_disk.common_convert.file_id</code>. 对应存储资源fileID
     */
    public final TableField<CommonConvertRecord, Long> FILE_ID = createField(DSL.name("file_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "对应存储资源fileID");

    /**
     * The column <code>cloud_disk.common_convert.user_id</code>. 执行人ID
     */
    public final TableField<CommonConvertRecord, Long> USER_ID = createField(DSL.name("user_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "执行人ID");

    /**
     * The column <code>cloud_disk.common_convert.name</code>. 文件名
     */
    public final TableField<CommonConvertRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "文件名");

    /**
     * The column <code>cloud_disk.common_convert.frequency_count</code>. 执行次数
     */
    public final TableField<CommonConvertRecord, Integer> FREQUENCY_COUNT = createField(DSL.name("frequency_count"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "执行次数");

    /**
     * The column <code>cloud_disk.common_convert.state</code>. 状态，0开始，1执行成功，2执行失败
     */
    public final TableField<CommonConvertRecord, String> STATE = createField(DSL.name("state"), org.jooq.impl.SQLDataType.CHAR(1).nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.CHAR)), this, "状态，0开始，1执行成功，2执行失败");

    /**
     * The column <code>cloud_disk.common_convert.remark</code>. 备注，如有执行异常，记录异常信息
     */
    public final TableField<CommonConvertRecord, String> REMARK = createField(DSL.name("remark"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "备注，如有执行异常，记录异常信息");

    /**
     * The column <code>cloud_disk.common_convert.schedule_frequency_count</code>. 自动执行次数
     */
    public final TableField<CommonConvertRecord, Integer> SCHEDULE_FREQUENCY_COUNT = createField(DSL.name("schedule_frequency_count"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.INTEGER)), this, "自动执行次数");

    /**
     * The column <code>cloud_disk.common_convert.modify_time</code>. 最后修改时间
     */
    public final TableField<CommonConvertRecord, LocalDateTime> MODIFY_TIME = createField(DSL.name("modify_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "最后修改时间");

    /**
     * The column <code>cloud_disk.common_convert.create_time</code>. 创建时间
     */
    public final TableField<CommonConvertRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "创建时间");

    /**
     * The column <code>cloud_disk.common_convert.schedule_time</code>. 定时任务执行修改时间
     */
    public final TableField<CommonConvertRecord, LocalDateTime> SCHEDULE_TIME = createField(DSL.name("schedule_time"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "定时任务执行修改时间");

    /**
     * The column <code>cloud_disk.common_convert.tenant_id</code>. 租户id
     */
    public final TableField<CommonConvertRecord, Long> TENANT_ID = createField(DSL.name("tenant_id"), org.jooq.impl.SQLDataType.BIGINT, this, "租户id");

    /**
     * Create a <code>cloud_disk.common_convert</code> table reference
     */
    public CommonConvert() {
        this(DSL.name("common_convert"), null);
    }

    /**
     * Create an aliased <code>cloud_disk.common_convert</code> table reference
     */
    public CommonConvert(String alias) {
        this(DSL.name(alias), COMMON_CONVERT);
    }

    /**
     * Create an aliased <code>cloud_disk.common_convert</code> table reference
     */
    public CommonConvert(Name alias) {
        this(alias, COMMON_CONVERT);
    }

    private CommonConvert(Name alias, Table<CommonConvertRecord> aliased) {
        this(alias, aliased, null);
    }

    private CommonConvert(Name alias, Table<CommonConvertRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("转码表"));
    }

    public <O extends Record> CommonConvert(Table<O> child, ForeignKey<O, CommonConvertRecord> key) {
        super(child, key, COMMON_CONVERT);
    }

    @Override
    public Schema getSchema() {
        return CloudDisk.CLOUD_DISK;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.COMMON_CONVERT_COMMON_CONVERT_STATE, Indexes.COMMON_CONVERT_ID);
    }

    @Override
    public Identity<CommonConvertRecord, Long> getIdentity() {
        return Keys.IDENTITY_COMMON_CONVERT;
    }

    @Override
    public List<UniqueKey<CommonConvertRecord>> getKeys() {
        return Arrays.<UniqueKey<CommonConvertRecord>>asList(Keys.KEY_COMMON_CONVERT_ID);
    }

    @Override
    public CommonConvert as(String alias) {
        return new CommonConvert(DSL.name(alias), this);
    }

    @Override
    public CommonConvert as(Name alias) {
        return new CommonConvert(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonConvert rename(String name) {
        return new CommonConvert(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CommonConvert rename(Name name) {
        return new CommonConvert(name, null);
    }

    // -------------------------------------------------------------------------
    // Row13 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row13<Long, Long, Long, Long, String, Integer, String, String, Integer, LocalDateTime, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }
}
