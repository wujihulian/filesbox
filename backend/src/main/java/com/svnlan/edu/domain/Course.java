package com.svnlan.edu.domain;

import lombok.Data;

@Data
public class Course {
    /** 课时id */
    private Long id;
    /**  */
    private Long rootId;
    /** 单元id */
    private Long courseCateId;
    private Long courseId;
    private Long courseCateTeacherId;
    private Integer type;
    /** 点击数 */
    private Integer pvNum;
    /** 直播状态 （0：未开始，1：直播中，2：已结束） */
    private Integer status;
    /** 课时名称 */
    private String courseName;
    private String orgName;
    private String courseUuid;
    private String teacherName;
    private String teacherIds;
    private String courseCateUuid;
    private String liveStartTime;
    private String liveEndTime;
    private String courseCateName;
    private String mainTeacherIds;
    private String cateName;
    private String termName;
    private String teacherAvatarImage;
    private String subjectName;
    private String coverImage;
    private String gradeName;
    private boolean review;
    /** 课时地址 */
    private String url;

}
