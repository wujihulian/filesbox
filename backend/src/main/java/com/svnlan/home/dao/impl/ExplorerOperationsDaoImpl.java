package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.ExplorerOperationsDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;
import com.svnlan.jooq.tables.IoSource;
import com.svnlan.jooq.tables.records.IoSourceRecord;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.svnlan.jooq.Tables.IO_SOURCE;

@Repository
public class ExplorerOperationsDaoImpl implements ExplorerOperationsDao {

    @Resource
    private DSLContext context;

    @Override
    public List<HomeExplorerShareDetailVO> getAllSourceList(Map<String, Object> paramMap) {
        SelectJoinStep<Record6<Long, Integer, String, LocalDateTime, String, Long>> selectJoinStep = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.CREATE_TIME,
                        IO_SOURCE.PARENT_LEVEL, IO_SOURCE.PARENT_ID)
                .from(IO_SOURCE);
        Long sourceId = (Long) paramMap.get("sourceID");
        if (Objects.nonNull(sourceId)) {
            selectJoinStep.where(IO_SOURCE.PARENT_ID.eq(sourceId));
        }
        return selectJoinStep.fetchInto(HomeExplorerShareDetailVO.class);
    }


    // 已弃用
    @Override
    public int batchUpdateLevel(List<HomeExplorerShareDetailVO> list){
        return 0;
    }
    @Override
    public List<IOSource> getSourceListByLevelToContSize(String parentLevel, Integer status){

        SelectConditionStep where = context.select(IO_SOURCE.ID.as("sourceID"), IO_SOURCE.TYPE, IO_SOURCE.TARGET_TYPE, IO_SOURCE.IS_FOLDER, IO_SOURCE.NAME, IO_SOURCE.FILE_TYPE
                , IO_SOURCE.FILE_ID.as("fileID"), IO_SOURCE.SIZE, IO_SOURCE.PARENT_ID.as("parentID"), IO_SOURCE.PARENT_LEVEL)
                .from(IO_SOURCE)
                .where(IO_SOURCE.IS_FOLDER.eq(0));
        if (status.intValue() == 1){
            where.and(IO_SOURCE.IS_DELETE.eq(0));
        }
        where.and(IO_SOURCE.PARENT_LEVEL.like(DSL.concat(parentLevel, "%")));
        where.orderBy(IO_SOURCE.PARENT_LEVEL.desc());
        return where.fetchInto(IOSource.class);
    }
    @Override
    public void batchUpdateSizeByCountSize(List<IOSource> list){
        List<UpdateConditionStep<IoSourceRecord>> updates = new ArrayList<>();
        for (IOSource dto : list) {
            updates.add(context.update(IoSource.IO_SOURCE)
                    .set(IoSource.IO_SOURCE.SIZE, dto.getSize())
                    .where(IoSource.IO_SOURCE.ID.eq(dto.getId())));
        }
        context.batch(updates).execute();
    }
}
