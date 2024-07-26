package com.svnlan.enums;

/**
 * @description: 业务类型
 */
public enum BusinessTypeEnum {

    COMMON("common", "通用"),
    SCAN_LOGIN("scanLogin", "扫码登录");

    private String code;
    private String text;

    BusinessTypeEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
