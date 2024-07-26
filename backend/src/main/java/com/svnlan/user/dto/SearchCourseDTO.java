package com.svnlan.user.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

@Data
public class SearchCourseDTO extends PageQuery {

    /** 关键词  必填 */
    private String keyword;
    /** 搜索类型 0默认全部 1名称 2老师 3学校 0  必填 */
    private String searchType;
    /** 学段 0是全部  */
    private Integer periodId;
    /** 年级 0是全部  */
    private Integer gradeId;
    /** 学科 0是全部  */
    private Integer subjectId;
    /** 版本 0是全部  */
    private Integer editionId;
    /** 册次 0是全部  */
    private Integer volumeId;
    /** 页大小  必填 */
    //private Integer pageSize;
    /** 第几页  必填 */
    private Integer pageNum;

    /** 课时id (课件) */
    private Long courseId;
    /** 课程id */
    private Long courseCateId;
}
