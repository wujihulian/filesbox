package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 */
public enum SendOperateTypeEnum {

    BINDING("1","绑定"),
    FIND("2","忘记登录密码"),
    FORGET("3","设置支付密码"),
    SAFE("4","私密保险箱");


    private String code;

    private String value;

    SendOperateTypeEnum(String code, String value) {
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
