package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.pojos.RoleModel;
import com.svnlan.jooq.tables.records.RoleRecord;
import com.svnlan.user.dao.RoleDao;
import com.svnlan.user.domain.Role;
import com.svnlan.user.dto.RoleDTO;
import com.svnlan.utils.TenantUtil;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.svnlan.jooq.tables.Role.ROLE;

@Service
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private DSLContext context;
    @Resource
    private TenantUtil tenantUtil;

    @Override
    public int insert(Role role) {
         LocalDateTime now = LocalDateTime.now();
        Integer id = context.insertInto(ROLE)
                .set(ROLE.ROLE_NAME, role.getRoleName())
                .set(ROLE.CODE, role.getCode())
                .set(ROLE.DESCRIPTION, role.getDescription()==null?"":role.getDescription())
                .set(ROLE.LABEL, role.getLabel()==null?"":role.getLabel())
                .set(ROLE.AUTH, role.getAuth()==null?"":role.getAuth())
                .set(ROLE.IGNORE_EXT, role.getIgnoreExt()==null?"":role.getIgnoreExt())
                .set(ROLE.DELETE_TIME, now)
                .set(ROLE.STATUS, role.getStatus())
                .set(ROLE.MODIFY_TIME, now)
                .set(ROLE.CREATE_TIME, now)
                .set(ROLE.ENABLE, role.getEnable())
                .set(ROLE.IS_SYSTEM, role.getIsSystem()==null ? 0 : role.getIsSystem())
                .set(ROLE.ADMINISTRATOR, role.getAdministrator()==null ? 0 : role.getAdministrator())
                .set(ROLE.IGNORE_FILE_SIZE, role.getIgnoreFileSize())
                .set(ROLE.SORT, role.getSort())
                .set(ROLE.ROLE_TYPE, role.getRoleType())
                .set(ROLE.TENANT_ID, tenantUtil.getTenantIdByServerName())
                .returning(ROLE.ID).fetchOne().getId();
        role.setRoleID(id);
        return 1;
    }

    @Override
    public int update(Role role) {
        return context.update(ROLE)
                .set(ROLE.ROLE_NAME, role.getRoleName())
                .set(ROLE.DESCRIPTION, role.getDescription())
                .set(ROLE.LABEL, role.getLabel())
                .set(ROLE.AUTH, role.getAuth())
                .set(ROLE.ENABLE, role.getEnable())
                .set(ROLE.IGNORE_FILE_SIZE, role.getIgnoreFileSize())
                .set(ROLE.MODIFY_TIME, LocalDateTime.now())
                .where(ROLE.ID.eq(role.getRoleID()))
                .execute();
    }

    @Override
    public List<Role> getRoleList(RoleDTO roleDTO) {
        SelectConditionStep where = context.select(ROLE.ID.as("roleID"), ROLE.ROLE_NAME, ROLE.DESCRIPTION, ROLE.LABEL, ROLE.AUTH, ROLE.ENABLE, ROLE.IS_SYSTEM, ROLE.ADMINISTRATOR, ROLE.IGNORE_FILE_SIZE, ROLE.SORT, ROLE.ROLE_TYPE, ROLE.MODIFY_TIME)
                .from(ROLE)
                .where(ROLE.STATUS.eq(1).and(ROLE.ROLE_TYPE.eq(roleDTO.getRoleType())));
        if(roleDTO.getEnable() != null){
            where.and(ROLE.ENABLE.eq(roleDTO.getEnable()));
        }
        where.and(ROLE.TENANT_ID.eq(tenantUtil.getTenantIdByServerName()));
        return where.orderBy(ROLE.SORT.asc()).fetchInto(Role.class);
    }

    @Override
    public int updateRoleState(RoleDTO roleDTO) {
        return context.update(ROLE)
                .set(ROLE.STATUS, roleDTO.getStatus())
                .set(ROLE.MODIFY_TIME, LocalDateTime.now())
                .where(ROLE.ID.eq(roleDTO.getRoleID())).and(ROLE.IS_SYSTEM.eq(0)).and(ROLE.ROLE_TYPE.eq(roleDTO.getRoleType()))
                .execute();
    }


     // TODO updateRoleSort 后续修改成一行更新
    @Override
    public void updateRoleSort(List<Role> list, String roleType) {
        List<UpdateConditionStep<RoleRecord>> updates = new ArrayList<>();
        for (Role role : list) {
            updates.add(context.update(ROLE)
                    .set(ROLE.SORT, role.getSort())
                    .where(ROLE.ID.eq(role.getRoleID())).and(ROLE.ROLE_TYPE.eq(roleType)));
        }
        context.batch(updates).execute();
    }

    @Override
    public Integer getMaxRoleSort(String roleType) {
        return context.select(ROLE.SORT)
                .from(ROLE)
                .where(ROLE.STATUS.eq(1))
                .and(ROLE.ROLE_TYPE.eq(roleType))
                .orderBy(ROLE.SORT.desc(),ROLE.CREATE_TIME.desc())
                .limit(1).fetchOneInto(Integer.class);
    }

    @Override
    public Role getSystemRoleInfo() {
        return context.select(ROLE.ID.as("roleID"),ROLE.ROLE_NAME,ROLE.DESCRIPTION,ROLE.LABEL,ROLE.AUTH,ROLE.ENABLE,ROLE.IS_SYSTEM,ROLE.ADMINISTRATOR,ROLE.IGNORE_FILE_SIZE,ROLE.SORT,ROLE.ROLE_TYPE,ROLE.MODIFY_TIME)
                .from(ROLE).where(ROLE.ADMINISTRATOR.eq(1).and(ROLE.IS_SYSTEM.eq(1))).limit(1).fetchOneInto(Role.class);
    }

    @Override
    public Role getRoleByKey(String code, Long tenantId){
        return context.select(ROLE.ID.as("roleID"),ROLE.ROLE_NAME,ROLE.DESCRIPTION,ROLE.LABEL,ROLE.AUTH,ROLE.ENABLE,ROLE.IS_SYSTEM,ROLE.ADMINISTRATOR,ROLE.IGNORE_FILE_SIZE,ROLE.SORT,ROLE.ROLE_TYPE,ROLE.MODIFY_TIME)
                .from(ROLE).where(ROLE.CODE.eq(code).and(ROLE.STATUS.eq(1)).and(ROLE.TENANT_ID.eq(tenantId))).fetchOneInto(Role.class);
    }
}
