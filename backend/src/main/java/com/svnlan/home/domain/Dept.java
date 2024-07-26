package com.svnlan.home.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 钉钉部门
 *
 * @author lingxu 2023/03/29 11:10
 */
@NoArgsConstructor
@Data
public class Dept {
    /**
     * 部门群已经创建后，有新人加入部门是否会自动加入该群
     */
    @JSONField(name = "auto_add_user")
    private Boolean autoAddUser;
    /**
     * 是否同步创建一个关联此部门的企业群
     */
    @JSONField(name = "create_dept_group")
    private Boolean createDeptGroup;
    /**
     * 部门ID
     */
    @JSONField(name = "dept_id")
    private Integer deptId;
    @JSONField(name = "ext")
    private String ext;
    /**
     * 部门名称
     */
    @JSONField(name = "name")
    private String name;
    /**
     * 父部门id
     */
    @JSONField(name = "parent_id")
    private Integer parentId;
}
