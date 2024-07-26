package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/26 9:41
 */
public enum DingSpaceTypeEnum {

    org("1", "org"),
    co("4", "co"),
    group("2", "group"),
    hideOrgEmp("3", "hideOrgEmp"),
    ;


    private String code;

    private String value;

    private DingSpaceTypeEnum(String code, String value) {
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
