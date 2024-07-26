/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.daos;


import com.svnlan.jooq.tables.LogSchedule;
import com.svnlan.jooq.tables.pojos.LogScheduleModel;
import com.svnlan.jooq.tables.records.LogScheduleRecord;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * 任务执行记录表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LogScheduleDao extends DAOImpl<LogScheduleRecord, LogScheduleModel, Long> {

    /**
     * Create a new LogScheduleDao without any configuration
     */
    public LogScheduleDao() {
        super(LogSchedule.LOG_SCHEDULE, LogScheduleModel.class);
    }

    /**
     * Create a new LogScheduleDao with an attached configuration
     */
    public LogScheduleDao(Configuration configuration) {
        super(LogSchedule.LOG_SCHEDULE, LogScheduleModel.class, configuration);
    }

    @Override
    public Long getId(LogScheduleModel object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<LogScheduleModel> fetchById(Long... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public LogScheduleModel fetchOneById(Long value) {
        return fetchOne(LogSchedule.LOG_SCHEDULE.ID, value);
    }

    /**
     * Fetch records that have <code>common_schedule_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfCommonScheduleId(String lowerInclusive, String upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.COMMON_SCHEDULE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>common_schedule_id IN (values)</code>
     */
    public List<LogScheduleModel> fetchByCommonScheduleId(String... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.COMMON_SCHEDULE_ID, values);
    }

    /**
     * Fetch records that have <code>gmt_start BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfGmtStart(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.GMT_START, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>gmt_start IN (values)</code>
     */
    public List<LogScheduleModel> fetchByGmtStart(LocalDateTime... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.GMT_START, values);
    }

    /**
     * Fetch records that have <code>gmt_end BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfGmtEnd(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.GMT_END, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>gmt_end IN (values)</code>
     */
    public List<LogScheduleModel> fetchByGmtEnd(LocalDateTime... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.GMT_END, values);
    }

    /**
     * Fetch records that have <code>state BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfState(String lowerInclusive, String upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.STATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>state IN (values)</code>
     */
    public List<LogScheduleModel> fetchByState(String... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.STATE, values);
    }

    /**
     * Fetch records that have <code>remark BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfRemark(String lowerInclusive, String upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.REMARK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>remark IN (values)</code>
     */
    public List<LogScheduleModel> fetchByRemark(String... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.REMARK, values);
    }

    /**
     * Fetch records that have <code>tenant_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<LogScheduleModel> fetchRangeOfTenantId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(LogSchedule.LOG_SCHEDULE.TENANT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tenant_id IN (values)</code>
     */
    public List<LogScheduleModel> fetchByTenantId(Long... values) {
        return fetch(LogSchedule.LOG_SCHEDULE.TENANT_ID, values);
    }
}