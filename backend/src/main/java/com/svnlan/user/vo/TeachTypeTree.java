package com.svnlan.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 之江汇返回 - 教学分类树
 *
 */
@NoArgsConstructor
@Data
public class TeachTypeTree<T> {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("typeName")
    private String typeName;
    /** 类型 1学段 2年级 3学科 4版本 5册次 */
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("nodeList")
    private List<T> nodeList;
}
