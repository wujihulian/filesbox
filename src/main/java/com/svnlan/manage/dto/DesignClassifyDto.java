package com.svnlan.manage.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/19 17:16
 */
@Data
public class DesignClassifyDto {
    private String keyword;
    private Integer designClassifyID;
    private Integer parentID;
    private String typeName;
    private String parentLevel;
    private Integer status;
    private Integer sort;
    private Long createUser;
    private Integer showCount;
}
