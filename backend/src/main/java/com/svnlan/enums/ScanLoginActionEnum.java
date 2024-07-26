package com.svnlan.enums;

/**
 * @description: 扫码登录消息动作
 */
public enum ScanLoginActionEnum {

    CONFIRM("confirm", "确认连接"),
    FEED_BACK("feedBack", "反馈登录结果"),
    AUTH("auth", "登录授权"),

    SCAN("scan", "扫描成功")
    ;

    ScanLoginActionEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    private String code;
    private String text;

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    /**
     * @description: 包含某CODE
     * @param code
     * @return java.lang.Boolean
     */
    public static Boolean contains(String code) {
        Boolean exists = Boolean.FALSE;
        for (ScanLoginActionEnum scanLoginMsgTypeEnum : ScanLoginActionEnum.values()) {
            if(scanLoginMsgTypeEnum.getCode().equals(code)) {
                exists = Boolean.TRUE;
            }
        }
        return exists;
    }

    /**
     * @description: 是否包含TV或WEB端发起的动作
     * @param code
     * @return java.lang.Boolean
     */
    public static Boolean containsTvWebAction(String code) {
        Boolean exists = Boolean.FALSE;
        if(CONFIRM.getCode().equals(code) || FEED_BACK.getCode().equals(code)) {
            exists = Boolean.TRUE;
        }
        return exists;
    }

    /**
     * @description: 是否包含APP端发起的动作
     * @param code
     * @return java.lang.Boolean
     */
    public static Boolean containsAppAction(String code) {
        Boolean exists = Boolean.FALSE;
        if(CONFIRM.getCode().equals(code) || AUTH.getCode().equals(code)) {
            exists = Boolean.TRUE;
        }
        return exists;
    }

}
