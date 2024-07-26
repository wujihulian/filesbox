package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/16 15:43
 */
@Data
public class RoleVo {
    /**  角色id */
    private Integer roleID;
    /** 角色名称 */
    private String roleName;

    private String code;
    /** 描述 */
    private String description;
    private String label;
    private String auth;
    /** 创建时间 */
    private Long createTime;
    private Long modifyTime;
    private Long deleteTime;

    /** 状态，1正常，2删除 */
    private Integer status;
    /** 是否启用，0未启用，1启用 */
    private Integer enable;
    /** 是否系统配置 */
    private Integer isSystem;
    /** 是否系统管理员 */
    private Integer administrator;
    /** 上传文件大小限制 单位 GB */
    private Double ignoreFileSize ;

    private String ignoreExt;
    private Integer sort;
    /** 角色类型，1用户角色，2文档角色 */
    private String roleType;
    private Long tenantId;
}
