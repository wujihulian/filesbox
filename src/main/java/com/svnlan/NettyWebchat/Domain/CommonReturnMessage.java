package com.svnlan.NettyWebchat.Domain;

/**
 * @Author:
 * @Description:
 */
public class CommonReturnMessage {
    private String type;
    private String code;
    private String message;

    public CommonReturnMessage(String code, String type) {
        this.type = type;
        this.code = code;
    }

    public CommonReturnMessage(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
