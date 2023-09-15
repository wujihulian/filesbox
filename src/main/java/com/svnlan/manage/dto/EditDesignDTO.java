package com.svnlan.manage.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class EditDesignDTO {
    @NotNull
    private Long designId;

    private String title;

    private Long sourceId;

    private String body;

    private String head;

    private String foot;

    private String setting;

    private String designType;

    private String clientType;

    private Integer isUsed;

    private String size;

    private String url;

    private String applet;

    private Boolean searchComponent;

    private String seoKeyword;

    private String seoDescription;

    //修改导入的
    private Boolean asImport;

    private Long importSourceId;
    private Integer designClassifyID;

    public Integer getDesignClassifyID() {
        return designClassifyID;
    }

    public void setDesignClassifyID(Integer designClassifyID) {
        this.designClassifyID = designClassifyID;
    }

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplet() {
        return applet;
    }

    public void setApplet(String applet) {
        this.applet = applet;
    }

    public Boolean getSearchComponent() {
        return searchComponent;
    }

    public void setSearchComponent(Boolean searchComponent) {
        this.searchComponent = searchComponent;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public Boolean getAsImport() {
        return asImport;
    }

    public void setAsImport(Boolean asImport) {
        this.asImport = asImport;
    }

    public Long getImportSourceId() {
        return importSourceId;
    }

    public void setImportSourceId(Long importSourceId) {
        this.importSourceId = importSourceId;
    }
}
