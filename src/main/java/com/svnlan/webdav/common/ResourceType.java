package com.svnlan.webdav.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public enum ResourceType {

    PRIVATE("private", "/webdav/private"), FAVOR("favor", "/webdav/favor"), NONE("none", " ");

    public final String name;

    public final String urlPrefix;

    ResourceType(String name, String urlPrefix) {
        this.name = name;
        this.urlPrefix = urlPrefix;
    }

    public static ResourceType getResourceType(String path) {
        // /webdav/private
        // /webdav/favor
        if (StringUtils.hasText(path)) {
            for (ResourceType resourceType : ResourceType.values()) {
                if (path.endsWith(resourceType.name)) {
                    return resourceType;
                }
            }
        }
        return ResourceType.NONE;
    }


}
