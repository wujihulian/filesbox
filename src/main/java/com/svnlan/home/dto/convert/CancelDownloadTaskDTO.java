package com.svnlan.home.dto.convert;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class CancelDownloadTaskDTO {
    @NotNull
    private String url;
    @NotNull
    private String uuid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
