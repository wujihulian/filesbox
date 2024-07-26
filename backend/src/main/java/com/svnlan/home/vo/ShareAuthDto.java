package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/8 13:08
 */
@Data
public class ShareAuthDto {
    private Integer authID;
    private Long targetID;
    private Integer targetType;
}
