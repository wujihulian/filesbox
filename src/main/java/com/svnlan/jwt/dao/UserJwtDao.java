package com.svnlan.jwt.dao;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.UserRole;
import com.svnlan.jwt.dto.UpdateUserCacheDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:用户信息接口数据类
 */
public interface UserJwtDao {
    /**
     * @Description:根据用户名获取用户信息
     */
    List<LoginUser> findByUserName(@Param("name") String name);

    LoginUser findByUserId(@Param("userID") Long userID);

    int updateLogin(Map<String, Object> map);

    /**
     * @description: 更新用户头像等信息
     * @param updateDTO
     * @return int
     */
    int updateUserMajor(UpdateUserCacheDTO updateDTO);


    String checkPassword(@Param("userID") Long userID);


    LoginUser findByLoginName(@Param("name") String name);

    UserRole getUserIdentity(Long userID);



}
