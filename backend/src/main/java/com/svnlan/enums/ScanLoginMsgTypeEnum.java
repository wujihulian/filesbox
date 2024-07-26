package com.svnlan.enums;

/**
 * @description: 扫码登录消息类型
 */
public enum ScanLoginMsgTypeEnum {

    TV_SCAN_LOGIN("tvScanLogin", "TV端"),
    WEB_SCAN_LOGIN("webScanLogin", "web端"),
    APP_SCAN_LOGIN("appScanLogin", "APP端")
    ;

    ScanLoginMsgTypeEnum(String code, String text) {
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
        for (ScanLoginMsgTypeEnum scanLoginMsgTypeEnum : ScanLoginMsgTypeEnum.values()) {
            if(scanLoginMsgTypeEnum.getCode().equals(code)) {
                exists = Boolean.TRUE;
            }
        }
        return exists;
    }

}
