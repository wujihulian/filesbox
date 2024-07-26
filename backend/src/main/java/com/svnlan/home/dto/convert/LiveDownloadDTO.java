package com.svnlan.home.dto.convert;

/**
 * @Author:
 * @Description:
 */
public class LiveDownloadDTO {
    private Long courseWareId;
    private String type;
    private String FileUrl;
    private Integer platformId;
    private Integer schoolId;
    private Long userId;
    private String cdnType;


    public Long getCourseWareId() {
        return courseWareId;
    }

    public void setCourseWareId(Long courseWareId) {
        this.courseWareId = courseWareId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCdnType() {
        return cdnType;
    }

    public void setCdnType(String cdnType) {
        this.cdnType = cdnType;
    }
}
