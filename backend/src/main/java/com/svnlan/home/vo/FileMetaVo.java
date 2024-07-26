package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/17 13:24
 */
@Data
public class FileMetaVo {

    /** 文档封面logo，主要用于视频格式，此为原始地址，小图在文件名后加_50_50类格式  */
    private String thumb;
    /** 图片,视频分辨率, width*height */
    private String resolution;
    /** previewUrl 预览地址,可为视频的m3u8地址或图片的存储地址或者文档类型的flash地址*/
    private String viewUrl;
    /**视频转h264的路径 */
    private String h264Path;
    /** APP文档预览路径*/
    private String appViewUrl;
    /** 获取视频长度*/
    private Integer length;

    /** 视频编码名称 */
    private String codecName;
    /** 平均帧率*/
    private String avgFrameRate;
    /** 实时帧率*/
    private String frameRate;
    /** 音频包的采样率 */
    private String sampleRate;
    /** 音频声道 */
    private String channels;
    /** 音频编码名称 */
    private String audioCodecName;
    /** 封面，可上传*/
    private String cover;
    private String yzViewData;
    private String yzEditData;
    private String frame;
    private String oexeContent;
}
