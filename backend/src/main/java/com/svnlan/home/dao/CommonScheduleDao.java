package com.svnlan.home.dao;

/**
 * 功能描述:
 *
 * @author:
 * @date:
 */
public interface CommonScheduleDao {


    int updateOperateTime(String commonScheduleId);

    /**
       * @Description:验证定时任务是不是当天执行了
       * @params:  [commonScheduleId, gmtModified]
       * @Return:  int
       * @Modified:
       */

}