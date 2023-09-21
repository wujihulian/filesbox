package com.svnlan.manage.enums;

/**
 * @Author:
 * @Description:
 */
public enum DesignUsedEnum {
    USED(1, "启用"),
    NOT_USED(0, "未启用");

    DesignUsedEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
