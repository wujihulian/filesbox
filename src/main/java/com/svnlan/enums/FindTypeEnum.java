package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 */
public enum FindTypeEnum {

    MOBILE("1", "手机"),

    EMAIL("2", "邮箱"),

    WXLOGIN("3", "微信"),

    QQLOGIN("4", "qq登录"),

    WEIBOLOGIN("5", "微博"),

    ALILOGIN("6", "支付宝");

    private String code;

    private String value;

    FindTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
