package com.svnlan.home.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@Data
public class GetM3u8NewDTO {
    @NotNull
    private String key;

    private Long sourceID;
    private String sourceType;
    private String idType;
    private String idType2;
    private Integer isReview;
    private Integer isCamera;
    private Long busId;

    private Long userId;
    private Long f;

}
