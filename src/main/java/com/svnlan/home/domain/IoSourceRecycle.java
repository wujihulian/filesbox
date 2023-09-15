package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/18 16:44
 */
@Data
public class IoSourceRecycle {

    private Long id;
    /** 文档所属类型 (0-sys,1-user,2-group)*/
    private Integer targetType;
    /** 拥有者对象id */
    private Long targetID;
    private Long sourceID;
    /** 操作者id */
    private Long userID;
    /** 文档上层关系;冗余字段,便于统计回收站信息 */
    private String parentLevel;


}
