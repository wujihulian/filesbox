package com.svnlan.jwt.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/7 9:17
 */
@Data
public class TargetSpaceVo {

    /** 群组存储空间大小(GB) 0-不限制  */
    private Double sizeMax;
    /** 已使用大小(byte)*/
    private Long sizeUse ;

    public TargetSpaceVo(){};
    public TargetSpaceVo(Double sizeMax, Long sizeUse){
        this.sizeMax = sizeMax;
        this.sizeUse = sizeUse;
    };
}
