package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSourceMeta;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 16:01
 */
public interface IoSourceMetaDao {

    int delMetaBySourceID(@Param("sourceID") Long sourceID, @Param("list")List<String> list);

    int delMetaBySourceIDList(@Param("sourceIdList") List<Long> sourceIdList, @Param("list")List<String> list);

    int batchInsert(List<IOSourceMeta> list);
    int insert(IOSourceMeta iOSourceMeta);
    List<IOSourceMeta> getSourceMetaListBySourceID(@Param("sourceID") Long sourceID, @Param("list")List<String> list);
    IOSourceMeta getSourceMetaVoBySourceID(@Param("sourceID") Long sourceID, @Param("key")String key);
    List<IOSourceMeta> getSourceMetaListByParam(@Param("sourceIdList") List<Long> sourceIdList, @Param("list")List<String> list);
    int updateMetaByKey(@Param("sourceID") Long sourceID, @Param("key") String key, @Param("desc") String desc);
    String getValueMetaByKey(@Param("sourceID") Long sourceID, @Param("key") String key);
    String getSourceIDMetaByKey(@Param("value") String value, @Param("key") String key);
}
