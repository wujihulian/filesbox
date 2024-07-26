package com.svnlan.user.dao.Impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jooq.tables.records.NoticeRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.dao.NoticeDao;
import com.svnlan.user.domain.Notice;
import com.svnlan.utils.TenantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.svnlan.jooq.Tables.*;

@Repository
public class NoticeDaoImpl implements NoticeDao {
    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public List<JSONObject> querySimpleInfo(List<Long> ids) {
        Condition condition = DSL.trueCondition();
        if (CollectionUtils.isNotEmpty(ids)) {
            condition = condition.and(NOTICE.ID.in(ids));
        } else {
            condition = condition.and(NOTICE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        }
        return context.select(NOTICE.ID, NOTICE.TITLE, NOTICE_DETAIL.CONTENT, NOTICE.CREATE_TIME, NOTICE.SEND_TIME, NOTICE.STATUS)
                .from(NOTICE)
                .innerJoin(NOTICE_DETAIL).on(NOTICE_DETAIL.NOTICE_ID.eq(NOTICE.ID))
                .and(NOTICE_DETAIL.NOTICE_DETAIL_ID.isNull())
                .where(NOTICE.STATUS.in(0, 1))
                .and(NOTICE_DETAIL.DR.eq(0))
                .and(condition)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public Long selectMaxOrder() {
        return context.select(DSL.max(NOTICE.SORT))
                .from(NOTICE)
                .where(NOTICE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(Long.class);
    }

    @Override
    public List<JSONObject> selectNoticeList(Long userId, int startIndex, Integer pageSize) {
        return context.select(NOTICE.ID, NOTICE.TITLE, NOTICE.SEND_TIME, NOTICE.CREATE_TIME, NOTICE_USER.IS_READ)
                .from(NOTICE)
                .innerJoin(NOTICE_USER).on(NOTICE_USER.NOTICE_ID.eq(NOTICE.ID))
                .and(NOTICE_USER.USER_ID.eq(userId))
                .where(NOTICE.STATUS.eq(1))
                .and(NOTICE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(NOTICE.CREATE_TIME.desc())
                .limit(DSL.val(startIndex), DSL.val(pageSize))
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public Long selectNoticeListTotal(Long userId) {
        return context.select(DSL.count(NOTICE.ID))
                .from(NOTICE)
                .innerJoin(NOTICE_USER).on(NOTICE_USER.NOTICE_ID.eq(NOTICE.ID))
                .and(NOTICE_USER.USER_ID.eq(userId))
                .where(NOTICE.STATUS.eq(1))
                .fetchOneInto(Long.class);
    }

    @Override
    public Long selectNoticeUnreadListTotal(Long userId) {
        return context.select(DSL.count(NOTICE.ID))
                .from(NOTICE)
                .innerJoin(NOTICE_USER).on(NOTICE_USER.NOTICE_ID.eq(NOTICE.ID))
                .and(NOTICE_USER.USER_ID.eq(userId))
                .where(NOTICE.STATUS.eq(1))
                .and(NOTICE_USER.IS_READ.eq(0))
                .fetchOneInto(Long.class);
    }

    @Override
    public Long save(Notice notice) {
        InsertQuery<NoticeRecord> insertQuery = context.insertQuery(NOTICE);
        populateSQLValue(notice, insertQuery);
        insertQuery.setReturning(NOTICE.ID);
        insertQuery.execute();
        Long id = insertQuery.getReturnedRecord().get(NOTICE.ID);
        notice.setId(id);
        return id;
    }

    private void populateSQLValue(Notice notice, StoreQuery<NoticeRecord> query) {
        Optional.ofNullable(notice.getTitle()).ifPresent(it -> query.addValue(NOTICE.TITLE, it));
        Optional.ofNullable(notice.getLevel()).ifPresent(it -> query.addValue(NOTICE.LEVEL, it));
        Optional.ofNullable(notice.getStatus()).ifPresent(it -> query.addValue(NOTICE.STATUS, it));
        Optional.ofNullable(notice.getEnable()).ifPresent(it -> query.addValue(NOTICE.ENABLE, it));
        Optional.ofNullable(notice.getSendType()).ifPresent(it -> query.addValue(NOTICE.SEND_TYPE, it));
        Optional.ofNullable(notice.getSendTime()).ifPresent(it -> query.addValue(NOTICE.SEND_TIME, it));
        Optional.ofNullable(notice.getSort()).ifPresent(it -> query.addValue(NOTICE.SORT, it));
        Optional.ofNullable(notice.getSenderId()).ifPresent(it -> query.addValue(NOTICE.SENDER_ID, it));
        Optional.ofNullable(notice.getSenderIp()).ifPresent(it -> query.addValue(NOTICE.SENDER_IP, it));
        Optional.ofNullable(notice.getNoticeType()).ifPresent(it -> query.addValue(NOTICE.NOTICE_TYPE, it));
        Optional.ofNullable(notice.getCreateTime()).ifPresent(it -> query.addValue(NOTICE.CREATE_TIME, it));
        Optional.ofNullable(notice.getModifyTime()).ifPresent(it -> query.addValue(NOTICE.MODIFY_TIME, it));
        query.addValue(NOTICE.TENANT_ID, tenantUtil.getTenantIdByServerName());
    }

    @Override
    public void updateById(Notice notice) {
        UpdateQuery<NoticeRecord> updateQuery = context.updateQuery(NOTICE);
        populateSQLValue(notice, updateQuery);
        updateQuery.addConditions(NOTICE.ID.eq(notice.getId()));
        updateQuery.execute();
    }
}
