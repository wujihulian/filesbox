package com.svnlan.home.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/14 14:57
 */
public class SaveUploadDTO {

    //分段文件
    private MultipartFile file;

    //文件md5
    private String hashMd5;

    //当前分段
    private Integer chunk;

    //分段数
    private Integer chunks;

    //文件业务类型
    private String busType;

    private Integer chunkSize;

    private Long userID;

    private BigDecimal score;

    private Long sourceID;

    //缩略图尺寸
    private String thumbSize;
    private Integer isCommon;
    private Long fileID;
    private String path;

    private String overrideName;
    private String content;
    private Double ignoreFileSize;

    private Boolean pushMsgFlag; //是否推送消息	boolean	可选。不传默认为否
    private String taskID;

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getIgnoreFileSize() {
        return ignoreFileSize;
    }

    public void setIgnoreFileSize(Double ignoreFileSize) {
        this.ignoreFileSize = ignoreFileSize;
    }

    public Boolean getPushMsgFlag() {
        return pushMsgFlag;
    }

    public void setPushMsgFlag(Boolean pushMsgFlag) {
        this.pushMsgFlag = pushMsgFlag;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }
//    private Long sourceID;


    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getThumbSize() {
        return thumbSize;
    }

    public void setThumbSize(String thumbSize) {
        this.thumbSize = thumbSize;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getOverrideName() {
        return overrideName;
    }

    public void setOverrideName(String overrideName) {
        this.overrideName = overrideName;
    }
}
