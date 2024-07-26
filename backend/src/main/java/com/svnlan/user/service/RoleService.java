package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.RoleDTO;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:34
 */
public interface RoleService {

    List getRoleList(LoginUser loginUser, RoleDTO paramDto);
    void editRole(String prefix, LoginUser loginUser, RoleDTO paramDto);
    void setRoleStatus(String prefix, LoginUser loginUser, RoleDTO paramDto);
    void setRoleSort(String prefix, LoginUser loginUser, RoleDTO paramDto);
}
