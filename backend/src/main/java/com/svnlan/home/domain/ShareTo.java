package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/8 13:11
 */
@Data
public class ShareTo {
    private Long id;
    private Long shareID;
    private Integer targetType;
    private Long targetID;
    private Integer authID;
    private Integer authDefine;
    private Long createTime;
    private Long modifyTime;
}
