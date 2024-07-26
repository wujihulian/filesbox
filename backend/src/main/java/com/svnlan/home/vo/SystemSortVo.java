package com.svnlan.home.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/15 14:48
 */
@Data
public class SystemSortVo {

    /** 文件夹 icon 显示大小*/
    private String fileIconSize;
    /** 文件夹 排序字段*/
    private String listSortField;
    /** 文件夹 排序升序降序*/
    private String listSortOrder;
    /** 显示方式 0 适用于所有文件夹 1 适用于单个文件夹 */
    private String listSortKeep;
    private String listType;

    private String sourceID;

    public SystemSortVo(){}
    public SystemSortVo(String sourceID,String listType,String fileIconSize){
        this.sourceID = sourceID;
        this.listType = listType;
        this.fileIconSize = fileIconSize;
    }
    public SystemSortVo(String sourceID,String listSortField,String listSortOrder, boolean sort){
        this.sourceID = sourceID;
        this.listSortField = listSortField;
        this.listSortOrder = listSortOrder;
    }

    List<SystemSortVo> listSortList;
    List<SystemSortVo> listTypeList;

    // 'listSortField','listSortOrder','fileIconSize','listSortKeep','listSort','listType'
   // ('listSortField','listSortOrder','fileIconSize','listSortKeep','listSort','listType')
/*
SELECT *
FROM user_option
where userID = 1 and key1 in ('listSortField','listSortOrder','fileIconSize','listSortKeep','listSort','listType')
 */
}
