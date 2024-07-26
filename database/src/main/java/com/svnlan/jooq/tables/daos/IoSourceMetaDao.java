/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.daos;


import com.svnlan.jooq.tables.IoSourceMeta;
import com.svnlan.jooq.tables.pojos.IoSourceMetaModel;
import com.svnlan.jooq.tables.records.IoSourceMetaRecord;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * 文档扩展表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoSourceMetaDao extends DAOImpl<IoSourceMetaRecord, IoSourceMetaModel, Long> {

    /**
     * Create a new IoSourceMetaDao without any configuration
     */
    public IoSourceMetaDao() {
        super(IoSourceMeta.IO_SOURCE_META, IoSourceMetaModel.class);
    }

    /**
     * Create a new IoSourceMetaDao with an attached configuration
     */
    public IoSourceMetaDao(Configuration configuration) {
        super(IoSourceMeta.IO_SOURCE_META, IoSourceMetaModel.class, configuration);
    }

    @Override
    public Long getId(IoSourceMetaModel object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchById(Long... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public IoSourceMetaModel fetchOneById(Long value) {
        return fetchOne(IoSourceMeta.IO_SOURCE_META.ID, value);
    }

    /**
     * Fetch records that have <code>source_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfSourceId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.SOURCE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>source_id IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchBySourceId(Long... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.SOURCE_ID, values);
    }

    /**
     * Fetch records that have <code>key_string BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfKeyString(String lowerInclusive, String upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.KEY_STRING, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>key_string IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchByKeyString(String... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.KEY_STRING, values);
    }

    /**
     * Fetch records that have <code>value_text BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfValueText(String lowerInclusive, String upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.VALUE_TEXT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>value_text IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchByValueText(String... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.VALUE_TEXT, values);
    }

    /**
     * Fetch records that have <code>create_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfCreateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.CREATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_time IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchByCreateTime(LocalDateTime... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.CREATE_TIME, values);
    }

    /**
     * Fetch records that have <code>modify_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfModifyTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.MODIFY_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>modify_time IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchByModifyTime(LocalDateTime... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.MODIFY_TIME, values);
    }

    /**
     * Fetch records that have <code>tenant_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoSourceMetaModel> fetchRangeOfTenantId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoSourceMeta.IO_SOURCE_META.TENANT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tenant_id IN (values)</code>
     */
    public List<IoSourceMetaModel> fetchByTenantId(Long... values) {
        return fetch(IoSourceMeta.IO_SOURCE_META.TENANT_ID, values);
    }
}