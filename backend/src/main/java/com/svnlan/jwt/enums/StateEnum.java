package com.svnlan.jwt.enums;

/**
 * @Author:
 * @Description: state状态枚举类
 */
public enum StateEnum {

    NOT_BUILD("0", "待生成"),

    ENABLE("1", "启用"),

    CLOSE("2", "关闭"),

    DELETE("3", "删除"),

    NOT_ACTIVATE("4", "未激活"),

    ACTIVATE("5", "已激活"),

    RESERVE("6", "备用"),

    LOCK("7", "锁定"),

    BLOCKING("8","屏蔽"),

    STAGING("9","暂存"),

    SEND("10","发送"),

    UNREAD("11","未读"),

    READ("12","已读"),

    SUBMIT("13","提交"),

    APPROVE("14","审核通过"),

    NOT_CLOSE("15", "解锁");

    private String code;

    private String value;

    private StateEnum(String code, String value) {
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
