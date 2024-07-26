package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.HomeFileDetailVO;
import com.svnlan.jooq.Tables;
import com.svnlan.jooq.tables.records.GroupsRecord;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.svnlan.jooq.Tables.IO_SOURCE_EVENT;
import static com.svnlan.jooq.Tables.IO_SOURCE_RECYCLE;
import static com.svnlan.jooq.tables.GroupSource.GROUP_SOURCE;
import static com.svnlan.jooq.tables.Groups.GROUPS;
import static com.svnlan.jooq.tables.IoFileMeta.IO_FILE_META;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;
import static com.svnlan.jooq.tables.IoFile.IO_FILE;
import static com.svnlan.jooq.tables.IoSourceMeta.IO_SOURCE_META;
import static com.svnlan.jooq.tables.Role.ROLE;
import static com.svnlan.jooq.tables.SystemLog.SYSTEM_LOG;
import static com.svnlan.jooq.tables.UserFav.USER_FAV;
import static com.svnlan.jooq.tables.UserGroup.USER_GROUP;
import static com.svnlan.jooq.tables.Users.USERS;

@Service
public class HomeExplorerDaoImpl implements HomeExplorerDao {
    @Autowired
    private DSLContext context;
    @Autowired
    private TenantUtil tenantUtil;

    @Override
    public List<HomeExplorerVO> getHomeExplorer(Map<String, Object> paramMap) {
        SelectJoinStep<Record> from = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType"), IO_SOURCE.TARGET_ID.as("targetID")
                , IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                        IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                        IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME.as("createTime")
                , IO_SOURCE.MODIFY_TIME.as("modifyTime"), IO_SOURCE.VIEW_TIME.as("viewTime"),
                        IO_FILE.PATH, DSL.val("").as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION, IO_FILE.IS_PREVIEW.as("preview"),
                        IO_FILE.CREATE_TIME.as("fileCreateTime"), IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_FILE.IS_M3U8.as("isM3u8"), IO_SOURCE.CAN_SHARE.as("canShare")
        ,IO_SOURCE.COVER_ID,IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE);

        from.leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
               // .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
        ;
        Object tagID = paramMap.get("tagID");
        if (tagID != null) {
            from.leftJoin(USER_FAV).on(DSL.cast(IO_SOURCE.ID, String.class).eq(USER_FAV.PATH).and(USER_FAV.TYPE.eq("source")));
        }
        Long tenantId = (Long) paramMap.get("tenantId");

