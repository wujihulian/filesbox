package com.svnlan.user.vo;

import lombok.Data;

/**
 * @description: 扫码登录
 */
@Data
public class LoginQRVO {

    private String qrUrl;
    private String token;
    private String roomName;

}
