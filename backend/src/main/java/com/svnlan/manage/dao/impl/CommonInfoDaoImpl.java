package com.svnlan.manage.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jooq.tables.records.CommonInfoRecord;
import com.svnlan.manage.dao.CommonInfoDao;
import com.svnlan.manage.domain.CommonInfo;
import com.svnlan.manage.domain.CommonSeo;
import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.svnlan.jooq.Tables.*;

@Repository
public class CommonInfoDaoImpl implements CommonInfoDao {
    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    public SelectSelectStep<Record> buildQueryColumns() {
        return context.select(COMMON_INFO.ID.as("infoID"), COMMON_INFO.TITLE, COMMON_INFO.COMPUTER_PIC_PATH, COMMON_INFO.MOBILE_PIC_PATH, COMMON_INFO.STATUS,
                COMMON_INFO.DETAIL, COMMON_INFO.FILE_DETAIL, COMMON_INFO.CREATE_TIME, COMMON_INFO.MODIFY_TIME, COMMON_INFO.CREATE_USER, COMMON_INFO.USER_IP,
                COMMON_INFO.INFO_TYPE_ID.as("infoTypeID"), COMMON_INFO.SORT, COMMON_INFO.INTRODUCE, COMMON_INFO.IS_TOP, COMMON_INFO.TOP_TIME, COMMON_INFO.SEO, COMMON_INFO.INFO_SOURCE,
                COMMON_INFO.IS_APPLY_ORIGINAL, COMMON_INFO.VIDEO_ID.as("videoID"), COMMON_INFO.THUMB, COMMON_INFO.PREVIEW_URL, COMMON_INFO.INFO_TYPE, COMMON_INFO.IS_VERTICAL, COMMON_INFO.THUMB_VERTICAL,
                COMMON_INFO.COMPUTER_PIC_PATH_VERTICAL, COMMON_INFO.IS_VIDEO_EXISTS, COMMON_INFO.GMT_PAGE, COMMON_INFO.USER_AGENT, COMMON_INFO.IS_URL_INFO,
                COMMON_INFO.INFO_URL, COMMON_INFO.ATTACHMENT_COUNT, COMMON_INFO.SHOW_ATTACHMENT, COMMON_INFO.REMARK, COMMON_INFO.IS_TRANSPORT, COMMON_INFO.RIGHT_FLAG,
                COMMON_INFO.SOURCE_ID.as("sourceID"), COMMON_INFO.IS_HIDE, COMMON_INFO.VIEW_COUNT, COMMON_INFO.LIKE_COUNT, COMMON_INFO.IS_LOGIN, COMMON_INFO.PATH_PRE);
    }

