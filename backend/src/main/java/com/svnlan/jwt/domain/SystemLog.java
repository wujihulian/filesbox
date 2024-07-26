package com.svnlan.jwt.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Author: slj
 * @Description:
 */
@Data
public class SystemLog {

    private Long id;
    private String sessionID;
    private Long userID;
    private String type;
    private String desc;
    private Long createTime;
    /** '日期'*/
    private Date visitDate;
    /** 1pc , 2h5, 3安卓app, 4 ios-app, 5小程序*/
    private String clientType;
    /** '操作系统'*/
    private String osName;

    public SystemLog(){}
    public SystemLog(Long userID, String type, String desc, String osName, Date visitDate, String clientType, String sessionID){
        this.sessionID = sessionID;
        this.userID = userID;
        this.type = type;
        this.desc = desc;
        this.osName = osName;
        this.visitDate = visitDate;
        this.clientType = clientType;
    }
    public SystemLog(Long userID, String type, String desc, String osName, Date visitDate, String clientType){
        this.sessionID = "";
        this.userID = userID;
        this.type = type;
        this.desc = desc;
        this.osName = osName;
        this.visitDate = visitDate;
        this.clientType = clientType;
    }

}
