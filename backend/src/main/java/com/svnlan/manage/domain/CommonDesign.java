package com.svnlan.manage.domain;

import java.util.Date;

public class CommonDesign {
    /**
     * 编辑器id
     */
    private Long commonDesignId;

    /**
     * 名称
     */
    private String title;

    /**
     * 偏移量，坐标(x,y)
     */
    private String offset;

    /**
     * 尺寸大小，长高(a,b)
     */
    private String size;

    /**
     * 缩略图，文件id
     */
    private Long pic;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 编辑器类型类型，1主页，2学习界面
     */
    private String designType;

    /**
     * 客户端类型，1PC，2APP，3小程序
     */
    private String clientType;

    /**
     * 当前使用中的设置
     */
    private Integer isUsed;

    /**
     * 页面内容
     */
    private String detail;

    private String url;

    private String fileUrl;

    private Long fkCommonDesignId;

    private String foot;

    private String head;

    private String setting;

    private String applet;

    private String approvalState;

    private Integer price;

    private Integer commonDesignClassifyId;

    private String sourcePath;

    private Long sourceDesignId;

    private Date gmtApproval;

    private String approvalDetail;

    private Integer commonDesignBuyRecordId;

    private Long newDesignId;

    private Integer sort;

    private String state;


    private Boolean isShare;

    private Boolean isPaste;

    private Long newImportSourceId;

    private Long userId;

    private String seo;

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

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCommonDesignClassifyId() {
        return commonDesignClassifyId;
    }

    public void setCommonDesignClassifyId(Integer commonDesignClassifyId) {
        this.commonDesignClassifyId = commonDesignClassifyId;
    }

    public String getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(String approvalState) {
        this.approvalState = approvalState;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCommonDesignId() {
        return commonDesignId;
    }

    public void setCommonDesignId(Long commonDesignId) {
        this.commonDesignId = commonDesignId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset == null ? null : offset.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType == null ? null : designType.trim();
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType == null ? null : clientType.trim();
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Long getFkCommonDesignId() {
        return fkCommonDesignId;
    }

    public void setFkCommonDesignId(Long fkCommonDesignId) {
        this.fkCommonDesignId = fkCommonDesignId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Long getPic() {
        return pic;
    }

    public void setPic(Long pic) {
        this.pic = pic;
    }

    public String getApplet() {
        return applet;
    }

    public void setApplet(String applet) {
        this.applet = applet;
    }

    public Long getSourceDesignId() {
        return sourceDesignId;
    }

    public void setSourceDesignId(Long sourceDesignId) {
        this.sourceDesignId = sourceDesignId;
    }

    public Date getGmtApproval() {
        return gmtApproval;
    }

    public void setGmtApproval(Date gmtApproval) {
        this.gmtApproval = gmtApproval;
    }

    public String getApprovalDetail() {
        return approvalDetail;
    }

    public void setApprovalDetail(String approvalDetail) {
        this.approvalDetail = approvalDetail;
    }

    public Integer getCommonDesignBuyRecordId() {
        return commonDesignBuyRecordId;
    }

    public void setCommonDesignBuyRecordId(Integer commonDesignBuyRecordId) {
        this.commonDesignBuyRecordId = commonDesignBuyRecordId;
    }

    public Long getNewDesignId() {
        return newDesignId;
    }

    public void setNewDesignId(Long newDesignId) {
        this.newDesignId = newDesignId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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


    public Long getNewImportSourceId() {
        return newImportSourceId;
    }

    public void setNewImportSourceId(Long newImportSourceId) {
        this.newImportSourceId = newImportSourceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public Long getMbDesignId() {
        return mbDesignId;
    }

    public void setMbDesignId(Long mbDesignId) {
        this.mbDesignId = mbDesignId;
    }
}