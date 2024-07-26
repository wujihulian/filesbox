package com.svnlan.user.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 分享资源
 *
 * @TableName share
 */
@Data
public class Share implements Serializable {
    /**
     * 自增id
     */
    private Long shareID;

    private String avatar;

    private String nickname;

    private String name;

    /**
     * 分享标题
     */
    private String title;

    /**
     * shareid
     */
    private String shareHash;

    /**
     * 分享用户id
     */
    private Long userID;

    /**
     * 用户数据id
     */
    private Long sourceID;

    /**
     * 分享文档路径
     */
    private String sourcePath;

    /**
     * 分享别名,替代shareHash
     */
    private String url;

    /**
     * 是否外链分享；默认为0
     */
    private Integer isLink;

    /**
     * 是否为内部分享；默认为0
     */
    private Integer isShareTo;

    /**
     * 访问密码,为空则无密码
     */
    private String password;

    /**
     * 到期时间,0-永久生效
     */
    private Long timeTo;

    /**
     * 预览次数
     */
    private Integer numView;

    /**
     * 下载次数
     */
    private Integer numDownload;

    /**
     * json 配置信息;是否可以下载,是否可以上传等
     */
    private String options;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后修改时间
     */
    private Long modifyTime;

    private static final long serialVersionUID = 1L;
}