package com.svnlan.home.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class GetAttachmentDTO {
    @NotNull
    private Long busId;
    @NotNull
    private String busType;

    private Long sourceID;
    @NotNull
    private String key;
    private Boolean viewSwf;
    private String busTypeCode;

    private Boolean fromWareDown;

    private String attachmentType;

    //用于兼容组合主键的业务
    private String busCode;
    private String targetServerNameForOverride;

    //获取word转pdf的文件
    private Integer forwtop = 0;

    private Integer getInfo = 0;
    private Integer view ;

    private String s;
    private String shareCode;
    private String d;
    private Long f;

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public Long getF() {
        return f;
    }

    public void setF(Long f) {
        this.f = f;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getViewSwf() {
        return viewSwf;
    }

    public void setViewSwf(Boolean viewSwf) {
        this.viewSwf = viewSwf;
    }

    public String getBusTypeCode() {
        return busTypeCode;
    }

    public void setBusTypeCode(String busTypeCode) {
        this.busTypeCode = busTypeCode;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Boolean getFromWareDown() {
        return fromWareDown;
    }

    public void setFromWareDown(Boolean fromWareDown) {
        this.fromWareDown = fromWareDown;
    }

    public String getTargetServerNameForOverride() {
        return targetServerNameForOverride;
    }

    public void setTargetServerNameForOverride(String targetServerNameForOverride) {
        this.targetServerNameForOverride = targetServerNameForOverride;
    }

    public Integer getForwtop() {
        return forwtop;
    }

    public void setForwtop(Integer forwtop) {
        this.forwtop = forwtop;
    }

    public Integer getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(Integer getInfo) {
        this.getInfo = getInfo;
    }
}
