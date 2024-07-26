package com.svnlan.user.dao.Impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jooq.Tables;
import com.svnlan.jooq.tables.records.TTenantRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.dao.TenantDao;
import com.svnlan.user.domain.Tenant;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.svnlan.jooq.tables.TTenant.T_TENANT;
import static com.svnlan.jooq.tables.Users.USERS;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/16 13:12
 */
@Repository
public class TenantDaoImpl implements TenantDao {
    @Resource
    private DSLContext context;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public List<JSONObject> selectTenantList(Integer startIndex, Integer pageSize) {
        return context.select(T_TENANT.TENANT_NAME, T_TENANT.SECOND_LEVEL_DOMAIN, T_TENANT.START_TIME, T_TENANT.EXPIRE_TIME, T_TENANT.SIZE_USE, T_TENANT.USER_COUNT,
                        T_TENANT.GROUP_COUNT, T_TENANT.STATUS, T_TENANT.ID, T_TENANT.DR, T_TENANT.REMARK, USERS.NAME.as("adminUserName"), T_TENANT.IS_SYSTEM)
                .from(T_TENANT)
                .leftJoin(USERS).on(T_TENANT.USER_ID.eq(USERS.ID))
                .where(T_TENANT.DR.eq(0))
                .limit(startIndex, pageSize)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public Long selectTenantListCount() {
        return context.select(DSL.count(T_TENANT.ID)).from(T_TENANT).where(T_TENANT.DR.eq(0)).fetchOneInto(Long.class);
    }

    @Override
    public JSONObject getById(Long id) {
        return context.select(T_TENANT.TENANT_NAME, T_TENANT.SECOND_LEVEL_DOMAIN, T_TENANT.START_TIME, T_TENANT.EXPIRE_TIME, T_TENANT.SIZE_USE, T_TENANT.USER_COUNT,
                        T_TENANT.GROUP_COUNT, T_TENANT.STATUS, T_TENANT.ID, T_TENANT.DR, Tables.USERS.NAME.as("nickname"), T_TENANT.IS_SYSTEM)
                .from(T_TENANT)
                .leftJoin(USERS).on(T_TENANT.USER_ID.eq(USERS.ID))
                .where(T_TENANT.ID.eq(id))
                .fetchOne(jsonObjectRecordMapper);
    }

    @Override
    public Long getTenantIdByServerName(String serverName) {
        return context.select(T_TENANT.ID).from(T_TENANT).where(T_TENANT.SECOND_LEVEL_DOMAIN.eq(serverName)).fetchOneInto(Long.class);
    }

    @Override
    public int updateAdminUserPassword(Long userId, String cryptoPassword) {
        return context.update(USERS)
                .set(USERS.PASSWORD, cryptoPassword)
                .where(USERS.ID.eq(userId))
                .execute();
    }

    @Override
    public Long checkIfAdminUserExist(Long id) {
        return context.select(T_TENANT.USER_ID)
                .from(T_TENANT)
                .innerJoin(USERS).on(USERS.ID.eq(T_TENANT.USER_ID))
                .where(T_TENANT.ID.eq(id))
                .fetchOneInto(Long.class);
    }

    @Override
    public List<Long> checkDomainExist(String secondLevelDomain) {
        return context.select(T_TENANT.ID)
                .from(T_TENANT)
                .where(T_TENANT.DR.eq(0))
                .and(T_TENANT.SECOND_LEVEL_DOMAIN.eq(secondLevelDomain).and(T_TENANT.DR.eq(0)))
                .fetch(T_TENANT.ID);
    }

    @Override
    public int updateDrById(Long id) {
        return context.update(T_TENANT)
                .set(T_TENANT.STATUS, 2)
                .where(T_TENANT.ID.eq(id))
                .and(T_TENANT.DR.eq(0))
                .execute();
    }

    @Override
    public int deleteById(Long id) {
        return context.update(T_TENANT)
                .set(T_TENANT.DR, 1)
                .where(T_TENANT.ID.eq(id))
                .execute();
    }

    @Override
    public void updateById(Tenant tenant) {
        UpdateQuery<TTenantRecord> updateQuery = context.updateQuery(T_TENANT);
        Optional.ofNullable(tenant.getTenantName()).ifPresent(it -> updateQuery.addValue(T_TENANT.TENANT_NAME, it));
        Optional.ofNullable(tenant.getSecondLevelDomain()).ifPresent(it -> updateQuery.addValue(T_TENANT.SECOND_LEVEL_DOMAIN, it));
        Optional.ofNullable(tenant.getStatus()).ifPresent(it -> updateQuery.addValue(T_TENANT.STATUS, it));
        Optional.ofNullable(tenant.getStartTime()).ifPresent(it -> updateQuery.addValue(T_TENANT.START_TIME, it));
        Optional.ofNullable(tenant.getExpireTime()).ifPresent(it -> updateQuery.addValue(T_TENANT.EXPIRE_TIME, it));
        Optional.ofNullable(tenant.getRemark()).ifPresent(it -> updateQuery.addValue(T_TENANT.REMARK, it));
        updateQuery.addConditions(T_TENANT.ID.eq(tenant.getId()));
        updateQuery.execute();
    }

    @Override
    public void insert(Tenant tenant) {
        LocalDateTime now = LocalDateTime.now();

        Long id = context.insertInto(Tables.T_TENANT)
                .set(Tables.T_TENANT.TENANT_NAME, tenant.getTenantName())
                .set(Tables.T_TENANT.SECOND_LEVEL_DOMAIN, tenant.getSecondLevelDomain())
                .set(Tables.T_TENANT.STATUS, 1)
                .set(Tables.T_TENANT.START_TIME, tenant.getStartTime())
                .set(Tables.T_TENANT.EXPIRE_TIME, tenant.getExpireTime())
                .set(Tables.T_TENANT.REMARK, ObjectUtils.isEmpty(tenant.getRemark()) ? "" : tenant.getRemark())
                .set(Tables.T_TENANT.USER_ID, tenant.getUserId())
                .set(Tables.T_TENANT.MODIFY_TIME, now)
                .set(Tables.T_TENANT.CREATE_TIME, now)
                .set(Tables.T_TENANT.DR, 0)
                .returning(Tables.USERS.ID).fetchOne().getId();
        tenant.setId(id);
    }

    @Override
    public int updateTenantUserById(Tenant tenant) {
        return context.update(T_TENANT)
                .set(T_TENANT.USER_ID, tenant.getUserId())
                .where(T_TENANT.ID.eq(tenant.getId()))
                .execute();
    }

    @Override
    public Long selectTenantIdByUserId(Long userId){
        return context.select(USERS.TENANT_ID)
                .from(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOneInto(Long.class);
    }
    @Override
    public Long selectTenantIdBySourceId(Long sourceId){
        return context.select(IO_SOURCE.TENANT_ID)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceId))
                .fetchOneInto(Long.class);
    }
}
