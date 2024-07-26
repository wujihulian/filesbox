package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.CommonLabelDao;
import com.svnlan.home.domain.CommonLabel;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jooq.tables.pojos.CommonLabelModel;
import com.svnlan.jooq.tables.records.CommonLabelRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.tables.CommonLabel.COMMON_LABEL;

@Service
public class CommonLabelDaoImpl implements CommonLabelDao {
    @Autowired
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(CommonLabel commonLabel) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMON_LABEL)
                .columns(COMMON_LABEL.USER_ID, COMMON_LABEL.LABEL_NAME, COMMON_LABEL.LABEL_EN_NAME, COMMON_LABEL.EN_NAME_SIMPLE, COMMON_LABEL.STATUS,
                        COMMON_LABEL.CREATE_TIME, COMMON_LABEL.MODIFY_TIME, COMMON_LABEL.STYLE, COMMON_LABEL.SORT, COMMON_LABEL.TAG_TYPE,
                        COMMON_LABEL.TENANT_ID)
                .values(commonLabel.getUserID(),commonLabel.getLabelName(),commonLabel.getLabelEnName(),commonLabel.getEnNameSimple(),1,now,now,commonLabel.getStyle()
                        ,commonLabel.getSort(),1, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_LABEL.ID).fetchOne().getId();
        commonLabel.setLabelId(id);
        return 1;
    }

    @Override
    public int update(CommonLabel commonLabel) {
        return context.update(COMMON_LABEL)
                .set(COMMON_LABEL.LABEL_NAME, commonLabel.getLabelName())
                .set(COMMON_LABEL.LABEL_EN_NAME, commonLabel.getLabelEnName())
                .set(COMMON_LABEL.EN_NAME_SIMPLE, commonLabel.getEnNameSimple())
                .set(COMMON_LABEL.STYLE, commonLabel.getStyle())
                .set(COMMON_LABEL.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_LABEL.ID.eq(commonLabel.getLabelId())).execute();
    }

    @Override
    public int deleteTag(Long labelId) {
        return context.delete(COMMON_LABEL).where(COMMON_LABEL.ID.eq(labelId)).execute();
    }

    @Override
    public List<CommonLabelVo> getUserLabelList(Long userID) {
        return context.select(COMMON_LABEL.ID.as("labelId"), COMMON_LABEL.USER_ID.as("userID"), COMMON_LABEL.LABEL_NAME, COMMON_LABEL.LABEL_EN_NAME, COMMON_LABEL.EN_NAME_SIMPLE,
                        COMMON_LABEL.CREATE_TIME, COMMON_LABEL.MODIFY_TIME, COMMON_LABEL.STYLE, COMMON_LABEL.SORT)
                .from(COMMON_LABEL)
                .where(
                        COMMON_LABEL.USER_ID.eq(userID)
                                .and(COMMON_LABEL.STATUS.eq(1))
                                .and(COMMON_LABEL.TAG_TYPE.eq(1))
                )
                .orderBy(COMMON_LABEL.SORT.desc(), COMMON_LABEL.CREATE_TIME.asc())
                .fetchInto(CommonLabelVo.class);
    }

    @Override
    public List<CommonLabelVo> getInfoLabelList(Long userID) {
        return context.select(COMMON_LABEL.ID.as("labelId"), COMMON_LABEL.USER_ID.as("userID"), COMMON_LABEL.LABEL_NAME, COMMON_LABEL.LABEL_EN_NAME, COMMON_LABEL.EN_NAME_SIMPLE, COMMON_LABEL.CREATE_TIME, COMMON_LABEL.MODIFY_TIME, COMMON_LABEL.STYLE, COMMON_LABEL.SORT)
                .from(COMMON_LABEL)
                .where(
                        COMMON_LABEL.USER_ID.eq(userID)
                                .and(COMMON_LABEL.STATUS.eq(1))
                                .and(COMMON_LABEL.TAG_TYPE.eq(2))
                )
                .orderBy(COMMON_LABEL.SORT.desc(), COMMON_LABEL.CREATE_TIME.asc())
                .fetchInto(CommonLabelVo.class);

    }

    @Override
    public Integer getMaxSort(Long userID, Integer tagType) {
        return context.select(COMMON_LABEL.SORT).from(COMMON_LABEL)
                .where(
                        COMMON_LABEL.USER_ID.eq(userID)
                                .and(COMMON_LABEL.STATUS.eq(1))
                                .and(COMMON_LABEL.TAG_TYPE.eq(tagType))
                )
                .orderBy(COMMON_LABEL.SORT.desc(), COMMON_LABEL.CREATE_TIME.desc())
                .limit(1)
                .fetchOneInto(Integer.class);
    }

    @Override
    public int updateSort(Long labelId, Integer sort) {
        return context.update(COMMON_LABEL)
                .set(COMMON_LABEL.SORT, sort)
                .set(COMMON_LABEL.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_LABEL.ID.eq(labelId)).execute();
    }

    @Override
    public List<Long> checkLabelNameRepeat(String labelName, Long userID, Integer tagType) {
        return context.select(COMMON_LABEL.ID)
                .from(COMMON_LABEL)
                .where(
                        COMMON_LABEL.LABEL_NAME.eq(labelName)
                                .and(COMMON_LABEL.USER_ID.eq(userID))
                                .and(COMMON_LABEL.TAG_TYPE.eq(tagType))
                                .and(COMMON_LABEL.STATUS.eq(1))
                )
                .fetchInto(Long.class);
    }
}
