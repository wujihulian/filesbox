package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/17 16:26
 */
@Data
public class GroupSizeVo {
    private Long groupID;
    private Long sizeUse;
    public GroupSizeVo(){}
    public GroupSizeVo(Long groupID, Long sizeUse){
        this.groupID = groupID;
        this.sizeUse = sizeUse;
    }
}
