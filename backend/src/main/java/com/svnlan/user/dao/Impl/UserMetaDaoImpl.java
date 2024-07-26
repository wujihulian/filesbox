package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.records.UserMetaRecord;
import com.svnlan.user.dao.UserMetaDao;
import com.svnlan.user.domain.UserMeta;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.Tables.USER_META;

@Repository
public class UserMetaDaoImpl implements UserMetaDao {
    @Resource
    private DSLContext context;
    @Override
    public int delMetaByUserID(Long userID, List<String> list) {
        return context.deleteFrom(USER_META)
                .where(USER_META.USER_ID.eq(userID))
                .and(USER_META.KEY_STRING.in(list))
                .execute();
    }

    @Override
    public int batchInsert(List<UserMeta> list) {
        InsertQuery<UserMetaRecord> insertQuery = context.insertQuery(USER_META);
        LocalDateTime now = LocalDateTime.now();
        for (UserMeta item : list) {
            insertQuery.newRecord();
            insertQuery.addValue(USER_META.USER_ID, item.getUserID());
            insertQuery.addValue(USER_META.KEY_STRING, item.getKey());
            insertQuery.addValue(USER_META.VALUE_TEXT, item.getValue());
            insertQuery.addValue(USER_META.CREATE_TIME, now);
            insertQuery.addValue(USER_META.MODIFY_TIME, now);
        }
        return insertQuery.execute();
    }
}
