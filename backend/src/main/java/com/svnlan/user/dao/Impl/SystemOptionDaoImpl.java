package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.pojos.SystemOptionModel;
import com.svnlan.jooq.tables.records.SystemOptionRecord;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.utils.TenantUtil;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.UpdateConditionStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.svnlan.jooq.tables.SystemOption.SYSTEM_OPTION;
import static com.svnlan.jooq.tables.IoSource.IO_SOURCE;

@Service
public class SystemOptionDaoImpl implements SystemOptionDao {
    @Autowired
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public List<OptionVo> getSystemConfig() {
        return context.select(SYSTEM_OPTION.KEY_STRING, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TYPE.eq(""))
                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchInto(OptionVo.class);
    }

    @Override
    public List<OptionVo> getSystemOtherConfig(String type, Long tenantId) {
        if (ObjectUtils.isEmpty(tenantId)){
            tenantId = tenantUtil.getTenantIdByServerName();
        }
        return context.select(SYSTEM_OPTION.ID, SYSTEM_OPTION.KEY_STRING, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TYPE.eq(type))
                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantId))
                .fetchInto(OptionVo.class);
    }

    @Override
    public String getSystemConfigByKey(String key) {
        return getSystemConfigByKey(key, null);
    }

    @Override
    public String getSystemConfigByKey(String key, Long tenantId){
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName(): tenantId;
        return context.select(SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(
                        SYSTEM_OPTION.KEY_STRING.eq(key)
                                .and(SYSTEM_OPTION.TYPE.eq(""))
                                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantId))
                )
                .fetchOneInto(String.class);
    }

    @Override
    public OptionVo getSystemOtherConfigByKey(String type, String key) {
        return context.select(SYSTEM_OPTION.KEY_STRING, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(
                        SYSTEM_OPTION.KEY_STRING.eq(key)
                                .and(SYSTEM_OPTION.TYPE.eq(type))
                                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .fetchOneInto(OptionVo.class);
    }

    // TODO updateSystemOption 后续修改成一行更新
    @Override
    public void updateSystemOption(String type, List<OptionVo> list, Long tenantId) {
        List<UpdateConditionStep<SystemOptionRecord>> updates = new ArrayList<>();
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        for (OptionVo optionVo : list) {
            updates.add(context.update(SYSTEM_OPTION)
                    .set(SYSTEM_OPTION.VALUE_TEXT, optionVo.getValue())
                    .where(SYSTEM_OPTION.KEY_STRING.eq(optionVo.getKey()))
                    .and(SYSTEM_OPTION.TENANT_ID.eq(tenantId))
                    .and(SYSTEM_OPTION.TYPE.eq(type)));
        }
        context.batch(updates).execute();
    }

    @Override
    public OptionVo getSystemOtherConfigById(Integer id) {
        return context.select(SYSTEM_OPTION.ID, SYSTEM_OPTION.KEY_STRING, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.ID.eq(id))
                .fetchOneInto(OptionVo.class);
    }

    @Override
    public int updateSystemOptionById(Integer id, String value) {
        UpdateSetMoreStep<SystemOptionRecord> set = context.update(SYSTEM_OPTION)
                .set(SYSTEM_OPTION.VALUE_TEXT, value);
        return set.where(SYSTEM_OPTION.ID.eq(id)).execute();
    }

    @Override
    public List<OptionVo> selectListByType(String storageType) {
        List<OptionVo> list = context.selectFrom(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TYPE.eq(storageType))
                .fetchInto(OptionVo.class);
        return list;
    }

    @Override
    public List<OptionVo> getSystemConfigByKeyListBy(List<String> list) {
        return context.select(SYSTEM_OPTION.KEY_STRING, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(
                        SYSTEM_OPTION.TYPE.eq("")
                                .and(SYSTEM_OPTION.KEY_STRING.in(list))
                                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                )
                .fetchInto(OptionVo.class);

    }


    @Override
    public int insert(OptionVo entity) {
        LocalDateTime now = LocalDateTime.now();
        Long tenantId = ObjectUtils.isEmpty(entity.getTenantId()) ? tenantUtil.getTenantIdByServerName() : entity.getTenantId();
        SystemOptionModel systemOptionModel = new SystemOptionModel();
        systemOptionModel.setType(entity.getType());
        systemOptionModel.setKeyString(entity.getKeyString());
        systemOptionModel.setValueText(entity.getValueText());
        systemOptionModel.setCreateTime(now);
        systemOptionModel.setModifyTime(now);
        systemOptionModel.setTenantId(tenantId);
        SystemOptionRecord systemOptionRecord = context.newRecord(SYSTEM_OPTION);
        systemOptionRecord.from(systemOptionModel);
        int size = systemOptionRecord.insert();
        entity.setId(systemOptionModel.getId());
        return size;
    }

    @Override
    public OptionVo selectById(Integer id) {
        return context.select()
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.ID.eq(id))
                .fetchOneInto(OptionVo.class);
    }

    @Override
    public OptionVo selectOneByKeyString(String keyString) {
        return context.select()
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.KEY_STRING.eq(keyString))
                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .fetchOneInto(OptionVo.class);
    }

    @Override
    public List<OptionVo> selectOneByTypeAndKeyString(String type, String keyString, Boolean isInclude) {
        return context.select(SYSTEM_OPTION.ID, SYSTEM_OPTION.VALUE_TEXT)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TYPE.eq(type))
                .and(isInclude ? SYSTEM_OPTION.KEY_STRING.eq(keyString) : SYSTEM_OPTION.KEY_STRING.ne(keyString))
                .fetchInto(OptionVo.class);
    }

    @Override
    public void updateById(OptionVo optionVo) {
        context.update(SYSTEM_OPTION)
                .set(SYSTEM_OPTION.MODIFY_TIME, optionVo.getModifyTime())
                .set(SYSTEM_OPTION.VALUE_TEXT, optionVo.getValueText())
                .set(SYSTEM_OPTION.KEY_STRING, optionVo.getKeyString())
                .where(SYSTEM_OPTION.ID.eq(optionVo.getId()))
                .execute();
    }

    @Override
    public void updateByKeyStringAndType(OptionVo optionVoUpdate) {
        LocalDateTime now = LocalDateTime.now();
        context.update(SYSTEM_OPTION)
                .set(SYSTEM_OPTION.VALUE_TEXT, optionVoUpdate.getValueText())
                .set(SYSTEM_OPTION.MODIFY_TIME, now)
                .where(SYSTEM_OPTION.TYPE.eq(optionVoUpdate.getType()))
                .and(SYSTEM_OPTION.KEY_STRING.eq(optionVoUpdate.getKeyString()))
                .and(SYSTEM_OPTION.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()))
                .execute();
    }

    @Override
    public int deleteById(Integer id) {
        return context.delete(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.ID.eq(id))
                .execute();
    }


    @Override
    public int batchInsert(List<OptionVo> list){

        InsertQuery<SystemOptionRecord> insertQuery = context.insertQuery(SYSTEM_OPTION);
        LocalDateTime now = LocalDateTime.now();
        Long orgTenantId = list.get(0).getTenantId();
        Long tenantId = ObjectUtils.isEmpty(orgTenantId) ? tenantUtil.getTenantIdByServerName() : orgTenantId;
        for (OptionVo vo : list) {
            insertQuery.newRecord();
            insertQuery.addValue(SYSTEM_OPTION.TYPE, vo.getType());
            insertQuery.addValue(SYSTEM_OPTION.KEY_STRING, vo.getKey());
            insertQuery.addValue(SYSTEM_OPTION.VALUE_TEXT, ObjectUtils.isEmpty(vo.getValue()) ? "" : vo.getValue());
            insertQuery.addValue(SYSTEM_OPTION.CREATE_TIME, now);
            insertQuery.addValue(SYSTEM_OPTION.MODIFY_TIME, now);
            insertQuery.addValue(SYSTEM_OPTION.TENANT_ID, tenantId);
        }
        return insertQuery.execute();

    }
    @Override
    public List<String> checkSystemConfigByKeyList(List<String> list, Long tenantId){
        tenantId = ObjectUtils.isEmpty(tenantId) ? tenantUtil.getTenantIdByServerName() : tenantId;
        return context.select(SYSTEM_OPTION.KEY_STRING)
                .from(SYSTEM_OPTION)
                .where(SYSTEM_OPTION.TENANT_ID.eq(tenantId))
                .and(SYSTEM_OPTION.KEY_STRING.in(list))
                .fetchInto(String.class);

    }

    @Override
    public void initSourceStorageId(Integer storageId){
        context.update(IO_SOURCE)
                .set(IO_SOURCE.STORAGE_ID, storageId)
                .where(IO_SOURCE.STORAGE_ID.eq(0))
                .execute();
    }

    @Override
    public List<OptionVo> getStorageList(String type) {
        return null;
    }


}
