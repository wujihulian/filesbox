package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.records.NoticeDetailRecord;
import com.svnlan.user.dao.NoticeDetailDao;
import com.svnlan.user.domain.NoticeDetail;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.svnlan.jooq.Tables.NOTICE_DETAIL;
import static com.svnlan.jooq.tables.Notice.NOTICE;

@Repository
public class NoticeDetailDaoImpl implements NoticeDetailDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public List<NoticeDetail> selectUnSyncList(LocalDateTime startQueryTime) {
        return context.select(NOTICE_DETAIL.NOTICE_ID, NOTICE_DETAIL.TARGET_USER_IDS, NOTICE_DETAIL.TARGET_DEPT_IDS, NOTICE_DETAIL.TARGET_ROLE_IDS,
                        NOTICE_DETAIL.IS_ALL, NOTICE_DETAIL.MODIFY_TIME)
                .from(NOTICE_DETAIL)
                .innerJoin(NOTICE).on(NOTICE.ID.eq(NOTICE_DETAIL.NOTICE_ID))
                .where(NOTICE.ENABLE.eq(1))
                .and(NOTICE_DETAIL.DR.eq(0))
                .and(NOTICE_DETAIL.MODIFY_TIME.gt(startQueryTime))
                .and(NOTICE_DETAIL.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(NoticeDetail.class);
    }

    @Override
    public int update(NoticeDetail noticeDetail, Long noticeId) {
        UpdateQuery<NoticeDetailRecord> updateQuery = context.updateQuery(NOTICE_DETAIL);
        buildValueBind(noticeDetail, updateQuery);
        updateQuery.addConditions(NOTICE_DETAIL.NOTICE_ID.eq(noticeId));
        return updateQuery.execute();
    }

    @Override
    public void save(NoticeDetail noticeDetail) {
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<NoticeDetailRecord> insertQuery = context.insertQuery(NOTICE_DETAIL);
        buildValueBind(noticeDetail, insertQuery);
        insertQuery.addValue(NOTICE_DETAIL.MODIFY_TIME, now);
        insertQuery.addValue(NOTICE_DETAIL.CREATE_TIME, now);
        insertQuery.execute();
    }

    private void buildValueBind(NoticeDetail noticeDetail, StoreQuery<NoticeDetailRecord> query) {
        LocalDateTime now = LocalDateTime.now();
        Optional.ofNullable(noticeDetail.getNoticeId()).ifPresent(it -> query.addValue(NOTICE_DETAIL.NOTICE_ID, it));
        Optional.ofNullable(noticeDetail.getContent()).ifPresent(it -> query.addValue(NOTICE_DETAIL.CONTENT, it));
        Optional.ofNullable(noticeDetail.getIsAll()).ifPresent(it -> query.addValue(NOTICE_DETAIL.IS_ALL, it));
        Optional.ofNullable(noticeDetail.getTargetUserIds()).ifPresent(it -> query.addValue(NOTICE_DETAIL.TARGET_USER_IDS, JSON.valueOf(it)));
        Optional.ofNullable(noticeDetail.getTargetDeptIds()).ifPresent(it -> query.addValue(NOTICE_DETAIL.TARGET_DEPT_IDS, JSON.valueOf(it)));
        Optional.ofNullable(noticeDetail.getTargetRoleIds()).ifPresent(it -> query.addValue(NOTICE_DETAIL.TARGET_ROLE_IDS, JSON.valueOf(it)));
        Optional.ofNullable(noticeDetail.getNoticeDetailId()).ifPresent(it -> query.addValue(NOTICE_DETAIL.NOTICE_DETAIL_ID, it));
        Optional.ofNullable(noticeDetail.getDr()).ifPresent(it -> query.addValue(NOTICE_DETAIL.DR, it));
        query.addValue(NOTICE_DETAIL.TENANT_ID, tenantUtil.getTenantIdByServerName());
        query.addValue(NOTICE_DETAIL.MODIFY_TIME, now);
    }
}
