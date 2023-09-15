package com.svnlan.enums;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 16:30
 */
public enum AuthEnum {

    explorerAdd(1, "explorer.add", ""),
    explorerUpload(2, "explorer.upload", ""),
    explorerView(3, "explorer.view", ""),
    explorerDownload(4, "explorer.download", ""),
    explorerShare(5, "explorer.share", ""),
    explorerRemove(6, "explorer.remove", ""),
    explorerEdit(7, "explorer.edit", ""),
    explorerMove(8, "explorer.move", ""),
    explorerServerDownload(9, "explorer.serverDownload", ""),
    explorerSearch(10, "explorer.search", ""),
    explorerUnzip(11, "explorer.unzip", ""),
    explorerZip(12, "explorer.zip", ""),

    userEdit(13, "user.edit", ""),
    userFav(14, "user.fav", ""),

    adminIndexDashboard(15, "admin.index.dashboard", ""),
    adminIndexSetting(16, "admin.index.setting", ""),
    adminIndexLoginLog(17, "admin.index.loginLog", ""),
    adminIndexLog(18, "admin.index.log", ""),
    adminIndexServer(19, "admin.index.server", ""),
    adminRoleList(20, "admin.role.list", ""),
    adminRoleEdit(21, "admin.role.edit", ""),
    adminJobList(22, "admin.job.list", ""),
    adminJobEdit(23, "admin.job.edit", ""),
    adminMemberList(24, "admin.member.list", ""),
    adminMemberUserEdit(25, "admin.member.userEdit", ""),
    adminMemberGroupEdit(26, "admin.member.groupEdit", ""),
    adminAuthList(26, "admin.auth.list", ""),
    adminAuthEdit(27, "admin.auth.edit", ""),
    adminPluginList(28, "admin.plugin.list", ""),
    adminPluginEdit(29, "admin.plugin.edit", ""),
    adminStorageList(28, "admin.storage.list", ""),
    adminStorageEdit(29, "admin.storage.edit", ""),
    adminAutoTaskList(28, "admin.autoTask.list", ""),
    adminAutoTaskEdit(29, "admin.autoTask.edit", ""),
    adminIndexInfo(30, "admin.index.information", ""),
    explorerInfoView(31, "explorer.informationView", ""),
    ;

    private int id;

    private String code;

    private String value;

    public static List<String> authList = Arrays.stream(values()).map(AuthEnum::getCode).collect(Collectors.toList());

    AuthEnum(int id, String code, String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public static Map<String, Object> getUserAuthMap(String userAuth){
        List<String> aList = !ObjectUtils.isEmpty(userAuth) ? Arrays.asList(userAuth.split(",")).stream().map(String::valueOf).collect(Collectors.toList()) : null;
        Map<String, Object> userAuthMap = new HashMap<>(1);
        for (String code : authList){
            int auth = 0;
            if (!CollectionUtils.isEmpty(aList) && aList.contains(code)){
                auth = 1;
            }
            userAuthMap.put(code , auth);
        }
        return userAuthMap;
    }

    public static boolean contains(String busType) {
        return authList.contains(busType);
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
