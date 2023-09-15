package com.svnlan.home.domain;

import com.svnlan.home.utils.ObjUtil;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/24 17:07
 */
@Data
public class IoSourceHistory {
    private Long id;
    private Long sourceID;
    private Long userID;
    private Long fileID;
    private Long size;
    private Long createTime;
    private Long modifyTime;
    private String detail;
    private Long parentID;
    private String parentLevel;
    private String name;
    private Integer targetType;

    public IoSourceHistory(){}
    public IoSourceHistory(Long sourceID, Long fileID, Long userID, Long size){
        this.sourceID = sourceID;
        this.fileID = fileID;
        this.userID = userID;
        this.size = size;
        this.detail = "";
    }
    public IoSourceHistory initializefield() {
        ObjUtil.initializefield(this);
        return this;
    }
}
