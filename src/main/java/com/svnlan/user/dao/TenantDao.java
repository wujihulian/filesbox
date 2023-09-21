package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.user.domain.Tenant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author zhanghanyi
 * @description 针对表【t_tenant】的数据库操作Mapper
 * @createDate 2023-08-26 10:47:33
 * @Entity generator.domain.Tenant
 */
public interface TenantDao extends BaseMapper<Tenant> {
    @Select("SELECT t.tenant_name tenantName, t.second_level_domain secondLevelDomain, t.start_time startTime, t.expire_time expireTime, " +
            "t.size_use AS sizeUse, t.user_count userCount, t.group_count groupCount, t.status, t.id, t.dr, IFNULL(u.nickname, u.name) AS nickname FROM t_tenant t LEFT JOIN user u ON t.user_id = u.userID limit #{startIndex}, #{pageSize}")
    List<JSONObject> selectTenantList(Integer startIndex, Integer pageSize);

    @Select("SELECT COUNT(id) FROM t_tenant")
    Long selectTenantListCount();

    @Select("SELECT t.tenant_name tenantName, t.second_level_domain secondLevelDomain, t.start_time startTime, t.expire_time expireTime, " +
            " t.status, t.id, t.remark,t.dr, IFNULL(u.nickname, u.name) AS nickname FROM t_tenant t LEFT JOIN user u ON t.user_id = u.userID WHERE id = #{id}")
    JSONObject getById(Long id);

    @Update("UPDATE user SET password = #{cryptoPassword} WHERE userID = #{userId} AND password != #{cryptoPassword}")
    int updateAdminUserPassword(@Param("userId") Long userId, @Param("cryptoPassword") String cryptoPassword);


    @Select("SELECT t.user_id FROM t_tenant t INNER JOIN user u ON t.user_id = u.userID WHERE t.id = #{id}")
    Long checkIfAdminUserExist(@Param("id") Long id);

    @Select("SELECT id FROM t_tenant WHERE dr = 0 AND second_level_domain = #{secondLevelDomain}")
    Long checkDomainExist(String secondLevelDomain);

    @Update("UPDATE t_tenant SET dr = 1 WHERE id = #{id} AND dr = 0")
    int updateDrById(Long id);
}
