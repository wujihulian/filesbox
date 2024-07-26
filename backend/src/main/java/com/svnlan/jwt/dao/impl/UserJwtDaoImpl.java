package com.svnlan.jwt.dao.impl;

import static com.svnlan.jooq.tables.Role.*;

import com.svnlan.jooq.tables.records.UsersRecord;
import com.svnlan.jwt.dao.UserJwtDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.UserRole;
import com.svnlan.jwt.dto.UpdateUserCacheDTO;

import static com.svnlan.jooq.tables.Users.*;

import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserJwtDaoImpl implements UserJwtDao {
    @Autowired
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;
    @Override
    public List<LoginUser> findByUserName(String name) {
        Long tenantId = tenantUtil.getTenantIdByServerName();
        List<LoginUser> list = context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.PASSWORD, USERS.LAST_LOGIN, USERS.PHONE, USERS.EMAIL, USERS.STATUS, USERS.SEX
                , USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.TENANT_ID)
                .from(USERS).where(USERS.NAME.eq(name).and(USERS.STATUS.in(0, 1)).and(USERS.TENANT_ID.eq(tenantId)))
                .union(
                        context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.PASSWORD, USERS.LAST_LOGIN, USERS.PHONE, USERS.EMAIL, USERS.STATUS, USERS.SEX, USERS.AVATAR
                                , USERS.ROLE_ID.as("roleID"), USERS.TENANT_ID)
                                .from(USERS).where(USERS.PHONE.eq(name).and(USERS.STATUS.in(0, 1)).and(USERS.TENANT_ID.eq(tenantId)))
                ).union(
                        context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.PASSWORD, USERS.LAST_LOGIN, USERS.PHONE, USERS.EMAIL, USERS.STATUS, USERS.SEX, USERS.AVATAR
                                , USERS.ROLE_ID.as("roleID"), USERS.TENANT_ID)
                                .from(USERS).where(USERS.EMAIL.eq(name).and(USERS.STATUS.in(0, 1)).and(USERS.TENANT_ID.eq(tenantId)))
                ).fetchInto(LoginUser.class);

        return list;
    }

    private SelectJoinStep<Record12<Long, String, String, String, LocalDateTime, String, String, Integer, Integer, String, Integer, Long>>
    buildQueryUsersColumn() {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.PASSWORD, USERS.LAST_LOGIN, USERS.PHONE, USERS.EMAIL, USERS.STATUS, USERS.SEX
                , USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.TENANT_ID)
                .from(USERS);
    }

    @Override
    public LoginUser findByUserId(Long userID) {
        return buildQueryUsersColumn()
                .where(USERS.ID.eq(userID))
                .and(USERS.STATUS.eq(1))
                .fetchOneInto(LoginUser.class);
    }

    @Override
    public int updateLogin(Map<String, Object> map) {
        return context.update(USERS)
                .set(USERS.LAST_LOGIN, DateUtil.getLocalDateTimeFromMilli((Long) map.get("lastLogin")))
                .where(USERS.ID.eq((Long) map.get("userID"))).execute();
    }

    @Override
    public int updateUserMajor(UpdateUserCacheDTO updateDTO) {
        UpdateSetMoreStep<UsersRecord> setMoreStep = context.update(USERS)
                .set(USERS.MODIFY_TIME, LocalDateTime.now());
        if (Objects.nonNull(updateDTO.getAvatar())) {
            setMoreStep.set(USERS.AVATAR, updateDTO.getAvatar());
        }
        return setMoreStep.where(USERS.ID.eq(updateDTO.getUserID()))
                .execute();
    }

    @Override
    public String checkPassword(Long userID) {
        return context.select(USERS.PASSWORD)
                .from(USERS)
                .where(USERS.ID.eq(userID))
                .fetchOne(USERS.PASSWORD);
    }

    @Override
    public LoginUser findByLoginName(String name) {
        return buildQueryUsersColumn()
                .where(USERS.NAME.eq(name))
                .and(USERS.STATUS.eq(1))
                .and(USERS.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(LoginUser.class);
    }

    @Override
    public UserRole getUserIdentity(Long userID) {
        UserRole ur = context.select(ROLE.ADMINISTRATOR, ROLE.AUTH).from(USERS)
                .join(ROLE).on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)))
                .where(USERS.ID.eq(userID).and(USERS.STATUS.eq(1))).fetchOneInto(UserRole.class);
        return ur;
    }
}
