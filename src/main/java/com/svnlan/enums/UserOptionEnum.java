package com.svnlan.enums;


import com.svnlan.user.domain.UserOption;
import com.svnlan.utils.ChinesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 10:23
 */
public enum UserOptionEnum {

    listSort("listSort", "", "folderInfo"),
    folderInfoListType("listType", "", "folderInfo"),
    listType("listType", "icon", ""),
    listSortField("listSortField", "name", ""),
    listSortOrder("listSortOrder", "up", ""),
    fileIconSize("fileIconSize", "80", ""),
    fileOpenClick("fileOpenClick", "dbclick", ""),
    fileShowDesc("fileShowDesc", "0", ""),
    animateOpen("animateOpen", "1", ""),
    soundOpen("soundOpen", "0", ""),
    theme("theme", "auto", ""),
    themeImage("themeImage", "", ""),
    wall("wall", "4", ""),
    listTypeKeep("listTypeKeep", "1", ""),
    listSortKeep("listSortKeep", "1", ""),
    fileRepeat("fileRepeat", "replace", ""),
    recycleOpen("recycleOpen", "1", ""),
    kodAppDefault("kodAppDefault", "", ""),
    fileIconSizeDesktop("fileIconSizeDesktop", "70", ""),
    fileIconSizePhoto("fileIconSizePhoto", "120", ""),
    photoConfig("photoConfig", "", ""),
    resizeConfig("resizeConfig", "{\"filename\":250,\"filetype\":80,\"filesize\":80,\"filetime\":215,\"editorTreeWidth\":200,\"explorerTreeWidth\":200}", ""),
    imageThumb("imageThumb", "1", ""),
    fileSelect("fileSelect", "1", ""),
    displayHideFile("displayHideFile", "0", ""),
    filePanel("filePanel", "1", ""),
    shareToMeShowType("shareToMeShowType", "list", ""),
    messageSendType("messageSendType", "enter", ""),
    loginDevice("loginDevice", "", ""),
    recycleList("recycleList", "[]", "recycle"),
    ;
    public static List<String> keyList = Arrays.stream(values()).map(UserOptionEnum::getCode).collect(Collectors.toList());

    public static List<String> sortKeyList = Arrays.asList(listSortField.getCode(),listSortOrder.getCode(),fileIconSize.getCode(),listSortKeep.getCode(),listSort.getCode(),listType.getCode());

    public static boolean contains(String value) {
        return keyList.contains(value);
    }

    public static List<UserOption> setDefaultOptionList(Long userID){
        List<UserOption> paramList = new ArrayList<>();
        for (UserOptionEnum optionEnum :values()){
            paramList.add(new UserOption(userID, optionEnum.getType(), optionEnum.getCode(), optionEnum.getValue()));
        }
        return paramList;
    }
    private String code;

    private String value;

    private String type;

    UserOptionEnum(String code, String value, String type) {
        this.code = code;
        this.value = value;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
