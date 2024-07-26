package com.svnlan.user.dao.Impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.home.vo.UserFavVo;
import com.svnlan.jooq.tables.pojos.UserFavModel;
import com.svnlan.jooq.tables.records.UserFavRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.utils.TenantUtil;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.SortOrder;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.tables.CommonLabel.COMMON_LABEL;
import static com.svnlan.jooq.tables.UserFav.USER_FAV;

@Service
public class UserFavDaoImpl implements UserFavDao {
    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(UserFavModel userFav) {
        LocalDateTime now = LocalDateTime.now();
        return context.insertInto(USER_FAV)
                .columns(USER_FAV.USER_ID, USER_FAV.TAG_ID, USER_FAV.NAME,
                        USER_FAV.PATH, USER_FAV.TYPE, USER_FAV.SORT,
                        USER_FAV.MODIFY_TIME, USER_FAV.CREATE_TIME, USER_FAV.TENANT_ID)
                .values(userFav.getUserId(), userFav.getTagId(), userFav.getName(),
                        userFav.getPath(), userFav.getType(), userFav.getSort(),
                        now, now, tenantUtil.getTenantIdByServerName())
                .execute();
    }

    @Override
    public int batchInsert(List<UserFavModel> list) {
        LocalDateTime now = LocalDateTime.now();
        InsertQuery<UserFavRecord> insertQuery = context.insertQuery(USER_FAV);
        Long tenantId = tenantUtil.getTenantIdByServerName();
        for (UserFavModel userFav : list) {
            insertQuery.newRecord();
            insertQuery.addValue(USER_FAV.USER_ID, userFav.getUserId());
            insertQuery.addValue(USER_FAV.TAG_ID, userFav.getTagId());
            insertQuery.addValue(USER_FAV.NAME, userFav.getName());
            insertQuery.addValue(USER_FAV.PATH, userFav.getPath());
            insertQuery.addValue(USER_FAV.TYPE, userFav.getType());
            insertQuery.addValue(USER_FAV.SORT, userFav.getSort());
            insertQuery.addValue(USER_FAV.MODIFY_TIME, now);
            insertQuery.addValue(USER_FAV.CREATE_TIME, now);
            insertQuery.addValue(USER_FAV.TENANT_ID, tenantId);
        }
        return insertQuery.execute();
    }

