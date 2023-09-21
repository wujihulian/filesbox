package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/20 11:01
 */
@Data
public class HomeSettingDTO {

    private Long targetID;
    private Long createUser;
    private Long modifyUser;
    private Integer isFolder;

    private String sourceID;
    private String key;
    private String value;
    private String listViewKey;
    private String listViewValue;

}
