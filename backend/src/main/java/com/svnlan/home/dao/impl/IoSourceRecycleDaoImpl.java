package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.IoSourceRecycleDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.svnlan.jooq.Tables.IO_SOURCE;
import static com.svnlan.jooq.Tables.IO_SOURCE_RECYCLE;

@Repository
public class IoSourceRecycleDaoImpl implements IoSourceRecycleDao {

    @Resource
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;
    @Override
    public int batchInsert(List<IOSource> list) {
        Long tenantId = list.get(0).getTenantId();
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        InsertValuesStep7 result;
        result = context.insertInto(IO_SOURCE_RECYCLE).columns(IO_SOURCE_RECYCLE.TARGET_TYPE,IO_SOURCE_RECYCLE.TARGET_ID,IO_SOURCE_RECYCLE.SOURCE_ID
                ,IO_SOURCE_RECYCLE.USER_ID,IO_SOURCE_RECYCLE.PARENT_LEVEL,IO_SOURCE_RECYCLE.CREATE_TIME,IO_SOURCE_RECYCLE.TENANT_ID);
        for (IOSource ioSource : list) {
            result.values(ioSource.getTargetType(),ioSource.getTargetId(), ioSource.getId(),ioSource.getUserId(),ioSource.getParentLevel(),LocalDateTime.now(),tenantId);
        }
        List<Long> idList = result.returning(IO_SOURCE_RECYCLE.ID).fetch().getValues(IO_SOURCE_RECYCLE.ID);
        if (idList.size() == list.size()){
            int i = 0;
            for (IOSource ioSource  : list) {
                ioSource.setOldSourceId(ioSource.getId());
                ioSource.setId(idList.get(i));
                i++;
            }
            return idList.size();
        }
        return idList.size();
    }

    @Override
    public int deleteUserRecycle(Long userID, Integer targetType, List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.deleteFrom(IO_SOURCE_RECYCLE)
                .where(IO_SOURCE_RECYCLE.USER_ID.eq(userID))
                .and(IO_SOURCE_RECYCLE.TARGET_TYPE.eq(targetType))
                .and(IO_SOURCE_RECYCLE.SOURCE_ID.in(list))
                .execute();
    }

    @Override
    public List<SourceOpDto> getUserRecycleBinList(Long userID) {
        return context.select(IO_SOURCE.NAME,IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL, IO_SOURCE.ID.as("sourceID"),
                DSL.iif(IO_SOURCE.IS_FOLDER.eq(1),"folder", "file").as("type"))
                .from(IO_SOURCE_RECYCLE)
                .innerJoin(IO_SOURCE).on(IO_SOURCE.ID.eq(IO_SOURCE_RECYCLE.SOURCE_ID))
                .where(IO_SOURCE_RECYCLE.USER_ID.eq(userID))
                .and(IO_SOURCE.IS_DELETE.eq(1))
                .fetchInto(SourceOpDto.class);
    }
}
