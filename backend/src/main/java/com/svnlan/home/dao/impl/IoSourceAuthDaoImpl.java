package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.IoSourceAuthDao;
import com.svnlan.home.domain.IoSourceAuth;
import com.svnlan.home.vo.IoSourceAuthVo;
import com.svnlan.jooq.tables.records.IoSourceAuthRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.svnlan.jooq.Tables.*;

@Repository
public class IoSourceAuthDaoImpl implements IoSourceAuthDao {

    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int batchInsert(List<IoSourceAuth> list) {
        InsertQuery<IoSourceAuthRecord> insertQuery = context.insertQuery(IO_SOURCE_AUTH);
        LocalDateTime now = LocalDateTime.now();
        Long tenantId = tenantUtil.getTenantIdByServerName();
        for (IoSourceAuth auth : list) {
            insertQuery.newRecord();
            insertQuery.addValue(IO_SOURCE_AUTH.SOURCE_ID, auth.getSourceID());
            insertQuery.addValue(IO_SOURCE_AUTH.TARGET_TYPE, auth.getTargetType());
            insertQuery.addValue(IO_SOURCE_AUTH.TARGET_ID, auth.getTargetID());
            insertQuery.addValue(IO_SOURCE_AUTH.AUTH_ID, auth.getAuthID());
            insertQuery.addValue(IO_SOURCE_AUTH.AUTH_DEFINE, auth.getAuthDefine());
            insertQuery.addValue(IO_SOURCE_AUTH.CREATE_TIME, now);
            insertQuery.addValue(IO_SOURCE_AUTH.MODIFY_TIME, now);
            insertQuery.addValue(IO_SOURCE_AUTH.TENANT_ID, tenantId);
        }
        return insertQuery.execute();
    }

    @Override
    public int deleteSourceAuth(Long sourceID) {
        return context.delete(IO_SOURCE_AUTH)
                .where(IO_SOURCE_AUTH.SOURCE_ID.eq(sourceID))
                .execute();
    }

    @Override
    public List<IoSourceAuthVo> getSourceAuthBySourceID(Long sourceID) {
        return context.select(IO_SOURCE_AUTH.ID, IO_SOURCE_AUTH.SOURCE_ID.as("sourceID"), IO_SOURCE_AUTH.TARGET_TYPE, IO_SOURCE_AUTH.TARGET_ID.as("targetID"),
                        IO_SOURCE_AUTH.AUTH_ID.as("authID"), IO_SOURCE_AUTH.AUTH_DEFINE,
                        GROUPS.NAME.as("nickname"), GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL)
                .from(IO_SOURCE_AUTH)
                .leftJoin(GROUPS).on(IO_SOURCE_AUTH.TARGET_ID.eq(GROUPS.ID)).and(GROUPS.STATUS.eq(1))
                .where(IO_SOURCE_AUTH.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_AUTH.TARGET_TYPE.eq(2))
                .unionAll(
                        context.select(IO_SOURCE_AUTH.ID, IO_SOURCE_AUTH.SOURCE_ID.as("sourceID"), IO_SOURCE_AUTH.TARGET_TYPE, IO_SOURCE_AUTH.TARGET_ID.as("targetID"),
                                        IO_SOURCE_AUTH.AUTH_ID.as("authID"), IO_SOURCE_AUTH.AUTH_DEFINE,
                                        DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"),
                                        DSL.inline(0L).as("parentID"), DSL.inline("").as("parentLevel"))
                                .from(IO_SOURCE_AUTH)
                                .leftJoin(USERS).on(IO_SOURCE_AUTH.TARGET_ID.eq(USERS.ID)).and(USERS.STATUS.eq(1))
                                .where(IO_SOURCE_AUTH.SOURCE_ID.eq(sourceID))
                                .and(IO_SOURCE_AUTH.TARGET_TYPE.eq(1))
                ).fetchInto(IoSourceAuthVo.class);
    }

    @Override
    public List<IoSourceAuthVo> getGroupNameListByGID(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(GROUPS.ID.as("groupID"), GROUPS.NAME.as("parentGroupName"))
                .from(GROUPS)
                .where(GROUPS.STATUS.eq(1))
                .and(GROUPS.ID.in(list))
                .fetchInto(IoSourceAuthVo.class);
    }

    @Override
    public List<IoSourceAuthVo> getSourceAuthBySourceIDList(List<Long> list, Long targetID) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE_AUTH.ID, IO_SOURCE_AUTH.SOURCE_ID.as("sourceID"), IO_SOURCE_AUTH.TARGET_TYPE, IO_SOURCE_AUTH.TARGET_ID.as("targetID"), IO_SOURCE_AUTH.AUTH_ID.as("authID"), IO_SOURCE_AUTH.AUTH_DEFINE,
                        GROUPS.NAME.as("nickname"), GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL,
                        ROLE.ROLE_NAME.as("authName"), ROLE.LABEL, ROLE.AUTH)
                .from(IO_SOURCE_AUTH)
                .join(GROUPS).on(IO_SOURCE_AUTH.TARGET_ID.eq(GROUPS.ID)).and(GROUPS.STATUS.eq(1))
                .leftJoin(ROLE).on(ROLE.ID.eq(IO_SOURCE_AUTH.AUTH_ID)).and(ROLE.ROLE_TYPE.eq("2"))
                .where(IO_SOURCE_AUTH.SOURCE_ID.in(list)).and(IO_SOURCE_AUTH.TARGET_TYPE.eq(2))
                .unionAll(
                        context.select(IO_SOURCE_AUTH.ID, IO_SOURCE_AUTH.SOURCE_ID, IO_SOURCE_AUTH.TARGET_TYPE, IO_SOURCE_AUTH.TARGET_ID, IO_SOURCE_AUTH.AUTH_ID, IO_SOURCE_AUTH.AUTH_DEFINE,
                                        DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"),
                                        DSL.inline(0L).as("parentID"), DSL.inline("").as("parentLevel"),
                                        ROLE.ROLE_NAME.as("authName"), ROLE.LABEL, ROLE.AUTH)
                                .from(IO_SOURCE_AUTH)
                                .leftJoin(USERS).on(IO_SOURCE_AUTH.TARGET_ID.eq(USERS.ID)).and(USERS.STATUS.eq(1))
                                .leftJoin(ROLE).on(ROLE.ID.eq(IO_SOURCE_AUTH.AUTH_ID)).and(ROLE.ROLE_TYPE.eq("2"))
                                .where(IO_SOURCE_AUTH.SOURCE_ID.in(list)).and(IO_SOURCE_AUTH.TARGET_TYPE.eq(1))
                )
                .fetchInto(IoSourceAuthVo.class);
    }
}