    @Override
    public int insert(CommonInfo commonInfo) {
        commonInfo.setThumb(ObjectUtils.isEmpty(commonInfo.getThumb()) ? "" : commonInfo.getThumb());
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMON_INFO)
                .columns(COMMON_INFO.TITLE, COMMON_INFO.COMPUTER_PIC_PATH, COMMON_INFO.MOBILE_PIC_PATH, COMMON_INFO.STATUS,
                        COMMON_INFO.DETAIL, COMMON_INFO.FILE_DETAIL, COMMON_INFO.CREATE_TIME, COMMON_INFO.MODIFY_TIME, COMMON_INFO.CREATE_USER,
                        COMMON_INFO.MODIFY_USER, COMMON_INFO.USER_IP,
                        COMMON_INFO.INFO_TYPE_ID, COMMON_INFO.SORT, COMMON_INFO.INTRODUCE, COMMON_INFO.IS_TOP, COMMON_INFO.TOP_TIME, COMMON_INFO.SEO, COMMON_INFO.INFO_SOURCE,
                        COMMON_INFO.IS_APPLY_ORIGINAL, COMMON_INFO.VIDEO_ID, COMMON_INFO.THUMB, COMMON_INFO.PREVIEW_URL, COMMON_INFO.INFO_TYPE, COMMON_INFO.IS_VERTICAL, COMMON_INFO.THUMB_VERTICAL,
                        COMMON_INFO.COMPUTER_PIC_PATH_VERTICAL, COMMON_INFO.IS_VIDEO_EXISTS, COMMON_INFO.GMT_PAGE, COMMON_INFO.USER_AGENT, COMMON_INFO.IS_URL_INFO,
                        COMMON_INFO.INFO_URL, COMMON_INFO.ATTACHMENT_COUNT, COMMON_INFO.SHOW_ATTACHMENT, COMMON_INFO.REMARK, COMMON_INFO.IS_TRANSPORT, COMMON_INFO.RIGHT_FLAG,
                        COMMON_INFO.SOURCE_ID, COMMON_INFO.IS_HIDE, COMMON_INFO.IS_LOGIN,
                        COMMON_INFO.ACTUAL_VIEW_COUNT,
                        COMMON_INFO.VIEW_COUNT, COMMON_INFO.NAME_PINYIN, COMMON_INFO.NAME_PINYIN_SIMPLE, COMMON_INFO.PATH_PRE, COMMON_INFO.TENANT_ID)
                .values(commonInfo.getTitle(), commonInfo.getComputerPicPath(), commonInfo.getMobilePicPath(), commonInfo.getStatus(), commonInfo.getDetail(), commonInfo.getFileDetail(),
                        now, now, commonInfo.getCreateUser(), commonInfo.getModifyUser(), commonInfo.getUserIp(), commonInfo.getInfoTypeID(), commonInfo.getSort(), commonInfo.getIntroduce(),
                        0, 0, commonInfo.getSeo(), commonInfo.getInfoSource(), commonInfo.getIsApplyOriginal(), commonInfo.getVideoID(), commonInfo.getThumb(), commonInfo.getPreviewUrl(),
                        commonInfo.getInfoType(), commonInfo.getIsVertical(), commonInfo.getThumbVertical(), commonInfo.getComputerPicPathVertical(), commonInfo.getIsVideoExists(),
                        commonInfo.getGmtPage(), commonInfo.getUserAgent(), commonInfo.getIsUrlInfo(), commonInfo.getInfoUrl(), commonInfo.getAttachmentCount(), commonInfo.getShowAttachment(), commonInfo.getRemark(),
                        commonInfo.getIsTransport(), commonInfo.getRightFlag(), commonInfo.getSourceID(), commonInfo.getIsHide(), commonInfo.getIsLogin(), 0, 0, commonInfo.getNamePinyin(),
                        commonInfo.getNamePinyinSimple(), commonInfo.getPathPre(), tenantUtil.getTenantIdByServerName())
                .returning(COMMON_INFO.ID).fetchOne().getId();
        commonInfo.setInfoID(id);
        return 1;
    }

    @Override
    public int deleteInfo(Long infoID, Long modifyUser) {
        return context.update(COMMON_INFO)
                .set(COMMON_INFO.STATUS, 2)
                .set(COMMON_INFO.MODIFY_USER, modifyUser)
                .set(COMMON_INFO.MODIFY_TIME, LocalDateTime.now())
                .where(COMMON_INFO.ID.eq(infoID))
                .execute();
    }

    @Override
    public CommonInfo getInfoById(Long infoID) {
        return buildQueryColumns()
                .from(COMMON_INFO)
                .where(COMMON_INFO.ID.eq(infoID))
                .fetchOneInto(CommonInfo.class);
    }

    @Override
    public CommonInfoVo getInfoVoById(Long infoID) {
        SelectSelectStep<Record> records = buildQueryColumns();
        return records.select(COMMON_INFO_TYPE.PARENT_LEVEL.as("typeParentLevel"), COMMON_INFO_TYPE.TYPE_NAME)
                .from(COMMON_INFO)
                .leftJoin(COMMON_INFO_TYPE).on(COMMON_INFO_TYPE.ID.eq(COMMON_INFO.INFO_TYPE_ID))
                .where(COMMON_INFO.ID.eq(infoID))
                .fetchOneInto(CommonInfoVo.class);
    }




    @Override
    public List<CommonInfoVo> getInfoVoListByParam(Map<String, Object> paramMap) {

        // Base_Column_List
        SelectJoinStep<Record> from = context.select(COMMON_INFO.ID.as("infoID"),COMMON_INFO.TITLE,COMMON_INFO.COMPUTER_PIC_PATH.as("computerPicPath")
                ,COMMON_INFO.MOBILE_PIC_PATH.as("mobilePicPath"),COMMON_INFO.STATUS,COMMON_INFO.DETAIL,COMMON_INFO.FILE_DETAIL.as("fileDetail")
                ,COMMON_INFO.CREATE_TIME.as("createTime"),COMMON_INFO.MODIFY_TIME.as("modifyTime"),COMMON_INFO.CREATE_USER.as("createUser"),COMMON_INFO.USER_IP.as("userIp")
                ,COMMON_INFO.INFO_TYPE_ID.as("infoTypeID"),COMMON_INFO.SORT,COMMON_INFO.INTRODUCE,COMMON_INFO.IS_TOP.as("isTop"),COMMON_INFO.TOP_TIME.as("topTime")
                ,COMMON_INFO.SEO,COMMON_INFO.INFO_SOURCE.as("infoSource"),COMMON_INFO.IS_APPLY_ORIGINAL.as("isApplyOriginal"),COMMON_INFO.VIDEO_ID.as("videoID")
                ,COMMON_INFO.THUMB,COMMON_INFO.PREVIEW_URL.as("previewUrl"),COMMON_INFO.INFO_TYPE.as("infoType"),COMMON_INFO.IS_VERTICAL.as("isVertical")
                ,COMMON_INFO.THUMB_VERTICAL.as("thumbVertical"),COMMON_INFO.COMPUTER_PIC_PATH_VERTICAL.as("computerPicPathVertical"),COMMON_INFO.IS_VIDEO_EXISTS.as("isVideoExists")
                ,COMMON_INFO.GMT_PAGE.as("gmtPage"),COMMON_INFO.USER_AGENT.as("userAgent"),COMMON_INFO.IS_URL_INFO.as("isUrlInfo"),COMMON_INFO.INFO_URL.as("infoUrl")
                ,COMMON_INFO.ATTACHMENT_COUNT.as("attachmentCount"),COMMON_INFO.REMARK,COMMON_INFO.IS_TRANSPORT.as("isTransport"),COMMON_INFO.RIGHT_FLAG.as("rightFlag")
                ,COMMON_INFO.SOURCE_ID.as("sourceID"),COMMON_INFO.IS_HIDE.as("isHide"), COMMON_INFO.VIEW_COUNT.as("viewCount"),COMMON_INFO.LIKE_COUNT.as("likeCount")
                ,COMMON_INFO.IS_LOGIN.as("isLogin"),COMMON_INFO.PATH_PRE.as("pathPre")
                ,USERS.NAME.as("userName"),USERS.NICKNAME,USERS.AVATAR
        )
                .from(COMMON_INFO);

        from.leftJoin(USERS).on(COMMON_INFO.CREATE_USER.eq(USERS.ID));
        String infoTypeLevel = (String) paramMap.get("infoTypeLevel");
        if (infoTypeLevel != null) {
            from.leftJoin(COMMON_INFO_TYPE).on(COMMON_INFO.INFO_TYPE_ID.eq(COMMON_INFO_TYPE.ID));
        }
        Integer tagID = (Integer) paramMap.get("tagID");
        if (tagID != null) {
            from.leftJoin(USER_FAV).on(DSL.cast(IO_FILE.ID, String.class).eq(USER_FAV.PATH).and(USER_FAV.TYPE.eq("info")));
        }
        SelectConditionStep<Record> where = from.where(buildWhereSearchParam(paramMap));
        where.orderBy(COMMON_INFO.TOP_TIME.desc(),COMMON_INFO.SORT.desc(),COMMON_INFO.CREATE_TIME.desc(),COMMON_INFO.ID.asc());
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        return from.fetchInto(CommonInfoVo.class);
    }


    @Override
    public int updateInfo(CommonInfo commonInfo) {
        UpdateQuery<CommonInfoRecord> updateQuery = context.updateQuery(COMMON_INFO);
        Optional.ofNullable(commonInfo.getTitle()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.TITLE, it));
        Optional.ofNullable(commonInfo.getComputerPicPath()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.COMPUTER_PIC_PATH, it));
        Optional.ofNullable(commonInfo.getDetail()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.DETAIL, it));
        Optional.ofNullable(commonInfo.getFileDetail()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.FILE_DETAIL, it));
        Optional.ofNullable(commonInfo.getInfoTypeID()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.INFO_TYPE_ID, it));
        Optional.ofNullable(commonInfo.getIntroduce()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.INTRODUCE, it));
        Optional.ofNullable(commonInfo.getSeo()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.SEO, it));
        Optional.ofNullable(commonInfo.getInfoSource()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.INFO_SOURCE, it));
        Optional.ofNullable(commonInfo.getIsApplyOriginal()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_APPLY_ORIGINAL, it));
        Optional.ofNullable(commonInfo.getVideoID()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.VIDEO_ID, it));
        Optional.ofNullable(commonInfo.getThumb()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.THUMB, it));
        Optional.ofNullable(commonInfo.getPreviewUrl()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.PREVIEW_URL, it));
        Optional.ofNullable(commonInfo.getInfoType()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.INFO_TYPE, it));
        Optional.ofNullable(commonInfo.getIsVertical()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_VERTICAL, it));
        Optional.ofNullable(commonInfo.getThumbVertical()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.THUMB_VERTICAL, it));
        Optional.ofNullable(commonInfo.getComputerPicPathVertical()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.COMPUTER_PIC_PATH_VERTICAL, it));
        Optional.ofNullable(commonInfo.getIsVideoExists()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_VIDEO_EXISTS, it));
        Optional.ofNullable(commonInfo.getIsUrlInfo()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_URL_INFO, it));
        Optional.ofNullable(commonInfo.getInfoUrl()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.INFO_URL, it));
        Optional.ofNullable(commonInfo.getAttachmentCount()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.ATTACHMENT_COUNT, it));
        Optional.ofNullable(commonInfo.getShowAttachment()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.SHOW_ATTACHMENT, it));
        Optional.ofNullable(commonInfo.getRemark()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.REMARK, it));
        Optional.ofNullable(commonInfo.getIsTransport()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_TRANSPORT, it));
        Optional.ofNullable(commonInfo.getRightFlag()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.RIGHT_FLAG, it));
        Optional.ofNullable(commonInfo.getSourceID()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.SOURCE_ID, it));
        Optional.ofNullable(commonInfo.getIsHide()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_HIDE, it));
        Optional.ofNullable(commonInfo.getIsLogin()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.IS_LOGIN, it));
        Optional.ofNullable(commonInfo.getViewCount()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.VIEW_COUNT, it));
        Optional.ofNullable(commonInfo.getNamePinyin()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.NAME_PINYIN, it));
        Optional.ofNullable(commonInfo.getNamePinyinSimple()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.NAME_PINYIN_SIMPLE, it));
        Optional.ofNullable(commonInfo.getPathPre()).ifPresent(it -> updateQuery.addValue(COMMON_INFO.PATH_PRE, it));
        updateQuery.addValue(COMMON_INFO.MODIFY_TIME, LocalDateTime.now());
        updateQuery.addConditions(COMMON_INFO.ID.eq(commonInfo.getInfoID()));
        return updateQuery.execute();
    }

    @Override
    public Integer getMaxSort(String infoType) {
        return context.select(DSL.max(COMMON_INFO.SORT))
                .from(COMMON_INFO)
                .where(COMMON_INFO.STATUS.eq(1))
                .and(COMMON_INFO.INFO_TYPE.eq(infoType))
                .and(COMMON_INFO.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_INFO.SORT.desc())
                .fetchOneInto(Integer.class);
    }

    @Override
    public List<CommonSeo> getSimpleInfoForSEO(List<Long> list) {
        return context.select(COMMON_INFO.ID, COMMON_INFO.TITLE, COMMON_INFO.INTRODUCE.as("keyword"), COMMON_INFO.INTRODUCE.as("description"),
                        DSL.val("information").as("typeStr"), COMMON_INFO.SEO.as("seoJson"), COMMON_INFO.DETAIL, COMMON_INFO.INFO_TYPE, COMMON_INFO.IS_URL_INFO)
                .from(COMMON_INFO)
                .where(COMMON_INFO.ID.in(list))
                .fetchInto(CommonSeo.class);
    }

    @Override
    public int updateInfoGmtPage(List<CommonSeo> infoList) {
        return context.update(COMMON_INFO)
                .set(COMMON_INFO.GMT_PAGE, LocalDateTime.now())
                .where(COMMON_INFO.ID.in(infoList))
                .execute();
    }

    @Override
    public void setTop(Map<String, Object> paramMap) {
        Integer isTop = (Integer) paramMap.get("isTop");
        UpdateSetMoreStep<CommonInfoRecord> setMoreStep = context.update(COMMON_INFO)
                .set(COMMON_INFO.IS_TOP, isTop);

        if (Objects.equals(isTop, 0)) {
            setMoreStep.set(COMMON_INFO.TOP_TIME, 0L);
        } else {
            setMoreStep.set(COMMON_INFO.TOP_TIME, System.currentTimeMillis());
        }
        setMoreStep.where(COMMON_INFO.ID.eq((Long) paramMap.get("infoID")))
                .execute();
    }

    @Override
    public List<CommonInfoVo> findHomePageListBySIdPId(String infoType) {
        return context.select(COMMON_INFO.ID.as("infoID"), COMMON_INFO.TITLE, COMMON_INFO.SORT)
                .from(COMMON_INFO)
                .where(COMMON_INFO.INFO_TYPE.eq(infoType))
                .and(COMMON_INFO.STATUS.ne(2))
                .and(COMMON_INFO.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_INFO.SORT, COMMON_INFO.MODIFY_TIME.desc())
                .fetchInto(CommonInfoVo.class);
    }

    @Override
    public int batchUpdateSort(List<CommonInfoVo> list) {
        for (CommonInfoVo commonInfoVo : list) {
            UpdateQuery<CommonInfoRecord> updateQuery = context.updateQuery(COMMON_INFO);
            updateQuery.addValue(COMMON_INFO.SORT, commonInfoVo.getSort());
            updateQuery.addConditions(COMMON_INFO.ID.eq(commonInfoVo.getInfoID()));
            updateQuery.execute();
        }
        return 1;
    }

    public Condition buildWhereSearchParam(Map<String, Object> map) {
        Condition condition = DSL.trueCondition().and(COMMON_INFO.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        Integer status = (Integer) map.get("status");
        String infoType = (String) map.get("infoType");
        Integer tagId = (Integer) map.get("tagID");
        Integer infoTypeId = (Integer) map.get("infoTypeID");
        String infoTypeLevel = (String) map.get("infoTypeLevel");
        String keyword = (String) map.get("keyword");
        if (Objects.nonNull(status)) {
            condition = condition.and(COMMON_INFO.STATUS.eq(status));
        } else {
            condition = condition.and(COMMON_INFO.STATUS.in(0, 1));
        }
        if (Objects.nonNull(infoType)) {
            condition = condition.and(COMMON_INFO.INFO_TYPE.eq(infoType));
        }
        if (Objects.nonNull(tagId)) {
            condition = condition.and(USER_FAV.TAG_ID.eq(tagId));
        }
        if (Objects.nonNull(infoTypeId) && Objects.nonNull(infoTypeLevel)) {
            condition = condition.and(COMMON_INFO.INFO_TYPE_ID.eq(infoTypeId).or(COMMON_INFO_TYPE.PARENT_LEVEL.like(infoTypeLevel + "%")));
        } else if (Objects.nonNull(infoTypeId)) {
            condition = condition.and(COMMON_INFO.INFO_TYPE_ID.eq(infoTypeId));
        } else if (Objects.nonNull(infoTypeLevel)) {
            condition = condition.and(COMMON_INFO_TYPE.PARENT_LEVEL.like(infoTypeLevel + "%"));
        }

        if (StringUtils.hasText(keyword)) {
            condition = condition.and(COMMON_INFO.TITLE.like("%" + keyword + "%"));
        }
        return condition;
    }

    @Override
    public List<CommonInfoVo> getInfoVoSortListByParam(Map<String, Object> map) {
        SelectJoinStep<Record2<Long, Integer>> selectJoinStep = context.select(COMMON_INFO.ID.as("infoID"), COMMON_INFO.SORT)
                .from(COMMON_INFO);
        String infoTypeLevel = (String) map.get("infoTypeLevel");
        Long tagId = (Long) map.get("tagID");
        if (StringUtils.hasText(infoTypeLevel)) {
            selectJoinStep.leftJoin(COMMON_INFO_TYPE).on(COMMON_INFO_TYPE.ID.eq(COMMON_INFO.INFO_TYPE_ID));
        }
        if (Objects.nonNull(tagId)) {
            selectJoinStep.leftJoin(USER_FAV).on(COMMON_INFO.ID.eq(USER_FAV.PATH.cast(Long.class))).and(USER_FAV.TYPE.eq("info"));
        }

        return selectJoinStep.where(buildWhereSearchParam(map))
                .orderBy(COMMON_INFO.TOP_TIME.desc(), COMMON_INFO.SORT.desc(), COMMON_INFO.CREATE_TIME.desc())
                .fetchInto(CommonInfoVo.class);
    }

    @Override
    public int updateViewCountById(Long id) {
        return context.update(COMMON_INFO)
                .set(COMMON_INFO.VIEW_COUNT, COMMON_INFO.VIEW_COUNT.add(1))
                .set(COMMON_INFO.ACTUAL_VIEW_COUNT, COMMON_INFO.ACTUAL_VIEW_COUNT.add(1))
                .where(COMMON_INFO.ID.eq(id))
                .execute();
    }

    @Override
    public int updateLikeCountById(Long id, Integer isLike) {
        return context.update(COMMON_INFO)
                .set(COMMON_INFO.LIKE_COUNT, Objects.equals(isLike, 1) ? COMMON_INFO.LIKE_COUNT.add(1) : COMMON_INFO.LIKE_COUNT.minus(1))
                .where(COMMON_INFO.ID.eq(id))
                .and(Objects.equals(isLike, 0) ? COMMON_INFO.LIKE_COUNT.gt(1) : DSL.noCondition())
                .execute();
    }

    @Override
    public Long getInfoVoListByParamTotal(Map<String, Object> paramMap) {
        // Base_Column_List
        SelectJoinStep<Record1<Integer>> from = context.selectCount().from(COMMON_INFO);

        String infoTypeLevel = (String) paramMap.get("infoTypeLevel");
        if (infoTypeLevel != null) {
            from.leftJoin(COMMON_INFO_TYPE).on(COMMON_INFO.INFO_TYPE_ID.eq(COMMON_INFO_TYPE.ID));
        }
        Integer tagID = (Integer) paramMap.get("tagID");
        if (tagID != null) {
            from.leftJoin(USER_FAV).on(DSL.cast(IO_FILE.ID, String.class).eq(USER_FAV.PATH).and(USER_FAV.TYPE.eq("info")));
        }

        return from.where(buildWhereSearchParam(paramMap))
                .orderBy(COMMON_INFO.TOP_TIME.desc(),COMMON_INFO.SORT.desc(),COMMON_INFO.CREATE_TIME.desc(),COMMON_INFO.ID.asc())
                .fetchOneInto(Long.class);


    }

    @Override
    public List<CommonInfoVo> selectByInfoIds(List<Long> ids){
        SelectOnConditionStep<Record18<Long, String, String, String, Integer, String, String, Long, Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime, String, String>> selectJoinStep =
                context.select(COMMON_INFO.ID.as("infoID"), COMMON_INFO.TITLE, COMMON_INFO.COMPUTER_PIC_PATH
                ,COMMON_INFO.MOBILE_PIC_PATH,COMMON_INFO.STATUS,COMMON_INFO.DETAIL,COMMON_INFO.FILE_DETAIL
                ,COMMON_INFO.CREATE_USER,COMMON_INFO.INFO_TYPE_ID,COMMON_INFO.VIEW_COUNT,COMMON_INFO.INTRODUCE
                        ,USERS.NICKNAME,USERS.NAME,USERS.AVATAR,COMMON_INFO.CREATE_TIME,COMMON_INFO.MODIFY_TIME,COMMON_INFO.THUMB
                        ,COMMON_INFO.INFO_URL.as("infoUrl")
                )
                .from(COMMON_INFO).leftJoin(USERS).on(COMMON_INFO.CREATE_USER.eq(USERS.ID));

        return selectJoinStep.where(COMMON_INFO.ID.in(ids))
                .fetchInto(CommonInfoVo.class);
    }
    @Override
    public List<CommonInfoVo> selectByParam(JSONObject paramMap){
        SelectOnConditionStep<Record18<Long, String, String, String, Integer, String, String, Long, Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime, String, String>>
                selectJoinStep =
                (SelectOnConditionStep<Record18<Long, String, String, String, Integer, String, String, Long, Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime, String, String>>) context.select(COMMON_INFO.ID.as("infoID"), COMMON_INFO.TITLE, COMMON_INFO.COMPUTER_PIC_PATH
                        ,COMMON_INFO.MOBILE_PIC_PATH,COMMON_INFO.STATUS,COMMON_INFO.DETAIL,COMMON_INFO.FILE_DETAIL
                        ,COMMON_INFO.CREATE_USER,COMMON_INFO.INFO_TYPE_ID,COMMON_INFO.VIEW_COUNT,COMMON_INFO.INTRODUCE
                        ,USERS.NICKNAME,USERS.NAME,USERS.AVATAR,COMMON_INFO.CREATE_TIME,COMMON_INFO.MODIFY_TIME,COMMON_INFO.THUMB
                        ,COMMON_INFO.INFO_URL.as("infoUrl")
                )
                        .from(COMMON_INFO);

        selectJoinStep.leftJoin(USERS).on(COMMON_INFO.CREATE_USER.eq(USERS.ID));
        String sequence = paramMap.getString("sequence");
        if (!ObjectUtils.isEmpty(sequence)) {
            selectJoinStep.leftJoin(COMMON_INFO_TYPE).on(COMMON_INFO_TYPE.ID.eq(COMMON_INFO.INFO_TYPE_ID));
        }
        SelectConditionStep<Record18<Long, String, String, String, Integer, String, String, Long, Integer, Integer, String, String, String, String, LocalDateTime, LocalDateTime, String, String>>
                where = selectJoinStep.where(buildWhereSelectByParam(paramMap));
        where.orderBy(COMMON_INFO.SORT.asc(),COMMON_INFO.CREATE_TIME.desc(),COMMON_INFO.ID.asc());

        Integer beginNum = paramMap.getInteger("beginNum");
        Integer i = paramMap.getInteger("i");
        if (!ObjectUtils.isEmpty(beginNum) && beginNum > 0) {
            selectJoinStep.limit(beginNum,i);
        }else {
            selectJoinStep.limit(10);
        }
        return selectJoinStep.fetchInto(CommonInfoVo.class);
    }

    public Condition buildWhereSelectByParam(JSONObject map) {
        Long tenantId = map.containsKey("tenantId") && !ObjectUtils.isEmpty(map.get("tenantId"))
                ? map.getLong("tenantId"): tenantUtil.getTenantIdByServerName();
        Condition condition = DSL.trueCondition().and(COMMON_INFO.TENANT_ID.eq(tenantId).and(COMMON_INFO.STATUS.eq(1)));
        String infoType = map.getString("infoType");
        Integer infoTypeId = map.getInteger("infoTypeId");
        String sequence = map.getString("sequence");
        String keyword = map.getString("keyword");

        if (!ObjectUtils.isEmpty(sequence)){
            condition = condition.and(COMMON_INFO_TYPE.PARENT_LEVEL.like(sequence + "%"));
        }else if (!ObjectUtils.isEmpty(infoTypeId)){
            condition = condition.and(COMMON_INFO.INFO_TYPE_ID.eq(infoTypeId));
        }
        if(!ObjectUtils.isEmpty(infoType)){
            condition = condition.and(COMMON_INFO.INFO_TYPE.eq(infoType));
        }
        if (StringUtils.hasText(keyword)) {
            condition = condition.and(COMMON_INFO.TITLE.like("%" + keyword + "%"));
        }
        return condition;
    }
}
