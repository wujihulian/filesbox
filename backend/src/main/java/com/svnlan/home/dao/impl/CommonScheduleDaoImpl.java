package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.CommonScheduleDao;
import com.svnlan.home.domain.CommonSchedule;
import com.svnlan.jooq.tables.pojos.CommonScheduleModel;
import com.svnlan.jooq.tables.records.CommonScheduleRecord;
import com.svnlan.utils.DateUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.svnlan.jooq.tables.CommonSchedule.COMMON_SCHEDULE;

@Service
public class CommonScheduleDaoImpl implements CommonScheduleDao {
    @Autowired
    private DSLContext context;
    @Override
    public int updateOperateTime(String commonScheduleId) {
        return context.update(COMMON_SCHEDULE).set(COMMON_SCHEDULE.GMT_MODIFIED,LocalDateTime.now())
                .where( COMMON_SCHEDULE.ID.eq(commonScheduleId)
                        .and(COMMON_SCHEDULE.GMT_MODIFIED.le(DSL.localDateTimeSub(LocalDateTime.now() ,COMMON_SCHEDULE.FREQUENCY, DatePart.SECOND)))
                ).execute();
    }

}
