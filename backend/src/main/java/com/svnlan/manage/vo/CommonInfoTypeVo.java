package com.svnlan.manage.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:58
 */
@Data
public class CommonInfoTypeVo {
    private Integer infoTypeID;
    private Integer parentID;
    private String typeName;
    private String parentLevel;
    private Integer status;
    private Integer sort;
    private Long createTime;
    private Long modifyTime;
    private String namePinyin;
    private String namePinyinSimple;
    private Long createUser;
    private List<CommonInfoTypeVo> children;
    private Integer count;

    public CommonInfoTypeVo(){}
    public CommonInfoTypeVo(Integer infoTypeID, String parentLevel, List<CommonInfoTypeVo> children){
        this.infoTypeID = infoTypeID;
        this.parentLevel = parentLevel;
        this.children = children;
    }
}
