package com.svnlan.home.schedule;

import com.svnlan.common.LogScheduleStateConstants;
import com.svnlan.home.dao.CommonScheduleDao;
import com.svnlan.home.service.LogScheduleService;
import com.svnlan.home.service.MonitorService;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@Component
public class ConvertSchedule {
    private static final String VIDEO_CONVERT_TIME = "video_convert_time";

    @Resource
    private LogScheduleService logScheduleService;

    @Resource
    CommonScheduleDao commonScheduleDaoImpl;

    @Resource
    private MonitorService monitorService;


    // 每分钟执行一次
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void videoConvertTime(){
        LogUtil.info("检查转码时长任务开始 ： " + DateUtil.getCurrentTime());
        String remark = "";
        String state = LogScheduleStateConstants.SUCCESS;
        //更新前任务时间
        Integer j = commonScheduleDaoImpl.updateOperateTime(VIDEO_CONVERT_TIME);
        if (j < 1) {
            LogUtil.error(VIDEO_CONVERT_TIME + " 检查转码时长 schedule表更新失败");
            return;
        }

        // TODO 入参需修改
        Long body2 = logScheduleService.addLog(VIDEO_CONVERT_TIME, 1L);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            remark = monitorService.videoConvertTime(resultMap);
        } catch (Exception e) {
            remark = "检查转码时长:" + e.getMessage();
            state = LogScheduleStateConstants.FAILURE;
            LogUtil.error(e, "");
        }
        LogUtil.info("检查转码时长," + remark + ", " + JsonUtils.beanToJson(resultMap));
        //修改定时任务状态
        Integer body1 = logScheduleService.updateLog(body2, state, remark);
        if (body1 != 1) {
            LogUtil.error("检查转码时长失败");
        }
        LogUtil.info("检查转码时长任务结束 ： " + DateUtil.getCurrentTime());
    }



}
