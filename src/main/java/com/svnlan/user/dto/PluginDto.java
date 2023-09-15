package com.svnlan.user.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/19 12:05
 */
@Data
public class PluginDto {
    private Integer pluginID;
    private Integer id;
    private String name;
    private String description;
    private String path;
    private Integer status;
    private String value;
    private String enable;
    private String warnType;
    private String useRatio;
    private String useTime;
    private String sendType;
    private String target;
    private String dingUrl;
    private String wechatUrl;


}
