package com.svnlan.home.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.vo.IoSourceEventVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:16
 */
public interface IoSourceEventDao {

    int insert(IoSourceEvent ioSourceEvent);
    int batchInsert(List<IoSourceEvent> list);
    List<IoSourceEventVo> getSourceEventBySourceID(@Param("sourceID") Long sourceID, @Param("isFolder") int isFolder);

    @Select("SELECT DATE(FROM_UNIXTIME(createTime)) `date`, COUNT(1) count from io_source_event WHERE createTime >= #{timeRange.first} AND createTime <= #{timeRange.second}  GROUP BY `date` ORDER BY `date`")
    List<JSONObject> queryFileOperateCount(@Param("timeRange") Pair<Long, Long> timeRange);

    List<JSONObject> queryVideoFileOperateCount(@Param("timeRange") Pair<Long, Long> timeRange);
}
