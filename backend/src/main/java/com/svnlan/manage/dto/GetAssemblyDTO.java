package com.svnlan.manage.dto;

/**
 * @Author:
 * @Description:
 */
public class GetAssemblyDTO {

    private String type;
    private String clientType;
    private String designType;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
