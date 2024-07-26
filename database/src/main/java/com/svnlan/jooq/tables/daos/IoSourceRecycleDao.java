/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.daos;


import com.svnlan.jooq.tables.IoSourceRecycle;
import com.svnlan.jooq.tables.pojos.IoSourceRecycleModel;
import com.svnlan.jooq.tables.records.IoSourceRecycleRecord;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * 文档回收站
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoSourceRecycleDao extends DAOImpl<IoSourceRecycleRecord, IoSourceRecycleModel, Long> {

    /**
     * Create a new IoSourceRecycleDao without any configuration
     */
    public IoSourceRecycleDao() {
        super(IoSourceRecycle.IO_SOURCE_RECYCLE, IoSourceRecycleModel.class);
    }

    /**
     * Create a new IoSourceRecycleDao with an attached configuration
     */
    public IoSourceRecycleDao(Configuration configuration) {
        super(IoSourceRecycle.IO_SOURCE_RECYCLE, IoSourceRecycleModel.class, configuration);
    }

    @Override
    public Long getId(IoSourceRecycleModel object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchById(Long... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public IoSourceRecycleModel fetchOneById(Long value) {
        return fetchOne(IoSourceRecycle.IO_SOURCE_RECYCLE.ID, value);
    }

    /**
     * Fetch records that have <code>target_type BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfTargetType(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.TARGET_TYPE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>target_type IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByTargetType(Integer... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.TARGET_TYPE, values);
    }

    /**
     * Fetch records that have <code>target_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfTargetId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.TARGET_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>target_id IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByTargetId(Long... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.TARGET_ID, values);
    }

    /**
     * Fetch records that have <code>source_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfSourceId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.SOURCE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>source_id IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchBySourceId(Long... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.SOURCE_ID, values);
    }

    /**
     * Fetch records that have <code>user_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfUserId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.USER_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_id IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByUserId(Long... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.USER_ID, values);
    }

    /**
     * Fetch records that have <code>parent_level BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfParentLevel(String lowerInclusive, String upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.PARENT_LEVEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>parent_level IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByParentLevel(String... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.PARENT_LEVEL, values);
    }

    /**
     * Fetch records that have <code>create_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfCreateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.CREATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_time IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByCreateTime(LocalDateTime... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.CREATE_TIME, values);
    }

    /**
     * Fetch records that have <code>tenant_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceRecycleModel> fetchRangeOfTenantId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceRecycle.IO_SOURCE_RECYCLE.TENANT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tenant_id IN (values)</code>
     */
    public List<IoSourceRecycleModel> fetchByTenantId(Long... values) {
        return fetch(IoSourceRecycle.IO_SOURCE_RECYCLE.TENANT_ID, values);
    }
}