package com.svnlan.manage.enums;

/**
 * @Author:
 * @Description: 装扮显示类型
 */
public enum DesignTypeEnum {
    MAIN("1", "主页"),
    SUB("2", "二级页"),
    PIC("4", "图片编辑"),
    APP("9", "APP")
    ;


    DesignTypeEnum(String code, String msg){
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
