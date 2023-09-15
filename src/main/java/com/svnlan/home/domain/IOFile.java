package com.svnlan.home.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 15:14
 */
@Data
@TableName("io_file")
public class IOFile {
    @TableId(value = "fileID", type = IdType.AUTO)
    private Long fileID;
    private String name;
    private Long size;
    @TableField("ioType")
    private Long ioType;
    private String path;
    @TableField("hashSimple")
    private String hashSimple;
    @TableField("hashMd5")
    private String hashMd5;
    @TableField("linkCount")
    private Long linkCount;
    @TableField("createTime")
    private Long createTime;
    @TableField("modifyTime")
    private Long modifyTime;
    @TableField("is_preview")
    private Integer isPreview;
    @TableField("app_preview")
    private Integer appPreview;
    @TableField("is_h264_preview")
    private Integer isH264Preview;
    @TableField("is_m3u8")
    private Integer isM3u8;
    @TableField("fileName")
    private String fileName;
    @TableField("convertSize")
    private Long convertSize;
    @TableField("thumbSize")
    private Long thumbSize;

    public static final IOFile NULL = new IOFile();

    /*

ALTER TABLE io_file ADD COLUMN `is_preview` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否支持预览,0否，1是';
ALTER TABLE io_file ADD COLUMN `app_preview` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '是否支持APP上的文档预览，主要指doc转成pdf后的html5预览';
ALTER TABLE io_file ADD COLUMN `is_h264_preview` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '视频转h264是否成功, 0未成功,1成功,2失败';
ALTER TABLE io_file ADD COLUMN `is_m3u8` tinyint(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '源文件为视频类型时,表示是否已经切片处理成m3u8格式,0否1是;文档类型(doc,ppt等)时,表示是否转成flash,0否1是';

     */

}
