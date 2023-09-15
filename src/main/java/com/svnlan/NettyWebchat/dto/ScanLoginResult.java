package com.svnlan.NettyWebchat.dto;

import lombok.Data;

/**
 * @description: 扫码登录结果信息
 */
@Data
public class ScanLoginResult {

    private String action;
    private String code;
    private String message;

    //TV或WEB用于登录的临时授权码
    private String tempAuth;

    //TV或WEB用于登录的网校域名，如test.1x.cn
    private String schoolDomain;

}
