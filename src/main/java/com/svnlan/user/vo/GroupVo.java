package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 14:52
 */
@Data
public class GroupVo {

    private Long groupID;
    private String name;
    private String gr;
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

    private Long sourceID;
    private Long sourceLevel;
    private Long sourceParentID;
    private String sourceParentLevel;
}
