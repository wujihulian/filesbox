package com.svnlan.home.dto;

import lombok.Data;

/**
 * @author KingMgg
 * @data 2023/2/8 11:45
 */
@Data
public class ExplorerShareDTO {
    /**
     * 自增id
     */
    Long shareID;
    /**
     * 是否外链分享；默认为0
     */
    Long isLink;
    /**
     * 用户数据id
     */
    Long sourceID;
    /**
     * 是否为内部分享；默认为0
     */
    Integer isShareTo;
    /**
     * 分享标题
     */
    String title;
    /**
     * 分享文档路径
     */
    String sourcePath;
    /**
     * 分享别名,替代shareHash
     */
    String url;
    /**
     * shareid
     */
    String shareHash;
    /**
     * 访问密码,为空则无密码
     */
    String password;
    /**
     * json 配置信息;是否可以下载,是否可以上传等
     */
    Options options;
    /**
     * 到期时间,0-永久生效
     */
    Long timeTo;

    @Data
    public static class Options {
        String login;
        String preview;
        String down;
        String downNum;
    }
}
