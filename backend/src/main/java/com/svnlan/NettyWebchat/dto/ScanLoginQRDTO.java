package com.svnlan.NettyWebchat.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description: （扫码登录）二维码存储信息
 */
@Data
public class ScanLoginQRDTO {

    private String roomName;
    //使用状态：0-未使用；1-已使用（登录）
    private String state;
    private Long userId;
    //二维码生成时间
    private Date gmtCreate;

}
