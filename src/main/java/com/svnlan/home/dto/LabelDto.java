package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/2 11:02
 */
@Data
public class LabelDto {

    private Long labelId;
    private Long tagID;
    private String labelName;
    private String style;
    private Integer sort;
    private String block;
    private String files;
    private Integer tagType;
}
