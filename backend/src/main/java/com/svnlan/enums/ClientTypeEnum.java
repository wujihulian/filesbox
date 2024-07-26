package com.svnlan.enums;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 16:32
 */
public enum ClientTypeEnum {
    // 1pc , 2h5, 3安卓app, 4 ios-app, 5小程序 6电脑app
    pc("1", "pc"),
    h5("2", "h5"),
    android("3", "安卓"),
    ios("4", "ios"),
    mini("5", "小程序"),
    pcApp("6", "pc app"),
    other("7", "其他"),
    ;


    private String code;

    private String value;

    /**
     * 获取所有枚举的 code 集合
     */
    public static List<String> getCodeList() {
        return Arrays.stream(ClientTypeEnum.values()).map(it -> it.code).collect(Collectors.toList());
    }

    /**
     * 获取所有枚举的 code 集合
     * @param exclude 需要排除的类型
     */
    public static List<String> getCodeList(List<String> exclude) {
        return Arrays.stream(ClientTypeEnum.values())
                .filter(it -> {
                    if (CollectionUtils.isEmpty(exclude)) {
                        return true;
                    }
                    return !exclude.contains(it.code);
                })
                .map(it -> it.code).collect(Collectors.toList());
    }

    ClientTypeEnum(String code, String value) {
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
