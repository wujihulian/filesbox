package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.Record18;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.svnlan.jooq.Tables.*;

@Repository
public class IoSourceHistoryDaoImpl implements IoSourceHistoryDao {
    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(IoSourceHistory ioSourceHistory) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(IO_SOURCE_HISTORY)
                .set(IO_SOURCE_HISTORY.SOURCE_ID, ioSourceHistory.getSourceID())
                .set(IO_SOURCE_HISTORY.USER_ID, ioSourceHistory.getUserID())
                .set(IO_SOURCE_HISTORY.FILE_ID, ioSourceHistory.getFileID())
                .set(IO_SOURCE_HISTORY.SIZE, ioSourceHistory.getSize())
                .set(IO_SOURCE_HISTORY.DETAIL, ioSourceHistory.getDetail())
                .set(IO_SOURCE_HISTORY.CREATE_TIME, now)
                .set(IO_SOURCE_HISTORY.MODIFY_TIME, now)
                .set(IO_SOURCE_HISTORY.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(IO_SOURCE_HISTORY.ID).fetchOne().getId();
        ioSourceHistory.setId(id);
        return 1;
    }

    @Override
    public List<HomeExplorerVO> getSourceHistoryBySourceID(Map<String, Object> hashMap) {
        Long sourceID = (Long) hashMap.get("sourceID");
        Integer startIndex = (Integer) hashMap.get("startIndex");
        Integer pageSize = (Integer) hashMap.get("pageSize");

        SelectSelectStep<Record18<Long, Long, Long, Long, Long, String, LocalDateTime, String, Integer, Integer, String, Integer, Integer, String, String, String, String, String>>
                selectStep = context.select(IO_SOURCE_HISTORY.ID, IO_SOURCE_HISTORY.SOURCE_ID.as("sourceID"), IO_SOURCE_HISTORY.FILE_ID.as("fileID")
                , IO_SOURCE_HISTORY.USER_ID.as("userID"), IO_SOURCE_HISTORY.SIZE,
                IO_SOURCE_HISTORY.DETAIL, IO_FILE.CREATE_TIME, USERS.AVATAR, USERS.SEX, USERS.STATUS,
                DSL.iif(USERS.NICKNAME.isNull().or(USERS.NICKNAME.eq("")), USERS.NAME, USERS.NICKNAME).as("nickname"),
                IO_SOURCE.TARGET_TYPE, IO_SOURCE.IS_FOLDER, IO_SOURCE.FILE_TYPE, IO_FILE.PATH, IO_FILE_META.VALUE_TEXT.as("value"),
                IO_FILE.HASH_MD5, IO_SOURCE.NAME);
        selectStep
                .from(IO_SOURCE_HISTORY)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_HISTORY.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE_HISTORY.FILE_ID))
                .leftJoin(IO_FILE_META).on(IO_SOURCE_HISTORY.FILE_ID.eq(IO_FILE_META.FILE_ID)).and(IO_FILE_META.KEY_STRING.eq("fileInfoMore"))
                .leftJoin(USERS).on(USERS.ID.eq(IO_SOURCE_HISTORY.USER_ID))
                .where(IO_SOURCE_HISTORY.SOURCE_ID.eq(sourceID))
                .orderBy(IO_SOURCE_HISTORY.CREATE_TIME.desc(), IO_SOURCE_HISTORY.ID.desc());
        if (Objects.nonNull(startIndex) && startIndex >= 0) {
            if (Objects.nonNull(pageSize) && pageSize > 0) {
                selectStep.limit(startIndex, pageSize);
            } else {
                selectStep.limit(startIndex);
            }
        }
        return selectStep.fetchInto(HomeExplorerVO.class);
    }

    @Override
    public Long getCountSourceHistoryBySourceID(Map<String, Object> hashMap) {
        Long sourceID = (Long) hashMap.get("sourceID");
        return context.select(DSL.count(IO_SOURCE_HISTORY.ID))
                .from(IO_SOURCE_HISTORY)
                .where(IO_SOURCE_HISTORY.SOURCE_ID.eq(sourceID))
                .fetchOneInto(Long.class);
    }

    @Override
    public int updateDetail(Long id, String detail) {
        return context.update(IO_SOURCE_HISTORY)
                .set(IO_SOURCE_HISTORY.DETAIL, detail)
                .set(IO_SOURCE_HISTORY.MODIFY_TIME, LocalDateTime.now())
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .execute();
    }

    @Override
    public int updateVerSource(IoSourceHistory ioSourceHistory) {
        return context.update(IO_SOURCE)
                .set(IO_SOURCE.FILE_ID, ioSourceHistory.getFileID())
                .set(IO_SOURCE.SIZE, ioSourceHistory.getSize())
                .set(IO_SOURCE.MODIFY_TIME, LocalDateTime.now())
                .set(IO_SOURCE.MODIFY_USER, ioSourceHistory.getUserID())
                .where(IO_SOURCE.ID.eq(ioSourceHistory.getSourceID()))
                .execute();
    }

    @Override
    public IoSourceHistory getFileInfoBySourceID(Long sourceID) {
        return context.select(IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.SIZE,IO_SOURCE.CREATE_USER.as("userID"),IO_SOURCE.PARENT_ID.as("parentID")
        ,IO_SOURCE.PARENT_LEVEL,IO_SOURCE.NAME,IO_SOURCE.TARGET_TYPE)
                .from(IO_SOURCE)
                .where(IO_SOURCE.ID.eq(sourceID))
                .fetchOneInto(IoSourceHistory.class);
    }

    @Override
    public CommonSource getHistorySourceInfo(Long id) {
        return context.select(IO_FILE.ID.as("fileID"), IO_FILE.NAME, IO_FILE.HASH_MD5, IO_FILE.SIZE, IO_FILE.PATH, IO_SOURCE.FILE_TYPE, IO_SOURCE.PARENT_ID.as("parentID")
                , IO_SOURCE.ID.as("sourceID"),IO_SOURCE.IS_FOLDER, IO_SOURCE.PARENT_LEVEL, IO_SOURCE.TARGET_TYPE, IO_SOURCE.NAME.as("sourceName"))
                .from(IO_SOURCE_HISTORY)
                .leftJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_HISTORY.SOURCE_ID))
                .leftJoin(IO_FILE).on(IO_FILE.ID.eq(IO_SOURCE_HISTORY.FILE_ID))
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .fetchOneInto(CommonSource.class);
    }

    @Override
    public int delByID(Long id) {
        return context.delete(IO_SOURCE_HISTORY)
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .execute();
    }

    @Override
    public int delBySourceID(Long sourceID) {
        return context.delete(IO_SOURCE_HISTORY)
                .where(IO_SOURCE_HISTORY.SOURCE_ID.eq(sourceID))
                .execute();
    }

    @Override
    public IoSourceHistory getHistoryInfo(Long id) {
        return context.select(IO_SOURCE_HISTORY.ID, IO_SOURCE_HISTORY.SOURCE_ID.as("sourceID"), IO_SOURCE_HISTORY.FILE_ID.as("fileID"), IO_SOURCE_HISTORY.SIZE
                ,IO_SOURCE_HISTORY.USER_ID.as("userID"), IO_SOURCE_HISTORY.CREATE_TIME.as("createTime"))
                .from(IO_SOURCE_HISTORY)
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .fetchOneInto(IoSourceHistory.class);
    }
    @Override
    public IoSourceHistory getHistoryInfoByFileId(Long sourceID, Long fileID){
        return context.select(IO_SOURCE_HISTORY.ID, IO_SOURCE_HISTORY.SOURCE_ID.as("sourceID"), IO_SOURCE_HISTORY.FILE_ID.as("fileID"), IO_SOURCE_HISTORY.SIZE
                , IO_SOURCE_HISTORY.USER_ID.as("userID"))
                .from(IO_SOURCE_HISTORY)
                .where(IO_SOURCE_HISTORY.SOURCE_ID.eq(sourceID).and(IO_SOURCE_HISTORY.FILE_ID.eq(fileID)))
                .fetchOneInto(IoSourceHistory.class);
    }

    @Override
    public int updateSize(Long id, Long size, String detail){

        return context.update(IO_SOURCE_HISTORY)
                .set(IO_SOURCE_HISTORY.SIZE, size)
                .set(IO_SOURCE_HISTORY.DETAIL, detail)
                .set(IO_SOURCE_HISTORY.MODIFY_TIME, LocalDateTime.now())
                .where(IO_SOURCE_HISTORY.ID.eq(id))
                .execute();
    }
}
