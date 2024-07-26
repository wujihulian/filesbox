package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.pojos.VisitCountRecordModel;
import com.svnlan.jooq.tables.records.VisitCountRecordRecord;
import com.svnlan.user.dao.VisitCountRecordDao;
import com.svnlan.user.domain.VisitCountRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.svnlan.jooq.Tables.VISIT_COUNT_RECORD;

@Repository
public class VisitCountRecordDaoImpl implements VisitCountRecordDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;
    @Override
    public int updateByPrimaryKeySelective(VisitCountRecord record, Integer isIncrease) {
        UpdateSetFirstStep<VisitCountRecordRecord> firstStep = context.update(VISIT_COUNT_RECORD);
        UpdateSetMoreStep<VisitCountRecordRecord> moreStep = null;
        if (Objects.equals(isIncrease, 1)) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.VISIT_COUNT, VISIT_COUNT_RECORD.VISIT_COUNT.add(record.getVisitCount()));
        } else {
            if (Objects.nonNull(record.getVisitCount())) {
                moreStep = firstStep.set(VISIT_COUNT_RECORD.VISIT_COUNT, record.getVisitCount());
            }
        }
        if (Objects.nonNull(record.getDeviceType())) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.DEVICE_TYPE, record.getDeviceType());
        }
        if (Objects.nonNull(record.getVisitDay())) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.VISIT_DAY, convertVisitDay(record.getVisitDay()));
        }
        if (Objects.nonNull(record.getType()) && record.getType() > 0) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.TYPE, record.getType());
        }
        if (StringUtils.hasText(record.getOsName())) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.OS_NAME, record.getOsName());
        }
//        if (Objects.nonNull(record.getModifyTime())) {
            moreStep = firstStep.set(VISIT_COUNT_RECORD.MODIFY_TIME, LocalDateTime.now());
