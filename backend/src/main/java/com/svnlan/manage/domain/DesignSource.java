package com.svnlan.manage.domain;

/**
 * @Author:
 * @Description:
 */
public class DesignSource {
    private String sourcePath;
    private String sourcePathPre;
    private String clientType;
    private String sourceName;
    private String url;
    private String designType;
    private Long sourceId;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourcePathPre() {
        return sourcePathPre;
    }

    public void setSourcePathPre(String sourcePathPre) {
        this.sourcePathPre = sourcePathPre;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
