package com.svnlan.home.dto;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/18 13:12
 */
public class VideoCutDto {

    private Double beginTime;
    private Double duration;
    private String name;
    private String path;
    private String pUrl;
    private String fileName;
    private String videoPathOrg;
    private String captions;
    /** 时长*/
    private Double length;
    /** 过渡特效 */
    private String transition;
    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public Double getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Double beginTime) {
        this.beginTime = beginTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getVideoPathOrg() {
        return videoPathOrg;
    }

    public void setVideoPathOrg(String videoPathOrg) {
        this.videoPathOrg = videoPathOrg;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }
}
