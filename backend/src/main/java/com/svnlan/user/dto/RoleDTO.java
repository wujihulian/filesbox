package com.svnlan.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description: 角色DTO
 */
@Data
public class RoleDTO {

    private String keyword; //条件
    private Integer roleID; //角色ID
    private String roleName; //角色名称
    private String description; //描述
    private Integer status; //状态
    private List<Integer> rightsIds; //模块权限ids

    private String code;
    private String label;
    private String auth;
    private Integer enable;
    /** 是否系统配置*/
    private Integer isSystem;
    /** 是否系统管理员**/
    private Integer administrator;
    /** 上传文件大小限制*/
    private Double ignoreFileSize;
    private String ignoreExt;
    private Integer sort;
    private String roleType;
    private String Ids;

 }
