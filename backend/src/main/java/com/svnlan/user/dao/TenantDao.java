package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.domain.Tenant;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/16 13:11
 */
public interface TenantDao {
    //    @Select("SELECT t.tenant_name tenantName, t.second_level_domain secondLevelDomain, t.start_time startTime, t.expire_time expireTime, " +
//            "t.size_use AS sizeUse, t.user_count userCount, t.group_count groupCount, t.status, t.id, t.dr, IFNULL(u.nickname, u.name) AS nickname FROM t_tenant t
//            LEFT JOIN user u ON t.user_id = u.userID limit #{startIndex}, #{pageSize}")
    List<JSONObject> selectTenantList(Integer startIndex, Integer pageSize);

    //    @Select("SELECT COUNT(id) FROM t_tenant")
    Long selectTenantListCount();

    //    @Select("SELECT t.tenant_name tenantName, t.second_level_domain secondLevelDomain, t.start_time startTime, t.expire_time expireTime, " +
//            " t.status, t.id, t.remark,t.dr, IFNULL(u.nickname, u.name) AS nickname FROM t_tenant t LEFT JOIN user u ON t.user_id = u.userID WHERE id = #{id}")
    JSONObject getById(Long id);

    Long getTenantIdByServerName(String serverName);

    //    @Update("UPDATE user SET password = #{cryptoPassword} WHERE userID = #{userId} AND password != #{cryptoPassword}")
    int updateAdminUserPassword(Long userId, String cryptoPassword);


    //    @Select("SELECT t.user_id FROM t_tenant t INNER JOIN user u ON t.user_id = u.userID WHERE t.id = #{id}")
    Long checkIfAdminUserExist(Long id);

    //    @Select("SELECT id FROM t_tenant WHERE dr = 0 AND second_level_domain = #{secondLevelDomain}")
    List<Long> checkDomainExist(String secondLevelDomain);

    //    @Update("UPDATE t_tenant SET dr = 1 WHERE id = #{id} AND dr = 0")
    int updateDrById(Long id);

    int deleteById(Long id);

    void updateById(Tenant tenant);

    void insert(Tenant tenant);

    int updateTenantUserById(Tenant tenant);

    Long selectTenantIdByUserId(Long userId);
    Long selectTenantIdBySourceId(Long sourceId);
}
