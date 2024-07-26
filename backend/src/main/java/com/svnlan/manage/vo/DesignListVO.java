package com.svnlan.manage.vo;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class DesignListVO {
    private Long designId;

    private String title;

    private String thumb;

    private Integer isUsed;

    private String url;

    private String size;

    private Boolean uploaded;

    private Long shareDesignId;

    private Boolean isShare;
    private Boolean isPaste;

    private String seoKeyword;
    private String seoDescription;
    private Long mbDesignId;
    private String designType;
    private Integer designClassifyID;
    private Integer parentID;
    private String parentLevel;
    private Long createTime;
    private List<DesignListVO> designList;
    private List<DesignListVO> children;

    public DesignListVO(){}
    public DesignListVO(Integer designClassifyID, String parentLevel, List<DesignListVO> children){
        this.designClassifyID = designClassifyID;
        this.parentLevel = parentLevel;
        this.children = children;
    }

    public List<DesignListVO> getChildren() {
        return children;
    }

    public void setChildren(List<DesignListVO> children) {
        this.children = children;
    }

    public List<DesignListVO> getDesignList() {
        return designList;
    }

    public void setDesignList(List<DesignListVO> designList) {
        this.designList = designList;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(String parentLevel) {
        this.parentLevel = parentLevel;
    }

    public Integer getDesignClassifyID() {
        return designClassifyID;
    }

    public void setDesignClassifyID(Integer designClassifyID) {
        this.designClassifyID = designClassifyID;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Long getShareDesignId() {
        return shareDesignId;
    }

    public void setShareDesignId(Long shareDesignId) {
        this.shareDesignId = shareDesignId;
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

}
