package com.svnlan.manage.vo;

/**
 * @Author:
 * @Description:
 */
public class DesignDetailVO {
    private String body;
    private String head;
    private String foot;
    private String setting;
    private String size;
    private String title;
    private Boolean isImport;
    private Long importSourceId;
    private String importFileName;
    private Long shareDesignId;
    private Boolean paste;
    private String seo;
    private String seoKeyword;
    private String seoDescription;
    private String designPath;

    private Long mbDesignId;
    private Integer designClassifyID;
    private String pathPre;

    public String getPathPre() {
        return pathPre;
    }

    public void setPathPre(String pathPre) {
        this.pathPre = pathPre;
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

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getImport() {
        return isImport;
    }

    public void setImport(Boolean anImport) {
        isImport = anImport;
    }

    public Long getImportSourceId() {
        return importSourceId;
    }

    public void setImportSourceId(Long importSourceId) {
        this.importSourceId = importSourceId;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public Long getShareDesignId() {
        return shareDesignId;
    }

    public void setShareDesignId(Long shareDesignId) {
        this.shareDesignId = shareDesignId;
    }

    public Boolean getPaste() {
        return paste;
    }

    public void setPaste(Boolean paste) {
        this.paste = paste;
    }

    public String getDesignPath() {
        return designPath;
    }

    public void setDesignPath(String designPath) {
        this.designPath = designPath;
    }
}
