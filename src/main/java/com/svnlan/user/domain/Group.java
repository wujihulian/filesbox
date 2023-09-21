package com.svnlan.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 14:48
 */
@Data
@TableName(value = "`group`")
public class Group {

    @TableId(value = "groupID")
    private Long groupID;
    private String name;
    /** 父群组id*/
    private Long parentID;
    /** 父路径id; 例如:  ,2,5,10,*/
    @TableField(value = "parentLevel")
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

    public Group(){}
    public Group(Long groupID, Integer sort){
        this.groupID = groupID;
        this.sort = sort;
    }
}
