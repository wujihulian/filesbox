package com.svnlan.user.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
   * @Description: 角色DOMAIN
   * @Author:  sulijuan
   * @Modified:
   */
@Data
public class Role {
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
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private LocalDateTime deleteTime;

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

    public Role(){}
    public Role(Integer roleID, Integer sort){
        this.roleID = roleID;
        this.sort = sort;
    }


}