//        }
//        if (Objects.nonNull(record.getCreateTime())) {
//            moreStep = firstStep.set(VISIT_COUNT_RECORD.CREATE_TIME, recordModel.getCreateTime());
//        }
        if (Objects.nonNull(moreStep)) {
            return moreStep.where(VISIT_COUNT_RECORD.ID.eq(record.getId())).execute();
        }
        return 0;
    }

    @Override
    public void insertBatch(List<VisitCountRecord> records) {
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<VisitCountRecordRecord> insertQuery = context.insertQuery(VISIT_COUNT_RECORD);
        for (VisitCountRecord record : records) {
            insertQuery.newRecord();
            insertQuery.addValue(VISIT_COUNT_RECORD.VISIT_COUNT, record.getVisitCount());
            insertQuery.addValue(VISIT_COUNT_RECORD.DEVICE_TYPE, record.getDeviceType());
            insertQuery.addValue(VISIT_COUNT_RECORD.OS_NAME, record.getOsName());
            insertQuery.addValue(VISIT_COUNT_RECORD.TYPE, record.getType());
            insertQuery.addValue(VISIT_COUNT_RECORD.VISIT_DAY, convertVisitDay(record.getVisitDay()));
            insertQuery.addValue(VISIT_COUNT_RECORD.MODIFY_TIME, now);
            insertQuery.addValue(VISIT_COUNT_RECORD.CREATE_TIME, now);
            insertQuery.addValue(VISIT_COUNT_RECORD.TENANT_ID, tenantUtil.getTenantIdByServerName());
        }
        insertQuery.execute();
    }

    @Override
    public Long getCumulativeVisitTotal() {
        return context.select(DSL.sum(VISIT_COUNT_RECORD.VISIT_COUNT))
                .from(VISIT_COUNT_RECORD)
                .where(VISIT_COUNT_RECORD.TYPE.eq(5))
                .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(Long.class);
    }

    @Override
    public List<VisitCountRecord> selectByOsNameAndType(List<VisitCountRecord> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        Long tenantId = tenantUtil.getTenantIdByServerName();
        return list.stream()
                .map(it -> context.select(VISIT_COUNT_RECORD.ID, VISIT_COUNT_RECORD.TYPE, VISIT_COUNT_RECORD.OS_NAME)
                        .from(VISIT_COUNT_RECORD)
                        .where(VISIT_COUNT_RECORD.TYPE.eq(it.getType()))
                        .and(VISIT_COUNT_RECORD.OS_NAME.eq(it.getOsName()))
                        .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantId))
                ).reduce((v1, v2) -> (SelectConditionStep<Record3<Long, Integer, String>>) v1.unionAll(v2))
                .map(it -> it.fetchInto(VisitCountRecord.class))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<VisitCountRecord> selectListByOsNameList(List<String> nullOsName, Long tenantId) {

        return context.select(VISIT_COUNT_RECORD.OS_NAME, VISIT_COUNT_RECORD.VISIT_COUNT)
                .from(VISIT_COUNT_RECORD)
                .where(VISIT_COUNT_RECORD.TYPE.eq(4))
                .and(VISIT_COUNT_RECORD.OS_NAME.in(nullOsName))
                .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantId))
                .fetchInto(VisitCountRecord.class);
    }

    //SELECT visitCount, deviceType, visitDay FROM visit_count_record WHERE visitDay >= #{startDate}
    // //AND visitDay <= #{endDate} AND type = 1 AND deviceType IS NOT NULL
    @Override
    public List<VisitCountRecord> queryDeviceClientVisitData(LocalDate startDate, LocalDate endDate) {
        return context.select(VISIT_COUNT_RECORD.VISIT_COUNT, VISIT_COUNT_RECORD.DEVICE_TYPE, VISIT_COUNT_RECORD.VISIT_DAY)
                .from(VISIT_COUNT_RECORD)
                .where(VISIT_COUNT_RECORD.VISIT_DAY.between(
                        Date.from(Instant.ofEpochSecond(startDate.atTime(0, 0, 0).toEpochSecond(ZoneOffset.ofHours(8)))),
                        Date.from(Instant.ofEpochSecond(endDate.atTime(23, 23, 59).toEpochSecond(ZoneOffset.ofHours(8))))
                ))
                .and(VISIT_COUNT_RECORD.TYPE.eq(1))
                .and(VISIT_COUNT_RECORD.DEVICE_TYPE.isNotNull())
                .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(VisitCountRecord.class);
    }

    @Override
    public VisitCountRecord selectByVisitDayAndType(String visitPerMonth, int type) {
        return context.select(VISIT_COUNT_RECORD.ID)
                .from(VISIT_COUNT_RECORD)
                .where(VISIT_COUNT_RECORD.TYPE.eq(type))
//                .and("DATE_FORMAT(visitDay,'%Y-%m') = ?", visitPerMonth)
                // zhy-note jooq 时间格式化
                .and(DSL.field("date_format({0},{1})", SQLDataType.VARCHAR, VISIT_COUNT_RECORD.VISIT_DAY, DSL.inline("%Y-%m")).eq(visitPerMonth))
                .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(VisitCountRecord.class);
    }

    @Override
    public int updateVisitCount(Long id, Long visitCountOneDay) {
        return context.update(VISIT_COUNT_RECORD)
                .set(VISIT_COUNT_RECORD.VISIT_COUNT, VISIT_COUNT_RECORD.VISIT_COUNT.add(visitCountOneDay))
                .where(VISIT_COUNT_RECORD.ID.eq(id))
                .execute();
    }

    private Date convertVisitDay(LocalDate localDate) {
        return Objects.nonNull(localDate) ? Date.valueOf(localDate)  : null;
    }

    @Override
    public void insertUserVisitRecord(VisitCountRecord record) {
        LocalDateTime now = LocalDateTime.now();
        context.insertInto(VISIT_COUNT_RECORD)
                .columns(VISIT_COUNT_RECORD.VISIT_COUNT, VISIT_COUNT_RECORD.VISIT_DAY, VISIT_COUNT_RECORD.TYPE,
                        VISIT_COUNT_RECORD.MODIFY_TIME, VISIT_COUNT_RECORD.CREATE_TIME, VISIT_COUNT_RECORD.TENANT_ID)
                .values(record.getVisitCount(), convertVisitDay(record.getVisitDay()), record.getType(), now, now, tenantUtil.getTenantIdByServerName())
                .execute();
    }

    @Override
    public List<String> selectOSNameList() {
        // SELECT osName FROM visit_count_record WHERE type = 4
        return context.select(VISIT_COUNT_RECORD.OS_NAME)
                .from(VISIT_COUNT_RECORD)
                .where(VISIT_COUNT_RECORD.TYPE.eq(4))
                .and(VISIT_COUNT_RECORD.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetch(VISIT_COUNT_RECORD.OS_NAME);
    }
}
