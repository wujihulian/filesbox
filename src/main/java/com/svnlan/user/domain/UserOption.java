package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 10:19
 */
@Data
public class UserOption {

    private Long id;
    private Long userID;
    private String type;
    private String key;
    private String value;
    public UserOption(){}
    public UserOption(Long userID, String type, String key, String value){
        this.userID = userID;
        this.type = type;
        this.key = key;
        this.value = value;
    }
}
