package com.svnlan.user.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/27 17:06
 */
@Data
public class GroupSource {

    private Long groupID;
    private Long sourceID;
    private Long createTime;
    public GroupSource(){}
    public GroupSource(Long groupID, Long sourceID){
        this.groupID = groupID;
        this.sourceID = sourceID;
    }
}
