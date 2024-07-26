package com.svnlan.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 之江汇返回数据
 *
 */
@NoArgsConstructor
@Data
public class ThirdSearchResult<T> {
    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("total")
    private Long total;
    @JsonProperty("pages")
    private Integer pages;
    @JsonProperty("pageSize")
    private Integer pageSize;
    @JsonProperty("current")
    private Integer current;
    @JsonProperty("pageNum")
    private Integer pageNum;
    @JsonProperty("hasNext")
    private Boolean hasNext;
    @JsonProperty("data")
    private T data;
}
