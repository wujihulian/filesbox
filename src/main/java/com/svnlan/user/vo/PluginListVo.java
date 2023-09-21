package com.svnlan.user.vo;

import lombok.Data;

import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/19 11:56
 */
@Data
public class PluginListVo {

    private Integer pluginID;
    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private String path;
    private String type;
    private String enable;
    private String warnType;
    private String useRatio;
    private String useTime;
    private String sendType;
    private String target;
    private String dingUrl;
    private String wechatUrl;
    private String email;
    private Map<String, Object> wingMsg;
}
