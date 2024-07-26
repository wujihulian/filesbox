package com.svnlan.home.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.dao.IoSourceEventDao;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.vo.IoSourceEventVo;
import com.svnlan.jooq.tables.records.IoSourceEventRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.svnlan.jooq.Tables.*;

@Repository
public class IoSourceEventDaoImpl implements IoSourceEventDao {
    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(IoSourceEvent ioSourceEvent) {
        return context.insertInto(IO_SOURCE_EVENT)
                .set(IO_SOURCE_EVENT.SOURCE_ID, ioSourceEvent.getSourceID())
                .set(IO_SOURCE_EVENT.SOURCE_PARENT, ioSourceEvent.getSourceParent())
                .set(IO_SOURCE_EVENT.USER_ID, ioSourceEvent.getUserID())
                .set(IO_SOURCE_EVENT.TYPE, ioSourceEvent.getType())
                .set(IO_SOURCE_EVENT.DETAIL, ioSourceEvent.getDesc())
                .set(IO_SOURCE_EVENT.CREATE_TIME, LocalDateTime.now())
                .set(IO_SOURCE_EVENT.TENANT_ID, ObjectUtils.isEmpty(ioSourceEvent.getTenantId()) ? tenantUtil.getTenantIdByServerName() : ioSourceEvent.getTenantId())
                .execute();
    }

    @Override
    public int batchInsert(List<IoSourceEvent> list) {
        Long paramTenantId = list.get(0).getTenantId();
        Long tenantId = ObjectUtils.isEmpty(paramTenantId) ? tenantUtil.getTenantIdByServerName() : paramTenantId;
        InsertQuery<IoSourceEventRecord> insertQuery = context.insertQuery(IO_SOURCE_EVENT);
        for (IoSourceEvent ioSourceEvent : list) {
            insertQuery.newRecord();
            insertQuery.addValue(IO_SOURCE_EVENT.SOURCE_ID, ioSourceEvent.getSourceID());
            insertQuery.addValue(IO_SOURCE_EVENT.SOURCE_PARENT, ioSourceEvent.getSourceParent());
            insertQuery.addValue(IO_SOURCE_EVENT.USER_ID, ioSourceEvent.getUserID());
            insertQuery.addValue(IO_SOURCE_EVENT.TYPE, ioSourceEvent.getType());
            insertQuery.addValue(IO_SOURCE_EVENT.DETAIL, ioSourceEvent.getDesc());
            insertQuery.addValue(IO_SOURCE_EVENT.CREATE_TIME, LocalDateTime.now());
            insertQuery.addValue(IO_SOURCE_EVENT.TENANT_ID, tenantId);
        }
        return insertQuery.execute();
    }

    @Override
    public List<IoSourceEventVo> getSourceEventBySourceID(Long sourceID, int isFolder) {
        SelectOrderByStep<Record14<Integer, Long, Long, Long, Long, String, String, LocalDateTime, String, String, Integer, Integer, Integer, String>> selectConditionStep
                = context.select(DSL.val(0).as("isChildren"), IO_SOURCE_EVENT.ID, IO_SOURCE_EVENT.SOURCE_ID.as("sourceID"), IO_SOURCE_EVENT.SOURCE_PARENT, IO_SOURCE_EVENT.USER_ID.as("userID"),
                        IO_SOURCE_EVENT.TYPE, IO_SOURCE_EVENT.DETAIL.as("desc"), IO_SOURCE_EVENT.CREATE_TIME,
                        USERS.AVATAR, DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"), USERS.SEX, USERS.STATUS,
                        IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME)
                .from(IO_SOURCE_EVENT)
                .innerJoin(IO_SOURCE).on(IO_SOURCE_EVENT.SOURCE_ID.eq(IO_SOURCE.ID))
                .leftJoin(USERS).on(USERS.ID.eq(IO_SOURCE_EVENT.USER_ID)).and(USERS.STATUS.in(0, 1))
                .where(IO_SOURCE_EVENT.SOURCE_ID.eq(sourceID));

        if (Objects.equals(isFolder, 1)) {
            selectConditionStep = selectConditionStep.unionAll(
                    context.select(DSL.val(1).as("isChildren"), IO_SOURCE_EVENT.ID, IO_SOURCE_EVENT.SOURCE_ID.as("sourceID"), IO_SOURCE_EVENT.SOURCE_PARENT, IO_SOURCE_EVENT.USER_ID.as("userID"),
                                    IO_SOURCE_EVENT.TYPE, IO_SOURCE_EVENT.DETAIL.as("desc"), IO_SOURCE_EVENT.CREATE_TIME,
                                    USERS.AVATAR, DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"), USERS.SEX, USERS.STATUS,
                                    IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME)
                            .from(IO_SOURCE_EVENT)
                            .innerJoin(IO_SOURCE).on(IO_SOURCE_EVENT.SOURCE_ID.eq(IO_SOURCE.ID))
                            .leftJoin(USERS).on(USERS.ID.eq(IO_SOURCE_EVENT.USER_ID)).and(USERS.STATUS.in(0, 1))
                            .where(IO_SOURCE_EVENT.SOURCE_PARENT.eq(sourceID))
            );
        }
        return selectConditionStep.orderBy(IO_SOURCE_EVENT.CREATE_TIME.desc(), IO_SOURCE_EVENT.ID.desc())
                .fetchInto(IoSourceEventVo.class);
    }

