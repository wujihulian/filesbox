package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 14:48
 */
@Data
public class Group {

    private Long groupID;
    private String name;
    /** 父群组id*/
    private Long parentID;
    /** 父路径id; 例如:  ,2,5,10,*/
    private String parentLevel;
    /** 扩展字段 */
    private String extraField;
    private Integer sort;
    /** 群组存储空间大小(GB) 0-不限制 */
    private Double sizeMax;
    /** 已使用大小(byte)*/
    private Long sizeUse;
    private Long createTime;
    private Long modifyTime;
    private Integer status;
    private Integer hasChildren;
    private String authShowType;
    private String authShowGroup;
    private Integer isSystem;
    private String pathDisplay;
    private Long dingDeptId;
    private String thirdDeptId;

    public Group(){}
    public Group(Long groupID, Integer sort){
        this.groupID = groupID;
        this.sort = sort;
    }
}
