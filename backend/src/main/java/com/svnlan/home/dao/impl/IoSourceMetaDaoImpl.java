package com.svnlan.home.dao.impl;

import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.jooq.tables.records.IoSourceMetaRecord;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.svnlan.jooq.tables.IoSourceMeta.IO_SOURCE_META;

@Service
public class IoSourceMetaDaoImpl implements IoSourceMetaDao {
    @Autowired
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int delMetaBySourceID(Long sourceID, List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.delete(IO_SOURCE_META)
                .where(IO_SOURCE_META.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_META.KEY_STRING.in(list))
                .execute();
    }

    @Override
    public int delMetaBySourceIDList(List<Long> sourceIdList, List<String> list) {
        if (CollectionUtils.isEmpty(sourceIdList) || CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return context.delete(IO_SOURCE_META)
                .where(IO_SOURCE_META.SOURCE_ID.in(sourceIdList))
                .and(IO_SOURCE_META.KEY_STRING.in(list))
                .execute();
    }

    @Override
    public int batchInsert(List<IOSourceMeta> list) {
        Long paramTenantId = list.get(0).getTenantId();
        Long tenantId = ObjectUtils.isEmpty(paramTenantId) ? tenantUtil.getTenantIdByServerName() : paramTenantId;
        List<IoSourceMetaRecord> ioSourceMetaRecordList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (IOSourceMeta meta : list) {
            IoSourceMetaRecord ioSourceMetaRecord = context.newRecord(IO_SOURCE_META);
            ioSourceMetaRecord.setSourceId(meta.getSourceID());
            ioSourceMetaRecord.setCreateTime(now);
            ioSourceMetaRecord.setModifyTime(now);
            ioSourceMetaRecord.setKeyString(meta.getKey());
            ioSourceMetaRecord.setValueText(meta.getValue());
            ioSourceMetaRecord.setTenantId(tenantId);
            ioSourceMetaRecordList.add(ioSourceMetaRecord);
        }
        int[] execute = context.batchInsert(ioSourceMetaRecordList).execute();
        return execute.length;
    }

    @Override
    public int insert(IOSourceMeta iOSourceMeta) {
        LocalDateTime now = LocalDateTime.now();
        return context.insertInto(IO_SOURCE_META)
                .set(IO_SOURCE_META.SOURCE_ID, iOSourceMeta.getSourceID())
                .set(IO_SOURCE_META.KEY_STRING, iOSourceMeta.getKey())
                .set(IO_SOURCE_META.VALUE_TEXT, iOSourceMeta.getValue())
                .set(IO_SOURCE_META.CREATE_TIME, now)
                .set(IO_SOURCE_META.MODIFY_TIME, now)
                .set(IO_SOURCE_META.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .execute();
    }

    @Override
    public List<IOSourceMeta> getSourceMetaListBySourceID(Long sourceID, List<String> list) {
        return context.select(IO_SOURCE_META.SOURCE_ID, IO_SOURCE_META.KEY_STRING, IO_SOURCE_META.VALUE_TEXT)
                .from(IO_SOURCE_META)
                .where(IO_SOURCE_META.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_META.KEY_STRING.in(list))
                .fetchInto(IOSourceMeta.class);
    }

    @Override
    public IOSourceMeta getSourceMetaVoBySourceID(Long sourceID, String key) {
        return context.select(IO_SOURCE_META.SOURCE_ID, IO_SOURCE_META.KEY_STRING, IO_SOURCE_META.VALUE_TEXT)
                .from(IO_SOURCE_META)
                .where(IO_SOURCE_META.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_META.KEY_STRING.eq(key))
                .fetchOneInto(IOSourceMeta.class);
    }

    @Override
    public int updateMetaByKey(Long sourceID, String key, String desc) {
        return context.update(IO_SOURCE_META)
                .set(IO_SOURCE_META.VALUE_TEXT, desc)
                .where(IO_SOURCE_META.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_META.KEY_STRING.eq(key))
                .execute();
    }

    @Override
    public String getValueMetaByKey(Long sourceID, String key) {
        return context.select(IO_SOURCE_META.VALUE_TEXT)
                .from(IO_SOURCE_META)
                .where(IO_SOURCE_META.SOURCE_ID.eq(sourceID))
                .and(IO_SOURCE_META.KEY_STRING.eq(key))
                .limit(1)
                .fetchOne(IO_SOURCE_META.VALUE_TEXT);
    }

    @Override
    public String getSourceIDMetaByKey(String value, String key, Long tenantId) {
        return context.select(IO_SOURCE_META.SOURCE_ID)
                .from(IO_SOURCE_META)
                .where(
                        IO_SOURCE_META.VALUE_TEXT.eq(value)
                                .and(IO_SOURCE_META.KEY_STRING.eq(key))
                )
                .fetchOneInto(String.class);
    }
}
