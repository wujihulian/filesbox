package com.svnlan.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 17:04
 */
public enum GroupMetaEnum {
    namePinyin(1, "namePinyin"),
    namePinyinSimple(2, "namePinyinSimple"),
    authShowType(3, "authShowType"),
    authShowGroup(4, "authShowGroup"),
    ;
    private Integer code;

    private String value;

    public static List<String> keyList = Arrays.stream(values()).map(GroupMetaEnum::getValue).collect(Collectors.toList());

    public static List<String> delKeyList(){
        return Arrays.asList(namePinyin.value, namePinyinSimple.value, authShowType.value, authShowGroup.value);
    }

    public static boolean contains(String value) {
        return keyList.contains(value);
    }

    GroupMetaEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
