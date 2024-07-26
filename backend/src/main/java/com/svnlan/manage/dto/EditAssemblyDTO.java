package com.svnlan.manage.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class EditAssemblyDTO {
    @NotNull
    private Long assemblyId;
    private String detail;
    private String title;
    private String setting;
    private String size;

    public Long getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(Long assemblyId) {
        this.assemblyId = assemblyId;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
