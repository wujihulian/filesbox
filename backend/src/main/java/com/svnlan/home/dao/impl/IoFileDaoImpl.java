package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOFileMeta;
import com.svnlan.jooq.tables.records.IoFileMetaRecord;
import com.svnlan.jooq.tables.records.IoFileRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.svnlan.jooq.Tables.*;

@Repository
public class IoFileDaoImpl implements IoFileDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public CommonSource getFileAttachment(Long sourceID) {
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5.as("hashMd5"), IO_FILE.SIZE, IO_FILE.PATH, IO_SOURCE.FILE_TYPE.as("fileType")
        ,IO_FILE_META.VALUE_TEXT.as("value"),IO_SOURCE.PARENT_ID.as("parentID"),IO_SOURCE.PARENT_LEVEL.as("parentLevel"),IO_SOURCE.ID.as("sourceID"),IO_FILE.IS_PREVIEW.as("isPreview")
        ,IO_FILE.IS_M3U8.as("isM3u8"),IO_FILE.APP_PREVIEW.as("appPreview"),IO_FILE.IS_H264_PREVIEW.as("isH264Preview"),IO_SOURCE.TARGET_ID.as("userID"),IO_FILE.CREATE_TIME.as("createTime"))
                .from(IO_FILE)
                .leftJoin(IO_SOURCE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .leftJoin(IO_FILE_META).on(IO_FILE.ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public int insert(IOFile ioFile) {
        LocalDateTime now = LocalDateTime.now();

        Long tenantId = ObjectUtils.isEmpty(ioFile.getTenantId()) ? tenantUtil.getTenantIdByServerName() : ioFile.getTenantId();

        ioFile.setSize(ObjectUtils.isEmpty(ioFile.getSize()) ? 0L : ioFile.getSize());
        ioFile.setIsH264Preview(ObjectUtils.isEmpty(ioFile.getIsH264Preview()) ? 0 : ioFile.getIsH264Preview());
        ioFile.setIsPreview(ObjectUtils.isEmpty(ioFile.getIsPreview()) ? 0 : ioFile.getIsPreview());
        ioFile.setAppPreview(ObjectUtils.isEmpty(ioFile.getAppPreview()) ? 0 : ioFile.getAppPreview());
        ioFile.setIsM3u8(ObjectUtils.isEmpty(ioFile.getIsM3u8()) ? 0 : ioFile.getIsM3u8());
        ioFile.setConvertSize(ObjectUtils.isEmpty(ioFile.getConvertSize()) ? 0L : ioFile.getConvertSize());
        ioFile.setThumbSize(ObjectUtils.isEmpty(ioFile.getThumbSize()) ? 0L : ioFile.getThumbSize());
        Long id = context.insertInto(IO_FILE)
                .columns(IO_FILE.NAME, IO_FILE.SIZE, IO_FILE.IO_TYPE, IO_FILE.PATH, IO_FILE.HASH_SIMPLE, IO_FILE.LINK_COUNT,
                        IO_FILE.CREATE_TIME, IO_FILE.MODIFY_TIME, IO_FILE.IS_PREVIEW, IO_FILE.APP_PREVIEW, IO_FILE.IS_H264_PREVIEW,
                        IO_FILE.IS_M3U8, IO_FILE.FILE_NAME, IO_FILE.CONVERT_SIZE, IO_FILE.THUMB_SIZE, IO_FILE.TENANT_ID,IO_FILE.HASH_MD5)
                .values(ioFile.getName(), ioFile.getSize(), ioFile.getIoType(), ioFile.getPath(), ioFile.getHashSimple(), 1,
                        now, now, ioFile.getIsPreview(), ioFile.getAppPreview(), ioFile.getIsH264Preview(),
                        ioFile.getIsM3u8(), ioFile.getFileName(), ioFile.getConvertSize(), ioFile.getThumbSize(), tenantId,ioFile.getHashMd5())
                 .returning(IO_FILE.ID).fetchOne().getId();
        ioFile.setId(id);
        return 1;
    }


    @Override
    public int insertMeta(IOFileMeta ioFile) {
        Long tenantId = ObjectUtils.isEmpty(ioFile.getTenantId()) ? tenantUtil.getTenantIdByServerName() : ioFile.getTenantId();

        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(IO_FILE_META)
                .columns(IO_FILE_META.FILE_ID, IO_FILE_META.KEY_STRING, IO_FILE_META.VALUE_TEXT,
                        IO_FILE_META.CREATE_TIME, IO_FILE_META.MODIFY_TIME, IO_FILE.TENANT_ID)
                .values(ioFile.getFileID(), ioFile.getKey(), ioFile.getValue(), now, now, tenantId)
                .returning(IO_FILE_META.ID).fetchOne().getId();
        ioFile.setId(id);
        return 1;
    }

    @Override
    public List<Long> getSameSourceEmptyInfo(String hashMd5, Long size) {
        return context.select(IO_FILE.ID)
                .from(IO_FILE)
                .where(IO_FILE.HASH_MD5.eq(hashMd5))
                .and(IO_FILE.SIZE.eq(size))
                .and(IO_FILE.IS_M3U8.eq(0))
                .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetch(IO_FILE.ID);
    }

    @Override
    public int updateVideoConvert(CommonSource commonSource) {

        UpdateSetMoreStep<IoFileRecord> updateSetMoreStep = context.update(IO_FILE)
                .set(IO_FILE.IS_M3U8, commonSource.getIsM3u8())
                .set(IO_FILE.IS_PREVIEW, commonSource.getIsPreview())
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now());
        if (Objects.nonNull(commonSource.getConvertSize())) {
            updateSetMoreStep.set(IO_FILE.CONVERT_SIZE, commonSource.getConvertSize());
        }
        return updateSetMoreStep.where(IO_FILE.ID.eq(commonSource.getFileID()))
                .execute();
    }

    @Override
    public int updateSameFile(CommonSource commonSource, List<Long> list) {
        return context.update(IO_FILE)
                .set(IO_FILE.IS_M3U8, 1)
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.in(list))
                .and(IO_FILE.IS_M3U8.eq(0))
                .execute();
    }

    @Override
    public int updateDocConvert(CommonSource commonSource) {
        return context.update(IO_FILE)
                .set(IO_FILE.APP_PREVIEW, commonSource.getAppPreview())
                .set(IO_FILE.IS_PREVIEW, commonSource.getIsPreview())
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.eq(commonSource.getFileID()))
                .execute();
    }

    @Override
    public List<Long> getSameSourceEmptyInfoDoc(String hashMd5, Long size) {
        return context.select(IO_FILE.ID)
                .from(IO_FILE)
                .where(IO_FILE.HASH_MD5.eq(hashMd5))
                .and(IO_FILE.SIZE.eq(size))
                .and(IO_FILE.APP_PREVIEW.eq(0))
                .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetch(IO_FILE.ID);
    }

    @Override
    public int updateSameFileSwf(Map<String, Object> updateMap, List<Long> list) {
        return context.update(IO_FILE)
                .set(IO_FILE.IS_M3U8, 1)
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.in(list))
                .and(IO_FILE.IS_M3U8.eq(0))
                .execute();
    }

    @Override
    public void updateSameFileDoc(Map<String, Object> docPath, List<Long> list) {
        context.update(IO_FILE)
                .set(IO_FILE.APP_PREVIEW, 1)
                .set(IO_FILE.IS_PREVIEW, 1)
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.in(list))
                .and(IO_FILE.APP_PREVIEW.eq(0))
                .execute();
    }

    @Override
    public CommonSource selectByChecksum(String hashMd5, Long size) {
        return selectByChecksumAndTime(hashMd5, size, null);
    }

    @Override
    public CommonSource selectByChecksumAndTime(String hashMd5, Long size, LocalDateTime time) {
        Condition condition = DSL.trueCondition()
                .and(IO_FILE.HASH_MD5.eq(hashMd5))
                .and(IO_FILE.SIZE.eq(size))
        .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));

        if (Objects.nonNull(time)) {
            condition = condition.and(IO_FILE.CREATE_TIME.gt(time));
        }
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5, IO_FILE.SIZE, IO_FILE.PATH,
                        IO_SOURCE.FILE_TYPE, IO_FILE_META.VALUE_TEXT.as("value"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL,
                        IO_SOURCE.ID.as("sourceID"), IO_FILE.IS_PREVIEW, IO_FILE.IS_M3U8, IO_FILE.APP_PREVIEW, IO_FILE.IS_H264_PREVIEW,
                        IO_FILE.CREATE_TIME, IO_FILE.CONVERT_SIZE, IO_FILE.THUMB_SIZE)
                .from(IO_FILE)
                .leftJoin(IO_SOURCE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .leftJoin(IO_FILE_META).on(IO_FILE.ID.eq(IO_FILE_META.FILE_ID)).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .where(condition)
                .orderBy(IO_FILE.APP_PREVIEW.desc(), IO_FILE.CREATE_TIME.asc()).limit(1)
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public int removeUserFile(List<Long> list) {
        return context.deleteFrom(IO_FILE)
                .where(IO_FILE.ID.in(list))
                .execute();
    }

    @Override
    public int removeUserFileMeta(List<Long> list) {
        return context.deleteFrom(IO_FILE_META)
                .where(IO_FILE_META.ID.in(list))
                .execute();
    }

    @Override
    public int removeUserFileContents(List<Long> list) {
        return context.deleteFrom(IO_FILE_META)
                .where(IO_FILE_META.ID.in(list))
                .execute();
    }

    @Override
    public String getFileUrlValue(Long fileID) {
        return context.select(IO_FILE_META.VALUE_TEXT)
                .from(IO_FILE_META)
                .where(IO_FILE_META.FILE_ID.eq(fileID))
                .and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .fetchOne(IO_FILE_META.VALUE_TEXT);
    }

    @Override
    public List<IOFileMeta> getFileUrlValueList(List<Long> list) {
        return context.select(IO_FILE_META.FILE_ID.as("fileID"), IO_FILE_META.VALUE_TEXT.as("value"))
                .from(IO_FILE_META)
                .where(IO_FILE_META.FILE_ID.in(list))
                .and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .fetchInto(IOFileMeta.class);
    }

    @Override
    public int updateFileUrlValue(List<IOFileMeta> list) {

        List<UpdateConditionStep<IoFileMetaRecord>> updates = new ArrayList<>();
        for (IOFileMeta meta : list) {
            if (!StringUtils.hasText(meta.getValue())) {
                continue;
            }
            updates.add(context.update(IO_FILE_META)
                    .set(IO_FILE_META.VALUE_TEXT, meta.getValue())
                    .where(IO_FILE_META.FILE_ID.eq(meta.getFileID())).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")));
        }
        context.batch(updates).execute();
        return list.size();
        // TODO 是否优化为一行
        /*
        UpdateQuery<IoFileMetaRecord> updateQuery = context.updateQuery(IO_FILE_META);
        for (IOFileMeta meta : list) {
            if (!StringUtils.hasText(meta.getValue())) {
                continue;
            }
            updateQuery.addValue(IO_FILE_META.VALUE_TEXT, DSL.when(IO_FILE_META.FILE_ID.eq(meta.getFileID()), meta.getValue()));
        }
        updateQuery.addConditions(IO_FILE_META.FILE_ID.in(list)); // 这里有错，优化时再修改
        updateQuery.addConditions(IO_FILE_META.KEY_STRING.eq("fileInfoMore"));
        return updateQuery.execute();
         */
    }

    @Override
    public int updateOneFileUrlValue(Long fileID, String value) {
        return context.update(IO_FILE_META)
                .set(IO_FILE_META.VALUE_TEXT, value)
                .where(IO_FILE_META.FILE_ID.eq(fileID))
                .and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .execute();
    }

    @Override
    public int updateAudioConvert(CommonSource commonSource) {
        UpdateSetMoreStep<IoFileRecord> updateSetMoreStep = context.update(IO_FILE)
                .set(IO_FILE.IS_H264_PREVIEW, commonSource.getIsH264Preview())
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now());
        if (Objects.nonNull(commonSource.getConvertSize())) {
            updateSetMoreStep.set(IO_FILE.CONVERT_SIZE, IO_FILE.CONVERT_SIZE.add(commonSource.getConvertSize()));
        }
        return updateSetMoreStep.where(IO_FILE.ID.eq(commonSource.getFileID()))
                .execute();
    }

    @Override
    public int updateCameraConvert(CommonSource commonSource) {
        UpdateSetMoreStep<IoFileRecord> updateSetMoreStep = context.update(IO_FILE)
                .set(IO_FILE.IS_H264_PREVIEW, commonSource.getIsH264Preview())
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now());
        if (Objects.nonNull(commonSource.getThumbSize())) {
            updateSetMoreStep.set(IO_FILE.THUMB_SIZE, IO_FILE.THUMB_SIZE.add(commonSource.getThumbSize()));
        }
        return updateSetMoreStep.where(IO_FILE.ID.eq(commonSource.getFileID()))
                .execute();
    }

    @Override
    public IOFileMeta getFileValue(Long sourceID) {
        return context.select(IO_FILE_META.FILE_ID.as("fileID"), IO_FILE_META.VALUE_TEXT.as("value"))
                .from(IO_FILE_META)
                .leftJoin(IO_SOURCE).on(IO_FILE_META.FILE_ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.eq(sourceID))
                .and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .fetchOneInto(IOFileMeta.class);
    }

    @Override
    public CommonSource findCommonSourceById(Long sourceID) {
        return context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.FILE_TYPE.as("fileType"), IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.IS_FOLDER.as("isFolder"),
                        IO_SOURCE.CREATE_USER.as("userID"), IO_SOURCE.PARENT_LEVEL.as("parentLevel"), IO_SOURCE.SIZE, IO_SOURCE.TARGET_TYPE.as("targetType"),
                        IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5.as("hashMd5"), IO_FILE.PATH, IO_FILE.IS_M3U8.as("isM3u8"), IO_FILE.IS_PREVIEW.as("isPreview"),
                        IO_FILE.APP_PREVIEW.as("appPreview"), IO_FILE.IS_H264_PREVIEW.as("isH264Preview"))
                .from(IO_SOURCE)
                .innerJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE.FILE_ID))
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public int deleteFileOrgPath(Long fileID) {
        return context.update(IO_FILE)
                .set(IO_FILE.IS_EXIST_FILE, 0)
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.eq(fileID))
                .execute();
    }

    @Override
    public CommonSource getHistoryFileAttachment(Long id) {
        return context.select(IO_FILE.ID.as("fileID"),IO_FILE.NAME,IO_FILE.HASH_MD5.as("hashMd5"),IO_FILE.SIZE,IO_FILE.PATH,IO_SOURCE.FILE_TYPE.as("fileType")
                ,IO_FILE_META.VALUE_TEXT.as("value"),IO_SOURCE.PARENT_ID.as("parentID"),IO_SOURCE.PARENT_LEVEL.as("parentLevel")
        ,IO_SOURCE.ID.as("sourceID"), IO_FILE.IS_PREVIEW.as("isPreview"),IO_FILE.IS_M3U8.as("isM3u8"),IO_FILE.APP_PREVIEW.as("appPreview")
        ,IO_FILE.IS_H264_PREVIEW.as("isH264Preview"),IO_SOURCE.TARGET_ID.as("userID"))
                .from(IO_SOURCE_HISTORY)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_HISTORY.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE_HISTORY.FILE_ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE_HISTORY.FILE_ID.eq(IO_FILE_META.FILE_ID).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore")))
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public int updateFileMd5ByPath(String hashMd5, String path) {
        return context.update(IO_FILE)
                .set(IO_FILE.HASH_MD5, hashMd5)
                .where(IO_FILE.PATH.eq(path))
                .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .execute();
    }

    @Override
    public int updateFileMd5AndSizeByFileID(String hashMd5, Long fileID, Long size) {
        return context.update(IO_FILE)
                .set(IO_FILE.HASH_MD5, hashMd5)
                .set(IO_FILE.SIZE, size)
                .where(IO_FILE.ID.eq(fileID))
                .execute();
    }

    @Override
    public int updateH264Info(CommonSource commonSource) {
        return context.update(IO_FILE)
                .set(IO_FILE.IS_H264_PREVIEW, commonSource.getIsH264Preview())
                .set(IO_FILE.MODIFY_TIME, LocalDateTime.now())
                .where(IO_FILE.ID.eq(commonSource.getFileID()))
                .execute();
    }

    @Override
    public Long selectStorageUsage(String location) {
        return context.select(DSL.sum(IO_FILE.SIZE))
                .from(IO_FILE)
                .where(IO_FILE.PATH.like(location + "%"))
                .and(IO_FILE.TENANT_ID.eq(1L))
                .fetchOneInto(Long.class);
    }

    @Override
    public IOFile selectById(Long id) {
        return context.select()
                .from(IO_FILE)
                .where(IO_FILE.ID.eq(id))
                .fetchOneInto(IOFile.class);
    }

    @Override
    public List<IOFile> selectBatchIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return context.select().from(IO_FILE)
                .where(IO_FILE.ID.in(ids))
                .fetchInto(IOFile.class);
    }

    @Override
    public void deleteById(Long id) {
        context.delete(IO_FILE)
                .where(IO_FILE.ID.eq(id))
                .execute();
    }

    @Override
    public void updateById(IOFile ioFile) {
        UpdateQuery<IoFileRecord> updateQuery = context.updateQuery(IO_FILE);
        Optional.ofNullable(ioFile.getHashSimple()).ifPresent(it -> updateQuery.addValue(IO_FILE.HASH_SIMPLE, it));
        Optional.ofNullable(ioFile.getName()).ifPresent(it -> updateQuery.addValue(IO_FILE.NAME, it));
        Optional.ofNullable(ioFile.getSize()).ifPresent(it -> updateQuery.addValue(IO_FILE.SIZE, it));
        Optional.ofNullable(ioFile.getIoType()).ifPresent(it -> updateQuery.addValue(IO_FILE.IO_TYPE, it));
        Optional.ofNullable(ioFile.getHashMd5()).ifPresent(it -> updateQuery.addValue(IO_FILE.HASH_MD5, it));
        Optional.ofNullable(ioFile.getPath()).ifPresent(it -> updateQuery.addValue(IO_FILE.PATH, it));
        Optional.ofNullable(ioFile.getIsM3u8()).ifPresent(it -> updateQuery.addValue(IO_FILE.IS_M3U8, it));
        Optional.ofNullable(ioFile.getIsH264Preview()).ifPresent(it -> updateQuery.addValue(IO_FILE.IS_H264_PREVIEW, it));
        Optional.ofNullable(ioFile.getIsH264Preview()).ifPresent(it -> updateQuery.addValue(IO_FILE.IS_H264_PREVIEW, it));
        Optional.ofNullable(ioFile.getIsPreview()).ifPresent(it -> updateQuery.addValue(IO_FILE.IS_PREVIEW, it));
        Optional.ofNullable(ioFile.getAppPreview()).ifPresent(it -> updateQuery.addValue(IO_FILE.APP_PREVIEW, it));
        Optional.ofNullable(ioFile.getFileName()).ifPresent(it -> updateQuery.addValue(IO_FILE.FILE_NAME, it));
        Optional.ofNullable(ioFile.getThumbSize()).ifPresent(it -> updateQuery.addValue(IO_FILE.THUMB_SIZE, it));
        Optional.ofNullable(ioFile.getConvertSize()).ifPresent(it -> updateQuery.addValue(IO_FILE.CONVERT_SIZE, it));
        updateQuery.addConditions(IO_FILE.ID.eq(ioFile.getId()));
        updateQuery.execute();
    }

    @Override
    public IOFile selectOneByPathLike(String path) {
        return context.select()
                .from(IO_FILE)
                .where(IO_FILE.PATH.like(path + "%"))
                .and(IO_FILE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(IOFile.class);
    }

    @Override
    public List<IOFile> selectPathList(List<Long> fileIdList) {
        return context.select(IO_FILE.PATH, IO_FILE.ID)
                .from(IO_FILE)
                .where(IO_FILE.ID.in(fileIdList))
                .fetchInto(IOFile.class);
    }
}
