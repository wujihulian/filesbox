package com.svnlan.home.domain;

import java.util.Date;

/**
 * @Author:
 * @Description:
 */
public class CloudFile {
    private Long fileId;
    private String fileName;
    private String filePath;
    private String filePathPre;
    private Long directoryId;
    private String directoryName;
    private Long fileSize;
    private String suffix;
    private Date gmtCreate;
    private Integer isM3u8;
    private Integer appPreview;
    private String previewUrl;
    private String appPreviewUrl;
    private String approvalState;
    private Integer playLength;
    private String directoryState;
    private String thumb;
    private Long userId;
    private Long deleteId;
    private String sourceType;
    private Long busId;
    private Long fkFileId;
    private Integer isCamera;
    private Integer isReview;
    private String resolution;
    private String checksum;
    private String h264Path;
    private Date gmtModified;
    private String description;

    private Integer isDefault;
    private String sequence;
    private Integer subDirectoryCount;
    private Integer isDirectory;
    private Integer isCommon;
    private Long commonSourceId;
    private Long commonSourceCloudId;
    private Long cloudUserId;

    private Integer isH264Preview;

    public Long getCloudUserId() {
        return cloudUserId;
    }

    public void setCloudUserId(Long cloudUserId) {
        this.cloudUserId = cloudUserId;
    }

    public Long getCommonSourceCloudId() {
        return commonSourceCloudId;
    }

    public void setCommonSourceCloudId(Long commonSourceCloudId) {
        this.commonSourceCloudId = commonSourceCloudId;
    }

    public Long getCommonSourceId() {
        return commonSourceId;
    }

    public void setCommonSourceId(Long commonSourceId) {
        this.commonSourceId = commonSourceId;
    }

    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Long directoryId) {
        this.directoryId = directoryId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePathPre() {
        return filePathPre;
    }

    public void setFilePathPre(String filePathPre) {
        this.filePathPre = filePathPre;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getAppPreviewUrl() {
        return appPreviewUrl;
    }

    public void setAppPreviewUrl(String appPreviewUrl) {
        this.appPreviewUrl = appPreviewUrl;
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

    public Integer getIsM3u8() {
        return isM3u8;
    }

    public void setIsM3u8(Integer isM3u8) {
        this.isM3u8 = isM3u8;
    }

    public String getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(String approvalState) {
        this.approvalState = approvalState;
    }

    public Integer getPlayLength() {
        return playLength;
    }

    public void setPlayLength(Integer playLength) {
        this.playLength = playLength;
    }

    public String getDirectoryState() {
        return directoryState;
    }

    public void setDirectoryState(String directoryState) {
        this.directoryState = directoryState;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(Long deleteId) {
        this.deleteId = deleteId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getFkFileId() {
        return fkFileId;
    }

    public void setFkFileId(Long fkFileId) {
        this.fkFileId = fkFileId;
    }

    public Integer getIsCamera() {
        return isCamera;
    }

    public void setIsCamera(Integer isCamera) {
        this.isCamera = isCamera;
    }

    public Integer getIsReview() {
        return isReview;
    }

    public void setIsReview(Integer isReview) {
        this.isReview = isReview;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getH264Path() {
        return h264Path;
    }

    public void setH264Path(String h264Path) {
        this.h264Path = h264Path;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getSubDirectoryCount() {
        return subDirectoryCount;
    }

    public void setSubDirectoryCount(Integer subDirectoryCount) {
        this.subDirectoryCount = subDirectoryCount;
    }

    public Integer getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(Integer isDirectory) {
        this.isDirectory = isDirectory;
    }

    public Integer getIsH264Preview() {
        return isH264Preview;
    }

    public void setIsH264Preview(Integer isH264Preview) {
        this.isH264Preview = isH264Preview;
    }
}
