package com.svnlan.user.schedule;

import com.svnlan.common.LogScheduleStateConstants;
import com.svnlan.home.dao.CommonScheduleDao;
import com.svnlan.home.service.LogScheduleService;
import com.svnlan.user.service.MsgWarningScheduleService;
import com.svnlan.user.service.SystemSettingService;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/7 17:05
 */
@Component
@Profile({"dev","test", "pre", "pro"})
public class MsgWarningSchedule {


    private static final String msgWarningSchedule = "cloud_msg_warning";

    @Resource
    private LogScheduleService logScheduleService;

    @Resource
    CommonScheduleDao commonScheduleDao;
    @Resource
    MsgWarningScheduleService msgWarningScheduleService;
    @Resource
    SystemSettingService systemSettingService;

    @Value("${environment.type}")
    private String environmentType;


    /**
     * @Description: 消息预警定时任务
     * @params:  []
     * @Return:  void
     * @Modified:
     */
    @Scheduled(cron = "${schedule.msgWarning}")
    public void msgWarningSchedule(){
        if (ObjectUtils.isEmpty(environmentType)){
            return;
        }
        String remark = "";
        String state = LogScheduleStateConstants.SUCCESS;
        //更新前任务时间
        Integer j = commonScheduleDao.updateOperateTime(msgWarningSchedule);
        if (j < 1) {
            LogUtil.error(msgWarningSchedule + "消息预警 schedule表更新失败");
            return;
        }
        PluginListVo pluginListVo = systemSettingService.getMsgWarningSetting();
        if (ObjectUtils.isEmpty(pluginListVo) || ObjectUtils.isEmpty(pluginListVo.getEnable()) || !"1".equals(pluginListVo.getEnable())
                || ObjectUtils.isEmpty(pluginListVo.getWarnType())|| ObjectUtils.isEmpty(pluginListVo.getUseRatio()) || ObjectUtils.isEmpty(pluginListVo.getUseTime())
                ){
            return ;
        }
        if (ObjectUtils.isEmpty(pluginListVo.getDingUrl()) && ObjectUtils.isEmpty(pluginListVo.getWechatUrl()) && ObjectUtils.isEmpty(pluginListVo.getEmail())){
            return ;
        }

        Long body2 = logScheduleService.addLog(msgWarningSchedule);
        try {
            String msg = msgWarningScheduleService.sendMsgWarming(pluginListVo);
            remark = "消息预警成功" + msg;
        } catch (Exception e) {
            remark = "消息预警, 失败:" + e.getMessage();
            state = LogScheduleStateConstants.FAILURE;
            LogUtil.error(e, "消息预警失败");
        }
        LogUtil.info("消息预警定时器," + remark);
        //修改定时任务状态
        Integer body1 = logScheduleService.updateLog(body2, state, remark);
        if (body1 != 1) {
            LogUtil.error("消息预警失败");
        }
    }
}