    @Override
    public Long getSourceEventBySourceIDCount(Long sourceID, int isFolder) {
        SelectOrderByStep<Record1<Integer>> selectConditionStep
                = context.selectCount()
                .from(IO_SOURCE_EVENT)
                .innerJoin(IO_SOURCE).on(IO_SOURCE_EVENT.SOURCE_ID.eq(IO_SOURCE.ID))
                .leftJoin(USERS).on(USERS.ID.eq(IO_SOURCE_EVENT.USER_ID)).and(USERS.STATUS.in(0, 1))
                .where(IO_SOURCE_EVENT.SOURCE_ID.eq(sourceID));

        if (Objects.equals(isFolder, 1)) {
            selectConditionStep = selectConditionStep.unionAll(
                    context.selectCount()
                            .from(IO_SOURCE_EVENT)
                            .innerJoin(IO_SOURCE).on(IO_SOURCE_EVENT.SOURCE_ID.eq(IO_SOURCE.ID))
                            .leftJoin(USERS).on(USERS.ID.eq(IO_SOURCE_EVENT.USER_ID)).and(USERS.STATUS.in(0, 1))
                            .where(IO_SOURCE_EVENT.SOURCE_PARENT.eq(sourceID))
            );
        }
        List<Integer> list =  selectConditionStep.fetchInto(Integer.class);
        long num = 0;
        if (!CollectionUtils.isEmpty(list)){
            num = list.stream().collect(Collectors.toList()).stream().reduce(Integer::sum).get();
        }
        return num;
    }

    @Override
    public List<JSONObject> queryFileOperateCount(Pair<LocalDateTime, LocalDateTime> timeRange) {
        return context.select(
                DSL.field("date_format({0},{1})", SQLDataType.VARCHAR, IO_SOURCE_EVENT.CREATE_TIME, DSL.inline("%Y-%m-%d")).as("date"),
                        IO_SOURCE_EVENT.CREATE_TIME.as("createTime"),
                        DSL.count(IO_SOURCE_EVENT.ID).as("count"))
                .from(IO_SOURCE_EVENT)
                .where(IO_SOURCE_EVENT.CREATE_TIME.between(timeRange.getFirst(), timeRange.getSecond()))
                .and(IO_SOURCE_EVENT.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .groupBy(DSL.field("date", String.class))
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<JSONObject> queryVideoFileOperateCount(Pair<LocalDateTime, LocalDateTime> timeRange) {
        return context.select(DSL.field("date_format({0},{1})", SQLDataType.VARCHAR, IO_SOURCE_EVENT.CREATE_TIME, DSL.inline("%Y-%m-%d")).as("date"),
                        DSL.count(IO_SOURCE_EVENT.ID).as("count"))
                .from(IO_SOURCE_EVENT)
                .innerJoin(IO_SOURCE).on(IO_SOURCE_EVENT.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(IO_SOURCE_EVENT.CREATE_TIME.between(timeRange.getFirst(), timeRange.getSecond()))
                .and(IO_SOURCE_EVENT.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .and(IO_SOURCE_EVENT.TYPE.eq("4"))
                .groupBy(DSL.field("date", String.class))
                .orderBy(DSL.field("date", String.class))
                .fetch(jsonObjectRecordMapper);
    }
}
