package com.svnlan.enums;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 15:18
 */
public enum MenuEnum {
    // 位置
    position("common.position", "files", "", "1", "position"),
    // 工具
    tools("common.tools", "tools", "", "1", "tool"),
    // 文件类型
    fileType("common.fileType", "fileType", "explorer.pathDesc.fileType", "0", "fileType"),
    // 标签
    tag("common.tag", "fileTag", "explorer.pathDesc.tag", "0", "tag"),
    ;
    private String code;

    private String type;

    private String desc;

    private String open;
    private String icon;

    MenuEnum(String code, String type, String desc, String open, String icon) {
        this.code = code;
        this.type = type;
        this.desc = desc;
        this.open = open;
        this.icon = icon;
    }

    public static String getCode(String msg) {
        for (MenuEnum codeMsg : MenuEnum.values()) {
            if (codeMsg.type.equals(msg)) {
                return codeMsg.code;
            }
        }
        return null;
    }

    public static String getMsg(String code) {
        for (MenuEnum codeMsg : MenuEnum.values()) {
            if (codeMsg.code.equals(code)) {
                return codeMsg.type;
            }
        }
        return null;
    }

    public String getIcon() {
        return icon;
    }
    public String getDesc() {
        return desc;
    }
    public String getOpen() {
        return open;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
