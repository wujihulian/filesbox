package com.svnlan.user.dao.Impl;

import com.svnlan.user.dao.ShareReportDao;
import com.svnlan.user.domain.ShareReport;
import com.svnlan.user.dto.ShareReportDTO;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.svnlan.jooq.Tables.SHARE;
import static com.svnlan.jooq.Tables.SHARE_REPORT;

@Repository
public class ShareReportDaoImpl implements ShareReportDao {
    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    public Condition buildQueryCondition(ShareReportDTO dto) {
        Condition condition = DSL.trueCondition();
        if (Objects.nonNull(dto.getTimeFrom())) {
            condition = condition.and(SHARE_REPORT.CREATE_TIME.ge(
                            LocalDateTime.parse(dto.getTimeFrom(), DateTimeFormatter.ofPattern(DateUtil.yyyy_MM_dd_HH_mm_ss))
                    )
            );
        }
        if (Objects.nonNull(dto.getTimeTo())) {
            condition = condition.and(SHARE_REPORT.CREATE_TIME.le(
                            LocalDateTime.parse(dto.getTimeTo(), DateTimeFormatter.ofPattern(DateUtil.yyyy_MM_dd_HH_mm_ss))
                    )
            );
        }
        if (Objects.nonNull(dto.getReportType())) {
            condition = condition.and(SHARE_REPORT.REPORT_TYPE.eq(dto.getReportType()));
        }
        Integer status = dto.getStatus();
        if (Objects.nonNull(status)) {
            if (Objects.equals(status, 0) || Objects.equals(status, 1)) {
                condition = condition.and(SHARE_REPORT.STATUS.eq(dto.getStatus()));
            }
            if (Objects.equals(status, 4) || Objects.equals(status, 3)) {
                condition = condition.and(SHARE_REPORT.STATUS.eq(1))
                        .and(SHARE.STATUS.eq(dto.getStatus()));
            }
        }
        condition = condition.and(SHARE_REPORT.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        return condition;
    }

    public SelectOnConditionStep<Record11<Long, Long, String, LocalDateTime, String, Long, Integer, Integer, Integer, Long, String>>
    buildQueryColumns() {
        return context.select(SHARE_REPORT.ID, SHARE_REPORT.SHARE_ID, SHARE_REPORT.REASON, SHARE_REPORT.CREATE_TIME, SHARE_REPORT.TITLE,
                        SHARE_REPORT.USER_ID, SHARE_REPORT.STATUS, SHARE.STATUS.as("shareStatus"), SHARE_REPORT.REPORT_TYPE,
                        SHARE_REPORT.SOURCE_ID, SHARE.SHARE_HASH)
                .from(SHARE_REPORT)
                .innerJoin(SHARE).on(SHARE_REPORT.SHARE_ID.eq(SHARE.ID));
    }

    @Override
    public Long selectCount(ShareReportDTO dto) {
        SelectJoinStep select = context.select(DSL.count(SHARE_REPORT.ID))
                .from(SHARE_REPORT);

        Integer status = dto.getStatus();
        if (Objects.nonNull(status)) {
            if (Objects.equals(status, 4) || Objects.equals(status, 3)) {
                select = select.innerJoin(SHARE).on(SHARE_REPORT.SHARE_ID.eq(SHARE.ID));
            }
        }
        return (Long)select.where(buildQueryCondition(dto)).fetchOneInto(Long.class);
    }

    @Override
    public List<ShareReport> selectList(ShareReportDTO dto) {
        return buildQueryColumns().where(buildQueryCondition(dto))
                .fetchInto(ShareReport.class);
    }

    @Override
    public List<ShareReport> selectListByIds(List<Long> ids) {
        return buildQueryColumns().where(SHARE_REPORT.ID.in(ids))
                .fetchInto(ShareReport.class);
    }

    @Override
    public int insert(ShareReport shareReport) {
        LocalDateTime now = LocalDateTime.now();
        return context.insertInto(SHARE_REPORT)
                .set(SHARE_REPORT.USER_ID, shareReport.getUserId())
                .set(SHARE_REPORT.SOURCE_ID, shareReport.getSourceId())
                .set(SHARE_REPORT.REPORT_TYPE, shareReport.getReportType())
                .set(SHARE_REPORT.REASON, shareReport.getReason())
                .set(SHARE_REPORT.FILE_ID, shareReport.getFileId())
                .set(SHARE_REPORT.STATUS, shareReport.getStatus())
                .set(SHARE_REPORT.CREATE_TIME, now)
                .set(SHARE_REPORT.MODIFY_TIME, now)
                .set(SHARE_REPORT.SHARE_ID, shareReport.getShareId())
                .set(SHARE_REPORT.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .execute();
    }
}
