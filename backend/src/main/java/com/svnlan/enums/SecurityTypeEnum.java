package com.svnlan.enums;

import java.util.Optional;

/**
 * @Author:
 * @Description: 安全设置类型
 */
public enum SecurityTypeEnum {

    MOBILE("1", "mobilePhone"),
    WECHAT("2", "wechat"),
    QQ("3", "qq"),
    EMAIL("4", "email"),
    WEIBO("5", "weibo"),
    ALIPAY("6", "alipay"),
    EN_WECHAT("7", "enWechat"),
    DING_DING("8", "dingding"),
    WECHAT_APP("12", "wechatApp"),
    CUBE("13", "cube")
    ;

    private String code;

    private String value;

    SecurityTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SecurityTypeEnum deriveEnum(Integer openIdType) {
        String type = String.valueOf(Optional.ofNullable(openIdType).orElse(-1));
        for (SecurityTypeEnum itemEnum : SecurityTypeEnum.values()) {
            if (itemEnum.code.equals(type)) {
                return itemEnum;
            }
        }
        return null;
    }

    /**
     * 1手机号，2微信，3 qq，4 email，5微博，6 支付宝
     */
    public static String getValue(String code) {
        switch (code) {
            case "1":
                return MOBILE.getValue();
            case "2":
                return WECHAT.getValue();
            case "3":
                return QQ.getValue();
            case "4":
                return EMAIL.getValue();
            case "5":
                return WEIBO.getValue();
            case "6":
                return ALIPAY.getValue();
            case "7":
                return EN_WECHAT.getValue();
            case "8":
                return DING_DING.getValue();
            case "12":
                return WECHAT_APP.getValue();
            default:
                return null;
        }
    }

    /**
     * 1手机号，2微信，3 qq，4 email，5微博，6 支付宝
     * getUserInfo type 不等于空时使用
     */
    public static String getValueString(String value) {
        switch (value) {
            case "mobilePhone":
                return MOBILE.getValue();
            case "wechat":
                return WECHAT.getValue();
            case "qq":
                return QQ.getValue();
            case "email":
                return EMAIL.getValue();
            case "weibo":
                return WEIBO.getValue();
            case "alipay":
                return ALIPAY.getValue();
            case "enWechat":
                return EN_WECHAT.getValue();
            case "dingding":
                return DING_DING.getValue();
            default:
                return null;
        }
    }
    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
