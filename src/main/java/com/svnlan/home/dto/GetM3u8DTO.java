package com.svnlan.home.dto;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class GetM3u8DTO {
    private String token;
    private Long sourceID;
    private String idType;
    private Integer isCamera;
    private String idType2;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public Integer getIsCamera() {
        return isCamera;
    }

    public void setIsCamera(Integer isCamera) {
        this.isCamera = isCamera;
    }

    public String getIdType2() {
        return idType2;
    }

    public void setIdType2(String idType2) {
        this.idType2 = idType2;
    }
}
