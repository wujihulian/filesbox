package com.svnlan.manage.domain;

import java.util.Date;

/**
 * @Description:
 */
public class CommonDesignList {
    private Long designId;

    private String title;

    private Integer isUsed;

    private String sourcePath;

    private String sourceServer;

    private String size;

    private String url;

    private String clientType;

    private Integer uploaded;

    private Integer sort;

    private Long shareDesignId;

    private Boolean isShare;

    private Boolean isPaste;

    private String seo;
    private Long mbDesignId;
    private Integer designClassifyID;
    private String designType;
    /**
     * 创建时间
     */
    private Date gmtCreate;


    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
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

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
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

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourceServer() {
        return sourceServer;
    }

    public void setSourceServer(String sourceServer) {
        this.sourceServer = sourceServer;
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

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    public Long getShareDesignId() {
        return shareDesignId;
    }

    public void setShareDesignId(Long shareDesignId) {
        this.shareDesignId = shareDesignId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public Boolean getPaste() {
        return isPaste;
    }

    public void setPaste(Boolean paste) {
        isPaste = paste;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

}
