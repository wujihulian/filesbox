package com.svnlan.enums;

import com.svnlan.common.I18nUtils;
import org.springframework.util.ObjectUtils;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/4 11:41
 */
public enum LogTypeEnum {


    authEdit(1, "admin.auth.edit", "", ""),
    authRemove(2, "admin.auth.remove", "", ""),
    groupEdit(3, "admin.group.edit", "", ""),
    memberAdd(4, "admin.member.add", "", ""),
    memberEdit(5, "admin.member.edit", "", ""),
    roleAdd(6, "admin.role.add", "", ""),
    roleEdit(7, "admin.role.edit", "", ""),
    fileDownload(8, "explorer.index.fileDownload", "文件下载", "admin.log.downFile"),
    fileEdit(9, "file.edit", "编辑文件", "admin.log.editFile"),
    fileMkDir(10, "file.mkdir", "新建文件夹", "log-type-create-mkdir"),
    fileMkFile(11, "file.mkfile", "新建文件", "log-type-create-mkfile"),
    fileRemove(12, "file.remove", "删除文件", "admin.log.delFile"),// 彻底删除文件
    fileRename(13, "file.rename", "重命名", "log-type-rename"),
    fileshareEdit(14, "file.shareEdit", "编辑协作", ""),
    fileShareLinkAdd(15, "file.shareLinkAdd", "外链分享", "log.file.shareLink"),
    fileShareToAdd(16, "file.shareToAdd", "协作分享", "log.file.shareTo"),
    fileShareToRemove(17, "file.shareToRemove", "取消协作分享", "admin.log.delShareTo"),
    fileToRecycle(18, "file.toRecycle", "移到回收站", "log-type-recycle-toRecycle"),
    fileUpload(19, "file.upload", "上传文件", "log-type-create-upload"),
    loginSubmit(20, "user.index.loginSubmit", "登录", "common.login"),
    logout(21, "user.index.logout", "退出", "explorer.toolbar.uiLogout"),
    restore(22, "file.restore", "还原回收站", "explorer.recycleRestore"),
    copy(23, "file.copy", "文件复制", "admin.role.readCopy"),
    favDel(24, "explorer.fav.del", "取消收藏", "explorer.delFav"),
    favAdd(25, "explorer.fav.add", "添加收藏", "explorer.addFav"),
    move(26, "file.move", "移动文件", "log-type-move"),
    moveOut(27, "file.moveOut", "移走文件", "log-type-moveOut"),
    shareLinkRemove(28, "file.shareLinkRemove", "取消外链分享", "admin.log.delLinkTo"),
    fileShareLinkEdit(15, "file.shareLinkEdit", "编辑分享", "log-type-share-shareEdit"),

    wechatBind(30, "wechat.bind", "", ""),

    wechatUnbind(31, "wechat.unbind", "", ""),
    zipDownload(32, "explorer.index.zipDownload", "文件夹下载", "admin.log.downFolder"),
    fileOut(33, "explorer.index.fileOut", "", "admin.log.downFile"),
    clearTenant(34, "tenant.clear", "租户资源清理", "admin.tenant.clear"),
    pullDing(35, "pull.ding", "同步钉钉组织结构", ""),
    pullDingGroup(36, "pull.ding.group", "同步钉钉部门", ""),
    pullDingUser(37, "pull.ding.user", "同步钉钉用户", ""),
    pullEnWebchat(38, "pull.enWebchat", "同步企业微信组织结构", ""),
    pullEnWebchatGroup(39, "pull.enWebchat.group", "同步企业微信部门", ""),
    pullEnWebchatUser(40, "pull.enWebchat.user", "同步企业微信用户", ""),
    pullCube(41, "pull.Cube", "同步魔方组织结构", ""),
    pullCubeGroup(42, "pull.Cube.group", "同步魔方部门", ""),
    pullCubeUser(43, "pull.Cube.user", "同步魔方用户", ""),
    ;

    private int id;

    private String code;

    private String value;

    private String tag;

    private LogTypeEnum(int id,String code, String value, String tag) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.tag = tag;
    }

    public static String getValueByCode(String code) {
        for (LogTypeEnum itemEnum : LogTypeEnum.values()) {
            if (itemEnum.code.equals(code) && !ObjectUtils.isEmpty(itemEnum.getTag())) {
                return I18nUtils.i18n(itemEnum.getTag());
            }
        }
        return "其他";
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() { return id; }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
