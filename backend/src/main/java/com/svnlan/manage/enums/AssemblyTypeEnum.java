package com.svnlan.manage.enums;

/**
 * @Author:
 * @Description:
 */
public enum AssemblyTypeEnum {
    EFFECT("1", "特效"),
    MODULE("2", "组合模块");

    AssemblyTypeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
