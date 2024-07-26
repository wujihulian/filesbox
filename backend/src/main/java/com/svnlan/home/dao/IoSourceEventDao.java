package com.svnlan.home.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.vo.IoSourceEventVo;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:16
 */
public interface IoSourceEventDao {

    int insert(IoSourceEvent ioSourceEvent);

    int batchInsert(List<IoSourceEvent> list);

    List<IoSourceEventVo> getSourceEventBySourceID(Long sourceID, int isFolder);

    Long getSourceEventBySourceIDCount(Long sourceID, int isFolder);
    //    @Select("SELECT DATE(FROM_UNIXTIME(createTime)) date, COUNT(1) count from io_source_event WHERE createTime >= #{timeRange.first} AND createTime <= #{timeRange.second}  GROUP BY date ORDER BY date")
    List<JSONObject> queryFileOperateCount(Pair<LocalDateTime, LocalDateTime> timeRange);

    List<JSONObject> queryVideoFileOperateCount(Pair<LocalDateTime, LocalDateTime> timeRange);
}
