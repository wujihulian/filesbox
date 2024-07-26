/*
 * This file is generated by jOOQ.
 */
package com.svnlan.jooq.tables.records;


import com.svnlan.jooq.tables.Role;

import java.time.LocalDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record18;
import org.jooq.Row18;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 角色表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoleRecord extends UpdatableRecordImpl<RoleRecord> implements Record18<Integer, String, String, String, String, String, Integer, Integer, Integer, Integer, Double, String, Integer, String, LocalDateTime, LocalDateTime, LocalDateTime, Long> {

    private static final long serialVersionUID = -1357652338;

    /**
     * Setter for <code>cloud_disk.role.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>cloud_disk.role.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>cloud_disk.role.role_name</code>. 角色名称
     */
    public void setRoleName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>cloud_disk.role.role_name</code>. 角色名称
     */
    public String getRoleName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>cloud_disk.role.code</code>. code
     */
    public void setCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>cloud_disk.role.code</code>. code
     */
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>cloud_disk.role.description</code>. 描述
     */
    public void setDescription(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>cloud_disk.role.description</code>. 描述
     */
    public String getDescription() {
        return (String) get(3);
    }

    /**
     * Setter for <code>cloud_disk.role.label</code>. label
     */
    public void setLabel(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>cloud_disk.role.label</code>. label
     */
    public String getLabel() {
        return (String) get(4);
    }

    /**
     * Setter for <code>cloud_disk.role.auth</code>. 权限
     */
    public void setAuth(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>cloud_disk.role.auth</code>. 权限
     */
    public String getAuth() {
        return (String) get(5);
    }

    /**
     * Setter for <code>cloud_disk.role.status</code>. 状态，1正常，2删除
     */
    public void setStatus(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>cloud_disk.role.status</code>. 状态，1正常，2删除
     */
    public Integer getStatus() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>cloud_disk.role.enable</code>. 是否启用，0未启用，1启用
     */
    public void setEnable(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>cloud_disk.role.enable</code>. 是否启用，0未启用，1启用
     */
    public Integer getEnable() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>cloud_disk.role.is_system</code>. 是否系统配置
     */
    public void setIsSystem(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>cloud_disk.role.is_system</code>. 是否系统配置
     */
    public Integer getIsSystem() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>cloud_disk.role.administrator</code>. 是否系统管理员
     */
    public void setAdministrator(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>cloud_disk.role.administrator</code>. 是否系统管理员
     */
    public Integer getAdministrator() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>cloud_disk.role.ignore_file_size</code>. 上传文件大小限制
     */
    public void setIgnoreFileSize(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>cloud_disk.role.ignore_file_size</code>. 上传文件大小限制
     */
    public Double getIgnoreFileSize() {
        return (Double) get(10);
    }

    /**
     * Setter for <code>cloud_disk.role.ignore_ext</code>.
     */
    public void setIgnoreExt(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>cloud_disk.role.ignore_ext</code>.
     */
    public String getIgnoreExt() {
        return (String) get(11);
    }

    /**
     * Setter for <code>cloud_disk.role.sort</code>. 排序
     */
    public void setSort(Integer value) {
        set(12, value);
    }

    /**
     * Getter for <code>cloud_disk.role.sort</code>. 排序
     */
    public Integer getSort() {
        return (Integer) get(12);
    }

    /**
     * Setter for <code>cloud_disk.role.role_type</code>. 角色类型，1用户角色，2文档角色
     */
    public void setRoleType(String value) {
        set(13, value);
    }

    /**
     * Getter for <code>cloud_disk.role.role_type</code>. 角色类型，1用户角色，2文档角色
     */
    public String getRoleType() {
        return (String) get(13);
    }

    /**
     * Setter for <code>cloud_disk.role.create_time</code>. 创建时间
     */
    public void setCreateTime(LocalDateTime value) {
        set(14, value);
    }

    /**
     * Getter for <code>cloud_disk.role.create_time</code>. 创建时间
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(14);
    }

    /**
     * Setter for <code>cloud_disk.role.modify_time</code>. 最后修改时间
     */
    public void setModifyTime(LocalDateTime value) {
        set(15, value);
    }

    /**
     * Getter for <code>cloud_disk.role.modify_time</code>. 最后修改时间
     */
    public LocalDateTime getModifyTime() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>cloud_disk.role.delete_time</code>. 删除时间
     */
    public void setDeleteTime(LocalDateTime value) {
        set(16, value);
    }

    /**
     * Getter for <code>cloud_disk.role.delete_time</code>. 删除时间
     */
    public LocalDateTime getDeleteTime() {
        return (LocalDateTime) get(16);
    }

    /**
     * Setter for <code>cloud_disk.role.tenant_id</code>. 租户id
     */
    public void setTenantId(Long value) {
        set(17, value);
    }

    /**
     * Getter for <code>cloud_disk.role.tenant_id</code>. 租户id
     */
    public Long getTenantId() {
        return (Long) get(17);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record18 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row18<Integer, String, String, String, String, String, Integer, Integer, Integer, Integer, Double, String, Integer, String, LocalDateTime, LocalDateTime, LocalDateTime, Long> fieldsRow() {
        return (Row18) super.fieldsRow();
    }

    @Override
    public Row18<Integer, String, String, String, String, String, Integer, Integer, Integer, Integer, Double, String, Integer, String, LocalDateTime, LocalDateTime, LocalDateTime, Long> valuesRow() {
        return (Row18) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Role.ROLE.ID;
    }

    @Override
    public Field<String> field2() {
        return Role.ROLE.ROLE_NAME;
    }

    @Override
    public Field<String> field3() {
        return Role.ROLE.CODE;
    }

    @Override
    public Field<String> field4() {
        return Role.ROLE.DESCRIPTION;
    }

    @Override
    public Field<String> field5() {
        return Role.ROLE.LABEL;
    }

    @Override
    public Field<String> field6() {
        return Role.ROLE.AUTH;
    }

    @Override
    public Field<Integer> field7() {
        return Role.ROLE.STATUS;
    }

    @Override
    public Field<Integer> field8() {
        return Role.ROLE.ENABLE;
    }

    @Override
    public Field<Integer> field9() {
        return Role.ROLE.IS_SYSTEM;
    }

    @Override
    public Field<Integer> field10() {
        return Role.ROLE.ADMINISTRATOR;
    }

    @Override
    public Field<Double> field11() {
        return Role.ROLE.IGNORE_FILE_SIZE;
    }

    @Override
    public Field<String> field12() {
        return Role.ROLE.IGNORE_EXT;
    }

    @Override
    public Field<Integer> field13() {
        return Role.ROLE.SORT;
    }

    @Override
    public Field<String> field14() {
        return Role.ROLE.ROLE_TYPE;
    }

    @Override
    public Field<LocalDateTime> field15() {
        return Role.ROLE.CREATE_TIME;
    }

    @Override
    public Field<LocalDateTime> field16() {
        return Role.ROLE.MODIFY_TIME;
    }

    @Override
    public Field<LocalDateTime> field17() {
        return Role.ROLE.DELETE_TIME;
    }

    @Override
    public Field<Long> field18() {
        return Role.ROLE.TENANT_ID;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getRoleName();
    }

    @Override
    public String component3() {
        return getCode();
    }

    @Override
    public String component4() {
        return getDescription();
    }

    @Override
    public String component5() {
        return getLabel();
    }

    @Override
    public String component6() {
        return getAuth();
    }

    @Override
    public Integer component7() {
        return getStatus();
    }

    @Override
    public Integer component8() {
        return getEnable();
    }

    @Override
    public Integer component9() {
        return getIsSystem();
    }

    @Override
    public Integer component10() {
        return getAdministrator();
    }

    @Override
    public Double component11() {
        return getIgnoreFileSize();
    }

    @Override
    public String component12() {
        return getIgnoreExt();
    }

    @Override
    public Integer component13() {
        return getSort();
    }

    @Override
    public String component14() {
        return getRoleType();
    }

    @Override
    public LocalDateTime component15() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime component16() {
        return getModifyTime();
    }

    @Override
    public LocalDateTime component17() {
        return getDeleteTime();
    }

    @Override
    public Long component18() {
        return getTenantId();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getRoleName();
    }

    @Override
    public String value3() {
        return getCode();
    }

    @Override
    public String value4() {
        return getDescription();
    }

    @Override
    public String value5() {
        return getLabel();
    }

    @Override
    public String value6() {
        return getAuth();
    }

    @Override
    public Integer value7() {
        return getStatus();
    }

    @Override
    public Integer value8() {
        return getEnable();
    }

    @Override
    public Integer value9() {
        return getIsSystem();
    }

    @Override
    public Integer value10() {
        return getAdministrator();
    }

    @Override
    public Double value11() {
        return getIgnoreFileSize();
    }

    @Override
    public String value12() {
        return getIgnoreExt();
    }

    @Override
    public Integer value13() {
        return getSort();
    }

    @Override
    public String value14() {
        return getRoleType();
    }

    @Override
    public LocalDateTime value15() {
        return getCreateTime();
    }

    @Override
    public LocalDateTime value16() {
        return getModifyTime();
    }

    @Override
    public LocalDateTime value17() {
        return getDeleteTime();
    }

    @Override
    public Long value18() {
        return getTenantId();
    }

    @Override
    public RoleRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public RoleRecord value2(String value) {
        setRoleName(value);
        return this;
    }

    @Override
    public RoleRecord value3(String value) {
        setCode(value);
        return this;
    }

    @Override
    public RoleRecord value4(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public RoleRecord value5(String value) {
        setLabel(value);
        return this;
    }

    @Override
    public RoleRecord value6(String value) {
        setAuth(value);
        return this;
    }

    @Override
    public RoleRecord value7(Integer value) {
        setStatus(value);
        return this;
    }

    @Override
    public RoleRecord value8(Integer value) {
        setEnable(value);
        return this;
    }

    @Override
    public RoleRecord value9(Integer value) {
        setIsSystem(value);
        return this;
    }

    @Override
    public RoleRecord value10(Integer value) {
        setAdministrator(value);
        return this;
    }

    @Override
    public RoleRecord value11(Double value) {
        setIgnoreFileSize(value);
        return this;
    }

    @Override
    public RoleRecord value12(String value) {
        setIgnoreExt(value);
        return this;
    }

    @Override
    public RoleRecord value13(Integer value) {
        setSort(value);
        return this;
    }

    @Override
    public RoleRecord value14(String value) {
        setRoleType(value);
        return this;
    }

    @Override
    public RoleRecord value15(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public RoleRecord value16(LocalDateTime value) {
        setModifyTime(value);
        return this;
    }

    @Override
    public RoleRecord value17(LocalDateTime value) {
        setDeleteTime(value);
        return this;
    }

    @Override
    public RoleRecord value18(Long value) {
        setTenantId(value);
        return this;
    }

    @Override
    public RoleRecord values(Integer value1, String value2, String value3, String value4, String value5, String value6, Integer value7, Integer value8, Integer value9, Integer value10, Double value11, String value12, Integer value13, String value14, LocalDateTime value15, LocalDateTime value16, LocalDateTime value17, Long value18) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
        value18(value18);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoleRecord
     */
    public RoleRecord() {
        super(Role.ROLE);
    }

    /**
     * Create a detached, initialised RoleRecord
     */
    public RoleRecord(Integer id, String roleName, String code, String description, String label, String auth, Integer status, Integer enable, Integer isSystem, Integer administrator, Double ignoreFileSize, String ignoreExt, Integer sort, String roleType, LocalDateTime createTime, LocalDateTime modifyTime, LocalDateTime deleteTime, Long tenantId) {
        super(Role.ROLE);

        set(0, id);
        set(1, roleName);
        set(2, code);
        set(3, description);
        set(4, label);
        set(5, auth);
        set(6, status);
        set(7, enable);
        set(8, isSystem);
        set(9, administrator);
        set(10, ignoreFileSize);
        set(11, ignoreExt);
        set(12, sort);
        set(13, roleType);
        set(14, createTime);
        set(15, modifyTime);
        set(16, deleteTime);
        set(17, tenantId);
    }
}