/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.daos;


import com.svnlan.jooq.tables.IoFileContents;
import com.svnlan.jooq.tables.pojos.IoFileContentsModel;
import com.svnlan.jooq.tables.records.IoFileContentsRecord;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * 文件id
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IoFileContentsDao extends DAOImpl<IoFileContentsRecord, IoFileContentsModel, Long> {

    /**
     * Create a new IoFileContentsDao without any configuration
     */
    public IoFileContentsDao() {
        super(IoFileContents.IO_FILE_CONTENTS, IoFileContentsModel.class);
    }

    /**
     * Create a new IoFileContentsDao with an attached configuration
     */
    public IoFileContentsDao(Configuration configuration) {
        super(IoFileContents.IO_FILE_CONTENTS, IoFileContentsModel.class, configuration);
    }

    @Override
    public Long getId(IoFileContentsModel object) {
        return object.getFileId();
    }

    /**
     * Fetch records that have <code>file_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoFileContentsModel> fetchRangeOfFileId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoFileContents.IO_FILE_CONTENTS.FILE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>file_id IN (values)</code>
     */
    public List<IoFileContentsModel> fetchByFileId(Long... values) {
        return fetch(IoFileContents.IO_FILE_CONTENTS.FILE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>file_id = value</code>
     */
    public IoFileContentsModel fetchOneByFileId(Long value) {
        return fetchOne(IoFileContents.IO_FILE_CONTENTS.FILE_ID, value);
    }

    /**
     * Fetch records that have <code>content BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoFileContentsModel> fetchRangeOfContent(String lowerInclusive, String upperInclusive) {
        return fetchRange(IoFileContents.IO_FILE_CONTENTS.CONTENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>content IN (values)</code>
     */
    public List<IoFileContentsModel> fetchByContent(String... values) {
        return fetch(IoFileContents.IO_FILE_CONTENTS.CONTENT, values);
    }

    /**
     * Fetch records that have <code>create_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoFileContentsModel> fetchRangeOfCreateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(IoFileContents.IO_FILE_CONTENTS.CREATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_time IN (values)</code>
     */
    public List<IoFileContentsModel> fetchByCreateTime(LocalDateTime... values) {
        return fetch(IoFileContents.IO_FILE_CONTENTS.CREATE_TIME, values);
    }

    /**
     * Fetch records that have <code>tenant_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<IoFileContentsModel> fetchRangeOfTenantId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(IoFileContents.IO_FILE_CONTENTS.TENANT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tenant_id IN (values)</code>
     */
    public List<IoFileContentsModel> fetchByTenantId(Long... values) {
        return fetch(IoFileContents.IO_FILE_CONTENTS.TENANT_ID, values);
    }
}