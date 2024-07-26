package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 15:40
 */
public enum  ToolsEnum {
    // 最近文档
    recentDoc("explorer.toolbar.recentDoc", "explorer.pathDesc.recentDoc", "recentDoc"),
    // 我的协作
    // shareTo("explorer.toolbar.shareTo", "explorer.pathDesc.shareTo", "shareTo"),
    // 外链分享
    shareLink("explorer.toolbar.shareLink", "explorer.pathDesc.shareLink", "shareLink"),
    // 我的相册
    photo("explorer.toolbar.photo", "explorer.photo.desc", "photo"),
    // 工具箱
    toolbox("explorer.toolbar.toolbox", "explorer.toolbox.desc", "toolbox"),
    // 回收站
    recycle("explorer.toolbar.recycle", "explorer.pathDesc.recycle", "recycle"),
    ;

    private String code;

    private String value;
    private String icon;

    ToolsEnum(String code, String value, String icon) {
        this.code = code;
        this.value = value;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
