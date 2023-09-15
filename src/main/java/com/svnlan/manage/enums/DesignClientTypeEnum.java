package com.svnlan.manage.enums;

import org.springframework.util.ObjectUtils;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/3 13:56
 */
public enum DesignClientTypeEnum {
    PC("1", "pc端"),
    MOBILE("2", "手机端"),
    APP("3", "app端"),
    MP("4", "小程序");

    DesignClientTypeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public static boolean exists(String code){
        if (ObjectUtils.isEmpty(code)){
            return false;
        }
        for (DesignClientTypeEnum r : DesignClientTypeEnum.values()){
            if (r.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

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
