package com.svnlan.edu.domain;

import lombok.Data;

import java.util.List;

@Data
public class EduInfo {
    /** 单元id */
    private Long courseCateId;
    /** 单元名称 */
    private String name;
    /** 1单元 2标签 */
    private Integer type;
    /** 标签列表 */
    private List<NodeInfo> nodeList;
    /** 课时列表 */
    private List<Course> courseList;
}
