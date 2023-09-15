package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 15:58
 */
@Data
public class IOSourceMeta {

    private Long id;
    private Long sourceID;
    private Long createTime;
    private Long modifyTime;
    private String key;
    private String value;

    public IOSourceMeta(){}
    public IOSourceMeta(Long sourceID, String key, String value){
        this.sourceID = sourceID;
        this.key = key;
        this.value = value;
    }
}
