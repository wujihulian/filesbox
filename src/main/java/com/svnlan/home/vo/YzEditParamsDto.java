package com.svnlan.home.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/3 11:35
 */
@Data
public class YzEditParamsDto {

    private String userId;
    private String fileId;
    private String fileName;
    private String filePath;
    /** 回调地址。
     如果为null，不会进行任何操作；
     如果不为null，WebOffice会在关闭该文档后向此地址发送HTTP请求以进行通知，
     请求带有3个参数：userId（String或List<String>，所有打开过此文档的用户的id）、fileId和filePath。
     （默认为null）
      */
    private String callbackUrl;

    public YzEditParamsDto(){}
    public YzEditParamsDto(String userId, String fileId, String fileName, String filePath){
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
    }
    public YzEditParamsDto(String userId, String fileId, String fileName, String filePath,String callbackUrl){
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.callbackUrl = callbackUrl;
    }
}
