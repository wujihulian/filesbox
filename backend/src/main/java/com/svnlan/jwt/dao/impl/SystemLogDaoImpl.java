package com.svnlan.jwt.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jooq.tables.pojos.SystemLogModel;
import com.svnlan.jooq.tables.records.SystemLogRecord;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.domain.SystemLog;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.vo.SystemLogVo;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.svnlan.jooq.Tables.USERS;
import static com.svnlan.jooq.tables.SystemLog.SYSTEM_LOG;

@Service
public class SystemLogDaoImpl implements SystemLogDao {
    @Autowired
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;
    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(SystemLog record) {
        SystemLogRecord slr = context.newRecord(SYSTEM_LOG);
        slr.set(SYSTEM_LOG.SESSION_ID, record.getSessionID());
        slr.set(SYSTEM_LOG.USER_ID, record.getUserID());
        slr.set(SYSTEM_LOG.TYPE, record.getType());
        slr.set(SYSTEM_LOG.DETAIL, record.getDesc());
        slr.set(SYSTEM_LOG.CREATE_TIME, LocalDateTime.now());
        slr.set(SYSTEM_LOG.VISIT_DATE, record.getVisitDate());
        slr.set(SYSTEM_LOG.CLIENT_TYPE, record.getClientType());
        slr.set(SYSTEM_LOG.OS_NAME, record.getOsName());
        slr.set(SYSTEM_LOG.TENANT_ID, tenantUtil.getTenantIdByServerName());
        return slr.insert();
    }

    @Override
    public int batchInsert(List<SystemLog> list) {
        List<SystemLogRecord> systemLogRecordList = new ArrayList<>();
        Long tenantId = tenantUtil.getTenantIdByServerName();
        for (SystemLog log : list) {
            SystemLogModel model = new SystemLogModel();
            model.setClientType(log.getClientType());
            model.setDetail(log.getDesc());
            model.setCreateTime(DateUtil.getLocalDateTimeFromMilli(log.getCreateTime()));
            model.setType(log.getType());
            model.setOsName(log.getOsName());
            model.setSessionId(log.getSessionID());
            model.setUserId(log.getUserID());
            model.setVisitDate(log.getVisitDate());
            model.setTenantId(tenantId);
            SystemLogRecord systemLogRecord = context.newRecord(SYSTEM_LOG);
            systemLogRecord.from(model);
            systemLogRecordList.add(systemLogRecord);
        }
        int[] execute = context.batchInsert(systemLogRecordList).execute();
        return execute.length;
    }

