package com.svnlan.home.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.home.vo.ParentPathDisplayVo;
import com.svnlan.jooq.tables.records.IoFileRecord;
import com.svnlan.jooq.tables.records.IoSourceRecord;
import com.svnlan.tools.JSONObjectRecordMapper;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.svnlan.jooq.Tables.IO_FILE_META;
import static com.svnlan.jooq.tables.IoFile.IO_FILE;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;

@Repository
public class IoSourceDaoImpl implements IoSourceDao {

    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private JSONObjectRecordMapper jsonObjectRecordMapper;

    @Override
    public int insert(IOSource source) {
        int isSafe = this.getSourceIsSafe(source.getTargetId(), source.getParentLevel());
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(IO_SOURCE)
                .set(IO_SOURCE.SOURCE_HASH, DSL.ifnull(source.getSourceHash(), ""))
                .set(IO_SOURCE.TYPE, DSL.ifnull(source.getType(), 0))
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
                .set(IO_SOURCE.SIZE, DSL.ifnull(source.getSize(), 0L))
                .set(IO_SOURCE.CREATE_TIME, now)
                .set(IO_SOURCE.MODIFY_TIME, now)
                .set(IO_SOURCE.VIEW_TIME, now)
                .set(IO_SOURCE.CONVERT_SIZE, DSL.ifnull(source.getConvertSize(), 0L))
                .set(IO_SOURCE.THUMB_SIZE, DSL.ifnull(source.getThumbSize(), 0L))
                .set(IO_SOURCE.STORAGE_ID, DSL.ifnull(source.getStorageId(), 0))
                .set(IO_SOURCE.TENANT_ID, ObjectUtils.isEmpty(source.getTenantId()) ? tenantUtil.getTenantIdByServerName() : source.getTenantId())
                .set(IO_SOURCE.NAME_PINYIN,DSL.ifnull(source.getNamePinyin(), ChinesUtil.getPingYin(source.getName())))
                .set(IO_SOURCE.NAME_PINYIN_SIMPLE,DSL.ifnull(source.getNamePinyinSimple(), ChinesUtil.getFirstSpell(source.getName())))
                .set(IO_SOURCE.DING_DENTRY_ID, ObjectUtils.isEmpty(source.getDingDentryId()) ? null : source.getDingDentryId())
                .set(IO_SOURCE.DING_UNION_ID, ObjectUtils.isEmpty(source.getDingUnionId()) ? null : source.getDingUnionId())
                .set(IO_SOURCE.DING_SPACE_ID, ObjectUtils.isEmpty(source.getDingSpaceId()) ? null : source.getDingSpaceId())
                .set(IO_SOURCE.IS_SAFE, isSafe)
                .returning(IO_SOURCE.ID).fetchOne().getId();
        source.setId(id);
        return 1;
    }

