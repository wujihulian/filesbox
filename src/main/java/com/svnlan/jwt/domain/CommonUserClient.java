package com.svnlan.jwt.domain;

import java.util.Date;

/**
   * @Description: 用户客户端信息
   * @params:
   * @Return:
   * @Author:
   * @Modified:
   */
public class CommonUserClient {
    private Long commonUserClientId;

    private Integer platformId;

    private Integer schoolId;

    private Long userID;

    private String loginName;

    private String realName;

    private Integer sex;

    private String operatingSystem;

    private String resolution;

    private String browser;

    private Date gmtLogin;

    private Integer state;

    private String token;

    private Date gmtTokenCreate;

    private Date gmtTokenGone;

    private String userIp;

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Long getCommonUserClientId() {
        return commonUserClientId;
    }

    public void setCommonUserClientId(Long commonUserClientId) {
        this.commonUserClientId = commonUserClientId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Date getGmtLogin() {
        return gmtLogin;
    }

    public void setGmtLogin(Date gmtLogin) {
        this.gmtLogin = gmtLogin;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGmtTokenCreate() {
        return gmtTokenCreate;
    }

    public void setGmtTokenCreate(Date gmtTokenCreate) {
        this.gmtTokenCreate = gmtTokenCreate;
    }

    public Date getGmtTokenGone() {
        return gmtTokenGone;
    }

    public void setGmtTokenGone(Date gmtTokenGone) {
        this.gmtTokenGone = gmtTokenGone;
    }
}