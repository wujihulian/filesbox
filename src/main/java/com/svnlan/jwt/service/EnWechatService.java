package com.svnlan.jwt.service;

import com.svnlan.user.domain.ThirdUserInitializeConfig;
import org.springframework.data.util.Pair;

import java.util.Map;

/**
 * 企业微信相关
 *
 * @author lingxu 2023/05/09 15:41
 */
public interface EnWechatService {
    /**
     * 获取企业微信 accessToken
     */
    String getAccessToken();

    Map<String, String> checkAndExecuteUserLogin(String accessToken, String code, String state);

    ThirdUserInitializeConfig getInitializeConfig();

    Pair<String, String> beforeLoginWithWechat(String type);
}
