package com.svnlan.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description: 角色
 */
@Data
public class RoleForm  {

    private Integer roleID;

    private List<Integer> rightsIdList;
}
