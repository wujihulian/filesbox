package com.svnlan.manage.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/19 17:16
 */
@Data
public class CommonDesignClassify {
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
}
