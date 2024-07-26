package com.svnlan.enums;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/15 16:20
 */
public enum  SourceSortEnum {
    asc("1", "asc"),
    desc("2", "desc"),
    ;
    private String code;

    private String value;

    SourceSortEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getSortType(String value) {
        switch (value) {
            case "up":
                return asc.getValue();
            case "asc":
                return asc.getValue();
            case "down":
                return desc.getValue();
            case "desc":
                return desc.getValue();
            default:
                return desc.getValue();
        }
    }

    public String getCode() {
        return code;
    }

    public String getValue() { return value; }
}
