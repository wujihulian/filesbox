package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:20
 */
@Data
public class IoSourceEvent {

    private Long id;
    private Long sourceID;
    private Long sourceParent;
    private Long userID;
    private String type;
    private String desc;
    private Long createTime;
    private Long tenantId;

    public IoSourceEvent(){}
    public IoSourceEvent(Long sourceID, Long sourceParent, Long userID, String type, String desc){
        this.sourceID = sourceID;
        this.userID = userID;
        this.sourceParent = sourceParent;
        this.type = type;
        this.desc = desc;
    }

    public IoSourceEvent(Long sourceID, Long sourceParent, Long userID, String type, String desc, Long tenantId){
        this.sourceID = sourceID;
        this.userID = userID;
        this.sourceParent = sourceParent;
        this.type = type;
        this.desc = desc;
        this.tenantId = tenantId;
    }
}