    @Override
    public int batchInsert(List<IOSource> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        LocalDateTime now = LocalDateTime.now();
        Long paramTenantId = list.get(0).getTenantId();
        Long tenantId = ObjectUtils.isEmpty(paramTenantId) ? tenantUtil.getTenantIdByServerName() : paramTenantId;


        Long targetId = list.get(0).getTargetId();
        String parentLevel = list.get(0).getParentLevel();
        int isSafe = this.getSourceIsSafe(targetId, parentLevel);

        InsertValuesStepN step = context.insertInto(IO_SOURCE).columns(IO_SOURCE.SOURCE_HASH,IO_SOURCE.TYPE,IO_SOURCE.TARGET_TYPE,IO_SOURCE.TARGET_ID,IO_SOURCE.CREATE_USER,IO_SOURCE.MODIFY_USER,IO_SOURCE.IS_FOLDER
                ,IO_SOURCE.NAME,IO_SOURCE.FILE_TYPE,IO_SOURCE.PARENT_ID,IO_SOURCE.PARENT_LEVEL,IO_SOURCE.FILE_ID,IO_SOURCE.IS_DELETE,IO_SOURCE.SIZE,IO_SOURCE.CREATE_TIME,IO_SOURCE.MODIFY_TIME
                ,IO_SOURCE.VIEW_TIME,IO_SOURCE.CONVERT_SIZE,IO_SOURCE.THUMB_SIZE,IO_SOURCE.STORAGE_ID,IO_SOURCE.TENANT_ID,IO_SOURCE.NAME_PINYIN,IO_SOURCE.NAME_PINYIN_SIMPLE,IO_SOURCE.IS_SAFE);

        for (IOSource ioSource : list) {

            step.values(DSL.ifnull(ioSource.getSourceHash(), ""),DSL.ifnull(ioSource.getType(), 0),ioSource.getTargetType(),ioSource.getTargetId()
                    ,ioSource.getCreateUser(),ioSource.getModifyUser(),ioSource.getIsFolder(),ioSource.getName(),ioSource.getFileType(),ioSource.getParentId(),ioSource.getParentLevel()
                    ,ioSource.getFileId(),0,DSL.ifnull(ioSource.getSize(), 0L),now,now,now,DSL.ifnull(ioSource.getConvertSize(), 0L),DSL.ifnull(ioSource.getThumbSize(), 0L)
                    ,DSL.ifnull(ioSource.getStorageId(), 0),tenantId ,DSL.ifnull(ioSource.getNamePinyin(), ChinesUtil.getPingYin(ioSource.getName()))
                    ,DSL.ifnull(ioSource.getNamePinyinSimple(), ChinesUtil.getFirstSpell(ioSource.getName())), isSafe
            );
        }
        List<Long> idList = step.returning(IO_SOURCE.ID).fetch().getValues(IO_SOURCE.ID);
        if (idList.size() == list.size()){
            int i = 0;
            for (IOSource source : list) {
                source.setId(idList.get(i));
                i++;
            }
            return idList.size();
        }
        return 0;
    }

