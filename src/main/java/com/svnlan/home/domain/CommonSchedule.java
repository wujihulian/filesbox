package com.svnlan.home.domain;

import java.util.Date;

public class CommonSchedule {
    /**
     * 定时任务标识
     */
    private String commonScheduleId;

    /**
     * 定时任务名称
     */
    private String scheduleName;

    /**
     * 任务重置时间
     */
    private Date gmtModified;

    private Integer frequency;

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getCommonScheduleId() {
        return commonScheduleId;
    }

    public void setCommonScheduleId(String commonScheduleId) {
        this.commonScheduleId = commonScheduleId == null ? null : commonScheduleId.trim();
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName == null ? null : scheduleName.trim();
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "CommonSchedule{" +
                "commonScheduleId='" + commonScheduleId + '\'' +
                ", scheduleName='" + scheduleName + '\'' +
                ", gmtModified=" + gmtModified +
                '}';
    }
}