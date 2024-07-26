package com.svnlan.user.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:37
 */
@Data
public class GroupDTO extends PageQuery {

    private Long groupID;
    private String keyword; //条件
    private String name;
    /** 父群组id*/
    private Long parentID;
    /** 父路径id; 例如:  ,2,5,10,*/
    private String parentLevel;
    /** 扩展字段 */
    private String extraField;
    private Integer sort;
    private Integer status;
    /** 群组存储空间大小(GB) 0-不限制 */
    private Double sizeMax;
    /** 已使用大小(byte)*/
    private Long sizeUse;
    private String ids;
    /** 该部门成员协作分享时,可以选择其他所有部门(及用户) all-所有 hide-仅支持选择当前部门及子部门(及用户) select-指定部门 */
    private String authShowType;
    /** authShowType 指定部门的id，多个部门用英文逗号隔开*/
    private String authShowGroup;
}
