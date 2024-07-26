package com.svnlan.user.vo;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/25 9:46
 */
@Data
public class DingConfigVo {
    private String appKey;
    private String appSecret;
    private String corpId;
    private String aesKey;
    private String aesToken;
    private String accessToken;
}
