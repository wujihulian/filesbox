package com.svnlan.jwt.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import org.springframework.data.util.Pair;

import java.util.Map;

/**
 * 微信相关
 *
 * @author lingxu 2023/05/11 09:33
 */
public interface WechatService {

    ThirdUserInitializeConfig getInitializeConfig();

    boolean checkSig(String sig);

    JSONObject getAccessToken(String code, String state);

    Pair<String, String> beforeLoginWithWechat(String type);

    Map<String, String> checkAndExecuteUserLogin(String accessToken, String openid, String code, String state);

}
