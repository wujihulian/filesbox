package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 17:26
 */
@Data
public class UserMeta {

    private Long id;
    private Long userID;
    private Long createTime;
    private Long modifyTime;
    private String key;
    private String value;

    public UserMeta(){}
    public UserMeta(Long userID, String key, String value){
        this.userID = userID;
        this.key = key;
        this.value = value;
    }
}
