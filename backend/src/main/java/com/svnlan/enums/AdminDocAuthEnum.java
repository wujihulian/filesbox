package com.svnlan.enums;

/**
 * @Author:
 * @Description:
 */
public enum AdminDocAuthEnum {

    show(1, "admin.auth.show", "文件列表"),
    showAction(2, "admin.auth.showAction", "文件列表查看"),
    view(3, "admin.auth.view", "文件预览"),
    viewAction(4, "admin.auth.viewAction", "文件打开预览"),
    download(5, "admin.auth.download", "下载/复制"),
    downloadAction(6, "admin.auth.downloadAction", "下载/复制/文件预览打印"),
    uploadAction(7, "admin.auth.uploadAction", "文件(夹)上传/远程下载"),
    edit(8, "admin.auth.edit", "编辑新建"),
    editAction(9, "admin.auth.editAction", "新建文件(夹)/重命名/粘贴到文件夹/编辑文件/设置备注/创建副本/解、压缩"),
    removeAction(10, "admin.auth.removeAction", "剪切/复制/移动"),
    shareAction(11, "admin.auth.shareAction", "外链分享/与他人协作分享"),
    comment(12, "admin.auth.comment", "文档评论"),
    commentAction(13, "admin.auth.commentAction", "文档评论查看；添加/删除自己的评论(需编辑权限)"),
    event(14, "admin.auth.event", "文档动态"),
    eventAction(15, "admin.auth.eventAction", "文档动态查看、订阅动态"),
    root(16, "admin.auth.root", "管理权限"),
    rootAction(17, "admin.auth.rootAction", "设置成员权限/评论管理/历史版本管理"),
    ;

    private int id;

    private String code;

    private String value;

    AdminDocAuthEnum(int id,String code, String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public int getId() { return id; }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
