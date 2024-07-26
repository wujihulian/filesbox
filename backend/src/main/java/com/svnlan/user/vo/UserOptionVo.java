package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/7 17:59
 */
@Data
public class UserOptionVo {

    private Long id;
    private Long userID;
    private String type;
    private String key;
    private String keyString;
    private String valueText;
    private String value;

    public void setKey(String key) {
        this.keyString = key;
        this.key = key;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
        this.key = keyString;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        this.value = valueText;
    }

    public void setValue(String value) {
        this.value = value;
        this.valueText = value;
    }
}
