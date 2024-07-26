package com.svnlan.home.dao;

import com.svnlan.home.domain.LogSchedule;

import java.util.Date;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author:
 * @date:
 */
public interface LogScheduleDao {
    /**
     * 功能描述: 添加日志
     *
     * @param record
     * @return int
     */
    int insert(LogSchedule record);



//    int updateLogSuccess(@Param("logScheduleId") Long logScheduleId, @Param("state") String state, @Param("remark") String remark);


    int updateLogSuccess(Map<String, Object> map);

}