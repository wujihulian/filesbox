package com.svnlan.manage.dao.impl;

import com.svnlan.jooq.tables.records.CommonDesignAssemblyRecord;
import com.svnlan.manage.dao.CommonDesignAssemblyDao;
import com.svnlan.manage.domain.CommonDesignAssembly;
import com.svnlan.manage.vo.DesignAssemblyVO;
import com.svnlan.utils.TenantUtil;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdateSetFirstStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.svnlan.jooq.Tables.COMMON_DESIGN_ASSEMBLY;

@Repository
public class CommonDesignAssemblyDaoImpl implements CommonDesignAssemblyDao {

    @Resource
    private DSLContext context;

    @Resource
    private TenantUtil tenantUtil;

    @Override
    public List<DesignAssemblyVO> selectList(CommonDesignAssembly record) {
        Condition condition = DSL.trueCondition().and(COMMON_DESIGN_ASSEMBLY.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));

        if (!ObjectUtils.isEmpty(record.getAssemblyType())){
            condition = condition.and(COMMON_DESIGN_ASSEMBLY.ASSEMBLY_TYPE.eq(record.getAssemblyType()));
        }
        if (!ObjectUtils.isEmpty(record.getClientType())){
            condition = condition.and(COMMON_DESIGN_ASSEMBLY.CLIENT_TYPE.eq(record.getClientType()));
        }
        if (!ObjectUtils.isEmpty(record.getDesignType())){
            condition = condition.and(COMMON_DESIGN_ASSEMBLY.DESIGN_TYPE.eq(record.getDesignType()));
        }
        return context.select(COMMON_DESIGN_ASSEMBLY.ID.as("assemblyId"), COMMON_DESIGN_ASSEMBLY.TITLE, COMMON_DESIGN_ASSEMBLY.ASSEMBLY_TYPE.as("type")
                , COMMON_DESIGN_ASSEMBLY.DETAIL,COMMON_DESIGN_ASSEMBLY.SETTING, COMMON_DESIGN_ASSEMBLY.SIZE)
                .from(COMMON_DESIGN_ASSEMBLY)
                .where(condition)
                .fetchInto(DesignAssemblyVO.class);
    }

    @Override
    public int insert(CommonDesignAssembly record) {
        LocalDateTime now = LocalDateTime.now();
        Long id = context.insertInto(COMMON_DESIGN_ASSEMBLY)
                .set(COMMON_DESIGN_ASSEMBLY.TITLE, record.getTitle())
                .set(COMMON_DESIGN_ASSEMBLY.ASSEMBLY_TYPE, record.getAssemblyType())
                .set(COMMON_DESIGN_ASSEMBLY.DETAIL, record.getDetail())
                .set(COMMON_DESIGN_ASSEMBLY.SETTING, record.getSetting())
                .set(COMMON_DESIGN_ASSEMBLY.SIZE, record.getSize())
                .set(COMMON_DESIGN_ASSEMBLY.CLIENT_TYPE, record.getClientType())
                .set(COMMON_DESIGN_ASSEMBLY.DESIGN_TYPE, record.getDesignType())
                .set(COMMON_DESIGN_ASSEMBLY.GMT_CREATE, now)
                .set(COMMON_DESIGN_ASSEMBLY.GMT_MODIFIED, now)
                .set(COMMON_DESIGN_ASSEMBLY.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(COMMON_DESIGN_ASSEMBLY.ID).fetchOne().getId();
        record.setId(id);
        record.setCommonDesignAssemblyId(id);
        return 1;
    }

    @Override
    public int updateByParam(CommonDesignAssembly record) {
        UpdateSetFirstStep<CommonDesignAssemblyRecord> updateSetFirstStep = context.update(COMMON_DESIGN_ASSEMBLY);
        updateSetFirstStep.set(COMMON_DESIGN_ASSEMBLY.TITLE, record.getTitle());
        Optional.ofNullable(record.getDetail()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_ASSEMBLY.DETAIL, it));
        Optional.ofNullable(record.getSize()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_ASSEMBLY.SIZE, it));
        Optional.ofNullable(record.getSetting()).ifPresent(it -> updateSetFirstStep.set(COMMON_DESIGN_ASSEMBLY.SETTING, it));
        return updateSetFirstStep.set(COMMON_DESIGN_ASSEMBLY.GMT_MODIFIED, LocalDateTime.now())
                .where(COMMON_DESIGN_ASSEMBLY.ID.eq(record.getCommonDesignAssemblyId()))
                .execute();

    }

    @Override
    public int deleteById(Long assemblyId) {
        return context.delete(COMMON_DESIGN_ASSEMBLY)
                .where(COMMON_DESIGN_ASSEMBLY.ID.eq(assemblyId))
                .execute();
    }
}
