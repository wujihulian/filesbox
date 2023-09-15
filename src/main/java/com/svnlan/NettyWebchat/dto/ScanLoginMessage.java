package com.svnlan.NettyWebchat.dto;

import lombok.Data;

/**
 * @description: 扫码登录消息
 */
@Data
public class ScanLoginMessage {

    //消息类型：tvScanLogin-TV端;webScanLogin-web端；appScanLogin-APP端
    private String msgType;
    //动作
    private String action;

    //动作值
    private String actionVal;

    //登录token
    private String token;
    //临时登录授权码
    private String tempAuth;

    //登录token的网校域名，如：test.1x.cn
    private String serverName;

}
