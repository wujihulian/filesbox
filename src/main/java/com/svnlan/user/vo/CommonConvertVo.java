package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 11:27
 */
@Data
public class CommonConvertVo {

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
    private Long size;
    private String fileType;
    private String nickname;
    private String avatar;
    private String userName;
    private Integer isM3u8;
    private Integer stateSort;
    private Long createUser;
    private Long fileSize;

}
