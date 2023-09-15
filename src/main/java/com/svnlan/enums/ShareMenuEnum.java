package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/11 15:42
 */
public enum ShareMenuEnum {
    // 与我协作
    shareToMe("explorer.toolbar.shareToMe", "", "0", "shareToMe"),
    info("explorer.toolbar.info", "", "0", "info"),
    ;

    private String code;

    private String desc;

    private String isRoot;
    private String icon;


    ShareMenuEnum(String code, String desc, String isRoot, String icon) {
        this.code = code;
        this.desc = desc;
        this.isRoot = isRoot;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public String getIsRoot() {
        return isRoot;
    }
}
