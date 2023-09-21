package com.svnlan.home.enums;


/**
 * @Author:
 * @Description:
 */
public enum CloudOperateEnum {
    RENAME_FILE("rename", "重命名文件"),
    FAV_RENAME_FILE("favRename", "收藏夹重命名"),
    MOVE_FILE("move", "移动文件"),
    COPY_FILE("copy", "复制文件"),
    DELETE_FILE("recycle", "删除文件"),
    RECYCLE_FILE("restore", "还原文件"),
    DELETE_BIN("remove", "从回收站删除"),
    SHARE_FILE("share", "是否分享文件"),
    FAV_FILE("fav", "收藏夹"),
    FAV_DEL("delFav", "取消收藏"),
    FILE_DESC("desc", "添加描述"),
    ADD_TOP("top", "置顶"),
   CANCEL_TOP("cancelTop", "置顶"),
   MAKE_FILE("mkfile", "新建文件"),
    DELETE_BIN_ALL("removeAll", "彻底删除回收站所有"),
    RECYCLE_FILE_ALL("restoreAll", "还原所有文件"),
    FILE_COVER("fileCover", "修改封面"),
    FILE_EDIT("editFile", "文件编辑"),
    ;

    private String code;
    private String operate;
    CloudOperateEnum(String code, String operate){
        this.code = code;
        this.operate = operate;
    }

    public static CloudOperateEnum getOperateEnum(String code) {
        for (CloudOperateEnum codeMsg : CloudOperateEnum.values()) {
            if (codeMsg.code.equals(code)) {
                return codeMsg;
            }
        }
        return null;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
