package com.svnlan.NettyWebchat.dto;

import lombok.Data;

/**
 * @description: 临时授权码对应的TOKEN信息
 */
@Data
public class TempAuthDTO {

    //登录TOKEN
    private String token;
    //
    private String roomName;

}
