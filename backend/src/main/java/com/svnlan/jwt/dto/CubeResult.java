package com.svnlan.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 魔方返回数据
 *
 */
@Data
public class CubeResult<T> {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("errCode")
    private String errCode;
    @JsonProperty("errMessage")
    private String errMessage;
    @JsonProperty("requestId")
    private Object requestId;
    @JsonProperty("data")
    private T data;
}