    @Override
    public Integer getFavMaxSort(Long userID) {
        return context.select(USER_FAV.SORT)
                .from(USER_FAV)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0))
                                .and(USER_FAV.TYPE.eq("source"))
                )
                .orderBy(USER_FAV.SORT.desc())
                .limit(1)
                .fetchOne(USER_FAV.SORT);
    }

    @Override
    public int removeUserFav(Long userID, List<Long> list) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.PATH.in(list))
                ).execute();
    }

    @Override
    public List<String> checkIsFavByUserId(Long userID) {
        return context.select(USER_FAV.PATH)
                .from(USER_FAV)
                .where(
                        USER_FAV.TAG_ID.eq(0)
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.USER_ID.eq(userID))
                )
                .fetch(USER_FAV.PATH);
    }

    @Override
    public int removeFileTag(Integer tagID, List<String> list) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.TAG_ID.eq(tagID)
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.PATH.in(list))
                                .and(USER_FAV.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .execute();
    }

    @Override
    public List<UserFavVo> getFileTagByUserId(Long userID) {
        return context.select(COMMON_LABEL.LABEL_NAME, COMMON_LABEL.LABEL_NAME,
                        COMMON_LABEL.STYLE,
                        DSL.groupConcat(USER_FAV.PATH).as("files")
                )
                .from(COMMON_LABEL)
                .leftJoin(USER_FAV)
                .on(COMMON_LABEL.ID.eq(USER_FAV.TAG_ID.cast(Long.class)))
                .where(
                        COMMON_LABEL.USER_ID.eq(userID)
                                .and(COMMON_LABEL.STATUS.eq(1))
                                .and(COMMON_LABEL.TAG_TYPE.eq(1))
                                .and(USER_FAV.TYPE.eq("info"))
                )
                .groupBy(COMMON_LABEL.ID)
                .orderBy(COMMON_LABEL.SORT.asc(), COMMON_LABEL.CREATE_TIME.asc())
                .fetchInto(UserFavVo.class);
    }

    @Override
    public List<UserFavVo> getFileTagBySourceID(Long userID, List<String> list) {
        return context.select(USER_FAV.USER_ID.as("userID"), USER_FAV.PATH, USER_FAV.TAG_ID.as("tagID"), COMMON_LABEL.LABEL_NAME, COMMON_LABEL.STYLE)
                .from(USER_FAV)
                .leftJoin(COMMON_LABEL).on(DSL.cast(USER_FAV.TAG_ID, Long.class).eq(COMMON_LABEL.ID))
                .where(USER_FAV.USER_ID.eq(userID).and(USER_FAV.TAG_ID.gt(0)).and(USER_FAV.TYPE.eq("source")).and(USER_FAV.PATH.in(list)))
                .fetchInto(UserFavVo.class);
    }

    @Override
    public int removeUserTag(Integer tagID, Long userID) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.TAG_ID.eq(tagID)
                                .and(USER_FAV.USER_ID.eq(userID))
                                .and(USER_FAV.TYPE.eq("source"))
                )
                .execute();
    }

    @Override
    public int removeInfoTag(Integer tagID) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.TAG_ID.eq(tagID)
                                .and(USER_FAV.USER_ID.eq(0L))
                                .and(USER_FAV.TYPE.eq("info"))
                                .and(USER_FAV.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .execute();
    }

    @Override
    public int removeInfoTagByID(String path) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.PATH.eq(path)
                                .and(USER_FAV.USER_ID.eq(0L))
                                .and(USER_FAV.TYPE.eq("info"))
                )
                .execute();
    }

    @Override
    public int addSortAll(Long userID) {
        return context.update(USER_FAV)
                .set(USER_FAV.SORT, USER_FAV.SORT.add(1))
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0))
                                .and(USER_FAV.TYPE.eq("source"))
                ).execute();
    }

    @Override
    public int subtractSortAll(Long userID) {
        return context.update(USER_FAV)
                .set(USER_FAV.SORT, USER_FAV.SORT.minus(1))
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.SORT.gt(0))
                ).execute();
    }

    @Override
    public int updateFavSort(Long userID, String path, int sort) {
        return context.update(USER_FAV).set(USER_FAV.SORT, sort)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.gt(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.PATH.eq(path))
                ).execute();
    }

    @Override
    public Integer getTagMaxSort(Long userID) {
        return context.select(USER_FAV.SORT).from(USER_FAV)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.gt(0))
                                .and(USER_FAV.TYPE.eq("source"))
                ).orderBy(USER_FAV.SORT.sort(SortOrder.DESC))
                .limit(1)
                .fetchOne(USER_FAV.SORT);
    }

    @Override
    public int addTagSortAll(Long userID) {
        return context.update(USER_FAV)
                .set(USER_FAV.SORT, USER_FAV.SORT.add(1))
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.gt(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.SORT.gt(0))
                ).returning().execute();
    }

    @Override
    public int subtractTagSortAll(Long userID) {
        return context.update(USER_FAV)
                .set(USER_FAV.SORT, USER_FAV.SORT.minus(1))
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.gt(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.SORT.gt(0))
                ).returning().execute();
    }

    @Override
    public int updateTagSort(Long userID, String path, int sort) {
        return context.update(USER_FAV).set(USER_FAV.SORT, sort)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.gt(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.PATH.eq(path))
                ).execute();
    }

    @Override
    public int removeUserFavByIdList(Long userID, List<Long> list) {
        return context.delete(USER_FAV)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0))
                                .and(USER_FAV.TYPE.eq("source"))
                                .and(USER_FAV.PATH.in(list))
                ).execute();
    }

    @Override
    public int updateFavName(Long id, String name) {
        return context.update(USER_FAV)
                .set(USER_FAV.NAME, name)
                .where(USER_FAV.ID.eq(id))
                .execute();
    }

    @Override
    public List<String> getFavNameList(Long userID) {
        return context.select(USER_FAV.NAME).from(USER_FAV)
                .where(
                        USER_FAV.USER_ID.eq(userID)
                                .and(USER_FAV.TAG_ID.eq(0)
                                        .and(USER_FAV.TYPE.eq("source")))
                ).fetch(USER_FAV.NAME);
    }

    @Override
    public List<CommonLabelVo> geTagListByInfoID(String path) {
        return context.select(USER_FAV.TAG_ID, COMMON_LABEL.LABEL_NAME, COMMON_LABEL.STYLE)
                .from(USER_FAV)
                .leftJoin(COMMON_LABEL)
                .on(COMMON_LABEL.ID.eq(USER_FAV.USER_ID))
                .where(
                        USER_FAV.USER_ID.eq(0L)
                                .and(USER_FAV.TYPE.eq("info"))
                                .and(USER_FAV.PATH.eq(path))
                                .and(USER_FAV.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).fetchInto(CommonLabelVo.class);
    }

    @Override
    public List<JSONObject> selectFavorSourceId(Long userId, String name) {
        Condition condition = USER_FAV.TAG_ID.eq(0).
                and(USER_FAV.TYPE.eq("source"))
                .and(USER_FAV.USER_ID.eq(userId));
        if (StringUtils.hasText(name)) {
            condition.and(USER_FAV.NAME.eq(name));
        }
        return context.select(USER_FAV.PATH.cast(Long.class).as("sourceID"), USER_FAV.NAME)
                .from(USER_FAV)
                .where(condition)
                .fetch(jsonObjectRecordMapper);
    }
}
