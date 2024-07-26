package com.svnlan.home.dto;

import com.svnlan.home.domain.CommonSource;
import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/17 11:28
 */
public class VideoCommonDto extends CheckFileDTO{

    private String pUrl;
    private String fileName;
    private Double beginTime;
    private Double endTime;
    private String taskID;
    private List<VideoCutDto> cutList;
    /** 1 剪切 2 拆分 */
    private String cutType = "1";
    /** 1 分 2 秒 3 帧 */
    private String timeType = "2";
    /** 1 剪辑，2 转码， 3 合并 */
    private String opType = "1";
    private String resolution;
    private String convertSuffix;
    private Integer frameRate;
    private String finalFilePath;
    private String videoPathOrg;
    private String serverUrl;
    private String otherType;
    private String crop;
    private Integer frame;
    /** 封面图片url*/
    private String coverImg;
    private String coverName;
    private Long length;
    private Integer num;
    private Integer isFirst = 0;
    private String cutTime;
    private Integer regeneration;
    private String checkName;
    private List<String> markTextList;
    private List<MarkUrlDto> markUrlList;
    private String key;
    private Integer showPreview;
    private String nameSuffix;
    private Integer isDown;
    private Integer mp4View;
    private String audio;
    private Long sid;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Integer getMp4View() {
        return mp4View;
    }

    public void setMp4View(Integer mp4View) {
        this.mp4View = mp4View;
    }

    public Integer getIsDown() {
        return isDown;
    }

    public void setIsDown(Integer isDown) {
        this.isDown = isDown;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public Integer getShowPreview() {
        return showPreview;
    }

    public void setShowPreview(Integer showPreview) {
        this.showPreview = showPreview;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getMarkTextList() {
        return markTextList;
    }

    public void setMarkTextList(List<String> markTextList) {
        this.markTextList = markTextList;
    }

    public List<MarkUrlDto> getMarkUrlList() {
        return markUrlList;
    }

    public void setMarkUrlList(List<MarkUrlDto> markUrlList) {
        this.markUrlList = markUrlList;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public Integer getRegeneration() {
        return regeneration;
    }

    public void setRegeneration(Integer regeneration) {
        this.regeneration = regeneration;
    }

    public String getCutTime() {
        return cutTime;
    }

    public void setCutTime(String cutTime) {
        this.cutTime = cutTime;
    }

    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    List<CommonSource> convertList;

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Integer getFrame() {
        return frame;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    @Override
    public String getpUrl() {
        return pUrl;
    }

    @Override
    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Double beginTime) {
        this.beginTime = beginTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public List<VideoCutDto> getCutList() {
        return cutList;
    }

    public void setCutList(List<VideoCutDto> cutList) {
        this.cutList = cutList;
    }

    public String getCutType() {
        return cutType;
    }

    public void setCutType(String cutType) {
        this.cutType = cutType;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getConvertSuffix() {
        return convertSuffix;
    }

    public void setConvertSuffix(String convertSuffix) {
        this.convertSuffix = convertSuffix;
    }

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public String getFinalFilePath() {
        return finalFilePath;
    }

    public void setFinalFilePath(String finalFilePath) {
        this.finalFilePath = finalFilePath;
    }

    public String getVideoPathOrg() {
        return videoPathOrg;
    }

    public void setVideoPathOrg(String videoPathOrg) {
        this.videoPathOrg = videoPathOrg;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getOtherType() {
        return otherType;
    }

    public void setOtherType(String otherType) {
        this.otherType = otherType;
    }

    public List<CommonSource> getConvertList() {
        return convertList;
    }

    public void setConvertList(List<CommonSource> convertList) {
        this.convertList = convertList;
    }
}
