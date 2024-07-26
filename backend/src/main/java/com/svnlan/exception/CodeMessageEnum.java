package com.svnlan.exception;

/**
 * @Description: 响应码枚举
 * @Date:
 */
public enum CodeMessageEnum {
    success("common.success", "成功"),
    system_error("explorer.systemError", "系统错误"),
    namePwdNotNull("user.namePwdNotNull", "账号密码不能为空!"),
    user_pwdNotNull("user.pwdNotNull", "密码不能为空!"),
    loginNoPermission("user.loginNoPermission", "抱歉,您没有该权限,请使用有此权限的账号登录!"),
    loginFirst("user.loginFirst", "您尚未登录!请先登录"),
    bindSignError("user.bindSignError", "签名异常，请重新操作一次！"),
    bindUpdateError("user.bindUpdateError", "用户信息更新失败，请重试"),
    bindTypeError("user.bindTypeError", "无效的绑定类型"),
    bindWxConfigError("user.bindWxConfigError", "获取配置信息异常"),
    loginTimeout("user.loginTimeout", "当前登录已超时，请重新登录！"),
    loginTokenError("common.loginTokenError", "登录已失效，请重新登录!"),
    loginSuccess("common.loginSuccess", "登录成功！"),
    loginError("common.loginError", "登录失败"),
    bindSuccess("common.bindSuccess", "绑定成功！"),
    bindError("common.bindError", "绑定失败"),
    passwordErrorLock("admin.setting.passwordErrorLock", "密码输入错误锁定"),
    errorAdminAuth("admin.auth.errorAdmin", "权限不足"),
    codeError("user.codeError", "验证码错误"),
    rootPwdEqual("user.rootPwdEqual", "两次密码不一致！"),
    rootPwdTips("user.rootPwdTips", "请设置管理员密码！"),
    pwdError("user.pwdError", "用户名或密码错误！"),
    oldPwdError("user.oldPwdError", "原密码错误!"),
    userEnabled("user.userEnabled", "账号被禁用或尚未启用！请联系管理员"),
    invalidEmail("user.invalidEmail", "您没有有效的邮箱地址,请联系管理员修改"),
    roleError("user.roleError", "所属权限组不存在，请联系管理员"),
    sendSuccess("user.sendSuccess", "发送成功"),
    sendFail("user.sendFail", "发送失败"),
    sendSuccessDesc("user.sendSuccessDesc", "验证码发送成功,请前往查看"),
    sendFailDesc("user.sendFailDesc", "验证码发送失败,请联系管理员"),
    codeExpired("user.codeExpired", "验证码已过期,请重新获取"),
    codeErrorTooMany("user.codeErrorTooMany", "验证码错误次数过多,请重新获取"),
    codeErrorFreq("user.codeErrorFreq", "发送频率过高，请稍后再试！"),
    codeErrorCnt("user.codeErrorCnt", "发送次数已超限制，将被锁定%s小时。"),
    registSuccess("user.registSuccess", "注册成功"),
    roleDelErrTips("admin.role.delErrTips", "该角色正在被使用，无法删除！"),
    shareErrorParam("explorer.share.errorParam", "参数错误!"),
    paramFormatError("explorer.paramFormatError", "参数格式错误！"),
    errorUser("explorer.share.errorUser", "用户信息错误！"),
    errorPwd("explorer.share.errorPwd", "密码错误!"),
    adminAuthError("admin.auth.error", "角色权限错误(没有权限设置)"),
    authTargetError("admin.auth.targetError", "权限对象类型错误，必须为用户或部门"),
    ERROR_USER_LOGIN_LOCK("ERROR_USER_LOGIN_LOCK", "抱歉,密码尝试输入错误过多,当前账号已锁定,请1分钟后再试!"),


    explorerNotNull("explorer.notNull", "必填项不能为空!"),
    registRoleEmpty("admin.setting.registRoleEmpty", "角色权限不能为空!"),
    parentNullError("admin.group.parentNullError", "上级部门不能为空"),
    picCannotNull("explorer.picCannotNull", "图片地址不能为空!"),
    inputEmailCode("user.inputEmailCode", "请输入邮箱验证码!"),
    inputSmsCode("user.inputSmsCode", "请输入短信验证码!"),
    inputVerifyCode("user.inputVerifyCode", "请输入验证码!"),
    inputPwd("user.inputPwd", "请输入密码!"),
    inputPwdAgain("user.inputPwdAgain", "请再次输入密码!"),
    inputNickName("user.inputNickName", "请输入昵称!"),
    inputEmail("user.inputEmail", "请输入邮箱地址!"),
    inputPhone("user.inputPhone", "请输入手机号!"),
    inputPhoneEmail("user.inputPhoneEmail", "请输入手机/Email!"),
    invalidPhoneEmail("user.invalidPhoneEmail", "无效的手机/Email!"),
    defAdminError("admin.install.defAdminError", "管理员账号添加失败!"),
    passwordCheckError("user.passwordCheckError", "密码格式不符合密码强度规则!"),

