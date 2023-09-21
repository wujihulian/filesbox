package com.svnlan.NettyWebchat.Domain;

import java.util.List;
import java.util.Set;

/**
 * @Author:
 * @Description: 房间内广播
 */
public class RoomMsg {

    /** 房间名 */
    private String roomName;

    /** 私聊 */
    private Long uid;

    /** 被屏蔽的session */
    private String sessionId;

    /** 被屏蔽的uid集合 */
    private Set<Long> quarantineUid;

    /** 消息体 */
    private String message;

    /** 发送给多个用户id */
    private List<Long> userIdList;

    /** 抽样发送比例 */
    private Long sampleRate;

    private Long sendToUid;

    private String channelId;

    private String groupNum;

    private String groupName;

    private Integer groupOnlineCount;

    //是否公开课
    private Integer isPublic;

    //包含游客的抽样发送比例
    private Long visitorSampleRate;

    private String visitorId;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Set<Long> getQuarantineUid() {
        return quarantineUid;
    }

    public void setQuarantineUid(Set<Long> quarantineUid) {
        this.quarantineUid = quarantineUid;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public Long getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Long sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Long getSendToUid() {
        return sendToUid;
    }

    public void setSendToUid(Long sendToUid) {
        this.sendToUid = sendToUid;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupOnlineCount() {
        return groupOnlineCount;
    }

    public void setGroupOnlineCount(Integer groupOnlineCount) {
        this.groupOnlineCount = groupOnlineCount;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Long getVisitorSampleRate() {
        return visitorSampleRate;
    }

    public void setVisitorSampleRate(Long visitorSampleRate) {
        this.visitorSampleRate = visitorSampleRate;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }
}
