package com.svnlan.home.dto.convert;

import com.svnlan.home.enums.BusTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class ConvertDTO {
    //相关业务主键id
    @NotNull
    private Long busId;
    //业务类型
    @NotNull
    private String busType;

    //其他类型，s直播白板，c直播摄像头
    private String otherType;

    private String cdnType;

    private BusTypeEnum busTypeEnum;

    //业务类型
    private String businessType;
    //业务ID
    private String businessId;

    private String opType;

    private Long userID;
    private String domain;
    private Integer isNew;

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public BusTypeEnum getBusTypeEnum() {
        return busTypeEnum;
    }

    public void setBusTypeEnum(BusTypeEnum busTypeEnum) {
        this.busTypeEnum = busTypeEnum;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public String getOtherType() {
        return otherType;
    }

    public void setOtherType(String otherType) {
        this.otherType = otherType;
    }

    public String getCdnType() {
        return cdnType;
    }

    public void setCdnType(String cdnType) {
        this.cdnType = cdnType;
    }
}