    @Override
    public CommonSource getSourceInfo(Long sourceID) {
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5, IO_FILE.SIZE, IO_FILE.PATH, IO_SOURCE.FILE_TYPE,
                        IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_TYPE, IO_SOURCE.NAME.as("sourceName"),
                        IO_SOURCE.CREATE_USER.as("userID"), IO_SOURCE.CAN_SHARE, IO_FILE.IS_H264_PREVIEW, IO_FILE.CONVERT_SIZE, IO_FILE.THUMB_SIZE,
                        IO_SOURCE.CONVERT_SIZE, IO_SOURCE.THUMB_SIZE, IO_SOURCE.TENANT_ID, IO_FILE.IS_EXIST_FILE, IO_SOURCE.IS_DELETE)
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public List<CommonSource> getSourceInfoList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5, IO_FILE.SIZE, IO_FILE.PATH, IO_SOURCE.FILE_TYPE,
                        IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME.as("sourceName"), IO_SOURCE.TYPE,
                        IO_FILE.CONVERT_SIZE.as("fileConvertSize"), IO_FILE.THUMB_SIZE.as("fileThumbSize"),
                        IO_SOURCE.CONVERT_SIZE, IO_SOURCE.THUMB_SIZE)
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(CommonSource.class);
    }

    @Override
    public List<IOSource> copySourceList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID, IO_SOURCE.SOURCE_HASH, IO_SOURCE.TYPE, IO_SOURCE.TARGET_TYPE, IO_SOURCE.IS_FOLDER,
                        IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID, IO_SOURCE.SIZE, IO_SOURCE.PARENT_ID, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_ID,
                        IO_SOURCE.CREATE_TIME, IO_SOURCE.CONVERT_SIZE, IO_SOURCE.THUMB_SIZE, IO_SOURCE.STORAGE_ID, IO_SOURCE.TENANT_ID, IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(IOSource.class);
    }

    @Override
    public List<IOSource> copySourceListByLevel(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID, IO_SOURCE.SOURCE_HASH, IO_SOURCE.TYPE, IO_SOURCE.TARGET_TYPE, IO_SOURCE.IS_FOLDER,
                        IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID, IO_SOURCE.SIZE, IO_SOURCE.PARENT_ID, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_ID,
                        IO_SOURCE.CREATE_TIME, IO_SOURCE.CONVERT_SIZE, IO_SOURCE.THUMB_SIZE, IO_SOURCE.STORAGE_ID, IO_SOURCE.TENANT_ID, IO_SOURCE.IS_SAFE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(buildParentLevelCondition(list))
                .fetchInto(IOSource.class);
    }

    @Override
    public int deleteDirOrFile(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.IS_DELETE, 1)
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.ID.in(list))
                .execute();
    }

    @Override
    public int deleteSourceByParent(List<String> list, Long tenantId) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.IS_DELETE, 1)
                .where(IO_SOURCE.TENANT_ID.eq(tenantId).and(IO_SOURCE.IS_DELETE.eq(0)))
                .and(buildParentLevelCondition(list))
                .execute();
    }

    @Override
    public int restoreSourceByParent(List<String> list, Long userID) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.IS_DELETE, 0)
                .where(IO_SOURCE.IS_DELETE.eq(1))
                .and(buildParentLevelCondition(list))
                .execute();
    }

    @Override
    public void batchFileRename(List<SourceOpDto> list, Long userID) {
        // TODO  batchFileRename 后续更新为一行

        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (SourceOpDto dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.NAME, dto.getName()).set(IO_SOURCE.NAME_PINYIN, dto.getNamePinyin()).set(IO_SOURCE.NAME_PINYIN_SIMPLE, dto.getNamePinyinSimple())

                    .where(IO_SOURCE.ID.eq(dto.getSourceID())));
        }
        context.batch(updates).execute();
    }

    @Override
    public void batchUpdateParent(List<IOSource> list) {
        // TODO case batchUpdateParent 后续更新为一行
        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.PARENT_ID, dto.getParentId()).set(IO_SOURCE.TARGET_TYPE, dto.getTargetType())
                    .set(IO_SOURCE.PARENT_LEVEL, dto.getParentLevel())
                    .where(IO_SOURCE.ID.eq(dto.getId())));
        }
        context.batch(updates).execute();
    }

    @Override
    public int restoreDirOrFile(List<Long> list, Long userID) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.IS_DELETE, 0)
                .where(IO_SOURCE.IS_DELETE.eq(1))
                .and(IO_SOURCE.ID.in(list))
                .execute();
    }
    @Override
    public void restoreDirOrFileAndName(List<IOSource> list, Long userID) {
        if (CollectionUtils.isEmpty(list)) {
            return ;
        }

        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.IS_DELETE, 0)
                    .set(IO_SOURCE.NAME, dto.getName())
                    .where(IO_SOURCE.ID.eq(dto.getId())).and(IO_SOURCE.IS_DELETE.eq(1)));
        }
        context.batch(updates).execute();
    }

    @Override
    public int removeUserSource(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.delete(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(1))
                .and(IO_SOURCE.ID.in(list))
                .execute();
    }

    @Override
    public int removeUserSourceByParent(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.delete(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(1))
                .and(buildParentLevelCondition(list))
                .execute();
    }

    @Override
    public List<Long> getFileIDBySourceID(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.FILE_ID).from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(list))
                .fetch(IO_SOURCE.FILE_ID);
    }

    @Override
    public List<IOSourceVo> getFileCountBySourceID(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.FILE_ID, DSL.count(IO_SOURCE.FILE_ID).as("fileCount")).from(IO_SOURCE)
                .where(IO_SOURCE.FILE_ID.in(list))
                .groupBy(IO_SOURCE.FILE_ID)
                .fetchInto(IOSourceVo.class);
    }

    @Override
    public int updateSourceMemoryList(List<Long> list, Long memory) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.SIZE, IO_SOURCE.SIZE.add(memory))
                .where(IO_SOURCE.ID.in(list))
                .execute();
    }

    @Override
    public int subtractSourceMemoryList(List<Long> list, Long memory) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.SIZE, IO_SOURCE.SIZE.minus(memory))
                .where(IO_SOURCE.ID.in(list))
                .execute();
    }

    @Override
    public void batchUpdateSourceMemoryList(List<IOSource> list) {
        // TODO case batchUpdateSourceMemoryList  后续更新为一行
        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.SIZE, IO_SOURCE.SIZE.add(dto.getSize()))
                    .where(IO_SOURCE.ID.eq(dto.getId())));
        }
        context.batch(updates).execute();

    }

    @Override
    public void batchSubtractSourceMemoryList(List<IOSource> list) {
        // TODO case batchSubtractSourceMemoryList 后续更新为一行
        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.SIZE, IO_SOURCE.SIZE.subtract(dto.getSize()))
                    .where(IO_SOURCE.ID.eq(dto.getId())));
        }
        context.batch(updates).execute();
    }

    @Override
    public Integer getMaxSort(Long parentID) {
        return context.select(DSL.max(IO_SOURCE.SORT))
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.PARENT_ID.eq(parentID))
                .fetchOneInto(Integer.class);
    }

    @Override
    public int updateSort(Long sourceID, Integer sort) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.SORT, sort)
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }

    @Override
    public int updateSourceInfo(IOSource source) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.MODIFY_USER, source.getModifyUser())
                .set(IO_SOURCE.SIZE, source.getSize())
                .set(IO_SOURCE.FILE_ID, source.getFileId())
                .set(IO_SOURCE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_SOURCE.ID.eq(source.getId()))
                .execute();
    }

    @Override
    public List<IOSourceVo> copySourcePathList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID,
                        IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME, IO_FILE.PATH, IO_FILE.NAME.as("fileName"), IO_SOURCE.PARENT_LEVEL
                , IO_FILE.SIZE.as("fileSize"))
                .from(IO_SOURCE).leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(IOSourceVo.class);
    }

    @Override
    public List<IOSourceVo> copySourcePathListByLevel(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID,
                        IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME, IO_FILE.PATH, IO_FILE.NAME.as("fileName"), IO_SOURCE.PARENT_LEVEL, IO_FILE.SIZE.as("fileSize"))
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(buildParentLevelCondition(list))
                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(IOSourceVo.class);
    }

    public Condition buildParentLevelCondition(List<String> list) {
        Condition condition = DSL.falseCondition();
        for (String keyword : list) {
            condition = condition.or(IO_SOURCE.PARENT_LEVEL.like(keyword + "%"));
        }
        return condition;
    }

    @Override
    public List<CommonSource> getSourceFileInfoList(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.SOURCE_HASH, IO_SOURCE.TARGET_TYPE, IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME,
                IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID, IO_SOURCE.SIZE, IO_SOURCE.PARENT_ID, IO_SOURCE.PARENT_LEVEL
                , IO_SOURCE.TARGET_ID, IO_SOURCE.CREATE_TIME, IO_FILE.NAME.as("fileName"), IO_FILE.HASH_MD5, IO_FILE.PATH)
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(CommonSource.class);
    }

    @Override
    public int updateSourceModifyUser(IOSource source) {
        UpdateSetMoreStep<IoSourceRecord> setMoreStep = context.update(IO_SOURCE)
                .set(IO_SOURCE.MODIFY_USER, source.getModifyUser())
                .set(IO_SOURCE.MODIFY_TIME, LocalDateTime.now());
        if (Objects.nonNull(source.getSize())) {
            setMoreStep.set(IO_SOURCE.SIZE, source.getSize());
        }
        return setMoreStep.where(IO_SOURCE.ID.eq(source.getId()))
                .execute();
    }

    @Override
    public int updateFileSize(IOSource source) {
        UpdateSetMoreStep<IoFileRecord> setMoreStep = context.update(IO_FILE)
                .set(IO_FILE.SIZE, source.getSize());
        if (Objects.nonNull(source.getHashMd5())) {
            setMoreStep.set(IO_FILE.HASH_MD5, source.getHashMd5());
        }
        return setMoreStep.where(IO_FILE.ID.eq(source.getFileId()))
                .execute();
    }

    @Override
    public List<String> getSourceNameList(Long sourceID) {
        return context.select(IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.PARENT_ID.eq(sourceID))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetch(IO_SOURCE.NAME);
    }

    @Override
    public List<CommonSource> checkSourceNameList(Long sourceID) {
        return context.select(IO_SOURCE.NAME, IO_SOURCE.ID.as("sourceID"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.PARENT_ID.eq(sourceID))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchInto(CommonSource.class);
    }

    @Override
    public List<JSONObject> fileCount() {
        return context.select(DSL.count(IO_SOURCE.ID).as("count"), DSL.val("fileCount").as(IO_SOURCE.TYPE))
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .unionAll(
                        context.select(DSL.count(IO_SOURCE.ID).as("count"), DSL.val("directoryCount").as(IO_SOURCE.TYPE))
                                .from(IO_SOURCE)
                                .where(IO_SOURCE.IS_DELETE.eq(0))
                                .and(IO_SOURCE.IS_FOLDER.eq(1))
                                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).unionAll(
                        context.select(DSL.count(IO_SOURCE.ID).as("count"), DSL.val("videoCount").as(IO_SOURCE.TYPE))
                                .from(IO_SOURCE)
                                .where(IO_SOURCE.IS_DELETE.eq(0))
                                .and(IO_SOURCE.IS_FOLDER.eq(0))
                                .and(IO_SOURCE.TYPE.eq(4))
                                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                ).fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<JSONObject> getFileTypeProportion() {
        return context.select(DSL.count(IO_SOURCE.ID).as("count"), IO_SOURCE.TYPE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.TYPE.isNotNull())
                .and(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .groupBy(IO_SOURCE.TYPE)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<IOSourceVo> getFileCountByPath(List<String> list) {
        return context.select(IO_FILE.PATH, DSL.count(IO_FILE.ID).as("fileCount"))
                .from(IO_FILE)
                .where(IO_FILE.PATH.in(list))
                .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .groupBy(IO_FILE.PATH)
                .fetchInto(IOSourceVo.class);
    }

    @Override
    public IOSource getSourceNameBySourceId(Long sourceID) {
        return context.select(IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(IOSource.class);
    }

    @Override
    public List<JSONObject> getUserDirectoryAndFile(Long userId, Long parentId) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TYPE, IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.PARENT_LEVEL,
                        IO_SOURCE.FILE_ID, DSL.inline(((String) null)).as("path"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.TARGET_ID.eq(userId))
                .and(IO_SOURCE.PARENT_ID.eq(parentId))
                .and(IO_SOURCE.TARGET_TYPE.eq(1))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.IS_FOLDER.eq(1))
                .unionAll(
                        context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TYPE, IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.PARENT_LEVEL,
                                        IO_SOURCE.FILE_ID, IO_FILE.PATH)
                                .from(IO_SOURCE)
                                .leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                                .where(IO_SOURCE.TARGET_ID.eq(userId))
                                .and(IO_SOURCE.PARENT_ID.eq(parentId))
                                .and(IO_SOURCE.TARGET_TYPE.eq(1))
                                .and(IO_SOURCE.IS_DELETE.eq(0))
                                .and(IO_SOURCE.IS_FOLDER.eq(0))
                                .and(IO_FILE.IS_EXIST_FILE.eq(1))
                ).fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<IOSourceVo> getSourceInfoByParentIdAndUser(Long id, long userId) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.PARENT_ID, IO_SOURCE.PARENT_LEVEL,
                        IO_SOURCE.FILE_ID, IO_SOURCE.MODIFY_TIME, IO_SOURCE.CREATE_TIME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.TARGET_ID.eq(userId))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.PARENT_ID.eq(id))
                .fetchInto(IOSourceVo.class);
    }

    @Override
    public IOSourceVo getUserRootDirectory(Long userId) {
        return context.select(IO_SOURCE.NAME, IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_LEVEL, IO_SOURCE.MODIFY_TIME,
                        IO_SOURCE.CREATE_TIME, IO_SOURCE.TARGET_TYPE, IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.PARENT_ID, IO_SOURCE.IS_FOLDER,
                        IO_SOURCE.STORAGE_ID,IO_SOURCE.TENANT_ID)
                .from(IO_SOURCE)
                .where(IO_SOURCE.TARGET_ID.eq(userId))
                .and(IO_SOURCE.PARENT_ID.eq(0L))
                .and(IO_SOURCE.IS_FOLDER.eq(1))
                .and(IO_SOURCE.TARGET_TYPE.eq(1))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchOneInto(IOSourceVo.class);
    }

    @Override
    public List<IOSourceVo> getSourceByNameAndUserId(String lastPathName, Long userId, Integer isFolder) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE)
                .where(IO_SOURCE.TARGET_ID.eq(userId))
                .and(IO_SOURCE.NAME.eq(lastPathName))
                .and(IO_SOURCE.IS_FOLDER.eq(isFolder))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchInto(IOSourceVo.class);
    }

    @Override
    public List<JSONObject> getFileTypeProportionByUserId(Long userId) {
        return context.select(DSL.count(IO_SOURCE.ID).as("count"), IO_SOURCE.TYPE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.CREATE_USER.eq(userId))
                .and(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TYPE.isNotNull())
                .groupBy(IO_SOURCE.TYPE)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public void batchUpdateParentAndName(List<IOSource> list) {
        // TODO case batchUpdateParentAndName 后续
        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IO_SOURCE)
                    .set(IO_SOURCE.PARENT_ID, dto.getParentId()).set(IO_SOURCE.TARGET_TYPE, dto.getTargetType()).set(IO_SOURCE.IS_DELETE, 0)
                    .set(IO_SOURCE.PARENT_LEVEL, dto.getParentLevel()).set(IO_SOURCE.NAME, dto.getName())
                    .set(IO_SOURCE.NAME_PINYIN, ChinesUtil.getPingYin(dto.getName())).set(IO_SOURCE.NAME_PINYIN_SIMPLE, ChinesUtil.getFirstSpell(dto.getName()))
                    .set(IO_SOURCE.IS_SAFE, dto.getIsSafe())
                    .where(IO_SOURCE.ID.eq(dto.getId())));
        }
        context.batch(updates).execute();
    }

    @Override
    public IOFile getFileContentByNameAndUserId(String fileName, Long userId, Boolean isVideoFile) {
        SelectQuery<Record> selectQuery = context.selectQuery();
        selectQuery.addSelect(IO_FILE.ID.as("fileID"), IO_FILE.PATH, IO_FILE.IS_EXIST_FILE, IO_FILE.IS_M3U8.as("isM3u8"),
                IO_FILE.IS_PREVIEW, IO_FILE.NAME);
        if (Boolean.TRUE.equals(isVideoFile)) {
            selectQuery.addSelect(IO_FILE_META.VALUE_TEXT.as("value"));
        }
        selectQuery.addFrom(IO_FILE);
        selectQuery.addJoin(IO_SOURCE, IO_SOURCE.FILE_ID.eq(IO_FILE.ID));
        if (Boolean.TRUE.equals(isVideoFile)) {
            selectQuery.addJoin(IO_FILE_META, IO_FILE_META.FILE_ID.eq(IO_FILE.ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")));
        }
        selectQuery.addConditions(IO_SOURCE.TARGET_ID.eq(userId).and(IO_FILE.FILE_NAME.eq(fileName)));
        selectQuery.addLimit(1);
        return selectQuery.fetchOneInto(IOFile.class);
    }

    @Override
    public List<String> getDirectoryByParentLevelAndName(String parentLevel, String lastPath) {
        return context.select(IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_FOLDER.eq(1))
                .and(IO_SOURCE.NAME.like(lastPath + "%"))
                .and(IO_SOURCE.PARENT_LEVEL.eq(parentLevel))
                .and(IO_SOURCE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetch(IO_SOURCE.NAME);
    }

    @Override
    public IOSourceVo querySourceVoByParentLevelAndUserIdAndName(String parentLevel, Long userId, String name) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.PARENT_LEVEL, IO_SOURCE.IS_FOLDER, IO_SOURCE.MODIFY_TIME, IO_SOURCE.CREATE_TIME,
                        IO_SOURCE.SIZE, IO_SOURCE.FILE_ID, IO_SOURCE.NAME, IO_SOURCE.TARGET_TYPE, IO_SOURCE.TARGET_ID, IO_SOURCE.PARENT_ID, IO_SOURCE.STORAGE_ID)
                .from(IO_SOURCE)
                .where(IO_SOURCE.TARGET_ID.eq(userId))
                .and(IO_SOURCE.PARENT_LEVEL.eq(parentLevel))
                .and(IO_SOURCE.NAME.eq(name))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchOneInto(IOSourceVo.class);
    }

    @Override
    public List<ParentPathDisplayVo> getParentPathDisplay(List<String> list) {
        // TODO child_query getParentPathDisplay 最新dev已弃用
        return null;
    }

    @Override
    public String getSourceName(Long sourceID) {
        return context.select(IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOne(IO_SOURCE.NAME);
    }

    @Override
    public int updateSourceAddSizeInfo(IOSource source) {
        UpdateSetMoreStep<IoSourceRecord> updateSetMoreStep = context.update(IO_SOURCE)
                .set(IO_SOURCE.MODIFY_USER, source.getModifyUser())
                .set(IO_SOURCE.SIZE, IO_SOURCE.SIZE.add(source.getSize()))
                .set(IO_SOURCE.FILE_ID, source.getFileId());
        if (Objects.nonNull(source.getThumbSize())) {
            updateSetMoreStep.set(IO_SOURCE.THUMB_SIZE, IO_SOURCE.THUMB_SIZE.add(source.getThumbSize()));
        }
        if (Objects.nonNull(source.getConvertSize())  && source.getConvertSize() > 0) {
            updateSetMoreStep.set(IO_SOURCE.CONVERT_SIZE, IO_SOURCE.CONVERT_SIZE.add(source.getConvertSize()));
        }
        if (Objects.nonNull(source.getFileType())) {
            updateSetMoreStep.set(IO_SOURCE.FILE_TYPE, source.getFileType());
        }
        return updateSetMoreStep.where(IO_SOURCE.ID.eq(source.getId()))
                .execute();
    }

    @Override
    public int updateSourceConvertSize(Long sourceID, Long convertSize) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.CONVERT_SIZE, IO_SOURCE.CONVERT_SIZE.add(convertSize))
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }

    @Override
    public int updateSourceThumbSize(Long sourceID, Long thumbSize) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.THUMB_SIZE, IO_SOURCE.THUMB_SIZE.add(thumbSize))
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }

    @Override
    public List<IOSourceVo> getDesktopSourceList(Long parentID, String name) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME, IO_FILE.PATH, IO_FILE.NAME, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.PARENT_ID.as("parentID"))
                .from(IO_SOURCE).leftJoin(IO_FILE).on(IO_SOURCE.FILE_ID.eq(IO_FILE.ID))
                .where(IO_SOURCE.PARENT_ID.eq(parentID).and(IO_SOURCE.NAME.eq(name))).fetchInto(IOSourceVo.class);
    }

    @Override
    public Long getSourceSize(Long sourceID) {
        return context.select(IO_SOURCE.SIZE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOne(IO_SOURCE.SIZE);
    }

    @Override
    public int updateSourceSize(Long sourceID, Long size) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.SIZE, size)
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }

    @Override
    public List<JSONObject> selectFileProportion(Long tenantId) {
        return context.select(IO_SOURCE.FILE_TYPE.as("ft"), DSL.count(IO_SOURCE.ID).as("c"), DSL.sum(IO_SOURCE.SIZE).as("s"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TENANT_ID.eq(tenantId)).groupBy(IO_SOURCE.FILE_TYPE)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<JSONObject> getTargetTypeProportion(Long tenantId) {
        return context.select(IO_SOURCE.TARGET_TYPE.as("ty"), DSL.sum(IO_SOURCE.SIZE).as("s"))
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_FOLDER.eq(0))
                .and(IO_SOURCE.TENANT_ID.eq(tenantId))
                .groupBy(IO_SOURCE.TARGET_TYPE)
                .fetch(jsonObjectRecordMapper);
    }

    @Override
    public List<IOSource> selectBatchIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return context.select()
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(ids))
                .fetchInto(IOSource.class);
    }

    @Override
    public void deleteById(Long id) {
        context.delete(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(id))
                .execute();
    }

    @Override
    public int updateCanShare(Integer canShare, List<Long> needUpdateIds) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.CAN_SHARE, canShare)
                .where(IO_SOURCE.ID.in(needUpdateIds))
                .execute();
    }

    @Override
    public List<IOSource> selectListByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return context.select()
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(ids))
                .fetchInto(IOSource.class);
    }

    @Override
    public IOSource selectById(Long id) {
        return context.select()
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(id))
                .fetchOneInto(IOSource.class);
    }

    @Override
    public List<IOSource> selectSimpleSourceById(List<Long> sourceIdList) {
        return context.select(IO_SOURCE.ID, IO_SOURCE.IS_FOLDER, IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(sourceIdList))
                .fetchInto(IOSource.class);
    }

    // TODO 最新版本dev使用
    @Override
    public List<ParentPathDisplayVo> getParentPathDisplayByIds(List<Long> list){
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.in(list))
                .fetchInto(ParentPathDisplayVo.class);
    }
    @Override
    public int updateSourceDesc(Long sourceID, String description){
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.DESCRIPTION, description)
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }

    @Override
    public IOSource getSourceByName(Long parentId, String name) {
        return context.select(IO_SOURCE.ID, IO_SOURCE.NAME, IO_SOURCE.TARGET_TYPE, IO_SOURCE.PARENT_ID, IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE)
                .where(IO_SOURCE.PARENT_ID.eq(parentId))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .and(IO_SOURCE.NAME.eq(name))
                .fetchOneInto(IOSource.class);
    }


    @Override
    public CommonSource getSourceInfoByDentryId(String dentryId) {
        List<CommonSource> list =  context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5, IO_FILE.SIZE, IO_FILE.PATH, IO_SOURCE.FILE_TYPE,
                IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_TYPE, IO_SOURCE.NAME.as("sourceName"),
                IO_SOURCE.CREATE_USER.as("userID"), IO_SOURCE.CAN_SHARE, IO_FILE.IS_H264_PREVIEW, IO_FILE.CONVERT_SIZE, IO_FILE.THUMB_SIZE,
                IO_SOURCE.CONVERT_SIZE, IO_SOURCE.THUMB_SIZE, IO_SOURCE.TENANT_ID, IO_SOURCE.IS_DELETE)
                .from(IO_SOURCE)
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.DING_DENTRY_ID.eq(dentryId)).orderBy(IO_SOURCE.CREATE_TIME.asc(), IO_SOURCE.ID.asc(),IO_SOURCE.IS_DELETE.asc())
                .fetchInto(CommonSource.class);
        if (!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Long checkSourceIsOwn(Long sourceId, Long userId) {
        return context.select(IO_SOURCE.CREATE_USER)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceId).and(IO_SOURCE.CREATE_USER.eq(userId)))
                .fetchOne(IO_SOURCE.CREATE_USER);
    }

    @Override
    public int updateCoverId(Long sourceID, Long coverId){
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.COVER_ID, coverId)
                .where(IO_SOURCE.ID.eq(sourceID))
                .execute();
    }
    @Override
    public HomeExplorerVO getMySafeBoxSource(Long userId){
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TARGET_TYPE, IO_SOURCE.TARGET_ID.as("targetID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE, IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.SIZE, IO_SOURCE.CREATE_TIME, IO_SOURCE.MODIFY_TIME, IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE).where(IO_SOURCE.TARGET_TYPE.eq(1).and(IO_SOURCE.TARGET_ID.eq(userId)).and(IO_SOURCE.SOURCE_HASH.eq("safe")).and(IO_SOURCE.IS_FOLDER.eq(1)).and(IO_SOURCE.IS_DELETE.eq(0)))
               .fetchOneInto(HomeExplorerVO.class);
    }


    /** 查看是否是私密保险箱 */
    @Override
    public int getSourceIsSafe(Long userId, String parentLevel){
        int isSafe = 0;
        String key = GlobalConfig.user_safe_redis_id_key + userId;
        String safeId = stringRedisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(safeId)) {
            HomeExplorerVO homeExplorerVO = getMySafeBoxSource(userId);
            if (!ObjectUtils.isEmpty(homeExplorerVO)) {
                safeId = String.valueOf(homeExplorerVO.getSourceID());
                Long t = DateUtil.getTimeDiff();
                stringRedisTemplate.opsForValue().set(key, safeId, t, TimeUnit.MINUTES);
            }
        }

        if (!ObjectUtils.isEmpty(safeId) && parentLevel.indexOf("," + safeId + ",") >= 0) {
            isSafe = 1;
        }
        return isSafe;
    }

    @Override
    public void clearUserDentryIdByDentryId(String dentryId) {
        context.update(IO_SOURCE)
                .set(IO_SOURCE.DING_DENTRY_ID, "")
                .set(IO_SOURCE.DING_SPACE_ID, "")
                .set(IO_SOURCE.DING_UNION_ID, "")
                .set(IO_SOURCE.SOURCE_HASH, "")
                .where(IO_SOURCE.IS_DELETE.eq(1))
                .and(IO_SOURCE.DING_DENTRY_ID.eq(dentryId))
                .execute();
    }

    @Override
    public Map<String, Long> getSourceNameListJson(Long sourceID){
        List<IOSource> list = context.select(IO_SOURCE.ID,IO_SOURCE.NAME)
                .from(IO_SOURCE)
                .where(IO_SOURCE.PARENT_ID.eq(sourceID))
                .and(IO_SOURCE.IS_DELETE.eq(0))
                .fetchInto(IOSource.class);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        Map<String, Long> reMap = new HashMap<>(list.size());
        for (IOSource source : list){
            reMap.put(source.getName(), source.getId() );
        }
        return reMap;
    }
}
