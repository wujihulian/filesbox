package com.svnlan.user.vo;

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
public class ThirdSearchVo<T> {

    @JsonProperty("data")
    private List<T> data;

}
