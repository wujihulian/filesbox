package com.svnlan.home.domain;

import com.svnlan.home.utils.ObjUtil;

import java.util.Date;

/**
 * 功能描述:定时任务日志
 *
 * @author:
 * @date:
 */
public class LogSchedule {
    /**
     * 任务执行日志主键
     */
    private Long logScheduleId;

    /**
     * 定时任务标识
     */
    private String commonScheduleId;

    /**
     * 任务执行开始时间
     */
    private Date gmtStart;

    /**
     * 任务执行结束时间
     */
    private Date gmtEnd;

    /**
     * 状态，0开始，1执行成功，2执行失败
     */
    private String state;

    /**
     * 备注，如有执行异常，记录异常信息
     */
    private String remark;

    public Long getLogScheduleId() {
        return logScheduleId;
    }

    public void setLogScheduleId(Long logScheduleId) {
        this.logScheduleId = logScheduleId;
    }

    public String getCommonScheduleId() {
        return commonScheduleId;
    }

    public void setCommonScheduleId(String commonScheduleId) {
        this.commonScheduleId = commonScheduleId == null ? null : commonScheduleId.trim();
    }

    public Date getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(Date gmtStart) {
        this.gmtStart = gmtStart;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public LogSchedule initializefield() {
        ObjUtil.initializefield(this);
        return this;
    }

    @Override
    public String toString() {
        return "LogSchedule{" +
                "logScheduleId=" + logScheduleId +
                ", commonScheduleId='" + commonScheduleId + '\'' +
                ", gmtStart=" + gmtStart +
                ", gmtEnd=" + gmtEnd +
                ", state='" + state + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}