package com.svnlan.home.dto.convert;

/**
 * @Author:
 * @Description:
 */
public class VideoCutDTO {
    private Long sourceId;
    private Double startTime;
    private Double endTime;
    private Long courseWareId;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Long getCourseWareId() {
        return courseWareId;
    }

    public void setCourseWareId(Long courseWareId) {
        this.courseWareId = courseWareId;
    }
}
