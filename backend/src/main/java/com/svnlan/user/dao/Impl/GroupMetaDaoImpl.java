package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.records.GroupMetaRecord;
import com.svnlan.user.dao.GroupMetaDao;
import com.svnlan.user.domain.GroupMeta;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.tables.GroupMeta.GROUP_META;

@Service
public class GroupMetaDaoImpl implements GroupMetaDao {
    @Autowired
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;
    @Override
    public int delMetaByGroupID(Long groupID, List<String> list) {
        return context.delete(GROUP_META)
                .where(GROUP_META.GROUP_ID.eq(groupID).and(GROUP_META.KEY_STRING.in(list)))
                .execute();
    }

    @Override
    public int batchInsert(List<GroupMeta> list) {
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<GroupMetaRecord> insertQuery = context.insertQuery(GROUP_META);
        for (GroupMeta groupMeta : list) {
            insertQuery.newRecord();
            insertQuery.addValue(GROUP_META.GROUP_ID, groupMeta.getGroupID());
            insertQuery.addValue(GROUP_META.KEY_STRING, groupMeta.getKey());
            insertQuery.addValue(GROUP_META.VALUE_TEXT, groupMeta.getValue());
            insertQuery.addValue(GROUP_META.CREATE_TIME, now);
            insertQuery.addValue(GROUP_META.MODIFY_TIME, now);
            insertQuery.addValue(GROUP_META.TENANT_ID, groupMeta.getTenantId());
            insertQuery.setReturning(GROUP_META.ID);
        }
        return insertQuery.execute();
    }

    @Override
    public List<GroupMeta> getGroupMetaList(List<Long> groupIdList, List<String> list) {
        return context.select(GROUP_META.GROUP_ID.as("groupID"),GROUP_META.KEY_STRING.as("key"),GROUP_META.VALUE_TEXT.as("value"))
                .from(GROUP_META)
                .where(GROUP_META.GROUP_ID.in(groupIdList).and(GROUP_META.KEY_STRING.in(list)))
                .fetchInto(GroupMeta.class);
    }

    @Override
    public Long getSystemGroup() {
        return context.select(GROUP_META.GROUP_ID)
                .from(GROUP_META)
                .where(GROUP_META.KEY_STRING.eq("systemGroupSource"))
                .and(GROUP_META.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(GROUP_META.CREATE_TIME.asc())
                .limit(1)
                .fetchOneInto(Long.class);
    }
}
