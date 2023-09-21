package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.Role;
import com.svnlan.user.dto.RoleDTO;
import org.apache.ibatis.annotations.Param;

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
    int updateRoleSort(@Param("list") List<Role> list, @Param("roleType") String roleType);
    Integer getMaxRoleSort(String roleType);
    Role getSystemRoleInfo();

    List<JSONObject> getNameByIds(@Param("roleIds") List<Long> roleIds);
}
