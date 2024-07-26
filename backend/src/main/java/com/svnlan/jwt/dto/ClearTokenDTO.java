package com.svnlan.jwt.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class ClearTokenDTO {
    @NotNull
    private Long userID;
    private List<Long> userIdList;
    private Boolean isParent;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public List<Long> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Long> userIdList) {
        this.userIdList = userIdList;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }
}
