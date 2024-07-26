package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/12 13:54
 */
@Data
public class IoSourceAuth {

    private Long id;
    private Long sourceID;
    private Integer targetType;
    private Long targetID;
    private Integer authID;
    private Integer authDefine;
    private Long createTime;
    private Long modifyTime;

    public IoSourceAuth(){}
    public IoSourceAuth(Long sourceID,Integer targetType,Long targetID,Integer authID, Integer authDefine){
        this.sourceID = sourceID;
        this.targetType = targetType;
        this.targetID = targetID;
        this.authID = authID;
        this.authDefine = authDefine;
    }
}
