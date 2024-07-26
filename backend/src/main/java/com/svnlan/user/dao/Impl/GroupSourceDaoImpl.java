package com.svnlan.user.dao.Impl;

import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.domain.GroupSource;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.svnlan.jooq.Tables.GROUPS;
import static com.svnlan.jooq.Tables.IO_SOURCE;
import static com.svnlan.jooq.tables.GroupSource.GROUP_SOURCE;

@Repository
public class GroupSourceDaoImpl implements GroupSourceDao {
    @Resource
    private DSLContext context;

    @Override
    public int insert(GroupSource groupSource) {
        return context.insertInto(GROUP_SOURCE)
                .columns(GROUP_SOURCE.GROUP_ID, GROUP_SOURCE.SOURCE_ID, GROUP_SOURCE.CREATE_TIME,GROUP_SOURCE.TENANT_ID)
                .values(groupSource.getGroupID(), groupSource.getSourceID(), LocalDateTime.now(), groupSource.getTenantId())
                .execute();
    }

    @Override
    public List<GroupSource> getGroupSourceList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(GROUP_SOURCE.GROUP_ID.as("groupID"), GROUP_SOURCE.SOURCE_ID.as("sourceID"))
                .from(GROUP_SOURCE)
                .where(GROUP_SOURCE.SOURCE_ID.in(list))
                .fetchInto(GroupSource.class);
    }

    @Override
    public Long checkIsGroup(List<Long> list) {
        return context.select(GROUP_SOURCE.GROUP_ID)
                .from(GROUP_SOURCE)
                .where(GROUP_SOURCE.SOURCE_ID.in(list))
                .limit(1)
                .fetchOne(GROUP_SOURCE.GROUP_ID);
    }

    @Override
    public List<GroupSource> getGroupSourceIDs(Long sourceID) {
        return context.select(GROUP_SOURCE.SOURCE_ID.as("sourceID"), GROUPS.STATUS)
                .from(GROUP_SOURCE)
                .join(IO_SOURCE).on(GROUP_SOURCE.SOURCE_ID.eq(IO_SOURCE.ID))
                .join(GROUPS).on(GROUP_SOURCE.GROUP_ID.eq(GROUPS.ID))
                .where(IO_SOURCE.PARENT_ID.in(sourceID))
                .fetchInto(GroupSource.class);
    }
}
