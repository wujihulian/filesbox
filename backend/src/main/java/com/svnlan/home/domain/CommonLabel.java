package com.svnlan.home.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 17:22
 */
@Data
public class CommonLabel {

    private Long labelId;
    private Long userID;
    private String labelName;
    private String labelEnName;
    private String enNameSimple;
    private Integer status;
    private Long createTime;
    private Long modifyTime;
    private String style;
    private Integer sort;
    private Integer tagType;

}
