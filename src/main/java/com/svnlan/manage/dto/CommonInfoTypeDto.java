package com.svnlan.manage.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 16:58
 */
@Data
public class CommonInfoTypeDto {
    private String keyword;
    private Integer infoTypeID;
    private Integer parentID;
    private String typeName;
    private String parentLevel;
    private Integer status;
    private Integer sort;
    private Long createUser;
    private Integer showCount;
}
