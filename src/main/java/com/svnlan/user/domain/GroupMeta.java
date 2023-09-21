package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 17:00
 */
@Data
public class GroupMeta {

    private Long id;
    private Long groupID;
    private Long createTime;
    private Long modifyTime;
    private String key;
    private String value;
    public GroupMeta(){}
    public GroupMeta(Long groupID,String key,String value){
        this.groupID = groupID;
        this.key = key;
        this.value = value;

    }
}
