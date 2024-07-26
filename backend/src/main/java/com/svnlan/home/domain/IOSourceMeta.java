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
    private String keyString;
    private String valueText;
    private Long tenantId;
    public IOSourceMeta(){}
    public IOSourceMeta(Long sourceID, String key, String value){
        this.sourceID = sourceID;
        this.key = key;
        this.value = value;
        this.keyString = key;
        this.valueText = value;
    }
    public IOSourceMeta(Long sourceID, String key, String value, Long tenantId){
        this.sourceID = sourceID;
        this.key = key;
        this.value = value;
        this.keyString = key;
        this.valueText = value;
        this.tenantId = tenantId;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
        this.key = keyString;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        this.value = valueText;
    }

    public void setKey(String key) {
        this.key = key;
        this.keyString = key;
    }

    public void setValue(String value) {
        this.value = value;
        this.valueText = value;
    }
}
