package com.svnlan.manage.domain;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/11 10:33
 */
@Data
public class CommonInfo {

    private Long infoID;
    private String title;
    /** 资讯图片地址，电脑端 */
    private String computerPicPath;
    /** 资讯图片地址，手机端 */
    private String mobilePicPath;
    /** 状态 0禁用、草稿，1启用 2 删除 */
    private Integer status;
    /** 资讯内容 */
    private String detail;
    /** 资讯附件地址，最多5个附件，json格式(文件id，文件名，文件类型，地址) */
    private String fileDetail;
    private Long createTime;
    private Long modifyTime;
    private Long createUser;
    private Long modifyUser;
    /** 编辑资讯的IP地址，json(222.22.22.22,杭州) */
    private String userIp;
    /** 资讯类型id */
    private Integer infoTypeID;
    /**  */
    private Integer sort;
    /** 资讯摘要 */
    private String introduce;
    /**  是否置顶 */
    private Integer isTop;
    /** 置顶时间 */
    private Long topTime;
    /** keyword, description json */
    private String seo;
    /** 资讯来源json, name,author,url */
    private String infoSource;
    /**  */
    private Integer isApplyOriginal;
    /**  短视频id */
    private Long videoID;
    /**  缩略图*/
    private String thumb;
    /** 视频播放地址 */
    private String previewUrl;
    /** 0资讯, 1短视频 */
    private String infoType;
    /** 是否竖版 */
    private Integer isVertical;
    /** 缩略图竖版 */
    private String thumbVertical;
    /** 原图横板 */
    private String computerPicPathVertical;
    /** 是否存在视频 */
    private Integer isVideoExists;
    /** 生成页面时间 */
    private Long gmtPage;
    /**  */
    private String userAgent;
    /** 是否url链接资讯 */
    private Integer isUrlInfo;
    /** url资讯的地址 */
    private String infoUrl;
    /** 附件数 */
    private Integer attachmentCount;
    /** 前3个附件图 */
    private String showAttachment;
    /** 备注, 审核拒绝原因等 */
    private String remark;
    /** 是否转载, 1是 */
    private Integer isTransport;
    /** 是否转载, 1是 */
    private Integer rightFlag;
    /** 文件id */
    private Long sourceID;
    /** 是否隐藏 0 默认否  1 是 */
    private Integer isHide;
    /**
     * 是否需要登录 0 否  1 是
     */
    private Integer isLogin;
    /** 实际浏览数 */
    private Integer actualViewCount;
    /**  浏览数*/
    private Integer viewCount;
    /** 拼音全称 */
    private String namePinyin;
    /**  拼音首字母 */
    private String namePinyinSimple;
    private String originDetail;
    private String pathPre;


    public static final Character STATE_UNABLE = '0'; //未启用
    public static final Character STATE_NORMAL = '1'; //已发布
    public static final Character STATE_WAIT_VERIFY = '4'; //待审核
    public static final Character STATE_VERIFY_DENY = '5'; //审核不通过
}
