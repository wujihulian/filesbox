package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/24 17:10
 */
@Data
public class IoSourceHistoryVo {
    private Long id;
    private Long sourceID;
    private Long userID;
    private Long fileID;
    private Long size;
    private Long createTime;
    private Long modifyTime;
    private String detail;
    private String name;
    private String avatar;
    private String nickname;
    private String sex;
    private String status;
}
