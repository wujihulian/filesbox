package com.svnlan.manage.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 */
@Data
public class CommonDesignClassifyVo {
    private Integer designClassifyID;
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
    private List<CommonDesignClassifyVo> children;
    private Integer count;

    public CommonDesignClassifyVo(){}
    public CommonDesignClassifyVo(Integer designClassifyID, String parentLevel, List<CommonDesignClassifyVo> children){
        this.designClassifyID = designClassifyID;
        this.parentLevel = parentLevel;
        this.children = children;
    }
}
