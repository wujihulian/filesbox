package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/8 13:11
 */
@Data
public class ShareToVo {
    private Long id;
    private Long shareID;
    private Integer targetType;
    private Long targetID;
    private Integer authID;
    private Integer authDefine;
    private Long createTime;
    private Long modifyTime;
}
