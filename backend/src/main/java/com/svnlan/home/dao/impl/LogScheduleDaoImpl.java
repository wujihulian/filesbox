package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.LogScheduleDao;
import com.svnlan.home.domain.LogSchedule;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

import static com.svnlan.jooq.Tables.LOG_SCHEDULE;

@Repository
public class LogScheduleDaoImpl implements LogScheduleDao {
    @Resource
    private DSLContext context;

    @Override
    public int insert(LogSchedule record) {
        Long id = context.insertInto(LOG_SCHEDULE)
                .set(LOG_SCHEDULE.COMMON_SCHEDULE_ID, record.getCommonScheduleId())
                .set(LOG_SCHEDULE.GMT_START, DSL.ifnull(record.getGmtStart(), LocalDateTime.now()))
                .set(LOG_SCHEDULE.GMT_END, DSL.ifnull(record.getGmtEnd(), LocalDateTime.now()))
                .set(LOG_SCHEDULE.STATE, record.getState())
                .set(LOG_SCHEDULE.REMARK, record.getRemark())
                .set(LOG_SCHEDULE.TENANT_ID, record.getTenantId())
                .returning(LOG_SCHEDULE.ID).fetchOne().getId();
        record.setLogScheduleId(id);
        return 1;
    }

    @Override
    public int updateLogSuccess(Map<String, Object> map) {
        return context.update(LOG_SCHEDULE)
                .set(LOG_SCHEDULE.STATE, (String) map.get("state"))
                .set(LOG_SCHEDULE.GMT_END, (LocalDateTime) map.get("gmtEnd"))
                .set(LOG_SCHEDULE.REMARK, DSL.concat(
                        (String) map.get("remark"),
                        ",定时任务执行时间:",
                        DSL.localDateTimeDiff(LOG_SCHEDULE.GMT_START, (LocalDateTime) map.get("gmtEnd")).get(LOG_SCHEDULE.newRecord()).toString()))
                .where(LOG_SCHEDULE.ID.eq((Long) map.get("logScheduleId")))
                .execute();


    }
}
