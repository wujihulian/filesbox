package com.svnlan.home.service.impl;

import com.svnlan.common.LogScheduleStateConstants;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.LogScheduleDao;
import com.svnlan.home.domain.LogSchedule;
import com.svnlan.home.enums.LogScheduleStateEnum;
import com.svnlan.home.service.LogScheduleService;
import com.svnlan.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:
 * @Date:
 * @Description:
 */
@Service
public class LogScheduleServiceImpl implements LogScheduleService {

    private static final Logger log = LoggerFactory.getLogger("error");

    @Resource
    LogScheduleDao logScheduleDao;

    /**
     * 功能描述: 添加定时任务日志
     *
     * @param commonScheduleId
     * @return int
     */
    @Override
    public Long addLog(String commonScheduleId, Long tenantId) {
        LogSchedule logSchedule = new LogSchedule();
        logSchedule.setCommonScheduleId(commonScheduleId);
        logSchedule.setState(LogScheduleStateConstants.UNSTART);

        int insert;
        LocalDateTime now = LocalDateTime.now();
        logSchedule.setGmtStart(now);
        logSchedule.setGmtEnd(now);
        logSchedule.setTenantId(tenantId);
        try {
            insert = logScheduleDao.insert(logSchedule.initializefield());
        } catch (Exception e) {
            log.error("addLog error=" + e.getMessage());
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode(), e.getMessage());
        }
        if (insert != 1) {
            log.error("添加定时任务失败" + logSchedule);
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode(), CodeMessageEnum.system_error.getMsg());
        }
        return logSchedule.getLogScheduleId();
    }

    @Override
    public int updateLog(Long logScheduleId, String state, String remark) {
        int affectedCount = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("logScheduleId", logScheduleId);
        map.put("state", state);
        map.put("gmtEnd", new Date());
        map.put("remark", remark);
        //
        affectedCount = this.logScheduleDao.updateLogSuccess(map);

        //执行失败才发送告警
        if(LogScheduleStateEnum.FAILURE.getCode().equals(state)) {
            //发送钉钉机器人告警消息
            //this.sendDingTalkMsgForSchedule("发送钉钉机器人告警消息", logScheduleId);
        }
        return affectedCount;
    }

    @Override
//    @Transactional(value = tx,rollbackFor = Exception.class)
    public LogSchedule logScheduleCheckAddTransaction(String scheduleId){
//        LogSchedule logSchedule = logScheduleDao.getLogScheduleInfoLock(scheduleId, new Date());
//        if (ObjectUtils.isEmpty(logSchedule)){
        //2021-06-17 固定插一条好了, 因为现在完成定时, log update的时候, 也只有其中一个实例里的结果数据会写进去
        //还不如所有实例都插一条数据
        LogSchedule logSchedule = new LogSchedule();
        logSchedule.setCommonScheduleId(scheduleId);
        logSchedule.setState(LogScheduleStateConstants.UNSTART);
        logSchedule.setRemark("本次读取日志文件 ");
        long i = this.addLog(scheduleId, 1L);
        if (i < 1) {
            LogUtil.error("读取流量统计定时任务，添加定时任务logSchedule日志失败");
            return null;
        }
        log.info("Add log schedule {} ", logSchedule.toString());
//        }
        return logSchedule;
    }

}
