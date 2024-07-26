package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/29 13:57
 */
@Data
public class LogDescVo {

    private Long sourceID;
    /** 父ID */
    private Long sourceParent;
    /** 文件名 */
    private String pathName;
    private String pathDisplay;
    private Long userID;
    private String type;
    /** 目标ID */
    private Long sourceTarget;
    private String country;
    private String nickName;
    private String ip;
    private String browser;
    private String name;
    private String ua;
    private String network;
    private String sourceParentName;
    private String fromName;
    private Long fromSourceID;

}