    public Condition buildSearchLogParamCondition(Map<String, Object> map) {
        Condition condition = DSL.trueCondition();
        Long userId = (Long) map.get("userID");
        if (!ObjectUtils.isEmpty(userId)){
            condition = condition.and(SYSTEM_LOG.USER_ID.eq(userId));
        }
        String type = (String) map.get("type");
        if (!ObjectUtils.isEmpty(type)){
            condition = condition.and(SYSTEM_LOG.TYPE.eq(type));
        }
        List<String> typeList = (List<String>) map.get("typeList");
        if (!CollectionUtils.isEmpty(typeList)){
            condition = condition.and(SYSTEM_LOG.TYPE.in(typeList));
        }
        Long minDateStr = (Long) map.get("minDate");
        Long maxDateStr = (Long) map.get("maxDate");
        LocalDateTime minDate = null;
        if (minDateStr != null) {
            minDate = DateUtil.getLocalDateTimeFromMilli(minDateStr * 1000);
        }
        LocalDateTime maxDate = null;
        if (maxDateStr != null) {
            maxDate = DateUtil.getLocalDateTimeFromMilli(maxDateStr * 1000);
        }
        if (Objects.nonNull(minDate)) {
            if (Objects.nonNull(maxDate)) {
                condition = condition.and(SYSTEM_LOG.CREATE_TIME.between(minDate, maxDate));
            } else {
                condition = condition.and(SYSTEM_LOG.CREATE_TIME.ge(minDate));
            }
        } else {
            if (Objects.nonNull(maxDate)) {
                condition = condition.and(SYSTEM_LOG.CREATE_TIME.le(maxDate));
            }
        }
        condition = condition.and(SYSTEM_LOG.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        return condition;
    }

    @Override
    public List<SystemLogVo> getSystemLogList(Map<String, Object> map) {
        SelectConditionStep<Record10<Long, String, Long, String, String, LocalDateTime, String, String, String, String>>
                from = context.select(SYSTEM_LOG.ID, SYSTEM_LOG.SESSION_ID, SYSTEM_LOG.USER_ID.as("userID"), SYSTEM_LOG.TYPE, SYSTEM_LOG.DETAIL.as("desc"), SYSTEM_LOG.CREATE_TIME, SYSTEM_LOG.OS_NAME,
                        USERS.NAME, USERS.NICKNAME, USERS.AVATAR)
                .from(SYSTEM_LOG)
                .leftJoin(USERS).on(USERS.ID.eq(SYSTEM_LOG.USER_ID))
                .where(buildSearchLogParamCondition(map));

        from.orderBy(DSL.field("system_log.create_time " + (String) map.get("sortType")));
        Integer startIndex = (Integer) map.get("startIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex, pageSize);
        }
        return from.fetchInto(SystemLogVo.class);
    }

    @Override
    public Long getLoginUserCountToday() {
        // select user_id,count(`system_log`.`user_id`) from `system_log`
        // where (`system_log`.`visit_date` = '2024-06-20' ) GROUP BY system_log.user_id
        //     @Select("SELECT count(1) FROM system_log
        //     WHERE type = 'user.index.loginSubmit' AND visit_date = DATE(now())")
        List<Long> list = context.select(SYSTEM_LOG.USER_ID)
                .from(SYSTEM_LOG)
                .where(DSL.field("visit_date")
                        .eq(DateUtil.DateToStr(DateUtil.yyyy_MM_dd, new Date() ))
                ).groupBy(SYSTEM_LOG.USER_ID).fetchInto(Long.class);
        return !CollectionUtils.isEmpty(list) ? list.size() : 0L;
    }

    @Override
    public List<JSONObject> queryUserLoginCount(Pair<LocalDateTime, LocalDateTime> pair) {
        return context.select(DSL.count(SYSTEM_LOG.ID).as("count"),
                DSL.field("date_format({0},{1})", SQLDataType.VARCHAR, SYSTEM_LOG.CREATE_TIME, DSL.inline("%Y-%m-%d")).as("date"),
                        SYSTEM_LOG.CREATE_TIME.as("createTime"))
                .from(SYSTEM_LOG)
                .where(SYSTEM_LOG.CREATE_TIME.between(pair.getFirst(), pair.getSecond()))
                .and(SYSTEM_LOG.TYPE.eq("user.index.loginSubmit"))
                .and(SYSTEM_LOG.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .groupBy(DSL.field("date"))
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<JSONObject> queryUserLoginCountStat(Pair<LocalDateTime, LocalDateTime> pair, Long userId) {
        return context.select(SYSTEM_LOG.DETAIL.as("desc"),
//                        DSL.field("date_format({0},{1})", SQLDataType.VARCHAR, "create_time", DSL.inline("%Y-%m-%d")).as("date"),
                        SYSTEM_LOG.CREATE_TIME.as("createTime")
                )
                .from(SYSTEM_LOG)
                .where(SYSTEM_LOG.CREATE_TIME.between(pair.getFirst(), pair.getSecond()))
                .and(SYSTEM_LOG.TYPE.eq("user.index.loginSubmit"))
                .and(SYSTEM_LOG.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .and(SYSTEM_LOG.USER_ID.eq(userId))
                .fetch(jsonObjectRecordMapper);

    }

    @Override
    public List<SystemLogVo> getSystemLogExportList(Map<String, Object> map) {
        return context.select(SYSTEM_LOG.ID, SYSTEM_LOG.DETAIL.as("desc"), SYSTEM_LOG.TYPE, SYSTEM_LOG.CREATE_TIME, SYSTEM_LOG.OS_NAME, USERS.NAME, USERS.NICKNAME)
                .from(SYSTEM_LOG)
                .innerJoin(USERS).on(SYSTEM_LOG.USER_ID.eq(USERS.ID))
                .where(buildSearchLogParamCondition(map))
                .orderBy(DSL.field("system_log.create_time " + (String) map.get("sortType")))
                .fetchInto(SystemLogVo.class);
    }

    @Override
    public Long getSystemLogListCount(Map<String, Object> map) {
        return context.selectCount()
                .from(SYSTEM_LOG)
                .where(buildSearchLogParamCondition(map)).fetchOneInto(Long.class);
    }
    @Override
    public JSONObject getSystemLogExportList(String sessionId, String type) {
        return context.select(SYSTEM_LOG.ID, SYSTEM_LOG.DETAIL.as("desc"))
                .from(SYSTEM_LOG)
                .where(SYSTEM_LOG.SESSION_ID.eq(sessionId), SYSTEM_LOG.TYPE.eq(type))
                .fetchOneInto(JSONObject.class);
    }

    @Override
    public int updateSystemLogDetail(String detail, Long id) {
        return context.update(SYSTEM_LOG)
                .set(SYSTEM_LOG.DETAIL,detail)
                .where(SYSTEM_LOG.ID.eq(id)).execute();
    }

}