    saveSuccess("explorer.saveSuccess", "保存成功!"),
    saveError("explorer.saveError", "保存失败!"),
    explorerSuccess("explorer.success", "操作成功!"),
    explorerError("explorer.error", "操作失败!"),
    explorerDataError("explorer.dataError", "数据异常!"),
    userAvatarExt("user.userAvatarExt", "仅支持 JPG、JPEG、PNG 的图片格式!"),
    ignoreFileSizeTips("admin.role.ignoreFileSizeTips", "抱歉,当文件超出大小限制; 具体请联系管理员!"),
    msgSysSizeErr("msgWarning.main.msgSysSizeErr", "服务器系统盘剩余空间不足(%s)"),
    pathNotSupport("explorer.pathNotSupport", "此类型目录不支持该操作!"),
    pathIsRoot("explorer.pathIsRoot", "已经到根目录了!"),
    pathNull("explorer.pathNull", "文件夹为空!"),
    zipFileLarge("explorer.zipFileLarge", "该文件太大，请解压后再进行预览操作!"),
    charNoSupport("explorer.charNoSupport", "不支持的特殊字符:"),
    moveError("explorer.moveError", "移动失败!"),
    lockError("explorer.lockError", "出错了,并发锁定超时!"),
    lockErrorDesc("explorer.lockErrorDesc", "请稍后再试(可降低请求频率,优化并发相关配置)."),
    moveSubPathError("explorer.moveSubPathError", "出错了，父目录不能移动到子目录！"),
    spaceIsFull("explorer.spaceIsFull", "剩余空间不足,请联系管理员!"),
    sessionSaveError("explorer.saveError", "session写入失败!请查看磁盘是否已满,或咨询服务商。"),
    pathNotExists("common.pathNotExists", "该文件不存在！"),
    fileLockError("explorer.fileLockError", "当前文件为锁定状态,请联系锁定者解锁后再试！"),
    downError("explorer.downError", "下载失败！"),
    sourceShareDisabled("source.shareDisabled","当前资源禁止分享"),
    shareNotExist("explorer.share.notExist", "分享不存在"),
    shareExpiredTips("explorer.share.expiredTips", "抱歉，该分享已过期,请联系分享者！"),
    shareDownExceedTips("explorer.share.downExceedTips", "抱歉，该分享下载次数超过分享者设置的上限"),
    shareLoginTips("explorer.share.loginTips", "抱歉，该分享必须登录用户才能访问"),
    shareNoDownTips("explorer.share.noDownTips", "抱歉，该分享者禁用了下载"),
    shareNoViewTips("explorer.share.noViewTips", "抱歉，该分享者禁用了预览"),
    shareNoUploadTips("explorer.share.noUploadTips", "抱歉，该分享者禁用了上传"),
    shareNeedPwd("explorer.share.needPwd", "该分享需要密码"),
    shareActionNotSupport("explorer.share.actionNotSupport", "分享内容,不支持该操作"),
    shareErrorPathTips("explorer.share.errorPathTips", "分享链接错误,或分享者已经取消了该外链分享"),

