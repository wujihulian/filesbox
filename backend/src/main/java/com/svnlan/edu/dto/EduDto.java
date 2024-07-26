package com.svnlan.edu.dto;

import lombok.Data;

@Data
public class EduDto {

    /** 学段 */
    private Long periodId;
    /** 年级 */
    private Long gradeId;
    /** 学科 */
    private Long subjectId;
    /** 版本 */
    private Long editionId;
    /** 册次 */
    private Long volumeId;

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

    /** 关键词 */
    private String keyword;
    /** 搜索类型 0默认全部 1名称 2老师 3学校 */
    private String searchType;
    private Integer pageSize;
    private Integer pageNum;

    private Long courseCateId;
    private String tagName;
}
