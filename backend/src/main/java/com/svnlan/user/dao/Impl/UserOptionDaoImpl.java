package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.pojos.SystemOptionModel;
import com.svnlan.jooq.tables.pojos.UserOptionModel;
import com.svnlan.jooq.tables.records.UserOptionRecord;
import com.svnlan.user.dao.UserOptionDao;
import com.svnlan.user.domain.UserOption;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserOptionVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.tables.UserOption.USER_OPTION;
@Service
public class UserOptionDaoImpl implements UserOptionDao {
    @Autowired
    private DSLContext context;
    @Override
    public List<OptionVo> getUserSystemConfig(Long userID) {
        return context.select(USER_OPTION.KEY_STRING,USER_OPTION.VALUE_TEXT).from(USER_OPTION)
                .where(USER_OPTION.TYPE.eq("").and(USER_OPTION.USER_ID.eq(userID))).fetchInto(OptionVo.class);
    }

    @Override
    public int batchInsert(List<UserOption> list) {
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<UserOptionRecord> insertQuery = context.insertQuery(USER_OPTION);
        for (UserOption userOption : list) {
            insertQuery.newRecord();
            insertQuery.addValue(USER_OPTION.USER_ID, userOption.getUserID());
            insertQuery.addValue(USER_OPTION.TYPE, userOption.getType());
            insertQuery.addValue(USER_OPTION.KEY_STRING, userOption.getKey());
            insertQuery.addValue(USER_OPTION.VALUE_TEXT, userOption.getValue());
            insertQuery.addValue(USER_OPTION.CREATE_TIME, now);
            insertQuery.addValue(USER_OPTION.MODIFY_TIME, now);
            insertQuery.addValue(USER_OPTION.TENANT_ID, userOption.getTenantId());
        }
        return insertQuery.execute();
    }

    @Override
    public int updateOptionValueByKey(Long userID, String key, String value, String type) {
        return  context.update(USER_OPTION)
                .set(USER_OPTION.VALUE_TEXT, value).set(USER_OPTION.MODIFY_TIME, LocalDateTime.now())
                .where(USER_OPTION.USER_ID.eq(userID).and(USER_OPTION.KEY_STRING.eq(key)).and(USER_OPTION.TYPE.eq(type))).execute();
    }

    @Override
    public int updateSystemOptionValueByKey(Long userID, String key, String value) {
        return  context.update(USER_OPTION)
                .set(USER_OPTION.VALUE_TEXT, value).set(USER_OPTION.MODIFY_TIME, LocalDateTime.now())
                .where(USER_OPTION.USER_ID.eq(userID).and(USER_OPTION.KEY_STRING.eq(key)).and(USER_OPTION.TYPE.eq(""))).execute();
    }

    @Override
    public OptionVo getUserConfigVoByKey(Long userID, String key) {
        return context.select(USER_OPTION.ID,USER_OPTION.VALUE_TEXT).from(USER_OPTION)
                .where(USER_OPTION.USER_ID.eq(userID).and(USER_OPTION.TYPE.eq("")).and(USER_OPTION.KEY_STRING.eq(key))).fetchOneInto(OptionVo.class);
    }

    @Override
    public String getUserOtherConfigByKey(Long userID, String type, String key) {
        return context.select(USER_OPTION.VALUE_TEXT).from(USER_OPTION)
                .where(USER_OPTION.USER_ID.eq(userID).and(USER_OPTION.TYPE.eq(type)).and(USER_OPTION.KEY_STRING.eq(key))).fetchOneInto(String.class);
    }

    @Override
    public List<UserOptionVo> getUserSortConfig(Long userID, List<String> list) {
        return context.select(USER_OPTION.ID,USER_OPTION.KEY_STRING,USER_OPTION.VALUE_TEXT).from(USER_OPTION)
                .where(USER_OPTION.USER_ID.eq(userID).and(USER_OPTION.KEY_STRING.in(list))).fetchInto(UserOptionVo.class);

    }

}
