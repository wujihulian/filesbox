package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 17:19
 */
public enum UserTypeEnum {

    MANAGER(1, "系统管理员"),
    USER(2, "其他用户"),
    GUEST(3, "游客"),
    ;
    private Integer code;

    private String value;

    UserTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
