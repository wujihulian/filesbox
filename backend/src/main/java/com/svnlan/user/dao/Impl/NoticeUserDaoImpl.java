package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.records.NoticeUserRecord;
import com.svnlan.user.dao.NoticeUserDao;
import com.svnlan.user.domain.NoticeUser;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.svnlan.jooq.Tables.NOTICE_USER;

@Repository
public class NoticeUserDaoImpl implements NoticeUserDao {
    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public LocalDateTime getMaxCreateTime(Long userId) {
        return context.select(DSL.max(NOTICE_USER.CREATE_TIME))
                .from(NOTICE_USER)
                .where(NOTICE_USER.USER_ID.eq(userId))
                .fetchOneInto(LocalDateTime.class);
    }

    @Override
    public int saveBatch(List<NoticeUser> needToInsertList) {
        if (CollectionUtils.isEmpty(needToInsertList)) {
            return 0;
        }
        InsertQuery<NoticeUserRecord> insertQuery = context.insertQuery(NOTICE_USER);
        LocalDateTime now = LocalDateTime.now();
        Long tenantId = tenantUtil.getTenantIdByServerName();
        for (NoticeUser noticeUser : needToInsertList) {
            insertQuery.newRecord();
            Optional.ofNullable(noticeUser.getNoticeId()).ifPresent(it -> insertQuery.addValue(NOTICE_USER.NOTICE_ID, it));
            Optional.ofNullable(noticeUser.getUserId()).ifPresent(it -> insertQuery.addValue(NOTICE_USER.USER_ID, it));
            Optional.ofNullable(noticeUser.getIsRead()).ifPresent(it -> insertQuery.addValue(NOTICE_USER.IS_READ, it));
            insertQuery.addValue(NOTICE_USER.CREATE_TIME, now);
            insertQuery.addValue(NOTICE_USER.MODIFY_TIME, now);
            insertQuery.addValue(NOTICE_USER.TENANT_ID, tenantId);
        }
        return insertQuery.execute();
    }
}
