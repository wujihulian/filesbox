package com.svnlan.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 14:09
 */
public enum  SortFieldEnum {
    sizeUse("1", "sizeUse"),
    roleID("2", "roleID"),
            ;
    private String code;

    private String value;

    SortFieldEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getSortField(String value) {
        switch (value) {
            case "sizeUse":
                return sizeUse.getValue();
            case "roleID":
                return roleID.getValue();
            default:
                return sizeUse.getValue();
        }
    }

    public static List<String> sortList = Arrays.stream(values()).map(SortFieldEnum::getValue).collect(Collectors.toList());

    public static boolean contains(String value) {
        return sortList.contains(value);
    }

    public String getCode() {
        return code;
    }

    public String getValue() { return value; }
}
