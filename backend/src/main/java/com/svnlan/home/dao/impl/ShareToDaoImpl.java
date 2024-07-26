package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.ShareToDao;
import com.svnlan.home.domain.ShareTo;
import com.svnlan.home.vo.ShareToVo;
import com.svnlan.jooq.tables.records.ShareToRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.Tables.SHARE_TO;

@Repository
public class ShareToDaoImpl implements ShareToDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int batchInsert(List<ShareTo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<ShareToRecord> insertQuery = context.insertQuery(SHARE_TO);
        Long tenantId = tenantUtil.getTenantIdByServerName();
        for (ShareTo shareTo : list) {
            insertQuery.newRecord();
            insertQuery.addValue(SHARE_TO.SHARE_ID, shareTo.getShareID());
            insertQuery.addValue(SHARE_TO.TARGET_TYPE, shareTo.getTargetType());
            insertQuery.addValue(SHARE_TO.TARGET_ID, shareTo.getTargetID());
            insertQuery.addValue(SHARE_TO.AUTH_ID, shareTo.getAuthID());
            insertQuery.addValue(SHARE_TO.AUTH_DEFINE, shareTo.getAuthDefine());
            insertQuery.addValue(SHARE_TO.CREATE_TIME, now);
            insertQuery.addValue(SHARE_TO.MODIFY_TIME, now);
            insertQuery.addValue(SHARE_TO.TENANT_ID, tenantId);
        }
        return insertQuery.execute();
    }

    @Override
    public int delete(Long shareID) {
        return context.delete(SHARE_TO)
                .where(SHARE_TO.SHARE_ID.eq(shareID))
                .execute();
    }

    @Override
    public int deleteList(List<Long> list) {
        return context.delete(SHARE_TO)
                .where(SHARE_TO.SHARE_ID.in(list))
                .execute();
    }

    @Override
    public List<ShareToVo> getShareToList(Long shareID) {
        return context.select(SHARE_TO.SHARE_ID.as("shareID"), SHARE_TO.TARGET_TYPE, SHARE_TO.TARGET_ID.as("targetID"), SHARE_TO.AUTH_ID.as("authID"))
                .from(SHARE_TO)
                .where(SHARE_TO.SHARE_ID.eq(shareID))
                .fetchInto(ShareToVo.class);
    }
}
