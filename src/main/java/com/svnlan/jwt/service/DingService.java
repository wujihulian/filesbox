package com.svnlan.jwt.service;

import com.svnlan.user.domain.ThirdUserInitializeConfig;

import java.util.Map;

/**
 * 钉钉相关
 *
 * @author lingxu 2023/04/20 15:56
 */
public interface DingService {
    /**
     * 三方登录获取 token
     */
    String getAccessToken(String authCode);
    /**
     * 企业内部应用 获取token
     */
    String getAccessToken();

    Map<String, String> checkAndExecuteUserLogin(String accessToken, String state);

    ThirdUserInitializeConfig getInitializeConfig();

    Map<String, String> checkAndExecuteInnerUserLogin(String accessToken, String authCode);
}
