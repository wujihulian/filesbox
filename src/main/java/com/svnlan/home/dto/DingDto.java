package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/28 14:56
 */
@Data
public class DingDto {
    private String signature;
    private String msg_signature;
    private String timestamp;
    private String nonce;
}
