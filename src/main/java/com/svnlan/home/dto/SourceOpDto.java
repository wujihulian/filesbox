package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/18 10:05
 */
@Data
public class SourceOpDto {
    private Long id;
    private Long favID;
    private Long sourceID;
    private Long parentID;
    private String type;
    private String parentLevel;
    private String name;
    private String oldName;
    private String path;
    private String namePinyin;
    private String namePinyinSimple;
}
