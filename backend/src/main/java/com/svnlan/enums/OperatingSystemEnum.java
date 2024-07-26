package com.svnlan.enums;

import org.springframework.util.StringUtils;

/**
 * 操作系统
 *
 * @author lingxu 2023/04/17 10:35
 */
public enum OperatingSystemEnum {

    MAC_OS_IPHONE("Mac OS X (iPhone)"),
    ANDROID("Android"),
    MAC_OS_X("Mac OS X"),
    WINDOWS("Windows");

    private String type;

    OperatingSystemEnum(String type) {
        this.type = type;
    }

    public static OperatingSystemEnum checkAndGet(String osName) {
        if (StringUtils.hasText(osName)) {
            for (OperatingSystemEnum item : OperatingSystemEnum.values()) {
                if (osName.startsWith(item.type)) {
                    return item;
                }
            }
        }
        return null;
    }
}
