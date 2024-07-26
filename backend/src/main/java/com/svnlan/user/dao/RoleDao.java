package com.svnlan.user.dao;

import com.svnlan.user.domain.Role;
import com.svnlan.user.dto.RoleDTO;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 17:20
 */
public interface RoleDao {

    int insert(Role role);

    int update(Role role);

    List<Role> getRoleList(RoleDTO roleDTO);

    int updateRoleState(RoleDTO roleDTO);

    void updateRoleSort(List<Role> list, String roleType);

    Integer getMaxRoleSort(String roleType);

    Role getSystemRoleInfo();

    Role getRoleByKey(String key, Long tenantId);

}
