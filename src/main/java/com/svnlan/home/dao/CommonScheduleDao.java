package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonSchedule;
import org.apache.ibatis.annotations.Param;

/**
 * 功能描述:
 *
 * @author:
 * @date:
 */
public interface CommonScheduleDao {

    int insert(CommonSchedule record);

    int insertSelective(CommonSchedule record);

    CommonSchedule selectByPrimaryKey(String commonScheduleId);

    int updateByPrimaryKeySelective(CommonSchedule record);

    int updateByPrimaryKey(CommonSchedule record);

    int updateOperateTime(String commonScheduleId);

    int getCountById(@Param("commonScheduleId") String commonScheduleId, @Param("gmtModified") String gmtModified);

    /**
       * @Description:验证定时任务是不是当天执行了
       * @params:  [commonScheduleId, gmtModified]
       * @Return:  int
       * @Modified:
       */
    int getCountByDay(@Param("commonScheduleId") String commonScheduleId, @Param("gmtModified") String gmtModified);

    /**
     * @description: 由日志ID得对应定时任务信息
     * @param logScheduleId
     */
    CommonSchedule findCommonScheduleByLogScheduleId(Long logScheduleId);
            
}