        SelectConditionStep<Record> where = from.where(IO_SOURCE.TENANT_ID.eq(tenantId).and(IO_SOURCE.IS_DELETE.eq(0)));
        getHomeExplorerWhere(where, paramMap);
        from.orderBy(IO_SOURCE.SORT.desc(), IO_SOURCE.IS_FOLDER.desc(), DSL.field("io_source."+(String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")), IO_SOURCE.ID.asc());
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        List<HomeExplorerVO> list = from.fetchInto(HomeExplorerVO.class);
        if (!CollectionUtils.isEmpty(list)){
            list = setListFileInfoMore(list, tenantId);
        }
        return list;
    }

    @Override
    public Map<Long, String> getFileInfoMoreMap(List<Long> fileIds, Long tenantId){
        SelectConditionStep where = context.select(IO_FILE_META.FILE_ID.as("fileID"),IO_FILE_META.VALUE_TEXT.as("value"))
                .from(IO_FILE_META)
                .where(IO_FILE_META.TENANT_ID.eq(tenantId).and(IO_FILE_META.FILE_ID.in(fileIds)));

        List<HomeExplorerVO> list = where.fetchInto(HomeExplorerVO.class);
        if (!CollectionUtils.isEmpty(list)){
            return list.stream().collect(Collectors.toMap(HomeExplorerVO::getFileID, HomeExplorerVO::getValue, (v1, v2) -> v2));
        }
        return null;
    }


    public Condition buildSearchParentLevelCondition(List<String> parentLevelList) {
        Condition condition = DSL.trueCondition();
        for (String level : parentLevelList) {
            condition = condition.or(IO_SOURCE.PARENT_LEVEL.like(DSL.concat("%", level, "%")));
        }
        return condition;
    }

    private void getHomeExplorerWhere(SelectConditionStep where, Map<String, Object> paramMap) {
        Long sourceID = (Long) paramMap.get("sourceID");
        if (sourceID != null) {
            where.and(IO_SOURCE.PARENT_ID.eq(sourceID));
        }
        Long tenantId = (Long) paramMap.get("tenantId");
        where.and(IO_SOURCE.TENANT_ID.eq(tenantId));
        Long thisSourceID = (Long) paramMap.get("thisSourceID");
        if (thisSourceID != null) {
            where.and(IO_SOURCE.ID.eq(thisSourceID));
        }
        Integer targetType = (Integer) paramMap.get("targetType");
        if (targetType != null) {
            where.and(IO_SOURCE.TARGET_TYPE.eq(targetType));
        }
        Integer isSafe = (Integer) paramMap.get("isSafe");
        if (isSafe != null) {
            where.and(IO_SOURCE.IS_SAFE.eq(isSafe));
        }
        String parentLevel = (String) paramMap.get("parentLevel");
        if (parentLevel != null) {
            where.and(IO_SOURCE.PARENT_LEVEL.eq(parentLevel));
        }
        Long tagID = (Long) paramMap.get("tagID");
        if (tagID != null) {
            where.and(USER_FAV.TAG_ID.eq(tagID.intValue()));
        }
        Integer isFolder = (Integer) paramMap.get("isFolder");
        if (isFolder != null) {
            where.and(IO_SOURCE.IS_FOLDER.eq(isFolder));
        }
        String repeatHashMd5 = (String) paramMap.get("repeatHashMd5");
        if (repeatHashMd5 != null) {
            where.and(IO_FILE.HASH_MD5.eq(repeatHashMd5));
        }
        String repeatName = (String) paramMap.get("repeatName");
        if (repeatName != null) {
            where.and(IO_SOURCE.NAME.eq(repeatName));
        }
        List<String> parentLevelList = (List<String>) paramMap.get("parentLevelList");
        if (parentLevelList != null) {
            where.and(buildSearchParentLevelCondition(parentLevelList));
        }
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            where.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
            .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }
        Long userID = (Long) paramMap.get("userID");
        if (userID != null) {
            Integer isSearch = (Integer) paramMap.get("isSearch");
            if (isSearch != null && isSearch == 1) {
                where.and(IO_SOURCE.CREATE_USER.eq(userID).or(IO_SOURCE.MODIFY_USER.eq(userID)));
            } else {
                where.and(IO_SOURCE.CREATE_USER.eq(userID));
            }
        }
        Integer documentType = (Integer) paramMap.get("documentType");
        if (documentType != null) {
            where.and(IO_SOURCE.TYPE.eq(documentType));
        } else {
            List<String> fileTypeList = (List<String>) paramMap.get("fileTypeList");
            if (fileTypeList != null) {
                where.and(IO_SOURCE.FILE_TYPE.in(fileTypeList));
            }
        }
        Long minSize = (Long) paramMap.get("minSize");
        Long maxSize = (Long) paramMap.get("maxSize");
        if (minSize != null) {
            if (maxSize != null) {
                where.and(IO_SOURCE.SIZE.between(minSize, maxSize));
            } else {
                where.and(IO_SOURCE.SIZE.ge(minSize));
            }
        } else if (maxSize != null) {
            where.and(IO_SOURCE.SIZE.le(maxSize));
        }
        Long minDateStr = (Long) paramMap.get("minDate");
        Long maxDateStr = (Long) paramMap.get("maxDate");

        LocalDateTime minDate = null;
        if (minDateStr != null){
            minDate = DateUtil.getLocalDateTimeFromMilli(minDateStr * 1000);
        }
        LocalDateTime maxDate = null;
        if (maxDateStr != null){
            maxDate = DateUtil.getLocalDateTimeFromMilli(maxDateStr * 1000);
        }
        if (minSize != null) {
            if (maxSize != null) {
                where.and(IO_SOURCE.CREATE_TIME.between(minDate, maxDate));
            } else {
                where.and(IO_SOURCE.CREATE_TIME.ge(minDate));
            }
        } else if (maxSize != null) {
            where.and(IO_SOURCE.CREATE_TIME.le(minDate));
        }
        if (targetType != null && targetType == 2) {
            where.andNotExists(context.select(GROUP_SOURCE.SOURCE_ID).from(GROUP_SOURCE).where(IO_SOURCE.ID.eq(GROUP_SOURCE.SOURCE_ID)));
        }
    }

    @Override
    public Long getCountHomeExplorer(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from = context.select(DSL.countDistinct(IO_SOURCE.ID))
                .from(IO_SOURCE);
        Long tenantId = (Long) paramMap.get("tenantId");
        String repeatHashMd5 = (String) paramMap.get("repeatHashMd5");
        if (repeatHashMd5 != null) {
            from.leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID));
        }
       // from.leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")));
        Long tagID = (Long) paramMap.get("tagID");
        if (tagID != null) {
            from.leftJoin(USER_FAV).on(DSL.cast(IO_SOURCE.ID, String.class).eq(USER_FAV.PATH).and(USER_FAV.TYPE.eq("source")));
        }
        SelectConditionStep where = from.where(IO_SOURCE.TENANT_ID.eq(tenantId).and(IO_SOURCE.IS_DELETE.eq(0)));
        getHomeExplorerWhere(where, paramMap);
        return (Long) where.fetchOneInto(Long.class);
    }

    @Override
    public List<HomeExplorerVO> getUserFavExplorer(Map<String, Object> paramMap) {

        Long userID = (Long) paramMap.get("userID");
        String keyword = (String) paramMap.get("keyword");
        SelectOrderByStep<Record> selectConditionStep = context.select(USER_FAV.ID.as("favID"),USER_FAV.SORT.as("favSort"),USER_FAV.PATH.as("favPath"),USER_FAV.NAME.as("favName"),
                USER_FAV.TYPE.as("favType"),
                IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TARGET_TYPE.as("targetType"), IO_SOURCE.TARGET_ID.as("targetID")
                , IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"),
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME.as("createTime")
                , IO_SOURCE.MODIFY_TIME.as("modifyTime"),
                IO_FILE.PATH, DSL.val("").as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,
                IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_FILE.IS_M3U8.as("isM3u8"), IO_SOURCE.CAN_SHARE.as("canShare"),IO_SOURCE.COVER_ID,IO_SOURCE.IS_SAFE)
                .from(USER_FAV).leftJoin(IO_SOURCE).on(USER_FAV.PATH.eq(DSL.coerce(IO_SOURCE.ID, String.class)))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                //.leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .where(USER_FAV.USER_ID.eq(userID).and(USER_FAV.TAG_ID.eq(0)).and(USER_FAV.TYPE.eq("source")).and(IO_SOURCE.IS_DELETE.eq(0)));

        if (keyword != null){
            ((SelectConditionStep<Record>) selectConditionStep).and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }


        selectConditionStep = selectConditionStep.unionAll(
                context.select(USER_FAV.ID.as("favID"),USER_FAV.SORT.as("favSort"),USER_FAV.PATH.as("favPath"),USER_FAV.NAME.as("favName"),
                        USER_FAV.TYPE.as("favType")
                        ,DSL.val(0).as("sourceID"), DSL.val("").as("targetType"), DSL.val(0).as("targetID")
                        , DSL.val(0).as("createUser"), DSL.val(0).as("modifyUser"),
                        DSL.val(0).as("isFolder"), DSL.val("").as("name"), DSL.val("").as("fileType"), DSL.val(0).as("parentID"),
                        DSL.val("").as("parentLevel"), DSL.val(0).as("fileID"), DSL.val(0).as("isDelete"), DSL.val(0).as("size")
                        , DSL.val(0).as("createTime"), DSL.val(0).as("modifyTime"),
                        DSL.val("").as("path"), DSL.val("").as("value"), DSL.val("").as("hashMd5"), DSL.val("").as("description"),
                        DSL.val(0).as("isExistFile"), DSL.val(0).as("isM3u8"), DSL.val(0).as("canShare"), DSL.val(0).as("coverId")
                        , DSL.val(0).as("isSafe")
                )
                        .from(USER_FAV).where(USER_FAV.USER_ID.eq(userID).and(USER_FAV.TAG_ID.eq(0)).and(USER_FAV.TYPE.eq("folder")))
        );

        selectConditionStep.orderBy(DSL.field("favSort").desc(), DSL.field("isFolder").desc()
                , DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")));
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            selectConditionStep.limit(startIndex,pageSize);
        }

        List<HomeExplorerVO> list = selectConditionStep.fetchInto(HomeExplorerVO.class);
        if (!CollectionUtils.isEmpty(list)){
            Long tenantId = (Long) paramMap.get("tenantId");
            list = setListFileInfoMore(list, tenantId);
        }
        return list;
    }

    private List<HomeExplorerVO> setListFileInfoMore(List<HomeExplorerVO> list, Long tenantId ){

        List<Long> fidList = new ArrayList<>();
        for (HomeExplorerVO vo : list){
            if (!ObjectUtils.isEmpty(vo.getFileID()) && vo.getFileID() > 0){
                fidList.add(vo.getFileID());
            }
        }
        if (!CollectionUtils.isEmpty(fidList) && fidList.size() > 0){
            Map<Long, String> metaMap = getFileInfoMoreMap(fidList, tenantId);
            for (HomeExplorerVO vo : list){
                if (!ObjectUtils.isEmpty(metaMap) && metaMap.containsKey(vo.getFileID())){
                    vo.setValue(metaMap.get(vo.getFileID()));
                }
            }
        }
        return list;
    }


    @Override
    public Long getUserFavExplorerCount(Map<String, Object> paramMap) {
        Long userID = (Long) paramMap.get("userID");
        String keyword = (String) paramMap.get("keyword");
        SelectConditionStep<Record1<Integer>> selectConditionStep = context.selectCount()
                .from(USER_FAV).leftJoin(IO_SOURCE).on(USER_FAV.PATH.eq(DSL.coerce(IO_SOURCE.ID, String.class)))
              //  .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                //.leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .where(USER_FAV.USER_ID.eq(userID).and(USER_FAV.TAG_ID.eq(0)).and(USER_FAV.TYPE.eq("source")).and(IO_SOURCE.IS_DELETE.eq(0)));

        if (keyword != null){
            ((SelectConditionStep<Record1<Integer>>) selectConditionStep).and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }


        selectConditionStep = (SelectConditionStep<Record1<Integer>>) selectConditionStep.unionAll(
                context.selectCount()
                        .from(USER_FAV).where(USER_FAV.USER_ID.eq(userID).and(USER_FAV.TAG_ID.eq(0)).and(USER_FAV.TYPE.eq("folder")))
        );

        List<Integer> list =  selectConditionStep.fetchInto(Integer.class);
        long num = 0;
        if (!CollectionUtils.isEmpty(list)){
            num = list.stream().collect(Collectors.toList()).stream().reduce(Integer::sum).get();
        }
        return num;
    }

    @Override
    public List<HomeExplorerVO> getUserRecycleExplorer(Map<String, Object> paramMap) {
        SelectJoinStep<Record> from = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType")
                , IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME.as("createTime")
                , IO_SOURCE.MODIFY_TIME.as("modifyTime"), IO_SOURCE.VIEW_TIME.as("viewTime"),
                IO_FILE.PATH, DSL.val("").as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,
               IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_FILE.IS_M3U8.as("isM3u8"), IO_SOURCE.CAN_SHARE.as("canShare"),IO_SOURCE.COVER_ID,IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE_RECYCLE);

        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_RECYCLE.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
              //  .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
        ;
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep where = from.where(IO_SOURCE_RECYCLE.USER_ID.eq(userID)).and(IO_SOURCE.IS_DELETE.eq(1));
        getUserRecycleExplorerWhere(where, paramMap);
        from.orderBy(DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")), IO_SOURCE_RECYCLE.ID.asc());
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        List<HomeExplorerVO> list = from.fetchInto(HomeExplorerVO.class);
        if (!CollectionUtils.isEmpty(list)){
            Long tenantId = (Long) paramMap.get("tenantId");
            list = setListFileInfoMore(list, tenantId);
        }
        return list;
    }

    @Override
    public Long getUserRecycleExplorerCount(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from = context.select(DSL.countDistinct(IO_SOURCE_RECYCLE.ID))
                .from(IO_SOURCE_RECYCLE);
        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_RECYCLE.SOURCE_ID));
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep where = from.where(IO_SOURCE_RECYCLE.USER_ID.eq(userID)).and(IO_SOURCE.IS_DELETE.eq(1));
        getUserRecycleExplorerWhere(where, paramMap);
        return (Long) where.fetchOneInto(Long.class);
    }

    private void getUserRecycleExplorerWhere(SelectConditionStep where, Map<String, Object> paramMap) {
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            where.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }
    }
    @Override
    public List<HomeExplorerVO> getUserRecentExplorer(Map<String, Object> paramMap) {

        Long tenantId = tenantUtil.getTenantIdByServerName();
        SelectJoinStep<Record> from = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType")
                , IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME.as("createTime")
                , IO_SOURCE_EVENT.CREATE_TIME.as("modifyTime"),
                IO_FILE.PATH
              //  , IO_FILE_META.VALUE_TEXT.as("value")
                , IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION,
                IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_FILE.IS_M3U8.as("isM3u8"), IO_SOURCE.CAN_SHARE.as("canShare"),IO_SOURCE.COVER_ID,IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE_EVENT);
        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_EVENT.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
              //  .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
        ;
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep<Record> where = from.where(IO_SOURCE_EVENT.USER_ID.eq(userID).and(IO_SOURCE_EVENT.TYPE.in("create","edit"))
                .and(IO_SOURCE.IS_DELETE.eq(0)).and(IO_SOURCE.IS_FOLDER.eq(0)).and(IO_SOURCE.TARGET_TYPE.in(1,2)).and(IO_SOURCE.IS_SAFE.eq(0)));
        getUserRecentExplorerWhere(where, paramMap);
        from.groupBy(IO_SOURCE.ID).orderBy(IO_SOURCE.SORT.desc(), IO_SOURCE.IS_FOLDER.desc(), DSL.field((String) paramMap.get("sortField") + " " + (String) paramMap.get("sortType")), IO_SOURCE_EVENT.ID.asc());
        Integer startIndex = (Integer) paramMap.get("startIndex");
        Integer pageSize = (Integer) paramMap.get("pageSize");
        if (startIndex >= 0 && pageSize != null && pageSize != 0) {
            from.limit(startIndex,pageSize);
        }
        List<HomeExplorerVO> list = from.fetchInto(HomeExplorerVO.class);
        if (!CollectionUtils.isEmpty(list)){
            List<Long> fidList = list.stream().map(HomeExplorerVO::getFileID).collect(Collectors.toList());
            List<HomeExplorerVO> fileMetaList = getUserRecentExplorerFileMetaList(fidList, tenantId);
            if (!CollectionUtils.isEmpty(fileMetaList)){
                Map<Long, String> metaMap = fileMetaList.stream().collect(Collectors.toMap(HomeExplorerVO::getFileID, HomeExplorerVO::getValue, (v1, v2) -> v2));
                for (HomeExplorerVO vo : list){
                    if (!ObjectUtils.isEmpty(metaMap) && metaMap.containsKey(vo.getFileID())){
                        vo.setValue(metaMap.get(vo.getFileID()));
                    }
                }
            }
        }
        return list;
    }

    public List<HomeExplorerVO> getUserRecentExplorerFileMetaList(List<Long> fidList, Long tenantId) {
        if (CollectionUtils.isEmpty(fidList)){
            return new ArrayList<>();
        }
        SelectConditionStep<Record2<Long,String>> from = context.select(IO_FILE_META.FILE_ID.as("fileID")
                  , IO_FILE_META.VALUE_TEXT.as("value"))
                .from(IO_FILE_META).where(IO_FILE_META.TENANT_ID.eq(tenantId).and(IO_FILE_META.FILE_ID.in(fidList)));
        return from.fetchInto(HomeExplorerVO.class);
    }


    @Override
    public Long getUserRecentExplorerCount(Map<String, Object> paramMap) {
        SelectJoinStep<Record1<Integer>> from = context.select(DSL.countDistinct(IO_SOURCE_EVENT.ID))
                .from(IO_SOURCE_EVENT);
        from.innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_EVENT.SOURCE_ID));
        Long userID = (Long) paramMap.get("userID");
        SelectConditionStep where = from.where(IO_SOURCE_EVENT.USER_ID.eq(userID).and(IO_SOURCE_EVENT.TYPE.in("create","edit"))
                .and(IO_SOURCE.IS_DELETE.eq(0)).and(IO_SOURCE.IS_FOLDER.eq(0)).and(IO_SOURCE.TARGET_TYPE.in(1,2)).and(IO_SOURCE.IS_SAFE.eq(0)));
        getUserRecentExplorerWhere(where, paramMap);
        return (Long) where.fetchOneInto(Long.class);
    }
    private void getUserRecentExplorerWhere(SelectConditionStep where, Map<String, Object> paramMap) {
        String keyword = (String) paramMap.get("keyword");
        if (keyword != null) {
            where.and(IO_SOURCE.NAME.like(DSL.concat("%", keyword, "%")).or(IO_SOURCE.NAME_PINYIN.like(DSL.concat("%", keyword, "%")))
                    .or(IO_SOURCE.NAME_PINYIN_SIMPLE.like(DSL.concat("%", keyword, "%"))));
        }
    }
    @Override
    public List<HomeExplorerVO> getSourceDescList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE_META.SOURCE_ID.as("sourceID"), IO_SOURCE_META.VALUE_TEXT.as("description"))
                .from(IO_SOURCE_META)
                .where(IO_SOURCE_META.KEY_STRING.eq("desc"))
                .and(IO_SOURCE_META.SOURCE_ID.in(list))
                .fetchInto(HomeExplorerVO.class);
    }

    @Override
    public int createDir(IOSource source) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(IO_SOURCE)
                .set(IO_SOURCE.SOURCE_HASH, DSL.ifnull(source.getSourceHash(), ""))
                .set(IO_SOURCE.TARGET_TYPE, source.getTargetType())
                .set(IO_SOURCE.TARGET_ID, source.getTargetId())
                .set(IO_SOURCE.CREATE_USER, source.getCreateUser())
                .set(IO_SOURCE.MODIFY_USER, source.getModifyUser())
                .set(IO_SOURCE.IS_FOLDER, source.getIsFolder())
                .set(IO_SOURCE.NAME, source.getName())
                .set(IO_SOURCE.FILE_TYPE, source.getFileType())
                .set(IO_SOURCE.PARENT_ID, source.getParentId())
                .set(IO_SOURCE.PARENT_LEVEL, source.getParentLevel())
                .set(IO_SOURCE.FILE_ID, source.getFileId())
                .set(IO_SOURCE.IS_DELETE, 0)
                .set(IO_SOURCE.SIZE, 0L)
                .set(IO_SOURCE.CREATE_TIME, now)
                .set(IO_SOURCE.MODIFY_TIME, now)
                .set(IO_SOURCE.VIEW_TIME, now)
                .set(IO_SOURCE.STORAGE_ID, DSL.ifnull(source.getStorageId(), 0))
                .set(IO_SOURCE.TENANT_ID, source.getTenantId())
                .set(IO_SOURCE.NAME_PINYIN, source.getNamePinyin())
                .set(IO_SOURCE.NAME_PINYIN_SIMPLE, source.getNamePinyinSimple())
                .set(IO_SOURCE.IS_SAFE, DSL.ifnull(source.getIsSafe(), 0))
                .returning(IO_SOURCE.ID).fetchOne().getId();
        source.setId(id);
        return 1;
    }

    @Override
    public int createDirectory(AddSubCloudDirectoryDTO source) {
        Long tenantId = tenantUtil.getTenantIdByServerName();
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(IO_SOURCE)
                .set(IO_SOURCE.SOURCE_HASH, "")
                .set(IO_SOURCE.TARGET_TYPE, source.getTargetType())
                .set(IO_SOURCE.TARGET_ID, source.getTargetID())
                .set(IO_SOURCE.CREATE_USER, source.getCreateUser())
                .set(IO_SOURCE.MODIFY_USER, source.getModifyUser())
                .set(IO_SOURCE.IS_FOLDER, source.getIsFolder())
                .set(IO_SOURCE.NAME, source.getName())
                .set(IO_SOURCE.FILE_TYPE, source.getFileType())
                .set(IO_SOURCE.PARENT_ID, source.getParentID())
                .set(IO_SOURCE.PARENT_LEVEL, source.getParentLevel())
                .set(IO_SOURCE.FILE_ID, source.getFileID())
                .set(IO_SOURCE.IS_DELETE, 0)
                .set(IO_SOURCE.SIZE, 0L)
                .set(IO_SOURCE.CREATE_TIME, now)
                .set(IO_SOURCE.MODIFY_TIME, now)
                .set(IO_SOURCE.VIEW_TIME, now)
                .set(IO_SOURCE.STORAGE_ID, DSL.ifnull(source.getStorageID(), 0))
                .set(IO_SOURCE.TENANT_ID, tenantId)
                .set(IO_SOURCE.NAME_PINYIN, source.getNamePinyin())
                .set(IO_SOURCE.NAME_PINYIN_SIMPLE, source.getNamePinyinSimple())
                .set(IO_SOURCE.IS_SAFE, DSL.ifnull(source.getIsSafe(), 0))
                .returning(IO_SOURCE.ID).fetchOne().getId();
        source.setSourceID(id);
        return 1;
    }

    @Override
    public HomeFileDetailVO getFileDetail(Long fileID) {
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.SIZE, IO_FILE.IO_TYPE, IO_FILE.PATH, IO_FILE.HASH_SIMPLE,
                        IO_FILE.HASH_MD5, IO_FILE.LINK_COUNT, IO_FILE.CREATE_TIME, IO_FILE.MODIFY_TIME)
                .from(IO_FILE)
                .where(IO_FILE.ID.eq(fileID))
                .fetchOneInto(HomeFileDetailVO.class);
    }

    @Override
    public HomeExplorerVO getHomeSpace(Long targetID, Long parentID) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TARGET_TYPE, IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME, IO_SOURCE.MODIFY_TIME, IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE).where(IO_SOURCE.TARGET_TYPE.eq(1).and(IO_SOURCE.TARGET_ID.eq(targetID)).and(IO_SOURCE.PARENT_ID.eq(parentID)).and(IO_SOURCE.IS_FOLDER.eq(1)).and(IO_SOURCE.IS_DELETE.eq(0)))
                .orderBy(IO_SOURCE.CREATE_TIME.asc()).limit(1).fetchOneInto(HomeExplorerVO.class);
    }

    @Override
    public List<HomeExplorerVO> getUserSpace(Long userID, Long groupID) {
        SelectConditionStep<Record8<String, Long, String, Double, Long, Double, Long, Double>>
                selectConditionStep = context.select(DSL.val("user").as("sourceType"), DSL.val(0L).as("groupID"), DSL.val("").as("groupName"),
                        DSL.val(0.0).as("sizeMax"), DSL.val(0L).as("sizeUse"), USERS.SIZE_MAX.as("userSizeMax"), USERS.SIZE_USE.as("userSizeUse"),
                        ROLE.IGNORE_FILE_SIZE)
                .from(USERS, ROLE)
                .where(ROLE.ID.eq(USERS.ROLE_ID)).and(USERS.ID.eq(userID));

        if (Objects.nonNull(groupID) && groupID > 0) {
            selectConditionStep.union(
                    context.select(DSL.val("group").as("sourceType"), GROUPS.ID.as("groupID"), GROUPS.NAME.as("groupName"),
                                    GROUPS.SIZE_MAX, GROUPS.SIZE_USE, DSL.val(0d).as("userSizeMax"), DSL.val(0L).as("userSizeUse"),
                                    DSL.val(0d).as("ignoreFileSize"))
                            .from(GROUPS)
                            .where(GROUPS.ID.eq(groupID))
            );
        }
        return selectConditionStep.fetchInto(HomeExplorerVO.class);
    }

    @Override
    public List<HomeExplorerVO> getSourceChileCont(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.IS_FOLDER, DSL.count(IO_SOURCE.ID).as("fileCount"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.PARENT_ID.in(list))
                .groupBy(IO_SOURCE.PARENT_ID, IO_SOURCE.IS_FOLDER)
                .fetchInto(HomeExplorerVO.class);
    }


    @Override
    public int updateMemoryList(Map<String, Object> paramMap) {
        Long memory = (Long) paramMap.get("memory");
        List<Long> groupIds = (List<Long>) paramMap.get("list");
        return context.update(Tables.GROUPS)
                .set(GROUPS.SIZE_USE, GROUPS.SIZE_USE.add(memory))
                .where(GROUPS.ID.in(groupIds))
                .execute();
    }

    @Override
    public void updateUserMemory(Map<String, Object> paramMap) {
        Long memory = (Long) paramMap.get("memory");
        Long userID = (Long) paramMap.get("userID");
        context.update(Tables.USERS)
                .set(USERS.SIZE_USE, USERS.SIZE_USE.add(memory))
                .where(USERS.ID.eq(userID))
                .execute();
    }

    @Override
    public void updateSubtractUseUserMemory(Map<String, Object> paramMap) {
        Long memory = (Long) paramMap.get("memory");
        Long userID = (Long) paramMap.get("userID");
        context.update(Tables.USERS)
                .set(USERS.SIZE_USE, USERS.SIZE_USE.minus(memory))
                .where(USERS.ID.eq(userID))
                .execute();
    }

    @Override
    public void batchUpdateGroupMemoryList(List<GroupSizeVo> list) {
        // TODO batchUpdateGroupMemoryList 需要测试是否正确
         /*context.update(GROUPS).set(GROUPS.SIZE_USE,USERS.SIZE_USE.add(DSL.case_(GROUPS.ID).mapValues(
                    list.stream().collect(Collectors.toMap(GroupSizeVo::getGroupID, GroupSizeVo::getSizeUse))
            ))).where(GROUPS.ID.in(list.stream().map(GroupSizeVo::getGroupID).collect(Collectors.toList())))
                    .execute();*/

        List<UpdateConditionStep<GroupsRecord>> updates = new ArrayList<>();
        for (GroupSizeVo vo : list) {
            updates.add(context.update(GROUPS)
                    .set(GROUPS.SIZE_USE, GROUPS.SIZE_USE.add(vo.getSizeUse()))
                    .where(GROUPS.ID.eq(vo.getGroupID())));
        }
        context.batch(updates).execute();
    }

    @Override
    public HomeExplorerVO getOneSourceInfo(Long sourceID) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_TYPE, IO_SOURCE.NAME, IO_SOURCE.SOURCE_HASH)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(HomeExplorerVO.class);
    }

    @Override
    public List<HomeExplorerVO> getUserGroupSourceList(Long userID) {
        return context.select(USER_GROUP.USER_ID.as("userID"), GROUPS.ID.as("groupID"), IO_SOURCE.NAME, IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, GROUPS.SIZE_MAX, IO_SOURCE.SIZE.as("sizeUse"),
                        USER_GROUP.AUTH_ID.as("authID"), ROLE.AUTH, IO_SOURCE.ID.as("sourceID"), IO_SOURCE.CREATE_TIME, IO_SOURCE.CREATE_USER, IO_SOURCE.MODIFY_TIME, IO_SOURCE.MODIFY_USER,
                        ROLE.ROLE_NAME.as("authName"), ROLE.LABEL
                )
                .from(USER_GROUP)
                .innerJoin(GROUPS).on(USER_GROUP.GROUP_ID.eq(GROUPS.ID))
                .innerJoin(GROUP_SOURCE).on(USER_GROUP.GROUP_ID.eq(GROUP_SOURCE.GROUP_ID))
                .innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(GROUP_SOURCE.SOURCE_ID))
                .innerJoin(ROLE).on(ROLE.ID.eq(USER_GROUP.AUTH_ID))
                .where(USER_GROUP.USER_ID.eq(userID).and(GROUPS.STATUS.eq(1)))
                .orderBy(IO_SOURCE.CREATE_TIME.asc()).fetchInto(HomeExplorerVO.class);
    }

    @Override
    public List<HomeExplorerVO> getSystemGroupSourceList(Long tenantId) {
        return context.select(GROUPS.ID, IO_SOURCE.NAME, IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, GROUPS.SIZE_MAX, IO_SOURCE.SIZE.as("sizeUse"), DSL.field("0").as("authID"),
                        DSL.field("''").as("auth"), IO_SOURCE.ID.as("sourceID"), IO_SOURCE.CREATE_TIME, IO_SOURCE.CREATE_USER, IO_SOURCE.MODIFY_TIME, IO_SOURCE.MODIFY_USER
                )
                .from(GROUPS)
                .innerJoin(GROUP_SOURCE).on(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(GROUP_SOURCE.SOURCE_ID))
                .where(GROUPS.PARENT_ID.eq(0L).and(GROUPS.STATUS.eq(1)).and(GROUPS.TENANT_ID.eq(tenantId)))
                .orderBy(IO_SOURCE.CREATE_TIME.asc()).fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Integer getUserIdentityInfo(Long userID) {
        return context.select(ROLE.ADMINISTRATOR).from(USERS).join(ROLE)
                .on(USERS.ROLE_ID.eq(ROLE.ID).and(ROLE.STATUS.eq(1)).and(ROLE.ENABLE.eq(1)))
                .where(USERS.ID.eq(userID).and(USERS.STATUS.eq(1))).fetchOneInto(Integer.class);

    }


    @Override
    public List<HomeExplorerVO> getParentNameList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.NAME.as("parentName"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Integer checkUserRecycleExplorer(Long userID) {
        return context.select(DSL.count(IO_SOURCE_RECYCLE.ID))
                .from(IO_SOURCE_RECYCLE)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_RECYCLE.SOURCE_ID))
                .where(IO_SOURCE_RECYCLE.USER_ID.eq(userID))
                .and(IO_SOURCE.IS_DELETE.eq(1))
                .fetchOneInto(Integer.class);
    }

    @Override
    public String getGroupParentLevel(Long groupID) {
        return context.select(GROUPS.PARENT_LEVEL)
                .from(GROUPS)
                .where(GROUPS.ID.eq(groupID))
                .fetchOne(GROUPS.PARENT_LEVEL);
    }

    @Override
    public List<String> getGroupParentLevelList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(DSL.concat(GROUPS.PARENT_LEVEL,GROUPS.ID).as("parentLevel"))
                .from(GROUPS, GROUP_SOURCE)
                .where(GROUPS.ID.eq(GROUP_SOURCE.GROUP_ID))
                .and(GROUP_SOURCE.SOURCE_ID.in(list))
                .fetchInto(String.class);
    }

    @Override
    public List<HomeExplorerVO> getFolderAndImgAndAudioHomeExplorer(List<Long> list, Integer lnkAudio) {
        if (Objects.equals(lnkAudio, 1)) {
            return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, IO_FILE.ID.as("fileID"),
                            IO_FILE.PATH, IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5, IO_FILE.IS_H264_PREVIEW.as("isH264Preview"),
                            IO_SOURCE.NAME)
                    .from(IO_SOURCE)
                    .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                    .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID)).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                    .where(IO_SOURCE.ID.in(list))
                    .fetchInto(HomeExplorerVO.class);
        } else {
            return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, IO_FILE.ID.as("fileID"),
                            IO_FILE.PATH, DSL.val("").as("value"), IO_FILE.HASH_MD5, DSL.val(0).as("isH264Preview"),
                            IO_SOURCE.NAME)
                    .from(IO_SOURCE)
                    .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                    .where(IO_SOURCE.ID.in(list))
                    .fetchInto(HomeExplorerVO.class);
        }

    }
    @Override
    public List<HomeExplorerVO> getImgByFolderList(List<String> fileTypeList, Long parentID){

        SelectConditionStep where = context.select(IO_SOURCE.ID.as("sourceID"),IO_SOURCE.SOURCE_HASH,IO_SOURCE.TARGET_TYPE,IO_SOURCE.TARGET_ID.as("targetID")
                ,IO_SOURCE.CREATE_USER,IO_SOURCE.MODIFY_USER,IO_SOURCE.IS_FOLDER,IO_SOURCE.NAME,IO_SOURCE.FILE_TYPE
                , IO_SOURCE.PARENT_ID.as("parentID"),IO_SOURCE.SORT, IO_SOURCE.PARENT_LEVEL, IO_FILE.ID.as("fileID"),IO_SOURCE.IS_DELETE,IO_SOURCE.SIZE,IO_SOURCE.CREATE_TIME
                ,IO_SOURCE.MODIFY_TIME,IO_SOURCE.VIEW_TIME,IO_FILE.PATH,IO_FILE.HASH_MD5,IO_FILE.IS_PREVIEW.as("preview"))
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.IS_DELETE.eq(0));
        if (parentID != null){
            where.and(IO_SOURCE.PARENT_ID.eq(parentID));
        }
        where.and(IO_SOURCE.IS_FOLDER.eq(0)).and(IO_SOURCE.FILE_TYPE.in(fileTypeList));
        where.orderBy(IO_SOURCE.SORT.desc(), IO_SOURCE.NAME_PINYIN.asc(), IO_SOURCE.ID.asc());
        return where.fetchInto(HomeExplorerVO.class);
    }
    @Override
    public HomeExplorerVO getHomeExplorerOne(Long sourceId) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH.as("sourceHash"), IO_SOURCE.TARGET_TYPE.as("targetType"), IO_SOURCE.TARGET_ID.as("targetID")
                , IO_SOURCE.CREATE_USER.as("createUser"), IO_SOURCE.MODIFY_USER.as("modifyUser"),
                IO_SOURCE.IS_FOLDER.as("isFolder"), IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.SORT,
                IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.IS_DELETE.as("isDelete"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME.as("createTime")
                , IO_SOURCE.MODIFY_TIME.as("modifyTime"), IO_SOURCE.VIEW_TIME.as("viewTime"),
                IO_FILE.PATH, IO_FILE_META.VALUE_TEXT.as("value"), IO_FILE.HASH_MD5.as("hashMd5"), IO_SOURCE.DESCRIPTION, IO_FILE.IS_PREVIEW.as("preview"),
                IO_FILE.CREATE_TIME.as("fileCreateTime"), IO_FILE.IS_EXIST_FILE.as("isExistFile"), IO_FILE.IS_M3U8.as("isM3u8"), IO_SOURCE.CAN_SHARE.as("canShare")
                ,IO_SOURCE.COVER_ID,IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE).leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .where(IO_SOURCE.ID.eq(sourceId)).fetchOneInto(HomeExplorerVO.class);
    }
}
