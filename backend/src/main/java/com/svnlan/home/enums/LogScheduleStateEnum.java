package com.svnlan.home.enums;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public enum LogScheduleStateEnum {

    BEGIN("0", "开始"),

    SUCCESS("1", "执行成功"),

    FAILURE("2", "执行失败");

    private String code;

    private String value;

    private LogScheduleStateEnum(String code, String value) {
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
