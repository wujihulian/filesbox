package com.svnlan.home.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:40
 */
@Data
public class ShareVo extends HomeExplorerResult{

    private Long shareID;
    /** 分享标题 */
    private String title;
    /** shareid */
    private String shareHash;
    /** 分享用户id */
    private Long userID;
    /** 用户数据id */
    private Long sourceID;
    /** 分享文档路径 */
    private String sourcePath;
    /** 分享别名,替代shareHash */
    private String url;
    /** 是否外链分享；默认为0 */
    private Integer isLink;
    /** 是否为内部分享；默认为0 */
    private Integer isShareTo;
    /** 访问密码,为空则无密码 */
    private String password;
    /** 到期时间,0-永久生效*/
    private Long timeTo;
    /** 预览次数 */
    private Integer numView;
    /**  下载次数*/
    private Integer numDownload;
    /**  json 配置信息;是否可以下载,是否可以上传等*/
    private String options;
    /** 创建时间 */
    private Long createTime;
    /** 最后修改时间 */
    private Long modifyTime;

    private Boolean success;
    private Integer needPwd;
    private String message;
    private String code;
    /**  login 是否需要登录  1 是  0 否 */
    private Integer login;
    /** 下载次数下载 */
    private Integer downNum;
    /**  是否禁用 1 允许下载 0 禁用*/
    private Integer down;
    /** 是否禁用浏览 1 允许流量 0 禁用*/
    private Integer preview;
    private Long shareToTimeout;
    private List<ShareToVo> authTo;
    private HomeExplorerVO shareFile;
    private String avatar;
    private String nickname;
    private String userName;

    private Integer status;
}
