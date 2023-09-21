package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/12 14:16
 */
@Data
public class IoSourceAuthVo {

    private Long id;
    private Long sourceID;
    private Integer targetType;
    private Long targetID;
    private Integer authID;
    private Integer authDefine;
    private Long createTime;
    private Long modifyTime;
    private String nickname;
    private Long parentID;
    private String parentLevel;
    private String parentGroupName;
    private String authName;
    private String label;
    private String auth;

    public IoSourceAuthVo(){}
    public IoSourceAuthVo(Integer authID, String authName, String label, String auth){
        this.authID = authID;
        this.authName = authName;
        this.label = label;
        this.auth = auth;
    }
}
