package com.svnlan.user.domain;


import lombok.Data;

/**
 * @Description: 角色与权限关联关系
 * @Author: sulijuan
 * @Modified:
 */
@Data
public class RoleRights {

    /** 角色ID */
    private Integer roleID;
    /** 权限ID */
    private Integer rightsId;


    public RoleRights() {}

    public RoleRights(Integer roleID, Integer rightsId) {
        this.roleID = roleID;
        this.rightsId = rightsId;
    }

}