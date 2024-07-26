package com.svnlan.jwt.dao;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.UserRole;
import com.svnlan.jwt.dto.UpdateUserCacheDTO;

import java.util.List;
import java.util.Map;

/**
 * @Description:用户信息接口数据类
 */
public interface UserJwtDao {
    /**
     * @Description:根据用户名获取用户信息
     */
    List<LoginUser> findByUserName(String name);

    LoginUser findByUserId(Long userID);

    int updateLogin(Map<String, Object> map);

    /**
     * @description: 更新用户头像等信息
     * @param updateDTO
     * @return int
     */
    int updateUserMajor(UpdateUserCacheDTO updateDTO);


    String checkPassword(Long userID);


    LoginUser findByLoginName(String name);

    UserRole getUserIdentity(Long userID);



}
