package com.svnlan.jwt.domain;

import lombok.Data;

import java.util.Date;
/**
   * @Description:
   * @params:
   * @Return:
   * @Author:
   * @Modified:
   */
@Data
public class LogLogin {

    private Long logLoginId;

    private Long userID;

    private String userIp;

    private String operatingSystem;

    private String resolution;

    private String browser;

    private Long gmtLogin;

    private String networkOperator;

    private String name;

    private String nickname;

    private String userType;

    private String sex;

    private String domain;

    private String countryCode;

    private String provinceCode;

    private String cityCode;

    private String avatar;

    private String userAgent;
    private String type;

    private String clientType;
    private String sessionID;

    @Override
    public String toString() {
        return "LogLogin{" +
                "logLoginId=" + logLoginId +
                ", userID=" + userID +
                ", userIp='" + userIp + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", resolution='" + resolution + '\'' +
                ", browser='" + browser + '\'' +
                ", gmtLogin=" + gmtLogin +
                ", networkOperator='" + networkOperator + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userType='" + userType + '\'' +
                ", sex='" + sex + '\'' +
                ", domain='" + domain + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sessionID='" + sessionID + '\'' +
                '}';
    }
}