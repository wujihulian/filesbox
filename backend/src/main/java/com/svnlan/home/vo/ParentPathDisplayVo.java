package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/22 15:19
 */
@Data
public class ParentPathDisplayVo {
    private String parentLevel;
    private String parentIDStr;
    private String parentLevelName;
    private Integer targetType;
    private Long sourceID;
    private String name;
}
