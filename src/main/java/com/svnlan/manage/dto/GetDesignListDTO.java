package com.svnlan.manage.dto;

import com.svnlan.utils.PageQuery;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 */
public class GetDesignListDTO extends PageQuery {
    @NotNull
    private String clientType;
    @NotNull
    private String designType;
    private Long commonDesignId;
    private Boolean getChildrenDesign;
    private Boolean withPage;
    private String keyword;
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

    public Long getCommonDesignId() {
        return commonDesignId;
    }

    public void setCommonDesignId(Long commonDesignId) {
        this.commonDesignId = commonDesignId;
    }

    public Boolean getGetChildrenDesign() {
        return getChildrenDesign;
    }

    public void setGetChildrenDesign(Boolean getChildrenDesign) {
        this.getChildrenDesign = getChildrenDesign;
    }

    public Boolean getWithPage() {
        return withPage;
    }

    public void setWithPage(Boolean withPage) {
        this.withPage = withPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
