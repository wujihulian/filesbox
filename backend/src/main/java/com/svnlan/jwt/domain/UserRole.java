package com.svnlan.jwt.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/10 15:37
 */
@Data
public class UserRole {

    private Long userID;
    private Integer administrator;
    private String auth;
}
