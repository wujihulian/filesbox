package com.svnlan.manage.dao.impl;

import com.svnlan.jooq.tables.records.CommonInfoTypeRecord;
import com.svnlan.manage.dao.CommonInfoTypeDao;
import com.svnlan.manage.domain.CommonInfoType;
import com.svnlan.manage.vo.CommonInfoTypeVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.svnlan.jooq.Tables.COMMON_INFO;
import static com.svnlan.jooq.Tables.COMMON_INFO_TYPE;

@Repository
public class CommonInfoTypeDaoImpl implements CommonInfoTypeDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(CommonInfoType commonInfoType) {
        LocalDateTime now = LocalDateTime.now();
        Integer id = context.insertInto(COMMON_INFO_TYPE)
                .set(COMMON_INFO_TYPE.TYPE_NAME, commonInfoType.getTypeName())
                .set(COMMON_INFO_TYPE.PARENT_ID, commonInfoType.getParentID())
                .set(COMMON_INFO_TYPE.PARENT_LEVEL, commonInfoType.getParentLevel())
                .set(COMMON_INFO_TYPE.STATUS, commonInfoType.getStatus())
                .set(COMMON_INFO_TYPE.SORT, commonInfoType.getSort())
                .set(COMMON_INFO_TYPE.NAME_PINYIN, commonInfoType.getNamePinyin())
                .set(COMMON_INFO_TYPE.NAME_PINYIN_SIMPLE, commonInfoType.getNamePinyinSimple())
                .set(COMMON_INFO_TYPE.CREATE_TIME, now)
                .set(COMMON_INFO_TYPE.MODIFY_TIME, now)
                .set(COMMON_INFO_TYPE.CREATE_USER, commonInfoType.getCreateUser())
                .set(COMMON_INFO_TYPE.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_INFO_TYPE.ID).fetchOne().getId();
        commonInfoType.setInfoTypeID(id);
        return 1;
    }

    @Override
    public int updateById(CommonInfoType commonInfoType) {
        UpdateQuery<CommonInfoTypeRecord> updateQuery = context.updateQuery(COMMON_INFO_TYPE);
        updateQuery.addValue(COMMON_INFO_TYPE.TYPE_NAME, commonInfoType.getTypeName());
        updateQuery.addValue(COMMON_INFO_TYPE.MODIFY_TIME, LocalDateTime.now());
        Optional.ofNullable(commonInfoType.getParentID()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.PARENT_ID, it));
        Optional.ofNullable(commonInfoType.getParentLevel()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.PARENT_LEVEL, it));
        Optional.ofNullable(commonInfoType.getStatus()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.STATUS, it));
        Optional.ofNullable(commonInfoType.getNamePinyin()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.NAME_PINYIN, it));
        Optional.ofNullable(commonInfoType.getNamePinyinSimple()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.NAME_PINYIN_SIMPLE, it));
        Optional.ofNullable(commonInfoType.getParentID()).ifPresent(it -> updateQuery.addValue(COMMON_INFO_TYPE.PARENT_ID, it));
        return updateQuery.execute();
    }

    @Override
    public CommonInfoType selectById(Integer infoTypeID) {
        return context.select(COMMON_INFO_TYPE.ID.as("infoTypeID"), COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.PARENT_ID.as("parentID"),
                        COMMON_INFO_TYPE.PARENT_LEVEL, COMMON_INFO_TYPE.STATUS, COMMON_INFO_TYPE.SORT, COMMON_INFO_TYPE.CREATE_TIME, COMMON_INFO_TYPE.MODIFY_TIME)
                .from(COMMON_INFO_TYPE)
                .where(COMMON_INFO_TYPE.ID.eq(infoTypeID))
                .fetchOneInto(CommonInfoType.class);
    }

    @Override
    public List<CommonInfoTypeVo> selectListByParam(Map<String, Object> map) {
        return context.select(COMMON_INFO_TYPE.ID.as("infoTypeID"), COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.PARENT_ID.as("parentID"),
                        COMMON_INFO_TYPE.PARENT_LEVEL, COMMON_INFO_TYPE.STATUS, COMMON_INFO_TYPE.SORT, COMMON_INFO_TYPE.NAME_PINYIN,
                        COMMON_INFO_TYPE.NAME_PINYIN_SIMPLE, COMMON_INFO_TYPE.CREATE_TIME, COMMON_INFO_TYPE.MODIFY_TIME)
                .from(COMMON_INFO_TYPE)
                .where(COMMON_INFO_TYPE.STATUS.eq((Integer) map.get("status")))
                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(CommonInfoTypeVo.class);
    }

    @Override
    public List<CommonInfoTypeVo> selectListAndCountByParam(Map<String, Object> map) {

        return context.select(COMMON_INFO_TYPE.ID.as("infoTypeID"), COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.PARENT_ID.as("parentID"), COMMON_INFO_TYPE.PARENT_LEVEL, COMMON_INFO_TYPE.STATUS,
                        COMMON_INFO_TYPE.SORT, COMMON_INFO_TYPE.NAME_PINYIN, COMMON_INFO_TYPE.NAME_PINYIN_SIMPLE, COMMON_INFO_TYPE.CREATE_TIME, COMMON_INFO_TYPE.MODIFY_TIME,
                        DSL.iif(DSL.field("ll.sum").isNull(), 0, DSL.field("ll.sum")).as("count"))
                .from(COMMON_INFO_TYPE)
                .leftJoin(context.select(DSL.count(COMMON_INFO.ID).as("sum"), COMMON_INFO.INFO_TYPE_ID.as("infoTypeId"))
                        .from(COMMON_INFO)
                        .where(COMMON_INFO.STATUS.in(0, 1))
                        .groupBy(COMMON_INFO.INFO_TYPE_ID)
                        .asTable("ll"))
                .on(DSL.field("ll.infoTypeId").eq(COMMON_INFO_TYPE.ID))
                .where(COMMON_INFO_TYPE.STATUS.eq(1))
                .and(Objects.nonNull(map.get("keyword")) ? COMMON_INFO_TYPE.TYPE_NAME.like("%" + map.get("keyword") + "%") : DSL.noCondition())
                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.CREATE_TIME.asc())
                .fetchInto(CommonInfoTypeVo.class);

    }

    @Override
    public int updateStatusById(CommonInfoType commonInfoType) {
        return context.update(COMMON_INFO_TYPE)
                .set(COMMON_INFO_TYPE.STATUS, commonInfoType.getStatus())
                .set(COMMON_INFO_TYPE.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_INFO_TYPE.ID.eq(commonInfoType.getInfoTypeID()))
                .execute();
    }

    @Override
    public List<CommonInfoType> copyInfoTypeListByLevel(String parentLevel) {
        return context.select(COMMON_INFO_TYPE.ID.as("infoTypeID"), COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.PARENT_ID.as("parentID"),
                        COMMON_INFO_TYPE.PARENT_LEVEL, COMMON_INFO_TYPE.STATUS, COMMON_INFO_TYPE.SORT)
                .from(COMMON_INFO_TYPE)
                .where(COMMON_INFO_TYPE.STATUS.eq(1))
                .and(COMMON_INFO_TYPE.PARENT_LEVEL.like(parentLevel + "%"))
                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_INFO_TYPE.CREATE_TIME.asc())
                .fetchInto(CommonInfoType.class);
    }

    @Override
    public int batchUpdateParent(List<CommonInfoType> list) {
        UpdateQuery<CommonInfoTypeRecord> updateQuery = context.updateQuery(COMMON_INFO_TYPE);
        for (CommonInfoType commonInfoType : list) {
            updateQuery.addValue(COMMON_INFO_TYPE.PARENT_LEVEL, commonInfoType.getParentLevel());
            updateQuery.addConditions(COMMON_INFO_TYPE.ID.eq(commonInfoType.getInfoTypeID()));
        }
        updateQuery.addConditions(COMMON_INFO_TYPE.ID.in(list));
        return updateQuery.execute();
    }

    @Override
    public List<Integer> checkChild(String parentLevel, Integer infoTypeID) {
        Long tenantId = tenantUtil.getTenantIdByServerName();
        return context.select(DSL.val(1))
                .from(COMMON_INFO_TYPE)
                .where(COMMON_INFO_TYPE.STATUS.eq(1))
                .and(COMMON_INFO_TYPE.PARENT_LEVEL.like(parentLevel + "%"))
                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantId))
                .limit(1)
                .union(
                        context.select(DSL.val(1))
                                .from(COMMON_INFO)
                                .where(COMMON_INFO.STATUS.in(0, 4, 1))
                                .and(COMMON_INFO.INFO_TYPE_ID.eq(infoTypeID))
                                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantId))
                ).fetchInto(Integer.class);
    }

    @Override
    public List<CommonInfoType> checkNameIsExist(String typeName) {
        return context.select(COMMON_INFO_TYPE.ID.as("infoTypeID"), COMMON_INFO_TYPE.TYPE_NAME, COMMON_INFO_TYPE.PARENT_ID.as("parentID"), COMMON_INFO_TYPE.PARENT_LEVEL,
                        COMMON_INFO_TYPE.STATUS, COMMON_INFO_TYPE.SORT)
                .from(COMMON_INFO_TYPE)
                .where(COMMON_INFO_TYPE.TYPE_NAME.eq(typeName))
                .and(COMMON_INFO_TYPE.STATUS.ne(0))
                .and(COMMON_INFO_TYPE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(CommonInfoType.class);
    }
}
