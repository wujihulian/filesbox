package com.svnlan.jwt.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jwt.domain.SystemLog;
import com.svnlan.user.vo.SystemLogVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description:
 */
public interface SystemLogDao {

    int insert(SystemLog record);
    int batchInsert(List<SystemLog> list);

    List<SystemLogVo> getSystemLogList(Map<String, Object> map);

    @Select("SELECT count(1) FROM system_log WHERE `type` = 'user.index.loginSubmit' AND visit_date = DATE(now())")
    Long getLoginUserCountToday();

    @Select("SELECT COUNT(id) count, DATE(FROM_UNIXTIME(createTime)) `date` FROM system_log WHERE createTime >= #{pair.first} AND createTime <= #{pair.second} AND type = 'user.index.loginSubmit' GROUP BY date")
    List<JSONObject> queryUserLoginCount(@Param("pair") Pair<Long, Long> pair);

    List<JSONObject> queryUserLoginCountStat(@Param("pair")  Pair<Long, Long> pair, Long userId);

    List<SystemLogVo> getSystemLogExportList(Map<String, Object> map);
}
