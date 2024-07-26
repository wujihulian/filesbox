package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.records.UserGroupRecord;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.vo.UserGroupVo;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.Tables.*;

@Repository
public class UserGroupDaoImpl implements UserGroupDao {
    @Resource
    private DSLContext context;

    @Override
    public List<UserGroupVo> getUserGroupInfoList(List<Long> list) {
        return context.select(USER_GROUP.USER_ID.as("userID"), USER_GROUP.GROUP_ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, USER_GROUP.AUTH_ID.as("authID"),
                        ROLE.CODE, ROLE.ROLE_NAME.as("authName"), ROLE.AUTH,
                        DSL.groupConcatDistinct(DSL.concat(GROUP_META.KEY_STRING, DSL.val(":"), GROUP_META.VALUE_TEXT)).separator("__").as("groupAuth"),
                        ROLE.IS_SYSTEM, ROLE.ADMINISTRATOR, ROLE.LABEL)
                .from(USER_GROUP)
                .leftJoin(ROLE).on(USER_GROUP.AUTH_ID.eq(ROLE.ID)).and(ROLE.ROLE_TYPE.eq("2"))
                .join(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID)).and(GROUPS.STATUS.in(0, 1))
                .join(GROUP_META).on(USER_GROUP.GROUP_ID.eq(GROUP_META.GROUP_ID))
                .where(USER_GROUP.USER_ID.in(list))
                .groupBy(USER_GROUP.USER_ID, USER_GROUP.GROUP_ID)
                .fetchInto(UserGroupVo.class);
    }

    @Override
    public List<UserGroupVo> getUserGroupInfoListByParam(List<Long> list, String keyword) {
        return context.select(USER_GROUP.USER_ID.as("userID"), USER_GROUP.GROUP_ID.as("groupID"), GROUPS.NAME, GROUPS.PARENT_ID.as("parentID"), GROUPS.PARENT_LEVEL, USER_GROUP.AUTH_ID.as("authID"),
                        ROLE.CODE, ROLE.ROLE_NAME.as("authName"),
                        DSL.groupConcatDistinct(DSL.concat(GROUP_META.KEY_STRING, DSL.val(":"), GROUP_META.VALUE_TEXT)).separator("__").as("groupAuth"),
                        ROLE.IS_SYSTEM, ROLE.ADMINISTRATOR, ROLE.LABEL)
                .from(USER_GROUP)
                .leftJoin(ROLE).on(USER_GROUP.AUTH_ID.eq(ROLE.ID)).and(ROLE.ROLE_TYPE.eq("2"))
                .join(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID)).and(GROUPS.STATUS.in(0, 1))
                .join(GROUP_META).on(USER_GROUP.GROUP_ID.eq(GROUP_META.GROUP_ID))
                .where(USER_GROUP.USER_ID.in(list))
                .and(StringUtils.hasText(keyword) ? DSL.falseCondition().or(GROUPS.NAME.like("%" + keyword + "%").or(GROUP_META.VALUE_TEXT.like("%" + keyword + "%"))) : DSL.noCondition())
                .groupBy(USER_GROUP.USER_ID, USER_GROUP.GROUP_ID)
                .fetchInto(UserGroupVo.class);
    }

    @Override
    public List<UserGroupVo> getGroupNameListByUserIds(List<Long> list) {
        return context.select(USER_GROUP.USER_ID.as("userID"), DSL.groupConcat(GROUPS.NAME).as("groupName"))
                .from(USER_GROUP)
                .join(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID)).and(GROUPS.STATUS.eq(1))
                .where(USER_GROUP.USER_ID.in(list))
                .groupBy(USER_GROUP.USER_ID, USER_GROUP.GROUP_ID)
                .fetchInto(UserGroupVo.class);
    }

    @Override
    public int delByUserID(Long userID) {
        return context.delete(USER_GROUP)
                .where(USER_GROUP.USER_ID.eq(userID))
                .execute();
    }

    @Override
    public int delByGroupID(Long groupID) {
        return context.delete(USER_GROUP)
                .where(USER_GROUP.GROUP_ID.eq(groupID))
                .execute();
    }

    @Override
    public int batchInsert(List<UserGroupVo> list) {
        InsertQuery<UserGroupRecord> insertQuery = context.insertQuery(USER_GROUP);
        LocalDateTime now = LocalDateTime.now();
        for (UserGroupVo item : list) {
            insertQuery.newRecord();
            insertQuery.addValue(USER_GROUP.USER_ID, item.getUserID());
            insertQuery.addValue(USER_GROUP.GROUP_ID, item.getGroupID());
            insertQuery.addValue(USER_GROUP.AUTH_ID, item.getAuthID());
            insertQuery.addValue(USER_GROUP.SORT, item.getSort());
            insertQuery.addValue(USER_GROUP.CREATE_TIME, now);
            insertQuery.addValue(USER_GROUP.MODIFY_TIME, now);
        }
        return insertQuery.execute();
    }

    @Override
    public List<Long> getMyGroupIDList(Long userID) {
        return context.select(USER_GROUP.GROUP_ID)
                .from(USER_GROUP)
                .where(USER_GROUP.USER_ID.eq(userID))
                .fetch(USER_GROUP.GROUP_ID);
    }
    @Override
    public Long checkRelationIsExsit(Long userId, Long groupId){
        return context.select(USER_GROUP.ID)
                .from(USER_GROUP)
                .where(USER_GROUP.USER_ID.eq(userId).and(USER_GROUP.GROUP_ID.eq(groupId))).limit(1)
                .fetchOneInto(Long.class);
    }
}
