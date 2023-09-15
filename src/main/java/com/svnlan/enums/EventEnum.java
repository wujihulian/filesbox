package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:25
 */
public enum EventEnum {
    mkdir("create", "mkdir"),
    mkfile("create", "mkfile"),
    upload("create", "upload"),
    uploadNew("create", "uploadNew"),
    copy("create", "copy"),
    rename("rename", "rename"),
    recycle("recycle", "toRecycle"),
    restore("recycle", "restore"),
    addDesc("addDesc", "addDesc"),
    shareToAdd("share", "shareToAdd"),
    edit("edit", "edit"),
    remove("remove", "remove"),
    shareLinkAdd("share", "shareLinkAdd"),
    shareLinkRemove("share", "shareLinkRemove"),
    shareEdit("share", "shareEdit"),
    shareLinkEdit("share", "shareLinkEdit"),
    shareToRemove("share", "shareToRemove"),
    rollBack("rollBack", "rollBack"),
    ;


    private String code;

    private String value;

    private EventEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
