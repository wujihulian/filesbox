package com.svnlan.user.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 17:02
 */
@Data
public class SystemOptionDTO {

    /** 浏览器图标（建议图片尺寸为32*32，大小不超过1M） */
    private String browserLogo;
    /** 日期格式	 	默认：Y-m-d */
    private String dateFormat;
    /**  备案及版权信息 */
    private String globalIcp;
    /**  企业网盘的显示名称 */
    private String groupRootName;
    /**  meta标签信息 */
    private String meta;
    /**  用户登录开启图形验证码（默认关闭）	0 关闭 1 开启 */
    private String needCheckCode;
    /**  密码输错6次自动锁定账号（默认关闭） */
    private String passwordErrorLock;
    /** 用户密码强弱设置（不限制 /长度大于6且必须同时包含英文和数字 / 长度大于6且必须同时包含数字大写	 ，	none不限制 ，strong 中强，strongMore高强  */
    private String passwordRule;
    /** seo关键词  */
    private String seo;
    /**  seo描述信息 */
    private String seoDesc;
    /** 允许文件外链分享（默认开启）  */
    private String shareLinkAllow;
    /**  允许未登录游客访问文件外链（默认开启） */
    private String shareLinkAllowGuest;
    /**  外链分享允许密码为空 (默认关闭) */
    private String shareLinkPasswordAllowEmpty;
    /** 文件属性不显示外链连接（默认关闭）  */
    private String showFileLink;
    /** 文件属性不显示md5信息（默认关闭）  */
    private String showFileMd5;
    /** 副标题  */
    private String systemDesc;
    /**  logo（建议图片尺寸高度48px） */
    private String systemLogo;
    /**  产品名称 */
    private String systemName;
    /**  树目录功能开关  my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink */
    private String treeOpen;
    /**  是否加水印 0否1是 */
    private String needMark;

    /**  水印类型  0无水印 1 文字 2 图片 */
    private String markType;
    /**  主标题文字 */
    private String wmTitle;
    /**  水印颜色 */
    private String wmColor;
    /**  水印字体 */
    private String wmFont;
    /** 水印字体大小  */
    private String wmSize;
    /**  副标题文字 */
    private String wmSubTitle;
    /**  副标题水印颜色 */
    private String wmSubColor;
    /**  副标题水印字体 */
    private String wmSubFont;
    /**  副标题水印字体大小 */
    private String wmSubSize;
    /**  水印透明度 */
    private String wmTransparency;
    /**  水印内容旋转角度 */
    private String wmRotate;
    /** 水印间距  */
    private String wmMargin;
    /**  图片水印路径 */
    private String wmPicPath;
    /** 图片水印大小  */
    private String wmPicSize;
    /**  水印的位置 */
    private String wmPosition;
    /** 第三方配置*/
    private String thirdLoginConfig;
    /** 注册配置*/
    private String registerConfig;
    /** 秒传开关 */
    private String needUploadCheck;
    /** 登录默认显示 1 桌面 2 文件管理 */
    private String defaultHome;
    private String dingConfig;
    private String networkConfig;
    private String taskID;
    private String enWechatConfig;
    private String cubeConfig;
    private String emailConfig;
}
