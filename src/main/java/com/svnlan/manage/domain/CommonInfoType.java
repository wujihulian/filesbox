package com.svnlan.manage.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:48
 */
@Data
public class CommonInfoType {
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
}
