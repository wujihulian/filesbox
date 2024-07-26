package com.svnlan.edu.domain;

import lombok.Data;

import java.util.List;

@Data
public class CatalogueResult {
    List<EduInfo> catalogueList;
    /** 学段名称 */
    private String periodName;
    /** 年级名称 */
    private String gradeName;
    /** 学科名称 */
    private String subjectName;
    /** 版本名称 */
    private String editionName;
    /** 册次名称 */
    private String volumeName;

    private Integer pvNum;

    private Integer courseNum;
}
