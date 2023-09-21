package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/7 17:59
 */
@Data
public class UserOptionVo {

    private Long id;
    private Long userID;
    private String type;
    private String key;
    private String value;
}
