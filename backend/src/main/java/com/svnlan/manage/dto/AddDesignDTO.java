package com.svnlan.manage.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class AddDesignDTO {
    @NotNull
    private String title;
    private Long sourceId;
    @NotNull
    private String clientType;

    private String size;

    private String url;

    @NotNull
    private String designType;

    private Boolean searchComponent;

    private String seoKeyword;
    private String seoDescription;
    private Long mbDesignId;
    private Integer designClassifyID;
    private Integer isPaste;

    public Integer getIsPaste() {
        return isPaste;
    }

    public void setIsPaste(Integer isPaste) {
        this.isPaste = isPaste;
    }

    public Integer getDesignClassifyID() {
        return designClassifyID;
    }

    public void setDesignClassifyID(Integer designClassifyID) {
        this.designClassifyID = designClassifyID;
    }

    public Long getMbDesignId() {
        return mbDesignId;
    }

    public void setMbDesignId(Long mbDesignId) {
        this.mbDesignId = mbDesignId;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSearchComponent() {
        return searchComponent;
    }

    public void setSearchComponent(Boolean searchComponent) {
        this.searchComponent = searchComponent;
    }
}
