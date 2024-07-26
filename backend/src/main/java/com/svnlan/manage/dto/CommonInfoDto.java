package com.svnlan.manage.dto;

import com.svnlan.annotation.SpecifiedValue;
import com.svnlan.utils.PageQuery;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/11 10:33
 */
@Data
public class CommonInfoDto extends PageQuery {

    private String keyword;
    @SpecifiedValue
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
    /** 资讯类型id */
    private Integer infoTypeID;
    /**  */
    private Integer sort;
    /** 资讯摘要 */
    private String introduce;
    /**  是否置顶 */
    private Integer isTop;
    /** keyword, description json */
    private String seo;
    /** 资讯来源json, name,author,url */
    private String infoSource;
    /**  是否申请原创，1 是 */
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
    /** 权限标识：0-私有，1-公开 */
    private Integer rightFlag;
    /** 文件id */
    private Long sourceID;
    /** 是否隐藏 0 默认否  1 是 */
    private Integer isHide;
    /**
     * 是否需要登录 0 否  1 是
     */
    private Integer isLogin;
    /**  浏览数*/
    private Integer viewCount;
    private String description = "";
    @Length(max = 20, message = "来源长度超限")
    private String infoSourceName;
    @Length(max = 50, message = "来源长度超限")
    private String infoSourceAuthor;
    @Length(max = 900, message = "来源长度超限")
    private String infoSourceUrl;

    private String fileIds;
    private String domain;
    private List<Long> infoIdList;
    private String url;
    private String parentLevel;
    private String tagIds;
    private Long tagID;
    private Boolean cancelTop;
    private Long total;
    private Integer verifyState;

    private Integer targetPos; // 排序的目标位置。置顶 1， [min,max]之间的位置就传相应的值即可，置尾 -1
    private String serviceName;
}
