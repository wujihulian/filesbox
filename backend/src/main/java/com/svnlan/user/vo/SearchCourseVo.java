package com.svnlan.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchCourseVo {

    /** 课时id (课件) */
    @JsonProperty("courseId")
    private Long courseId;
    /** 课程id */
    @JsonProperty("courseCateId")
    private Long courseCateId;
    /** 课时名称 */
    @JsonProperty("courseName")
    private String courseName;
    /** 老师 */
    @JsonProperty("teacherName")
    private String teacherName;
    /** 老师头像 */
    @JsonProperty("teacherAvatarImage")
    private String teacherAvatarImage;
    /** 机构名称 */
    @JsonProperty("orgName")
    private String orgName;
    /** 直播状态 （0：未开始，1：直播中，2：已结束） */
    @JsonProperty("status")
    private Integer status;
    /** 地址 */
    @JsonProperty("url")
    private String url;
    /**  */
    @JsonProperty("liveStartTime")
    private String liveStartTime;
    /** 课时结束时间 */
    @JsonProperty("liveEndTime")
    private String liveEndTime;
    /** 点击数 */
    @JsonProperty("pvNum")
    private Integer pvNum;
    /**  */
     @JsonProperty("mainTeacherIds")
     private String mainTeacherIds;
    /**  */
    @JsonProperty("teacherIds")
    private String teacherIds;
    /**  */
    @JsonProperty("teacherId")
    private Integer teacherId;
    @JsonProperty("courseCateTeacherId")
    private Integer courseCateTeacherId;

}
