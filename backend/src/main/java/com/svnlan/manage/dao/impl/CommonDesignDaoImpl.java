package com.svnlan.manage.dao.impl;

import com.svnlan.jooq.tables.records.CommonDesignRecord;
import com.svnlan.manage.dao.CommonDesignDao;
import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.domain.CommonDesignList;
import com.svnlan.manage.vo.DesignDetailVO;
import com.svnlan.manage.vo.DesignListVO;
import com.svnlan.utils.*;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.svnlan.jooq.Tables.*;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;

@Repository
public class CommonDesignDaoImpl implements CommonDesignDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(CommonDesign record) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime approvalTime = now;
        if (Objects.nonNull(record.getGmtApproval())) {
            approvalTime = DateUtil.getLocalDateTimeFromMilli(record.getGmtApproval().getTime());
        }
        LogUtil.info("insert 装扮 record=" + JsonUtils.beanToJson(record));
        Long id = context.insertInto(COMMON_DESIGN)
                .set(COMMON_DESIGN.TITLE, record.getTitle())
                .set(COMMON_DESIGN.OFFSET, record.getOffset())
                .set(COMMON_DESIGN.SIZE, record.getSize())
                .set(COMMON_DESIGN.PIC, record.getPic())
                .set(COMMON_DESIGN.GMT_CREATE, now)
                .set(COMMON_DESIGN.GMT_MODIFIED, now)
                .set(COMMON_DESIGN.DETAIL, record.getDetail())
                .set(COMMON_DESIGN.DESIGN_TYPE, record.getDesignType())
                .set(COMMON_DESIGN.CLIENT_TYPE, record.getClientType())
                .set(COMMON_DESIGN.IS_USED, record.getIsUsed())

                .set(COMMON_DESIGN.URL, record.getUrl())
                .set(COMMON_DESIGN.FILE_URL, record.getFileUrl())
                .set(COMMON_DESIGN.FK_COMMON_DESIGN_ID, record.getFkCommonDesignId())
                .set(COMMON_DESIGN.FOOT, record.getFoot())
                .set(COMMON_DESIGN.HEAD, record.getHead())

                .set(COMMON_DESIGN.SETTING, record.getSetting())
                .set(COMMON_DESIGN.APPLET, record.getApplet())
                .set(COMMON_DESIGN.SOURCE_DESIGN_ID, record.getSourceDesignId())
                .set(COMMON_DESIGN.APPROVAL_STATE, record.getApprovalState())
                .set(COMMON_DESIGN.APPROVAL_DETAIL, record.getApprovalDetail())
                .set(COMMON_DESIGN.GMT_APPROVAL, approvalTime)
                .set(COMMON_DESIGN.SORT, record.getSort())
                .set(COMMON_DESIGN.STATE, record.getState())
                .set(COMMON_DESIGN.IS_PASTE, ObjectUtils.isEmpty(record.getPaste()) ? 0 : (record.getPaste() ? 1 : 0))
                .set(COMMON_DESIGN.SEO, record.getSeo())
                .set(COMMON_DESIGN.MB_DESIGN_ID, record.getMbDesignId())
                .set(COMMON_DESIGN.DESIGN_CLASSIFY_ID, record.getDesignClassifyID())
                .set(COMMON_DESIGN.PATH_PRE, record.getPathPre())
                .set(COMMON_DESIGN.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_DESIGN.ID).fetchOne().getId();
        record.setCommonDesignId(id);
        return 1;
    }

    @Override
    public List<CommonDesignList> getDesignList(Map<String, Object> paramMap) {
        SelectConditionStep where = context.select(COMMON_DESIGN.TITLE, COMMON_DESIGN.ID.as("designId"), IO_FILE.PATH.as("sourcePath"), COMMON_DESIGN.IS_USED, COMMON_DESIGN.SIZE,
                COMMON_DESIGN.URL, DSL.field(DSL.max(COMMON_DESIGN.as("dp").GMT_CREATE).gt(LocalDateTime.now())).as("uploaded"),
                COMMON_DESIGN.SORT, COMMON_DESIGN.IS_PASTE, COMMON_DESIGN.SEO, COMMON_DESIGN.MB_DESIGN_ID.as("mbDesignId"), COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"),
                COMMON_DESIGN.DESIGN_TYPE, COMMON_DESIGN.GMT_CREATE)
                .from(COMMON_DESIGN)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(COMMON_DESIGN.PIC))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .leftJoin(COMMON_DESIGN.as("dp")).on(COMMON_DESIGN.ID.eq(COMMON_DESIGN.as("dp").ID))
                .where(COMMON_DESIGN.CLIENT_TYPE.eq((String) paramMap.get("clientType")))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq((String) paramMap.get("designType")))
                //.and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));

        where.groupBy(COMMON_DESIGN.ID)
                .orderBy(COMMON_DESIGN.SORT.desc(), COMMON_DESIGN.GMT_CREATE.desc(), COMMON_DESIGN.ID.desc());

        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            where.limit(startIndex,pageSize);
        }
        return where.fetchInto(CommonDesignList.class);
    }

    @Override
    public List<CommonDesignList> getChildrenDesignList(Map<String, Object> paramMap) {

        SelectConditionStep where = context.select(COMMON_DESIGN.TITLE, COMMON_DESIGN.ID.as("designId"), IO_FILE.PATH.as("sourcePath"), COMMON_DESIGN.IS_USED, COMMON_DESIGN.SIZE,
                COMMON_DESIGN.URL, COMMON_DESIGN.SORT, COMMON_DESIGN.IS_PASTE, COMMON_DESIGN.SEO, COMMON_DESIGN.MB_DESIGN_ID.as("mbDesignId"), COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"),
                COMMON_DESIGN.DESIGN_TYPE, COMMON_DESIGN.GMT_CREATE)
                .from(COMMON_DESIGN)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(COMMON_DESIGN.PIC))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(COMMON_DESIGN.CLIENT_TYPE.eq((String) paramMap.get("clientType")))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq((String) paramMap.get("designType")))
                .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));

        where.groupBy(COMMON_DESIGN.ID)
                .orderBy(COMMON_DESIGN.SORT.desc(), COMMON_DESIGN.GMT_CREATE.desc(), COMMON_DESIGN.ID.desc());

        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            where.limit(startIndex,pageSize);
        }
        return where.fetchInto(CommonDesignList.class);
    }

    @Override
    public DesignDetailVO getDesignDetail(Map<String, Object> paramMap) {
        return context.select(COMMON_DESIGN.DETAIL.as("body"), COMMON_DESIGN.HEAD, COMMON_DESIGN.FOOT, COMMON_DESIGN.SETTING, COMMON_DESIGN.SIZE,
                        COMMON_DESIGN.TITLE, COMMON_DESIGN.IS_PASTE.as("paste"), COMMON_DESIGN.SEO, COMMON_DESIGN.MB_DESIGN_ID.as("mbDesignId"),
                        COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"))
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.ID.eq(((Long) paramMap.get("designId"))))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .fetchOneInto(DesignDetailVO.class);
    }

    @Override
    public int editDesign(CommonDesign design) {
        UpdateSetMoreStep<CommonDesignRecord> setMoreStep = context.update(COMMON_DESIGN).set(COMMON_DESIGN.GMT_MODIFIED, LocalDateTime.now());
        Optional.ofNullable(design.getDetail()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.DETAIL, it));
        Optional.ofNullable(design.getHead()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.HEAD, it));
        Optional.ofNullable(design.getHead()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.HEAD, it));
        Optional.ofNullable(design.getFoot()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.FOOT, it));
        Optional.ofNullable(design.getPic()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.PIC, it));
        Optional.ofNullable(design.getSetting()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.SETTING, it));
        Optional.ofNullable(design.getTitle()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.TITLE, it));
        Optional.ofNullable(design.getIsUsed()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.IS_USED, it));
        Optional.ofNullable(design.getSize()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.SIZE, it));
        Optional.ofNullable(design.getSeo()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.SEO, it));
        Optional.ofNullable(design.getPathPre()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.PATH_PRE, it));
        Optional.ofNullable(design.getDesignClassifyID()).ifPresent(it -> setMoreStep.set(COMMON_DESIGN.DESIGN_CLASSIFY_ID, it));
        return setMoreStep.where(COMMON_DESIGN.ID.eq(design.getCommonDesignId()))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.in("0", "1"))
                .execute();
    }

    @Override
    public int deleteDesign(CommonDesign commonDesign) {
        return context.update(COMMON_DESIGN)
                .set(COMMON_DESIGN.STATE, "3")
                .where(COMMON_DESIGN.ID.eq(commonDesign.getCommonDesignId()))
                .and(COMMON_DESIGN.IS_USED.eq(0).or(COMMON_DESIGN.DESIGN_TYPE.ne("1")))
                .execute();
    }

    @Override
    public CommonDesign selectById(Long designId) {
        return context.select(COMMON_DESIGN.ID.as("common_design_id"), COMMON_DESIGN.TITLE, COMMON_DESIGN.OFFSET, COMMON_DESIGN.SIZE, COMMON_DESIGN.PIC,
                        COMMON_DESIGN.GMT_CREATE, COMMON_DESIGN.GMT_MODIFIED, COMMON_DESIGN.DESIGN_TYPE, COMMON_DESIGN.CLIENT_TYPE, COMMON_DESIGN.IS_USED, COMMON_DESIGN.DETAIL,
                        COMMON_DESIGN.URL, COMMON_DESIGN.FILE_URL, COMMON_DESIGN.FK_COMMON_DESIGN_ID, COMMON_DESIGN.FOOT, COMMON_DESIGN.HEAD, COMMON_DESIGN.SETTING, COMMON_DESIGN.IS_PASTE,
                        COMMON_DESIGN.SEO, COMMON_DESIGN.MB_DESIGN_ID.as("mbDesignId"), COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"), COMMON_DESIGN.PATH_PRE)
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.ID.eq(designId))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .fetchOneInto(CommonDesign.class);
    }

    @Override
    public CommonDesign getDesignSimple(Map<String, Object> paramMap) {
        return context.select(COMMON_DESIGN.ID.as("common_design_id"), COMMON_DESIGN.CLIENT_TYPE, COMMON_DESIGN.DESIGN_TYPE, COMMON_DESIGN.IS_USED, COMMON_DESIGN.URL,
                        COMMON_DESIGN.SEO, COMMON_DESIGN.IS_PASTE, COMMON_DESIGN.MB_DESIGN_ID, COMMON_DESIGN.DESIGN_CLASSIFY_ID.as("designClassifyID"))
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.ID.eq((Long) paramMap.get("designId")))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .fetchOneInto(CommonDesign.class);
    }

    @Override
    public CommonDesign selectForMainPage(String clientType) {
        return context.select(COMMON_DESIGN.HEAD, COMMON_DESIGN.FOOT, COMMON_DESIGN.SETTING)
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.DESIGN_TYPE.eq("1"))
                .and(COMMON_DESIGN.CLIENT_TYPE.eq(clientType))
                .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(CommonDesign.class);
    }

    @Override
    public int updateToNotUsed(CommonDesign originDesign) {
        return context.update(COMMON_DESIGN)
                .set(COMMON_DESIGN.IS_USED, 0)
                .set(COMMON_DESIGN.GMT_MODIFIED, LocalDateTime.now())
                .where(COMMON_DESIGN.CLIENT_TYPE.eq(originDesign.getClientType()))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq(originDesign.getDesignType()))
                .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .execute();
    }

    @Override
    public int updateToUsed(Long designId) {
        return context.update(COMMON_DESIGN)
                .set(COMMON_DESIGN.IS_USED, 1)
                .set(COMMON_DESIGN.GMT_MODIFIED, LocalDateTime.now())
                .where(COMMON_DESIGN.ID.eq(designId))
                .execute();
    }

    @Override
    public Integer getCountByClientType(CommonDesign commonDesign) {
        return context.select(DSL.count(COMMON_DESIGN.ID))
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.DESIGN_TYPE.eq(commonDesign.getDesignType()))
                .and(COMMON_DESIGN.CLIENT_TYPE.eq(commonDesign.getClientType()))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .execute();
    }

    @Override
    public Map<String, CommonDesign> getSubPageByMain(CommonDesign mainPage) {
        List<CommonDesign> list = context.select(COMMON_DESIGN.URL, COMMON_DESIGN.SEO, COMMON_DESIGN.FK_COMMON_DESIGN_ID.as("commonDesignId"),
                COMMON_DESIGN.HEAD, COMMON_DESIGN.FOOT)
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.CLIENT_TYPE.eq(mainPage.getClientType()))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq("2"))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .groupBy(COMMON_DESIGN.URL)
                .fetchInto(CommonDesign.class);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.stream().collect(Collectors.toMap(CommonDesign::getUrl, Function.identity(), (v1, v2) -> v2));
    }

    @Override
    public DesignDetailVO getUsingDesign(Map<String, Object> clientType) {
        return context.select(COMMON_DESIGN.HEAD, COMMON_DESIGN.FOOT, COMMON_DESIGN.SETTING)
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.CLIENT_TYPE.eq((String) clientType.get("clientType")))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq((String) clientType.get("designType")))
                .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(DesignDetailVO.class);
    }

    @Override
    public DesignDetailVO getDesignPreview(Long designId) {
        return context.select(COMMON_DESIGN.DETAIL.as("body"), COMMON_DESIGN.HEAD, COMMON_DESIGN.FOOT, COMMON_DESIGN.SETTING,
                        COMMON_DESIGN.SIZE, COMMON_DESIGN.TITLE)
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.ID.eq(designId))
                .fetchOneInto(DesignDetailVO.class);
    }

    @Override
    public CommonDesign geForExistsUrl(CommonDesign commonDesign) {
        return context.select(COMMON_DESIGN.ID.as("commonDesignId"))
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.CLIENT_TYPE.eq(commonDesign.getClientType()))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq(commonDesign.getDesignType()))
                .and(COMMON_DESIGN.URL.eq(commonDesign.getUrl()))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(CommonDesign.class);
    }

    @Override
    public List<DesignListVO> selectClassifyListByParam() {
        return context.select(COMMON_DESIGN_CLASSIFY.ID.as("designClassifyID"), COMMON_DESIGN_CLASSIFY.TYPE_NAME.as("title"), COMMON_DESIGN_CLASSIFY.PARENT_ID.as("parentID"), COMMON_DESIGN_CLASSIFY.PARENT_LEVEL,
                        COMMON_DESIGN_CLASSIFY.CREATE_TIME, DSL.val("classify").as("designType"))
                .from(COMMON_DESIGN_CLASSIFY)
                .where(COMMON_DESIGN_CLASSIFY.STATUS.eq(1))
                .and(COMMON_DESIGN_CLASSIFY.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .orderBy(COMMON_DESIGN_CLASSIFY.CREATE_TIME.asc())
                .fetchInto(DesignListVO.class);
    }

    @Override
    public Long getChildrenDesignListCount(Map<String, Object> paramMap) {
        return context.selectCount()
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.CLIENT_TYPE.eq((String) paramMap.get("clientType")))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq((String) paramMap.get("designType")))
                .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName())).groupBy(COMMON_DESIGN.ID).fetchOneInto(Long.class);
    }

    @Override
    public Long getDesignListCount(Map<String, Object> paramMap) {
        return context.selectCount()
                .from(COMMON_DESIGN)
                .leftJoin(COMMON_DESIGN.as("dp")).on(COMMON_DESIGN.ID.eq(COMMON_DESIGN.as("dp").ID))
                .where(COMMON_DESIGN.CLIENT_TYPE.eq((String) paramMap.get("clientType")))
                .and(COMMON_DESIGN.DESIGN_TYPE.eq((String) paramMap.get("designType")))
              //  .and(COMMON_DESIGN.IS_USED.eq(1))
                .and(COMMON_DESIGN.SOURCE_DESIGN_ID.eq(0L))
                .and(COMMON_DESIGN.STATE.eq("1"))
                .and(COMMON_DESIGN.TENANT_ID.eq(tenantUtil.getTenantIdByServerName())).groupBy(COMMON_DESIGN.ID).fetchOneInto(Long.class);
    }
    @Override
    public CommonDesign getDesignSimpleById(Long designId) {
        return context.select(COMMON_DESIGN.ID.as("commonDesignId"), COMMON_DESIGN.CLIENT_TYPE,COMMON_DESIGN.DESIGN_TYPE
                , COMMON_DESIGN.IS_USED
        )
                .from(COMMON_DESIGN)
                .where(COMMON_DESIGN.ID.eq(designId).and(COMMON_DESIGN.STATE.eq("1")))
                .fetchOneInto(CommonDesign.class);
    }
    @Override
    public Long addCopyDesign(Long designId){
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMON_DESIGN)
                .columns(COMMON_DESIGN.TITLE, COMMON_DESIGN.OFFSET, COMMON_DESIGN.SIZE,COMMON_DESIGN.PIC,COMMON_DESIGN.GMT_CREATE
                        ,COMMON_DESIGN.GMT_MODIFIED,COMMON_DESIGN.DESIGN_TYPE,COMMON_DESIGN.CLIENT_TYPE,COMMON_DESIGN.IS_USED,
                        COMMON_DESIGN.DETAIL,COMMON_DESIGN.URL,COMMON_DESIGN.FILE_URL,COMMON_DESIGN.FK_COMMON_DESIGN_ID,
                        COMMON_DESIGN.FOOT,COMMON_DESIGN.HEAD,COMMON_DESIGN.SETTING,COMMON_DESIGN.APPLET,COMMON_DESIGN.APPROVAL_STATE
                , COMMON_DESIGN.PATH_PRE,COMMON_DESIGN.TENANT_ID)
                .select(select(COMMON_DESIGN.TITLE, COMMON_DESIGN.OFFSET, COMMON_DESIGN.SIZE,COMMON_DESIGN.PIC,val(now)
                        ,val(now),COMMON_DESIGN.DESIGN_TYPE,COMMON_DESIGN.CLIENT_TYPE,val(0),
                        COMMON_DESIGN.DETAIL,COMMON_DESIGN.URL,COMMON_DESIGN.FILE_URL,COMMON_DESIGN.FK_COMMON_DESIGN_ID,
                        COMMON_DESIGN.FOOT,COMMON_DESIGN.HEAD,COMMON_DESIGN.SETTING,COMMON_DESIGN.APPLET,val("0")
                        , COMMON_DESIGN.PATH_PRE,COMMON_DESIGN.TENANT_ID)
                        .from(COMMON_DESIGN)
                        .where(COMMON_DESIGN.ID.eq(designId)))
                .returning(COMMON_DESIGN.ID).fetchOne().getId();
        return id;
    }
}
