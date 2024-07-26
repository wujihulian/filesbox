package com.svnlan.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 之江汇返回数据
 *
 */
@NoArgsConstructor
@Data
public class ThirdDataResult<T> {
    @JsonProperty("code")
    private String code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private List<T> data;
}
