package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.DingAboutDao;
import com.svnlan.user.vo.UserVo;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.svnlan.jooq.Tables.IO_FILE;
import static com.svnlan.jooq.Tables.USERS;
import static com.svnlan.jooq.tables.Role.ROLE;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/20 13:30
 */
@Service
public class DingAboutDaoImpl implements DingAboutDao {
    @Autowired
    private DSLContext context;

    @Override
    public List<UserVo> getUserInfoByUnionId(String unionId) {
        return context.select(USERS.ID.as("userID"), USERS.NAME, USERS.NICKNAME, USERS.LAST_LOGIN.as("lastLogin"), USERS.PHONE,
                USERS.EMAIL, USERS.STATUS, USERS.SEX, USERS.AVATAR, USERS.ROLE_ID.as("roleID"), USERS.DING_OPEN_ID.as("dingOpenId"),
                USERS.WECHAT_OPEN_ID.as("wechatOpenId"), USERS.ALIPAY_OPEN_ID.as("alipayOpenId"), USERS.EN_WECHAT_OPEN_ID.as("enWechatOpenId"), USERS.PASSWORD, USERS.SIZE_MAX.as("sizeMax")
                , USERS.SIZE_USE.as("sizeUse"),
                ROLE.ROLE_NAME.as("roleName"), ROLE.CODE, ROLE.DESCRIPTION, ROLE.LABEL, ROLE.AUTH, USERS.IS_SYSTEM.as("isSystem"), ROLE.ADMINISTRATOR,
                ROLE.IGNORE_FILE_SIZE.as("ignoreFileSize")).from(USERS)
                .leftJoin(ROLE).on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)))
                .where(USERS.DING_UNION_ID.eq(unionId)).fetchInto(UserVo.class);
    }

    @Override
    public Map<Long, List<UserVo>> getDingUsers(Long tenantId){
        Condition condition = DSL.and(USERS.DING_UNION_ID.isNotNull())
                .and(USERS.DING_UNION_ID.notEqual(""))
                .and(USERS.STATUS.eq(1));

        if (Objects.nonNull(tenantId)) {
            condition = condition.and(USERS.TENANT_ID.eq(tenantId));
        }
        Map<Long, List<UserVo>> map = context.select(USERS.TENANT_ID, USERS.DING_UNION_ID).from(USERS)
                .where(condition)
                .fetchGroups(USERS.TENANT_ID,UserVo.class);
        return map;
    }
}
