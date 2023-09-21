package com.svnlan.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/15 16:28
 */
public enum SourceFieldEnum {
    name("1", "name"),
    fileType("2", "fileType"),
    size("3", "size"),
    modifyTime("4", "modifyTime"),
    createTime("3", "createTime"),
    ;
    private String code;

    private String value;

    SourceFieldEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getSortField(String value) {
        switch (value) {
            case "name":
                return "name_pinyin";
            case "size":
                return size.getValue();
            case "ext":
            case "fileType":
                return fileType.getValue();
            case "modifyTime":
                return modifyTime.getValue();
            default:
                return name.getValue();
        }
    }

    public static String getConvertSortField(String value) {
        if (ObjectUtils.isEmpty(value)){
            return "stateSort";
        }
        switch (value) {
            case "name":
                return "io." + name.getValue();
            case "size":
                return "io." + size.getValue();
            case "fileType":
            case "ext":
                return "io." + fileType.getValue();
            case "modifyTime":
                return "cc." + modifyTime.getValue();
            case "createTime":
                return "cc." + createTime.getValue();
            default:
                return "stateSort";
        }
    }


    public static List<String> sortList = Arrays.stream(values()).map(SourceFieldEnum::getValue).collect(Collectors.toList());

    public static boolean contains(String value) {
        return sortList.contains(value);
    }

    public String getCode() {
        return code;
    }

    public String getValue() { return value; }
}
