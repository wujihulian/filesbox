package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 16:37
 */
public enum MyMenuEnum {
    // 收藏夹
    fav("explorer.toolbar.fav", "", "0", "fav"),
    // 个人空间
    rootPath("explorer.toolbar.rootPath", "explorer.pathDesc.home", "1", "space"),
    ;

    private String code;

    private String desc;

    private String isRoot;
    private String icon;


    MyMenuEnum(String code, String desc, String isRoot, String icon) {
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
