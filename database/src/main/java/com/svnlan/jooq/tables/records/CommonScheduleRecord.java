/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.CommonSchedule;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 定时任务表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CommonScheduleRecord extends UpdatableRecordImpl<CommonScheduleRecord> implements Record6<String, String, LocalDateTime, Integer, String, Long> {

    private static final long serialVersionUID = -899612474;

    /**
     * Setter for <code>cloud_disk.common_schedule.id</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>cloud_disk.common_schedule.schedule_name</code>. 定时任务名称
     */
    public void setScheduleName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.schedule_name</code>. 定时任务名称
     */
    public String getScheduleName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>cloud_disk.common_schedule.gmt_modified</code>. 任务重置时间
     */
    public void setGmtModified(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.gmt_modified</code>. 任务重置时间
     */
    public LocalDateTime getGmtModified() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>cloud_disk.common_schedule.frequency</code>. 执行频率(秒)
     */
    public void setFrequency(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.frequency</code>. 执行频率(秒)
     */
    public Integer getFrequency() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>cloud_disk.common_schedule.api_url</code>. 手动执行接口地址
     */
    public void setApiUrl(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.api_url</code>. 手动执行接口地址
     */
    public String getApiUrl() {
        return (String) get(4);
    }

    /**
     * Setter for <code>cloud_disk.common_schedule.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.common_schedule.tenant_id</code>. 租户id
     */
    public Long getTenantId() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, LocalDateTime, Integer, String, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, String, LocalDateTime, Integer, String, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return CommonSchedule.COMMON_SCHEDULE.ID;
    }

    @Override
    public Field<String> field2() {
        return CommonSchedule.COMMON_SCHEDULE.SCHEDULE_NAME;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return CommonSchedule.COMMON_SCHEDULE.GMT_MODIFIED;
    }

    @Override
    public Field<Integer> field4() {
        return CommonSchedule.COMMON_SCHEDULE.FREQUENCY;
    }

    @Override
    public Field<String> field5() {
        return CommonSchedule.COMMON_SCHEDULE.API_URL;
    }

    @Override
    public Field<Long> field6() {
        return CommonSchedule.COMMON_SCHEDULE.TENANT_ID;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getScheduleName();
    }

    @Override
    public LocalDateTime component3() {
        return getGmtModified();
    }

    @Override
    public Integer component4() {
        return getFrequency();
    }

    @Override
    public String component5() {
        return getApiUrl();
    }

    @Override
    public Long component6() {
        return getTenantId();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getScheduleName();
    }

    @Override
    public LocalDateTime value3() {
        return getGmtModified();
    }

    @Override
    public Integer value4() {
        return getFrequency();
    }

    @Override
    public String value5() {
        return getApiUrl();
    }

    @Override
    public Long value6() {
        return getTenantId();
    }

    @Override
    public CommonScheduleRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public CommonScheduleRecord value2(String value) {
        setScheduleName(value);
        return this;
    }

    @Override
    public CommonScheduleRecord value3(LocalDateTime value) {
        setGmtModified(value);
        return this;
    }

    @Override
    public CommonScheduleRecord value4(Integer value) {
        setFrequency(value);
        return this;
    }

    @Override
    public CommonScheduleRecord value5(String value) {
        setApiUrl(value);
        return this;
    }

    @Override
    public CommonScheduleRecord value6(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public CommonScheduleRecord values(String value1, String value2, LocalDateTime value3, Integer value4, String value5, Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CommonScheduleRecord
     */
    public CommonScheduleRecord() {
        super(CommonSchedule.COMMON_SCHEDULE);
    }

    /**
     * Create a detached, initialised CommonScheduleRecord
     */
    public CommonScheduleRecord(String id, String scheduleName, LocalDateTime gmtModified, Integer frequency, String apiUrl, Long tenantId) {
        super(CommonSchedule.COMMON_SCHEDULE);

        set(0, id);
        set(1, scheduleName);
        set(2, gmtModified);
        set(3, frequency);
        set(4, apiUrl);
        set(5, tenantId);
    }
}