    selectUserTips("admin.member.selectUserTips", "请选择要操作的账号!"),
    rptSelectTips("admin.share.rptSelectTips", "请选择待操作项!"),
    selectValidFolder("explorer.selectValidFolder", "请选择要有效的文件夹!"),
    selectFolderFile("explorer.selectFolderFile", "选择文件或文件夹!"),
    renameSuccess("explorer.renameSuccess", "重命名成功!"),
    noPermissionRead("explorer.noPermissionRead", "您没有读取权限!"),
    noPermissionDownload("explorer.noPermissionDownload", "您没有下载权限!"),
    noPermissionWrite("explorer.noPermissionWrite", "该目录没有写权限!"),
    noPermissionAction("explorer.noPermissionAction", "您没有此权限，请联系管理员!"),
    noPermissionAuthAll("explorer.noPermissionAuthAll", "%s ,没有此操作权限"),
    noPermissionWriteAll("explorer.noPermissionWriteAll", "该文件或目录没有写权限!"),
    noPermissionWriteFile("explorer.noPermissionWriteFile", "该文件没有写权限!"),
    noPermissionReadAll("explorer.noPermissionReadAll", "该文件或目录没有读权限!"),
    noPermission("explorer.noPermission", "管理员禁止了此权限!"),
    noPermissionExt("explorer.noPermissionExt", "管理员禁止了该类型文件权限!"),
    descTooLong("explorer.descTooLong", "描述长度过长!"),
    notSupport("explorer.notSupport", "出错了, 不支持该内容格式!"),
    noPermissionGroup("explorer.noPermissionGroup", "您不在该用户组!"),
    ERROR_USER_NOT_EXISTS("ERROR_USER_NOT_EXISTS", "用户不存在"),
    ERROR_USER_PASSWORD_ERROR("ERROR_USER_PASSWORD_ERROR", "密码错误!"),
    ERROR_USER_EXIST_NAME("ERROR_USER_EXIST_NAME", "用户名已存在"),
    ERROR_USER_EXIST_PHONE("ERROR_USER_EXIST_PHONE", "手机号已存在"),
    ERROR_USER_EXIST_EMAIL("ERROR_USER_EXIST_EMAIL", "该邮箱已存在"),
    ERROR_USER_EXIST_NICKNAME("ERROR_USER_EXIST_NICKNAME", "昵称已存在"),
    ERROR_IP_NOT_ALLOW("ERROR_IP_NOT_ALLOW", "您当前IP或访问设备不允许登录,请联系管理员"),
    emailCodeText("admin.emailCodeText", "您正在进行邮箱验证，本次请求的验证码如下，为了保障您帐号的安全性，请及时完成验证。"),
    repeatError("explorer.repeatError", "操作失败，该名称已存在"),
    pathExists("explorer.pathExists", "该名称已存在"),
    groupDelError("explorer.groupDelError", "抱歉部门文件夹不支持删除"),
    unzipErrorTips("explorer.unzipErrorTips", "出错了！未识别的压缩文件格式；请检查该文件是否为压缩文件或者是否损坏。"),
    dragDownloadOpenTips("admin.setting.dragDownloadOpenTips", "请联系管理员在后台设置中开启"),
    infoTypeDelError("admin.info.typeDelError", "删除失败，有子分类或数据"),
    DOMAIN_CANNOT_RECOGNIZE("admin.info.domainIdentifyError", "网站无法识别"),
    ARTICLE_CANNOT_RECOGNIZE("admin.info.articleIdentifyError", "文章无法识别"),
    domainSupportError("admin.info.domainSupportError", "该网站暂不支持采集"),
    fileTooLarge("admin.info.fileTooLarge", "文件过大"),
    EXCEEDS_LIMIT("admin.exceeds.limit", "超出限制"),
    Enabling_Not_Deleted("admin.design.deleted", "启用状态无法删除"),
    designUrlLocked("admin.design.url.locked", "当前url被锁定, 暂时不能使用"),
    SING_INVALID("explorer.SING_INVALID", "签名异常"),
    TEMP_AUTH_INVALID("explorer.TEMP_AUTH_INVALID", "临时授权码无效（失效）"),
    QR_INVALID("explorer.QR_INVALID", "二维码已失效"),
    uploadError("explorer.upload.error", "上传失败"),
    totalUsersLimit("admin.totalUsers.exceeds.limit", "用户总数超过限制"),
    userBindingExist("user.binding.exist", "已绑定该账号"),
    userBindingOtherExist("user.binding.other.exist", "已绑定其他账号，请先进行解绑"),
    downLimitSizeError("explorer.downLimitSizeError", "下载超过文件限制大小！"),
    ;
    private String code;
    private String msg;

    CodeMessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getCode(String msg) {
        for (CodeMessageEnum codeMsg : CodeMessageEnum.values()) {
            if (codeMsg.msg.equals(msg)) {
                return codeMsg.code;
            }
        }
        return null;
    }

    public static String getMsg(String code) {
        for (CodeMessageEnum codeMsg : CodeMessageEnum.values()) {
            if (codeMsg.code.equals(code)) {
                return codeMsg.msg;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
