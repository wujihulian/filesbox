package com.svnlan.manage.dao.impl;

import com.svnlan.jooq.tables.records.UserCommonInfoRecord;
import com.svnlan.manage.dao.UserCommonInfoDao;
import com.svnlan.manage.domain.UserCommonInfo;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.svnlan.jooq.Tables.USER_COMMON_INFO;

@Repository
public class UserCommonInfoDaoImpl implements UserCommonInfoDao {
    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int updateViewCount(Long id, String ip, Long userId) {

        return context.update(USER_COMMON_INFO)
                .set(USER_COMMON_INFO.VIEW_COUNT, USER_COMMON_INFO.VIEW_COUNT.add(1))
                .where(USER_COMMON_INFO.INFO_ID.eq(id))
                .and(Objects.nonNull(userId) ? USER_COMMON_INFO.USER_ID.eq(userId) : USER_COMMON_INFO.IP.eq(ip))
                .execute();
    }

    @Override
    public void updateLikeById(Long id, Integer isLike) {
        context.update(USER_COMMON_INFO)
                .set(USER_COMMON_INFO.IS_LIKE, isLike)
                .where(USER_COMMON_INFO.ID.eq(id))
                .execute();
    }

    @Override
    public int insert(UserCommonInfo userCommonInfoInsert) {
        InsertSetStep<UserCommonInfoRecord> insertSetStep = context.insertInto(USER_COMMON_INFO);

        if (Objects.nonNull(userCommonInfoInsert.getIp())) {
            insertSetStep.set(USER_COMMON_INFO.IP, userCommonInfoInsert.getIp());
        } else if (Objects.nonNull(userCommonInfoInsert.getUserId())) {
            insertSetStep.set(USER_COMMON_INFO.USER_ID, userCommonInfoInsert.getUserId());
        }
        LocalDateTime now = LocalDateTime.now();
        return insertSetStep.set(USER_COMMON_INFO.INFO_ID, userCommonInfoInsert.getInfoId())
                .set(USER_COMMON_INFO.VIEW_COUNT, userCommonInfoInsert.getViewCount())
                .set(USER_COMMON_INFO.CREATE_TIME, now)
                .set(USER_COMMON_INFO.MODIFY_TIME, now)
                .set(USER_COMMON_INFO.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .execute();
    }
}
