package com.svnlan.manage.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class DeleteAssemblyDTO {
    @NotNull
    private Long assemblyId;

    public Long getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(Long assemblyId) {
        this.assemblyId = assemblyId;
    }
}
