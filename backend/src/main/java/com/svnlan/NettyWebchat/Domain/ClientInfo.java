package com.svnlan.NettyWebchat.Domain;

import com.svnlan.enums.BusinessTypeEnum;

/**
 * @Author:
 * @Description:
 */
public class ClientInfo {
    private String userAgent;
    private String ip;
    private String roomName;
    private Long userId;
    private Boolean platformManager;
    private String groupId;
    private String name;
    private Boolean isCommon;
    private String from;
    private Boolean isLogin;
    private String uuid;
    private Long loginTime;
    //大班分组序号
    private Integer groupIndex;
    private String groupName;

    //游客ID
    private String visitorId;

    private Boolean isDevice;


    //业务类型:common-通用；scanLogin-扫码登录
    private String businessType = BusinessTypeEnum.COMMON.getCode();

    private String referer;

    //登录方式
    private Integer CSLoginFrom;
    //客服加密串
    private String CServiceKey;
    //上次发送消息的时间戳
    private Long lastMsgTimestamp;
    //是否客服人员
    private Boolean isStaff;
    //下线, 重复登录被踢
    private Boolean offline;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getPlatformManager() {
        return platformManager;
    }

    public void setPlatformManager(Boolean platformManager) {
        this.platformManager = platformManager;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCommon() {
        return isCommon;
    }

    public void setCommon(Boolean common) {
        isCommon = common;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public Boolean getDevice() {
        return isDevice;
    }

    public void setDevice(Boolean device) {
        isDevice = device;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }


    public Integer getCSLoginFrom() {
        return CSLoginFrom;
    }

    public void setCSLoginFrom(Integer CSLoginFrom) {
        this.CSLoginFrom = CSLoginFrom;
    }

    public String getCServiceKey() {
        return CServiceKey;
    }

    public void setCServiceKey(String CServiceKey) {
        this.CServiceKey = CServiceKey;
    }

    public Long getLastMsgTimestamp() {
        return lastMsgTimestamp;
    }

    public void setLastMsgTimestamp(Long lastMsgTimestamp) {
        this.lastMsgTimestamp = lastMsgTimestamp;
    }

    public Boolean getStaff() {
        return isStaff;
    }

    public void setStaff(Boolean staff) {
        isStaff = staff;
    }

    public Boolean getOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }
}
