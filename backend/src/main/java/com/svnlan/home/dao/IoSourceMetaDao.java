package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSourceMeta;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 16:01
 */
public interface IoSourceMetaDao {

    int delMetaBySourceID(Long sourceID, List<String> list);

    int delMetaBySourceIDList(List<Long> sourceIdList, List<String> list);

    int batchInsert(List<IOSourceMeta> list);

    int insert(IOSourceMeta iOSourceMeta);

    List<IOSourceMeta> getSourceMetaListBySourceID(Long sourceID, List<String> list);

    IOSourceMeta getSourceMetaVoBySourceID(Long sourceID, String key);

    int updateMetaByKey(Long sourceID, String key, String desc);

    String getValueMetaByKey(Long sourceID, String key);

    String getSourceIDMetaByKey(String value, String key, Long tenantId);
}
