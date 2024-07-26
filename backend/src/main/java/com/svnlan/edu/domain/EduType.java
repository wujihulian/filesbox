package com.svnlan.edu.domain;

import lombok.Data;

import java.util.List;

/**
 * 之江汇教学分类
 */
@Data
public class EduType {
    /** 类型id*/
    private Long id;
    private Long rlId;
    private Long parentId;
    private Boolean hasTag;
    /** 类型名称*/
    private String typeName;
    /**  类型 1学段 2年级 3学科 4版本 5册次*/
    private Integer type;
    private List<EduType> nodeList;
}
