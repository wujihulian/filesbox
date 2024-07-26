package com.svnlan.manage.domain;

import lombok.Data;

/**
 * @Author:
 * @Description:
 */
@Data
public class CommonSeo {
    private Long id;
    private String title;
    private String keyword = "";
    private String description = "";
    private String typeStr;
    private String seoJson;
    private String detail;
    private String seoKeyword = "";
    private String seoDescription = "";
    private Boolean single;
    private String infoType;
    private Integer isUrlInfo;

    private String realName;
    private String headPortrait;
    private Long userId;

}
