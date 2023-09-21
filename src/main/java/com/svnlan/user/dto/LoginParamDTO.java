package com.svnlan.user.dto;

import lombok.Data;

/**
 * @description: 扫码登录请求参数
 */
@Data
public class LoginParamDTO {

    //加密后的值
    private String signature;
    //时间戳
    private Long timestamp;
    //tvScanLogin-TV端;webScanLogin-web端；
    private String clientType;

    //额外的
    //随机数
    private String nonceStr;
    //临时授权码对应的源网校域名
    private String sourceDomain;
    private String tempAuth;

}
