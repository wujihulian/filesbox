package com.svnlan.user.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 15:03
 */
@Data
public class UserGroupVo {
    private Long userID;
    private Long groupID;
    private String name;
    /** 父群组id*/
    private Long parentID;
    private String parentName;
    /** 父路径id; 例如:  ,2,5,10,*/
    private String parentLevel;
    /** 扩展字段 */
    private String extraField;
    private Integer sort;
    /** 群组存储空间大小(GB) 0-不限制 */
    private Double sizeMax;
    /** 已使用大小(byte)*/
    private Long sizeUse;
    private Integer authID;
    private String code;
    private String authName;
    private String auth;
    private String groupAuth;
    private Integer isSystem;
    private Integer administrator;
    private boolean hasChildren;
    List<UserGroupVo> children;
    private String groupName;

    /** 用户角色 */
    private Integer roleID;
    /** 邮箱 */
    private String email;
    /** 手机 */
    private String phone;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 性别 (0女1男) */
    private Integer sex;
    /** 用户启用状态 0-未启用 1-启用 */
    private Integer status;
    private String label;

    public UserGroupVo(){}
    public UserGroupVo(Long groupID, Integer authID){
        this.groupID = groupID;
        this.authID = authID;
    }
    public UserGroupVo(Long groupID, Long parentID, String name, String parentName, boolean hasChildren){
        this.groupID = groupID;
        this.parentID = parentID;
        this.hasChildren = hasChildren;
        this.name = name;
        this.parentName = parentName;
    }

}
