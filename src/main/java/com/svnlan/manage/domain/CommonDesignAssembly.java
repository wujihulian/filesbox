package com.svnlan.manage.domain;

import java.util.Date;

public class CommonDesignAssembly {
    private Long commonDesignAssemblyId;
    /**  标题 **/
    private String title;
    /**  创建时间 **/
    private Date gmtCreate;
    /**  修改时间 **/
    private Date gmtModified;
    /**  组件类型 **/
    private String assemblyType;
    /**  内容 **/
    private String detail;
    /**  大小 **/
    private String size;
    /**  css **/
    private String setting;
    /**  1PC,2手机 **/
    private String clientType;
    /** 编辑器类型类型，1主页，2二级页，3学习中心 **/
    private String designType;

    public Long getCommonDesignAssemblyId() {
        return commonDesignAssemblyId;
    }

    public void setCommonDesignAssemblyId(Long commonDesignAssemblyId) {
        this.commonDesignAssemblyId = commonDesignAssemblyId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAssemblyType() {
        return assemblyType;
    }

    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getDesignType() {
        return designType;
    }

    public void setDesignType(String designType) {
        this.designType = designType;
    }
}