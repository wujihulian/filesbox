package com.svnlan.home.schedule;

import com.svnlan.common.LogScheduleStateConstants;
import com.svnlan.home.dao.CommonScheduleDao;
import com.svnlan.home.service.LogScheduleService;
import com.svnlan.home.utils.SourceDownUrlUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/15 11:41
 */
@Component
public class SourceSchedule {

    private static final String CLOUD_DELETE_DOWNLOAD = "cloud_delete_download";

    @Resource
    private LogScheduleService logScheduleService;

    @Resource
    CommonScheduleDao commonScheduleDao;
    @Resource
    SourceDownUrlUtils sourceDownUrlUtils;

    /**
     * @Description: 删除下载定时器
     * @params:  []
     * @Return:  void
     * @Modified:
     */
    @Scheduled(cron = "${schedule.deleteCloudDownload}")
    public void deleteCourseDownload(){
        LogUtil.info("删除下载文件开始");
        String remark = "";
        String state = LogScheduleStateConstants.SUCCESS;
        //更新前任务时间
        Integer j = commonScheduleDao.updateOperateTime(CLOUD_DELETE_DOWNLOAD);
        if (j < 1) {
            LogUtil.error(CLOUD_DELETE_DOWNLOAD + "删除下载文件 schedule表更新失败");
            return;
        }

        Long body2 = logScheduleService.addLog(CLOUD_DELETE_DOWNLOAD);
        try {
            int ret = sourceDownUrlUtils.deleteCommonDownload();
            remark = "删除下载文件，影响" + ret + "条";
        } catch (Exception e) {
            remark = "删除下载文件, 失败:" + e.getMessage();
            state = LogScheduleStateConstants.FAILURE;
            LogUtil.error(e, "删除下载文件失败");
        }
        LogUtil.info("删除下载文件定时器," + remark);
        //修改定时任务状态
        Integer body1 = logScheduleService.updateLog(body2, state, remark);
        if (body1 != 1) {
            LogUtil.error("删除下载文件失败");
        }
    }
}
