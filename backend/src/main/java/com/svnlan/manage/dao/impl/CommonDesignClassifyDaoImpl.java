package com.svnlan.manage.dao.impl;

import com.svnlan.jooq.tables.records.CommonDesignClassifyRecord;
import com.svnlan.manage.dao.CommonDesignClassifyDao;
import com.svnlan.manage.domain.CommonDesignClassify;
import com.svnlan.manage.vo.CommonDesignClassifyVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.svnlan.jooq.Tables.COMMON_DESIGN;
import static com.svnlan.jooq.Tables.COMMON_DESIGN_CLASSIFY;

@Repository
public class CommonDesignClassifyDaoImpl implements CommonDesignClassifyDao {

    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(CommonDesignClassify commonDesignClassify) {
        LocalDateTime now = LocalDateTime.now();
        Integer id = context.insertInto(COMMON_DESIGN_CLASSIFY)
                .set(COMMON_DESIGN_CLASSIFY.TYPE_NAME, commonDesignClassify.getTypeName())
                .set(COMMON_DESIGN_CLASSIFY.PARENT_ID, commonDesignClassify.getParentID())
                .set(COMMON_DESIGN_CLASSIFY.STATUS, commonDesignClassify.getStatus())
                .set(COMMON_DESIGN_CLASSIFY.SORT, commonDesignClassify.getSort())
                .set(COMMON_DESIGN_CLASSIFY.NAME_PINYIN, commonDesignClassify.getNamePinyin())
                .set(COMMON_DESIGN_CLASSIFY.NAME_PINYIN_SIMPLE, commonDesignClassify.getNamePinyinSimple())
                .set(COMMON_DESIGN_CLASSIFY.CREATE_TIME, now)
                .set(COMMON_DESIGN_CLASSIFY.MODIFY_TIME, now)
                .set(COMMON_DESIGN_CLASSIFY.CREATE_USER, commonDesignClassify.getCreateUser())
                .set(COMMON_DESIGN_CLASSIFY.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_DESIGN_CLASSIFY.ID).fetchOne().getId();
        commonDesignClassify.setDesignClassifyID(id);
        return 1;
    }

