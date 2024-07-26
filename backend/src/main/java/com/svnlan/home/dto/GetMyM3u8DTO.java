package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2020/9/10 8:51
 */
@Data
public class GetMyM3u8DTO {

    private String sourceType;
    private String sourceID;
    private String isCamera;
    private String isReview;
    private String key;
}
