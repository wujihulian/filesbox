/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.daos;


import com.svnlan.jooq.tables.Share;
import com.svnlan.jooq.tables.pojos.ShareModel;
import com.svnlan.jooq.tables.records.ShareRecord;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


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
public class ShareDao extends DAOImpl<ShareRecord, ShareModel, Long> {

    /**
     * Create a new ShareDao without any configuration
     */
    public ShareDao() {
        super(Share.SHARE, ShareModel.class);
    }

    /**
     * Create a new ShareDao with an attached configuration
     */
    public ShareDao(Configuration configuration) {
        super(Share.SHARE, ShareModel.class, configuration);
    }

    @Override
    public Long getId(ShareModel object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<ShareModel> fetchById(Long... values) {
        return fetch(Share.SHARE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public ShareModel fetchOneById(Long value) {
        return fetchOne(Share.SHARE.ID, value);
    }

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<ShareModel> fetchByTitle(String... values) {
        return fetch(Share.SHARE.TITLE, values);
    }

    /**
     * Fetch records that have <code>share_hash BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfShareHash(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.SHARE_HASH, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>share_hash IN (values)</code>
     */
    public List<ShareModel> fetchByShareHash(String... values) {
        return fetch(Share.SHARE.SHARE_HASH, values);
    }

    /**
     * Fetch records that have <code>user_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfUserId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.USER_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_id IN (values)</code>
     */
    public List<ShareModel> fetchByUserId(Long... values) {
        return fetch(Share.SHARE.USER_ID, values);
    }

    /**
     * Fetch records that have <code>source_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfSourceId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.SOURCE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>source_id IN (values)</code>
     */
    public List<ShareModel> fetchBySourceId(Long... values) {
        return fetch(Share.SHARE.SOURCE_ID, values);
    }

    /**
     * Fetch records that have <code>source_path BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfSourcePath(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.SOURCE_PATH, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>source_path IN (values)</code>
     */
    public List<ShareModel> fetchBySourcePath(String... values) {
        return fetch(Share.SHARE.SOURCE_PATH, values);
    }

    /**
     * Fetch records that have <code>url BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfUrl(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.URL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>url IN (values)</code>
     */
    public List<ShareModel> fetchByUrl(String... values) {
        return fetch(Share.SHARE.URL, values);
    }

    /**
     * Fetch records that have <code>is_link BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfIsLink(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.IS_LINK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>is_link IN (values)</code>
     */
    public List<ShareModel> fetchByIsLink(Integer... values) {
        return fetch(Share.SHARE.IS_LINK, values);
    }

    /**
     * Fetch records that have <code>status BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfStatus(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.STATUS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>status IN (values)</code>
     */
    public List<ShareModel> fetchByStatus(Integer... values) {
        return fetch(Share.SHARE.STATUS, values);
    }

    /**
     * Fetch records that have <code>is_share_to BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfIsShareTo(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.IS_SHARE_TO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>is_share_to IN (values)</code>
     */
    public List<ShareModel> fetchByIsShareTo(Integer... values) {
        return fetch(Share.SHARE.IS_SHARE_TO, values);
    }

    /**
     * Fetch records that have <code>password BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfPassword(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.PASSWORD, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>password IN (values)</code>
     */
    public List<ShareModel> fetchByPassword(String... values) {
        return fetch(Share.SHARE.PASSWORD, values);
    }

    /**
     * Fetch records that have <code>time_to BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfTimeTo(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.TIME_TO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>time_to IN (values)</code>
     */
    public List<ShareModel> fetchByTimeTo(Long... values) {
        return fetch(Share.SHARE.TIME_TO, values);
    }

    /**
     * Fetch records that have <code>num_view BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfNumView(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.NUM_VIEW, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>num_view IN (values)</code>
     */
    public List<ShareModel> fetchByNumView(Integer... values) {
        return fetch(Share.SHARE.NUM_VIEW, values);
    }

    /**
     * Fetch records that have <code>num_download BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfNumDownload(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.NUM_DOWNLOAD, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>num_download IN (values)</code>
     */
    public List<ShareModel> fetchByNumDownload(Integer... values) {
        return fetch(Share.SHARE.NUM_DOWNLOAD, values);
    }

    /**
     * Fetch records that have <code>options BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfOptions(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.OPTIONS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>options IN (values)</code>
     */
    public List<ShareModel> fetchByOptions(String... values) {
        return fetch(Share.SHARE.OPTIONS, values);
    }

    /**
     * Fetch records that have <code>create_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfCreateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Share.SHARE.CREATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_time IN (values)</code>
     */
    public List<ShareModel> fetchByCreateTime(LocalDateTime... values) {
        return fetch(Share.SHARE.CREATE_TIME, values);
    }

    /**
     * Fetch records that have <code>modify_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfModifyTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Share.SHARE.MODIFY_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>modify_time IN (values)</code>
     */
    public List<ShareModel> fetchByModifyTime(LocalDateTime... values) {
        return fetch(Share.SHARE.MODIFY_TIME, values);
    }

    /**
     * Fetch records that have <code>tenant_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ShareModel> fetchRangeOfTenantId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.TENANT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tenant_id IN (values)</code>
     */
    public List<ShareModel> fetchByTenantId(Long... values) {
        return fetch(Share.SHARE.TENANT_ID, values);
    }
}
