package com.svnlan.home.service;

import com.svnlan.home.domain.LogSchedule;

/**
 * 功能描述:定时任务service
 *
 */
public interface LogScheduleService {
    /**
     * 功能描述: 添加定时任务日志
     *
     * @param commonScheduleId
     * @return Long
     */
    Long addLog(String commonScheduleId);

    int updateLog(Long logScheduleId, String state, String remark);

    LogSchedule logScheduleCheckAddTransaction(String scheduleId);
}
