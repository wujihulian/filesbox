package com.svnlan.home.dto;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class AddCloudDirectoryDTO {

    private Long sourceID;

    private String name;

    private String busType;

    private List<AddSubCloudDirectoryDTO> children;

    public List<AddSubCloudDirectoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AddSubCloudDirectoryDTO> children) {
        this.children = children;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
