package com.svnlan.home.vo;

import java.util.Date;

/**
 * @Author:
 * @Description:
 */
public class CommonSourceVO {
    private Long fileID;
    private Long sourceID;

    private String hashMd5;

    private String path;

    private String name;

    private String fileType;

    private Long size;

    private String sourceServer;

    private Integer isPreview;

    private Integer isM3u8;

    private String previewUrl;

    private Integer appPreview;

    private String appPreviewUrl;

    private String thumb;

    private Integer sourceLength;

    private Integer platformId;

    private Long userID;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer state;

    private Integer schoolId;

    private String msg;

    private Integer sourceType;

    private Long gmtCreateTimeStamp;

    private Integer isSwf;

    private String swfPreviewUrl;

    private Boolean isStatic;

    private Boolean isVideoCover;

    private String downloadUrl;

    private String resolution;

    private String busType;

    private Long courseWareId;
    //视频转h264的路径
    private String h264Path;
    //视频转h264是否成功, 0未成功,1成功,2失败
    private Integer isH264Preview;
    private Integer isCommon;
    private Long subCommonSourceId;
    private String remark;
    private String previewPath;

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }

    public Long getSubCommonSourceId() {
        return subCommonSourceId;
    }

    public void setSubCommonSourceId(Long subCommonSourceId) {
        this.subCommonSourceId = subCommonSourceId;
    }

    public String getH264Path() {
        return h264Path;
    }

    public void setH264Path(String h264Path) {
        this.h264Path = h264Path;
    }

    public Integer getIsH264Preview() {
        return isH264Preview;
    }

    public void setIsH264Preview(Integer isH264Preview) {
        this.isH264Preview = isH264Preview;
    }

    public Long getGmtCreateTimeStamp() {
        return gmtCreateTimeStamp;
    }

    public void setGmtCreateTimeStamp(Long gmtCreateTimeStamp) {
        this.gmtCreateTimeStamp = gmtCreateTimeStamp;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSourceServer() {
        return sourceServer;
    }

    public void setSourceServer(String sourceServer) {
        this.sourceServer = sourceServer;
    }

    public Integer getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Integer isPreview) {
        this.isPreview = isPreview;
    }

    public Integer getIsM3u8() {
        return isM3u8;
    }

    public void setIsM3u8(Integer isM3u8) {
        this.isM3u8 = isM3u8;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getAppPreview() {
        return appPreview;
    }

    public void setAppPreview(Integer appPreview) {
        this.appPreview = appPreview;
    }

    public String getAppPreviewUrl() {
        return appPreviewUrl;
    }

    public void setAppPreviewUrl(String appPreviewUrl) {
        this.appPreviewUrl = appPreviewUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getSourceLength() {
        return sourceLength;
    }

    public void setSourceLength(Integer sourceLength) {
        this.sourceLength = sourceLength;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        this.gmtCreateTimeStamp = gmtCreate.getTime();
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getIsSwf() {
        return isSwf;
    }

    public void setIsSwf(Integer isSwf) {
        this.isSwf = isSwf;
    }

    public String getSwfPreviewUrl() {
        return swfPreviewUrl;
    }

    public void setSwfPreviewUrl(String swfPreviewUrl) {
        this.swfPreviewUrl = swfPreviewUrl;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public Boolean getVideoCover() {
        return isVideoCover;
    }

    public void setVideoCover(Boolean videoCover) {
        isVideoCover = videoCover;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Long getCourseWareId() {
        return courseWareId;
    }

    public void setCourseWareId(Long courseWareId) {
        this.courseWareId = courseWareId;
    }
}
