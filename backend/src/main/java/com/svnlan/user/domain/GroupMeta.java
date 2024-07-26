package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 17:00
 */
@Data
public class GroupMeta {

    private Long id;
    private Long groupID;
    private Long createTime;
    private Long modifyTime;
    private String key;
    private String value;
    private String keyString;
    private String valueText;
    private Long tenantId;
    public GroupMeta(){}
    public GroupMeta(Long groupID,String key,String value){
        this.groupID = groupID;
        this.key = key;
        this.value = value;
        this.keyString = key;
        this.valueText = value;
    }
    public GroupMeta(Long groupID,String key,String value, Long tenantId){
        this.groupID = groupID;
        this.key = key;
        this.value = value;
        this.keyString = key;
        this.valueText = value;
        this.tenantId = tenantId;
    }
    public void setKey(String key) {
        this.key = key;
        this.keyString = key;
    }

    public void setValue(String value) {
        this.value = value;
        this.valueText = value;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
        this.key = keyString;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        this.value = valueText;
    }
}
