package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 17:51
 */
@Data
public class CommonLabelVo {

    private Long labelId;
    private Long tagID;
    private Long userID;
    private String labelName;
    private String tagName;
    private String labelEnName;
    private String enNameSimple;
    private Integer status;
    private Long createTime;
    private Long modifyTime;
    private String style;
    private Integer sort;
    private Integer selected;
    private Integer tagType;
    private String labelColor;

    public CommonLabelVo(){}
    public CommonLabelVo(Long tagID, Long userID, String labelName, String style){
        this.tagID = tagID;
        this.userID = userID;
        this.labelName = labelName;
        this.style = style;
    }
}
