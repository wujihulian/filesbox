package com.svnlan.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 13:50
 */
public enum  SortEnum {
    asc("1", "asc"),
    desc("2", "desc"),
    ;
    private String code;

    private String value;

    SortEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getSortType(String value) {
        switch (value) {
            case "asc":
                return asc.getValue();
            case "up":
                return asc.getValue();
            case "desc":
                return desc.getValue();
            default:
                return desc.getValue();
        }
    }

    public static List<String> sortList = Arrays.stream(values()).map(SortEnum::getValue).collect(Collectors.toList());

    public static boolean contains(String value) {
        return sortList.contains(value);
    }

    public String getCode() {
        return code;
    }

    public String getValue() { return value; }
}
