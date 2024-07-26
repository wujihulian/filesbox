package com.svnlan.user.dao.Impl;


import com.svnlan.jooq.tables.records.CommonConvertRecord;
import com.svnlan.user.dao.CommonConvertDao;
import com.svnlan.user.domain.CommonConvert;
import com.svnlan.user.vo.CommonConvertVo;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.svnlan.jooq.Tables.*;

@Repository
public class CommonConvertDaoImpl implements CommonConvertDao {
    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(CommonConvert commonConvert) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMON_CONVERT)
                .set(COMMON_CONVERT.SOURCE_ID, commonConvert.getSourceID())
                .set(COMMON_CONVERT.FILE_ID, commonConvert.getFileID())
                .set(COMMON_CONVERT.USER_ID, commonConvert.getUserID())
                .set(COMMON_CONVERT.NAME, commonConvert.getName())
                .set(COMMON_CONVERT.STATE, commonConvert.getState())
                .set(COMMON_CONVERT.FREQUENCY_COUNT, commonConvert.getFrequencyCount())
                .set(COMMON_CONVERT.REMARK, commonConvert.getRemark())
                .set(COMMON_CONVERT.CREATE_TIME, now)
                .set(COMMON_CONVERT.SCHEDULE_FREQUENCY_COUNT, 0)
                .set(COMMON_CONVERT.MODIFY_TIME, now)
                .set(COMMON_CONVERT.SCHEDULE_TIME, now)
                .set(COMMON_CONVERT.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_CONVERT.ID).fetchOne().getId();

        commonConvert.setConvertID(id);
        return 1;
    }

    @Override
    public int updateStatus(Long convertID, String state) {
        return context.update(COMMON_CONVERT)
                .set(COMMON_CONVERT.STATE, state)
                .set(COMMON_CONVERT.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_CONVERT.ID.eq(convertID))
                .execute();
    }

    @Override
    public int updateStatusAndCount(Long convertID, Long userID, String state, String remark) {

        UpdateSetMoreStep<CommonConvertRecord> moreStep = context.update(COMMON_CONVERT)
                .set(COMMON_CONVERT.STATE, state)
                .set(COMMON_CONVERT.FREQUENCY_COUNT, COMMON_CONVERT.FREQUENCY_COUNT.add(1))
                .set(COMMON_CONVERT.USER_ID, userID)
                .set(COMMON_CONVERT.MODIFY_TIME, LocalDateTime.now());

        if (Objects.nonNull(remark)) {
            moreStep.set(COMMON_CONVERT.REMARK, remark);
        }
        return moreStep.where(COMMON_CONVERT.ID.eq(convertID))
                .execute();
    }

    @Override
    public int updateScheduleStatus(Long convertID, String state) {
        return context.update(COMMON_CONVERT)
                .set(COMMON_CONVERT.STATE, state)
                .set(COMMON_CONVERT.SCHEDULE_FREQUENCY_COUNT, COMMON_CONVERT.SCHEDULE_FREQUENCY_COUNT.add(1))
                .set(COMMON_CONVERT.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_CONVERT.ID.eq(convertID))
                .execute();
    }

    @Override
    public Long checkExist(Long sourceID, Long fileID) {
        return context.select(COMMON_CONVERT.ID)
                .from(COMMON_CONVERT)
                .where(COMMON_CONVERT.SOURCE_ID.eq(sourceID))
                .and((Objects.nonNull(fileID) && fileID > 0) ? COMMON_CONVERT.FILE_ID.eq(fileID) : DSL.noCondition())
                .limit(1)
                .fetchOneInto(Long.class);
    }

    @Override
    public List<CommonConvertVo> getConvertList(Map<String, Object> map) {
        SelectOnConditionStep<Record18<Long, Long, Long, Long, String, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, String, Long, String, Integer, Long, Long, Integer>>
                step = context.selectDistinct(COMMON_CONVERT.SOURCE_ID.as("sourceID"), COMMON_CONVERT.ID.as("convertID"), IO_SOURCE.FILE_ID.as("fileID")
                , COMMON_CONVERT.USER_ID.as("userID"),
                        IO_FILE.NAME, COMMON_CONVERT.STATE, COMMON_CONVERT.FREQUENCY_COUNT, COMMON_CONVERT.CREATE_TIME, COMMON_CONVERT.MODIFY_TIME,
                        COMMON_CONVERT.SCHEDULE_FREQUENCY_COUNT, COMMON_CONVERT.SCHEDULE_TIME, COMMON_CONVERT.REMARK, IO_SOURCE.SIZE, IO_SOURCE.FILE_TYPE,
                        IO_FILE.IS_M3U8, IO_SOURCE.CREATE_USER, IO_FILE.SIZE.as("fileSize"),
                        DSL.when(IO_FILE.IS_EXIST_FILE.eq(0), DSL.val(2))
                                .when(COMMON_CONVERT.STATE.eq("1"), DSL.val(3))
                                .when(COMMON_CONVERT.STATE.eq("0"), DSL.val(1))
                                .when(COMMON_CONVERT.STATE.eq("2"), DSL.val(0))
                                .as("stateSort")

                ).from(COMMON_CONVERT)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(COMMON_CONVERT.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID));
        String sortField = (String) map.get("sortField");
        String sortType = (String) map.get("sortType");

        SelectConditionStep<Record18<Long, Long, Long, Long, String, String, Integer, LocalDateTime, LocalDateTime, Integer, LocalDateTime, String, Long, String, Integer, Long, Long, Integer>>
                where = step.where(getConvertListCondition(map));

        if ("asc".equals(sortType)) {
            where.orderBy(DSL.field(sortField).asc(), COMMON_CONVERT.CREATE_TIME.desc());
        }else {
            where.orderBy(DSL.field(sortField).desc(), COMMON_CONVERT.CREATE_TIME.desc());
        }
        Integer startIndex = (Integer) map.get("startIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            where.limit(startIndex,pageSize);
        }
        return where.fetchInto(CommonConvertVo.class);
    }

    @Override
    public Long queryConvertExceptionCountInstant() {
        return context.select(DSL.count(COMMON_CONVERT.ID)).from(COMMON_CONVERT)
                .where(COMMON_CONVERT.STATE.eq("2"))
                .and(COMMON_CONVERT.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(Long.class);
    }

    @Override
    public Long getConvertListCount(Map<String, Object> map) {
        SelectOnConditionStep<Record1<Integer>>
                step = context.select(DSL.countDistinct(COMMON_CONVERT.SOURCE_ID)).from(COMMON_CONVERT)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(COMMON_CONVERT.SOURCE_ID));
        return step.where(getConvertListCondition(map)).fetchOneInto(Long.class);
    }

    public Condition getConvertListCondition(Map<String, Object> map) {
        Condition condition = DSL.trueCondition().and(COMMON_CONVERT.STATE.in("0", "1", "2").and(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName())));

        Long userId = (Long) map.get("userID");
        if (!ObjectUtils.isEmpty(userId)){
            condition = condition.and(COMMON_CONVERT.USER_ID.eq(userId));
        }
        Long sourceID = (Long) map.get("sourceID");
        if (!ObjectUtils.isEmpty(sourceID)){
            condition = condition.and(IO_SOURCE.ID.eq(sourceID));
        }
        String state = (String) map.get("state");
        if (!ObjectUtils.isEmpty(state)){
            condition = condition.and(COMMON_CONVERT.STATE.eq(state));
        }
        List<String> fileType = (List<String>) map.get("fileType");
        if (!CollectionUtils.isEmpty(fileType)){
            condition = condition.and(IO_SOURCE.FILE_TYPE.in(fileType));
        }
        String keyword = (String) map.get("keyword");
        if (keyword != null) {
            condition = condition.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }

        Long minDateStr = (Long) map.get("minDate");
        Long maxDateStr = (Long) map.get("maxDate");

        LocalDateTime minDate = null;
        if (minDateStr != null){
            minDate = DateUtil.getLocalDateTimeFromMilli(minDateStr * 1000);
        }
        LocalDateTime maxDate = null;
        if (maxDateStr != null){
            maxDate = DateUtil.getLocalDateTimeFromMilli(maxDateStr * 1000);
        }

        if (Objects.nonNull(minDate)) {
            if (Objects.nonNull(maxDate)) {
                condition = condition.and(IO_SOURCE.CREATE_TIME.between(minDate, maxDate));
            } else {
                condition = condition.and(IO_SOURCE.CREATE_TIME.ge(minDate));
            }
        } else {
            if (Objects.nonNull(maxDate)) {
                condition = condition.and(IO_SOURCE.CREATE_TIME.le(maxDate));
            }
        }
        return condition;
    }
}
