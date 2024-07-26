package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.ShareDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.Share;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.svnlan.jooq.Tables.*;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;

@Repository
public class ShareDaoImpl implements ShareDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(Share share) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(SHARE)
                .columns(SHARE.TITLE, SHARE.SHARE_HASH, SHARE.USER_ID, SHARE.SOURCE_ID, SHARE.SOURCE_PATH,
                        SHARE.URL, SHARE.IS_LINK, SHARE.IS_SHARE_TO, SHARE.PASSWORD, SHARE.TIME_TO, SHARE.NUM_VIEW,
                        SHARE.NUM_DOWNLOAD, SHARE.OPTIONS, SHARE.CREATE_TIME, SHARE.MODIFY_TIME, SHARE.TENANT_ID)
                .values(share.getTitle(), share.getShareHash(), share.getUserID(), share.getSourceID(), share.getSourcePath(),
                        share.getUrl(), share.getIsLink(), share.getIsShareTo(), share.getPassword(), share.getTimeTo(), share.getNumView(),
                        share.getNumDownload(), share.getOptions(), now, now, tenantUtil.getTenantIdByServerName())
                .returning(SHARE.ID).fetchOne().getId();
        share.setShareID(id);
        return 1;
    }

    @Override
    public int update(Share share) {
        return context.update(SHARE)
                .set(SHARE.TITLE, share.getTitle())
                .set(SHARE.PASSWORD, share.getPassword())
                .set(SHARE.OPTIONS, share.getOptions())
                .set(SHARE.TIME_TO, share.getTimeTo())
                .set(SHARE.STATUS, share.getStatus())
                .set(SHARE.MODIFY_TIME, LocalDateTime.now())
                .where(SHARE.ID.eq(share.getShareID()))
                .execute();
    }

    @Override
    public int updateNumView(Long shareID, Integer numView) {
        return context.update(SHARE)
                .set(SHARE.NUM_VIEW, SHARE.NUM_VIEW.add(1))
                .where(SHARE.ID.eq(shareID))
                .execute();
    }

    @Override
    public int updateNumDownload(Long shareID, Integer numDownload) {
        return context.update(SHARE)
                .set(SHARE.NUM_DOWNLOAD, SHARE.NUM_DOWNLOAD.add(1))
                .where(SHARE.ID.eq(shareID))
                .execute();
    }

    @Override
    public int deleteList(List<Long> list) {
        return context.deleteFrom(SHARE)
                .where(SHARE.ID.in(list))
                .execute();
    }

    @Override
    public ShareVo getShare(Long sourceID, Long userID, Integer isShareTo, Integer isLink) {
        return context.select(SHARE.ID.as("shareID"), SHARE.TITLE, SHARE.SHARE_HASH, SHARE.USER_ID.as("userID"), SHARE.SOURCE_ID.as("sourceID"), SHARE.SOURCE_PATH,
                        SHARE.URL, SHARE.IS_LINK, SHARE.IS_SHARE_TO, SHARE.PASSWORD, SHARE.TIME_TO, SHARE.NUM_VIEW,
                        SHARE.NUM_DOWNLOAD, SHARE.OPTIONS, SHARE.CREATE_TIME, SHARE.MODIFY_TIME)
                .from(SHARE)
                .where(SHARE.USER_ID.eq(userID))
                .and(SHARE.SOURCE_ID.eq(sourceID))
                .and(SHARE.IS_SHARE_TO.eq(isShareTo))
                .and(SHARE.IS_LINK.eq(isLink))
                .fetchOneInto(ShareVo.class);
    }

    @Override
    public ShareVo getShareById(Long shareID) {
        return context.select(SHARE.ID, SHARE.TITLE, SHARE.SHARE_HASH, SHARE.USER_ID, SHARE.SOURCE_ID, SHARE.SOURCE_PATH,
                        SHARE.URL, SHARE.IS_LINK, SHARE.IS_SHARE_TO, SHARE.PASSWORD, SHARE.TIME_TO, SHARE.NUM_VIEW,
                        SHARE.NUM_DOWNLOAD, SHARE.OPTIONS, SHARE.CREATE_TIME, SHARE.MODIFY_TIME)
                .from(SHARE)
                .where(SHARE.ID.eq(shareID))
                .fetchOneInto(ShareVo.class);
    }

    @Override
    public List<HomeExplorerVO> getShareList(Map<String, Object> paramMap) {

        SelectJoinStep<Record> from = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType")
                , IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE,IO_FILE.PATH
                , IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,SHARE.TITLE,SHARE.SOURCE_PATH.as("sourcePath")
                ,SHARE.CREATE_TIME.as("createTime"),SHARE.TIME_TO.as("timeTo"),SHARE.NUM_VIEW.as("numView"),SHARE.NUM_DOWNLOAD.as("numDownload"),SHARE.SHARE_HASH.as("shareHash")
                ,SHARE.ID.as("shareID"),IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_SOURCE.CAN_SHARE.as("canShare"))
                .from(SHARE);
        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(SHARE.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")));
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep<Record> where = from.where(SHARE.USER_ID.eq(userID));
        getShareListWhere(where, paramMap);
        from.orderBy( DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")), SHARE.ID.asc());
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        return from.fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Long getShareListCount(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from = context.select(DSL.count(SHARE.ID))
                .from(SHARE);
        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(SHARE.SOURCE_ID));
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep<Record1<Integer>> where = from.where(SHARE.USER_ID.eq(userID));
        getShareListWhere(where, paramMap);
        return (Long) where.fetchOneInto(Long.class);
    }
    private void getShareListWhere(SelectConditionStep where, Map<String, Object> paramMap) {

        Integer isLink = (Integer) paramMap.get("isLink");
        if (isLink != null){
            where.and(SHARE.IS_LINK.eq(isLink));
        }
        Integer isShareTo = (Integer) paramMap.get("isShareTo");
        if (isShareTo != null){
            where.and(SHARE.IS_SHARE_TO.eq(isShareTo));
        }
        where.and(IO_SOURCE.IS_DELETE.eq(0));
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            where.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }
    }

    @Override
    public List<Long> checkUserIsShare(Long userID) {
        return context.select(SHARE.ID)
                .from(SHARE)
                .where(SHARE.USER_ID.eq(userID))
                .and(SHARE.IS_LINK.eq(1))
                .and(SHARE.STATUS.eq(1))
                .fetch(SHARE.ID);
    }

    @Override
    public List<ShareVo> getShareByCode(String shareCode) {
        return context.select(SHARE.ID.as("shareID"), SHARE.TITLE, SHARE.SHARE_HASH, SHARE.USER_ID.as("userID"), SHARE.SOURCE_ID.as("sourceID"), SHARE.SOURCE_PATH,
                        SHARE.URL, SHARE.IS_LINK, SHARE.IS_SHARE_TO, SHARE.PASSWORD, SHARE.TIME_TO, SHARE.NUM_VIEW, SHARE.NUM_DOWNLOAD, SHARE.OPTIONS,
                        SHARE.CREATE_TIME, USERS.AVATAR, DSL.ifnull(USERS.NICKNAME, USERS.NAME).as("nickname"), SHARE.STATUS, USERS.NAME.as("userName"))
                .from(SHARE)
                .leftJoin(USERS).on(USERS.ID.eq(SHARE.USER_ID))
                .where(SHARE.SHARE_HASH.eq(shareCode))
                .and(SHARE.STATUS.eq(1))
                .and(SHARE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(ShareVo.class);
    }

    @Override
    public HomeExplorerVO getLinkShareInfo(Long sourceID) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH, IO_SOURCE.TARGET_TYPE, IO_SOURCE.TARGET_ID.as("targetID"),
                        IO_SOURCE.CREATE_USER, IO_SOURCE.MODIFY_USER, IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.PARENT_ID.as("parentID"),
                        IO_SOURCE.PARENT_LEVEL, IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE, IO_SOURCE.SIZE,
                        IO_FILE.PATH, IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5, IO_SOURCE.DESCRIPTION,
                        IO_SOURCE.CREATE_TIME, IO_FILE.IS_EXIST_FILE)
                .from(IO_SOURCE)
                .leftJoin(IO_SOURCE_META).on(IO_SOURCE_META.SOURCE_ID.eq(IO_SOURCE.ID)).and(IO_SOURCE_META.KEY_STRING.eq("desc"))
                .leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                .leftJoin(IO_FILE_META).on(IO_FILE_META.FILE_ID.eq(IO_SOURCE.FILE_ID)).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .where(IO_SOURCE.ID.eq(sourceID))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchOneInto(HomeExplorerVO.class);
    }

    @Override
    public List<HomeExplorerVO> getShareToMeList(Map<String, Object> paramMap) {
        // TODO 需优化
        Long userID = (Long) paramMap.get("userID");
        SelectOrderByStep<Record> selectConditionStep = context.select(SHARE_TO.SHARE_ID.as("shareID"),SHARE_TO.AUTH_ID.as("authID"),SHARE_TO.TARGET_ID.as("targetID")
                ,SHARE_TO.TARGET_TYPE.as("targetType"),IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.CREATE_USER.as("createUser")
                , IO_SOURCE.MODIFY_USER.as("modifyUser"),IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID")
                ,IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE,IO_FILE.PATH
                , IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,SHARE.TITLE,SHARE.SOURCE_PATH.as("sourcePath")
                ,SHARE.CREATE_TIME.as("createTime"),SHARE.TIME_TO.as("timeTo"),SHARE.NUM_VIEW.as("numView"),SHARE.NUM_DOWNLOAD.as("numDownload")
                ,ROLE.ROLE_NAME.as("authName"),ROLE.AUTH ,IO_FILE.IS_EXIST_FILE.as("isExistFile"))
                .from(SHARE_TO)
                .innerJoin(SHARE).on(SHARE_TO.SHARE_ID.eq(SHARE.ID))
                .leftJoin(IO_SOURCE).on(SHARE.SOURCE_ID.eq(IO_SOURCE.ID))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .leftJoin(ROLE).on(ROLE.ID.eq(SHARE_TO.AUTH_ID).and(ROLE.ROLE_TYPE.eq("2")))
                .where(SHARE_TO.TARGET_ID.eq(userID).and(SHARE_TO.TARGET_TYPE.eq(0)).and(SHARE.IS_LINK.eq(0)).and(SHARE.IS_SHARE_TO.eq(1)).and(IO_SOURCE.IS_DELETE.eq(0)));

        if (paramMap.containsKey("list") && !ObjectUtils.isEmpty(paramMap.get("list"))){
            List<Long> groupIDList = ObjUtil.objectToList(paramMap.get("list"), Long.class);
            if (!CollectionUtils.isEmpty(groupIDList)){
                selectConditionStep = selectConditionStep.unionAll(
                        context.select(SHARE_TO.SHARE_ID.as("shareID"),SHARE_TO.AUTH_ID.as("authID"),SHARE_TO.TARGET_ID.as("targetID")
                                ,SHARE_TO.TARGET_TYPE.as("targetType"),IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.CREATE_USER.as("createUser")
                                , IO_SOURCE.MODIFY_USER.as("modifyUser"),IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID")
                                ,IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE,IO_FILE.PATH
                                , IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,SHARE.TITLE,SHARE.SOURCE_PATH.as("sourcePath")
                                ,SHARE.CREATE_TIME.as("createTime"),SHARE.TIME_TO.as("timeTo"),SHARE.NUM_VIEW.as("numView"),SHARE.NUM_DOWNLOAD.as("numDownload")
                                ,ROLE.ROLE_NAME.as("authName"),ROLE.AUTH ,IO_FILE.IS_EXIST_FILE.as("isExistFile"))
                                .from(SHARE_TO)
                                .innerJoin(SHARE).on(SHARE_TO.SHARE_ID.eq(SHARE.ID))
                                .leftJoin(IO_SOURCE).on(SHARE.SOURCE_ID.eq(IO_SOURCE.ID))
                                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                                .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                                .leftJoin(ROLE).on(ROLE.ID.eq(SHARE_TO.AUTH_ID).and(ROLE.ROLE_TYPE.eq("2")))
                                .where(SHARE_TO.TARGET_ID.in(groupIDList).and(SHARE_TO.TARGET_TYPE.eq(1)).and(SHARE.IS_LINK.eq(0)).and(SHARE.IS_SHARE_TO.eq(1))
                                        .and(IO_SOURCE.IS_DELETE.eq(0)))
                );
            }
        }
        selectConditionStep.orderBy(DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")));
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            selectConditionStep.limit(startIndex,pageSize);
        }
        return selectConditionStep.fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Long getShareToMeListCount(Map<String, Object> paramMap) {
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep<Record1<Integer>> selectConditionStep = context.selectCount()
                .from(SHARE_TO)
                .innerJoin(SHARE).on(SHARE_TO.SHARE_ID.eq(SHARE.ID))
                .leftJoin(IO_SOURCE).on(SHARE.SOURCE_ID.eq(IO_SOURCE.ID))
                .where(SHARE_TO.TARGET_ID.eq(userID).and(SHARE_TO.TARGET_TYPE.eq(0)).and(SHARE.IS_LINK.eq(0)).and(SHARE.IS_SHARE_TO.eq(1)).and(IO_SOURCE.IS_DELETE.eq(0)));

        if (paramMap.containsKey("list") && !ObjectUtils.isEmpty(paramMap.get("list"))) {
            List<Long> groupIDList = ObjUtil.objectToList(paramMap.get("list"), Long.class);
            if (!CollectionUtils.isEmpty(groupIDList)) {
                selectConditionStep = (SelectConditionStep<Record1<Integer>>) selectConditionStep.unionAll(
                        context.selectCount()
                                .from(SHARE_TO)
                                .innerJoin(SHARE).on(SHARE_TO.SHARE_ID.eq(SHARE.ID))
                                .leftJoin(IO_SOURCE).on(SHARE.SOURCE_ID.eq(IO_SOURCE.ID))
                                .where(SHARE_TO.TARGET_ID.in(groupIDList).and(SHARE_TO.TARGET_TYPE.eq(1)).and(SHARE.IS_LINK.eq(0)).and(SHARE.IS_SHARE_TO.eq(1))
                                        .and(IO_SOURCE.IS_DELETE.eq(0)))
                );
            }
        }

        List<Integer> list =  selectConditionStep.fetchInto(Integer.class);
        long num = 0;
        if (!CollectionUtils.isEmpty(list)){
            num = list.stream().collect(Collectors.toList()).stream().reduce(Integer::sum).get();
        }
        return num;
    }

    @Override
    public List<HomeExplorerVO> getLinkShareList(Map<String, Object> paramMap) {
        SelectJoinStep<Record20<Long, String, Integer, Long, Long, Long, Integer, String, String, Long, Integer, String, Long, Integer, Long, String, String, String, String, Integer>>
                from = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType")
                , IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE,IO_FILE.PATH
                , IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION
                ,IO_FILE.IS_EXIST_FILE.as("isExistFile"))
                .from(IO_SOURCE);
        from.leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")));
        Long parentID = (Long) paramMap.get("parentID");
        SelectConditionStep<Record20<Long, String, Integer, Long, Long, Long, Integer, String, String, Long, Integer, String, Long, Integer, Long, String, String, String, String, Integer>>
                where = from.where(IO_SOURCE.PARENT_ID.eq(parentID));
        getLinkShareListWhere(where, paramMap);
        from.orderBy( DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")));
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        return from.fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Long getLinkShareListCount(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from = context.select(DSL.count(IO_SOURCE.ID))
                .from(IO_SOURCE);
        Long parentID = (Long) paramMap.get("parentID");
        SelectConditionStep<Record1<Integer>> where = from.where(IO_SOURCE.PARENT_ID.eq(parentID));
        getLinkShareListWhere(where, paramMap);
        return (Long) where.fetchOneInto(Long.class);
    }

    private void getLinkShareListWhere(SelectConditionStep where, Map<String, Object> paramMap) {

        Integer isFolder = (Integer) paramMap.get("isFolder");
        if (isFolder != null){
            where.and(IO_SOURCE.IS_FOLDER.eq(isFolder));
        }
        where.and(IO_SOURCE.IS_DELETE.eq(0));
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            where.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }
    }
    @Override
    public List<UserVo> getSelectUserListByParam(Map<String, Object> paramMap) {

        SelectJoinStep<Record13<Long, String, String, LocalDateTime, String, String, Integer, String, Integer, Integer, Double, Long, Integer>>
                from = context.selectDistinct(USERS.ID.as("userID"), USERS.NAME,USERS.NICKNAME,USERS.LAST_LOGIN.as("lastLogin"),USERS.PHONE,USERS.EMAIL,USERS.STATUS
                ,USERS.AVATAR,USERS.SEX,USERS.ROLE_ID.as("roleID"),USERS.SIZE_MAX.as("sizeMax"),USERS.SIZE_USE.as("sizeUse"),USERS.IS_SYSTEM.as("isSystem"))
                .from(USERS);
        Long groupID = (Long) paramMap.get("groupID");
        String keyword = (String) paramMap.get("keyword");
        if (groupID != null) {
            from.leftJoin(USER_GROUP).on(USERS.ID.eq(USER_GROUP.USER_ID));
        }
        if (keyword != null) {
            from.leftJoin(USER_META).on(USERS.ID.eq(USER_META.USER_ID)).and(USER_META.KEY_STRING.in("namePinyin","namePinyinSimple"));
        }

        SelectConditionStep<Record13<Long, String, String, LocalDateTime, String, String, Integer, String, Integer, Integer, Double, Long, Integer>>
                where = from.where(USERS.STATUS.eq(1));
        if (groupID != null) {
            where.and(USER_GROUP.GROUP_ID.eq(groupID));
        }
        if (keyword != null) {
            where.and(USERS.NAME.like(DSL.concat("%", keyword, "%")).or(USERS.NICKNAME.like(DSL.concat("%", keyword, "%")))
                    .or(USERS.PHONE.like(DSL.concat("%", keyword, "%"))).or(USERS.EMAIL.like(DSL.concat("%", keyword, "%")))
                    .or(USER_META.VALUE_TEXT.like(DSL.concat("%", keyword, "%")))
            );
        }
        if (groupID == null) {
            where.andNotExists(context.select(USER_GROUP.USER_ID).from(USER_GROUP).where(USERS.ID.eq(USER_GROUP.USER_ID)));
        }
        return where.fetchInto(UserVo.class);
    }

    @Override
    public List<UserVo> getNotGroupUserListByParam(Map<String, Object> paramMap) {

        SelectJoinStep<Record13<Long, String, String, LocalDateTime, String, String, Integer, String, Integer, Integer, Double, Long, Integer>>
                from = context.selectDistinct(USERS.ID.as("userID"), USERS.NAME,USERS.NICKNAME,USERS.LAST_LOGIN.as("lastLogin"),USERS.PHONE,USERS.EMAIL,USERS.STATUS
                ,USERS.AVATAR,USERS.SEX,USERS.ROLE_ID.as("roleID"),USERS.SIZE_MAX.as("sizeMax"),USERS.SIZE_USE.as("sizeUse"),USERS.IS_SYSTEM.as("isSystem"))
                .from(USERS);
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            from.leftJoin(USER_META).on(USERS.ID.eq(USER_META.USER_ID)).and(USER_META.KEY_STRING.in("namePinyin","namePinyinSimple"));
        }
        SelectConditionStep<Record13<Long, String, String, LocalDateTime, String, String, Integer, String, Integer, Integer, Double, Long, Integer>>
                where = from.where(USERS.STATUS.eq(1));

        if (ObjectUtils.isEmpty(keyword)){
            where.andNotExists(context.select(USER_GROUP.USER_ID).from(USER_GROUP).where(USERS.ID.eq(USER_GROUP.USER_ID)));
        }else {
            where.and(USERS.NAME.like(DSL.concat("%", keyword, "%")).or(USERS.NICKNAME.like(DSL.concat("%", keyword, "%")))
                    .or(USERS.PHONE.like(DSL.concat("%", keyword, "%"))).or(USERS.EMAIL.like(DSL.concat("%", keyword, "%")))
                    .or(USER_META.VALUE_TEXT.like(DSL.concat("%", keyword, "%")))
            );
        }
        return where.fetchInto(UserVo.class);
    }

    @Override
    public List<ShareVo> getShareByIdList(List<Long> list) {
        return context.select(SHARE.ID.as("shareID"), SHARE.TITLE, SHARE.SHARE_HASH, SHARE.USER_ID.as("userID"), SHARE.SOURCE_ID.as("sourceID"), SHARE.SOURCE_PATH,
                SHARE.URL, SHARE.IS_LINK, SHARE.IS_SHARE_TO, SHARE.PASSWORD, SHARE.TIME_TO, SHARE.NUM_VIEW, SHARE.NUM_DOWNLOAD, SHARE.OPTIONS,
                SHARE.CREATE_TIME)
                .from(SHARE)
                .where(SHARE.ID.in(list))
                .fetchInto(ShareVo.class);
    }

    @Override
    public void updateStatus(Integer operateType, List<Long> ids) {
        context.update(SHARE)
                .set(SHARE.STATUS, operateType)
                .where(SHARE.ID.in(ids))
                .execute();
    }
}
