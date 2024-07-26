package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 13:53
 */
@Data
public class SystemOption {

    private Integer id;
    private String type;
    private String key;
    private String value;
    private Long createTime;
    private Long modifyTime;
}