    @Override
    public int updateById(CommonDesignClassify commonDesignClassify) {
        UpdateSetFirstStep<CommonDesignClassifyRecord> updateSetFirstStep = context.update(COMMON_DESIGN_CLASSIFY);
        updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.TYPE_NAME, commonDesignClassify.getTypeName());
        Optional.ofNullable(commonDesignClassify.getParentID()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.PARENT_ID, it));
        Optional.ofNullable(commonDesignClassify.getParentLevel()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.PARENT_LEVEL, it));
        Optional.ofNullable(commonDesignClassify.getStatus()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.STATUS, it));
        Optional.ofNullable(commonDesignClassify.getNamePinyin()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.NAME_PINYIN, it));
        Optional.ofNullable(commonDesignClassify.getNamePinyinSimple()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.NAME_PINYIN_SIMPLE, it));
        return updateSetFirstStep.set(COMMON_DESIGN_CLASSIFY.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_DESIGN_CLASSIFY.ID.eq(commonDesignClassify.getDesignClassifyID()))
                .execute();

    }

    @Override
    public CommonDesignClassify selectById(Integer infoTypeID) {
        return context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME.as("typeName"), COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID")
                , COMMON_DESIGN_CLASSIFY.PARENT_LEVEL,COMMON_DESIGN_CLASSIFY.STATUS, COMMON_DESIGN_CLASSIFY.SORT, COMMON_DESIGN_CLASSIFY.CREATE_TIME
                , COMMON_DESIGN_CLASSIFY.MODIFY_TIME)
                .from(COMMON_DESIGN_CLASSIFY)
                .where(COMMON_DESIGN_CLASSIFY.ID.eq(infoTypeID))
                .fetchOneInto(CommonDesignClassify.class);
    }

    @Override
    public List<CommonDesignClassifyVo> selectListByParam(Map<String, Object> map) {
        Condition condition = DSL.trueCondition().and(COMMON_DESIGN_CLASSIFY.STATUS.eq(1))
                .and(COMMON_DESIGN_CLASSIFY.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        String keyword = (String) map.get("keyword");
        if (!ObjectUtils.isEmpty(keyword)){
            condition = condition.and(COMMON_DESIGN_CLASSIFY.TYPE_NAME.like("%" + keyword + "%"));
        }
        return context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME, COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID")
                , COMMON_DESIGN_CLASSIFY.PARENT_LEVEL,COMMON_DESIGN_CLASSIFY.STATUS, COMMON_DESIGN_CLASSIFY.SORT, COMMON_DESIGN_CLASSIFY.CREATE_TIME
                , COMMON_DESIGN_CLASSIFY.NAME_PINYIN,COMMON_DESIGN_CLASSIFY.NAME_PINYIN_SIMPLE, COMMON_DESIGN_CLASSIFY.MODIFY_TIME)
                .from(COMMON_DESIGN_CLASSIFY)
                .where(condition)
                .orderBy(COMMON_DESIGN_CLASSIFY.SORT, COMMON_DESIGN_CLASSIFY.CREATE_TIME)
                .fetchInto(CommonDesignClassifyVo.class);
    }

    @Override
    public List<CommonDesignClassifyVo> selectListAndCountByParam(Map<String, Object> paramMap) {

        // TODO 需注意测试
        SelectJoinStep
                from = context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME.as("typeName")
                , COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID"), COMMON_DESIGN_CLASSIFY.PARENT_LEVEL.as("parentLevel"), COMMON_DESIGN_CLASSIFY.STATUS, COMMON_DESIGN_CLASSIFY.SORT
                , COMMON_DESIGN_CLASSIFY.NAME_PINYIN.as("namePinyin"), COMMON_DESIGN_CLASSIFY.NAME_PINYIN_SIMPLE.as("namePinyinSimple"), COMMON_DESIGN_CLASSIFY.CREATE_TIME.as("createTime")
                , COMMON_DESIGN_CLASSIFY.MODIFY_TIME.as("modifyTime"), DSL.isnull(DSL.field("ll.sum"), 0).as("count")
        ).from(COMMON_DESIGN_CLASSIFY);

        from.leftJoin(
                context.select(COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"), DSL.count(COMMON_DESIGN.ID).as("sum"))
                        .from(COMMON_DESIGN).where(COMMON_DESIGN.STATE.eq("1")).groupBy(COMMON_DESIGN.DESIGN_CLASSIFY_ID).asTable("ll")
        ).on(DSL.field("ll.designClassifyID").eq(COMMON_DESIGN_CLASSIFY.ID));
        String keyword = (String) paramMap.get("keyword");

        SelectConditionStep<Record10<Integer, String, Integer, String, Integer, Integer, String, String, LocalDateTime, LocalDateTime>>
                where = from.where(COMMON_DESIGN_CLASSIFY.STATUS.eq(1));

        if (keyword != null) {
            where.and(COMMON_DESIGN_CLASSIFY.TYPE_NAME.like("%") + keyword + "%");
        }
        where.orderBy(COMMON_DESIGN_CLASSIFY.SORT.asc(), COMMON_DESIGN_CLASSIFY.CREATE_TIME.asc(), COMMON_DESIGN_CLASSIFY.ID.asc());

        return from.fetchInto(CommonDesignClassifyVo.class);

    }

    @Override
    public int updateStatusById(CommonDesignClassify commonDesignClassify) {
        return context.update(COMMON_DESIGN_CLASSIFY)
                .set(COMMON_DESIGN_CLASSIFY.STATUS, commonDesignClassify.getStatus())
                .set(COMMON_DESIGN_CLASSIFY.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_DESIGN_CLASSIFY.ID.eq(commonDesignClassify.getDesignClassifyID()))
                .execute();
    }

    @Override
    public List<CommonDesignClassify> copyInfoTypeListByLevel(String parentLevel) {
        return context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME, COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID")
                , COMMON_DESIGN_CLASSIFY.PARENT_LEVEL,
                        COMMON_DESIGN_CLASSIFY.STATUS, COMMON_DESIGN_CLASSIFY.SORT)
                .from(COMMON_DESIGN_CLASSIFY)
                .where(COMMON_DESIGN_CLASSIFY.STATUS.eq(1))
                .and(COMMON_DESIGN_CLASSIFY.PARENT_LEVEL.like(parentLevel + "%"))
                .and(COMMON_DESIGN_CLASSIFY.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_DESIGN_CLASSIFY.CREATE_TIME)
                .fetchInto(CommonDesignClassify.class);
    }

    @Override
    public int batchUpdateParent(List<CommonDesignClassify> list) {
        UpdateQuery<CommonDesignClassifyRecord> updateQuery = context.updateQuery(COMMON_DESIGN_CLASSIFY);
        for (CommonDesignClassify dto : list) {
            updateQuery.addValue(COMMON_DESIGN_CLASSIFY.PARENT_LEVEL, DSL.when(COMMON_DESIGN_CLASSIFY.ID.eq(dto.getDesignClassifyID()), dto.getParentLevel()));
//            updateQuery.addValue(COMMON_DESIGN_CLASSIFY.PARENT_LEVEL, dto.getParentLevel());
//            updateQuery.addConditions(COMMON_DESIGN_CLASSIFY.ID.eq(dto.getDesignClassifyID()));
        }
        updateQuery.addConditions(COMMON_DESIGN_CLASSIFY.ID.in(list));
        return updateQuery.execute();
    }

    @Override
    public List<Integer> checkChild(String parentLevel, Integer designClassifyID) {
        return context.select(DSL.val(1))
                .from(COMMON_DESIGN_CLASSIFY)
                .where(COMMON_DESIGN_CLASSIFY.STATUS.eq(1))
                .and(COMMON_DESIGN_CLASSIFY.PARENT_LEVEL.like(parentLevel + "%"))
                .union(
                        context.select(DSL.val(1))
                                .from(COMMON_DESIGN)
                                .where(COMMON_DESIGN.STATE.in("0", "4", "1"))
                                .and(COMMON_DESIGN.DESIGN_CLASSIFY_ID.eq(designClassifyID))
                ).fetchInto(Integer.class);
    }

    @Override
    public List<CommonDesignClassify> checkNameIsExist(String typeName) {
        return context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME, COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID")
                , COMMON_DESIGN_CLASSIFY.PARENT_LEVEL,
                        COMMON_DESIGN_CLASSIFY.STATUS, COMMON_DESIGN_CLASSIFY.SORT)
                .from(COMMON_DESIGN_CLASSIFY)
                .where(COMMON_DESIGN_CLASSIFY.TYPE_NAME.eq(typeName))
                .and(COMMON_DESIGN_CLASSIFY.STATUS.ne(0))
                .and(COMMON_DESIGN_CLASSIFY.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(CommonDesignClassify.class);
    }
}
