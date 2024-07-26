package com.svnlan.user.domain;

import com.svnlan.home.utils.ObjUtil;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 11:27
 */
@Data
public class CommonConvert {

    private Long id;
    private Long convertID;
    private Long sourceID;
    private Long fileID;
    private Long userID;
    private String name;
    private String state;
    private String remark;
    private Integer frequencyCount;
    private Long createTime;
    private Long modifyTime;
    private Integer scheduleFrequencyCount;
    private Long scheduleTime;
    private Long tenantId;

    public CommonConvert initializefield() {
        ObjUtil.initializefield(this);
        return this;
    }
}
