package com.svnlan.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 9:29
 */
@Data
public class  OptionVo {
    private Integer id;

    private String keyString;
    private String valueText;
    private String value;
    private String key;
    private String type;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
    private Long tenantId;

    public OptionVo(){
    }
    public OptionVo(String key, String value){
        this.keyString = key;
        this.value = value;
        this.key = key;
        this.valueText = value;
    }

    public OptionVo(String key, String value, Long tenantId){
        this.keyString = key;
        this.value = value;
        this.key = key;
        this.valueText = value;
        this.tenantId = tenantId;
    }
    public OptionVo(String type,String key, String value, Long tenantId){
        this.keyString = key;
        this.value = value;
        this.key = key;
        this.type = type;
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
