package com.svnlan.home.dto.convert;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class Mp4H264DTO {
    private List<Long> sourceIdList;
    private String busType;
    private String cardBusType;

    public List<Long> getSourceIdList() {
        return sourceIdList;
    }

    public void setSourceIdList(List<Long> sourceIdList) {
        this.sourceIdList = sourceIdList;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getCardBusType() {
        return cardBusType;
    }

    public void setCardBusType(String cardBusType) {
        this.cardBusType = cardBusType;
    }
}
