package com.svnlan.manage.dto;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class DeleteDesignDTO {
    @NotNull
    private Long designId;

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }
}
