package com.svnlan.jwt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.jwt.service.WechatService;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.domain.User;
import com.svnlan.user.vo.UserVo;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微信相关
 *
 * @author lingxu 2023/05/11 09:33
 */
@Slf4j
@Service
public class WechatServiceImpl extends ThirdBaseService implements WechatService {

    @Value("${wechat.web.wx_appid}")
    private String wxWebAppId;

    @Value("${wechat.web.wx_appSecret}")
    private String wxWebSecret;

    @Value("${wechat.app.wx_appid}")
    private String wxAppAppId;

    @Value("${wechat.app.wx_appSecret}")
    private String wxAppSecret;

    @Override
    ThirdUser getThirdUser(String accessToken, String code) {
        String openId = openIdThreadLocal.get();

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://api.weixin.qq.com/sns/userinfo?access_token={accessToken}&openid={openId}",
                HttpMethod.GET, httpEntity, String.class, accessToken, openId);

        String body = responseEntity.getBody();
        byte[] bytes = body.getBytes(StandardCharsets.ISO_8859_1);
        body = new String(bytes, StandardCharsets.UTF_8);
        log.info("getThirdUser str =>{}", body);
        JSONObject jsonObj = JSONObject.parseObject(body);

        return new ThirdUser.ThirdUserBuilder()
                .nick(jsonObj.getString("nickname"))
                .avatar(jsonObj.getString("headimgurl"))
                .openId(openId)
                .sex(Objects.equals(jsonObj.getString("sex"), "0") ? "1" : "0")
                .build();
    }

    @Override
    public ThirdUserInitializeConfig getInitializeConfig() {
        return super.getInitializeConfig(SecurityTypeEnum.WECHAT);
    }

    @SneakyThrows
    @Override
    public JSONObject getAccessToken(String code, String state) {
        JSONObject jsonObj;
        Boolean isApp = isAppThreadLocal.get();
        if (StringUtils.hasText(platformUrl)) {
            jsonObj = restTemplate.getForObject(platformUrl + "/api/platform/wechat/accessToken?code=" + code +
                    "&state=" + state + "&isApp=" + isApp, JSONObject.class);
        } else {
            jsonObj = getAccessTokenInternal(code, state, isApp);
        }
        log.info("jsonObj => {}", jsonObj);
        Assert.hasText(jsonObj.getString("openid"), "查询到openid 为空");
        return jsonObj;
    }

    @SneakyThrows
    @Override
    public Pair<String, String> beforeLoginWithWechat(String type) {
        return beforeLogin(type, () -> {
            if (SecurityTypeEnum.WECHAT_APP.getCode().equals(type)) {
                return wxAppAppId;
            } else {
                return wxWebAppId;
            }
        }, "");
    }

    @Override
    public Map<String, String> checkAndExecuteUserLogin(String accessToken, String openId, String code, String state) {
        try {
            openIdThreadLocal.set(openId);
            return executeUser(accessToken, SecurityTypeEnum.WECHAT, state, code);
        } finally {
            openIdThreadLocal.remove();
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject doBind(UserVo userVo, String code, String state, Long userId) {
        org.springframework.util.Assert.isTrue(!StringUtils.hasText(userVo.getWechatOpenId()), "已绑定微信，请先进行解绑");
        log.info("开始微信绑定 --------");
        JSONObject jsonObj = getAccessToken(code, state);
        log.info("jsonObj => {}", jsonObj);
        String openId = jsonObj.getString("openid");
        Assert.hasText(openId, "未查询到openId");
        String accessToken = jsonObj.getString("access_token");
        // 校验对应的openId 是否已经绑定了账号
        checkBeforeBind(() -> "wechatOpenId", openId, userId);
        try {
            openIdThreadLocal.set(openId);
            populateAndUpdateUser(userVo, getThirdUser(accessToken, null));
        } finally {
            openIdThreadLocal.remove();
        }
        return afterExecuteUser(userVo.getUserID());
    }

    @Override
    public String getAccessToken(Long tenantId) {
        return null;
    }

    /**
     * 从平台中获取 accessToken 相关信息
     */
    public JSONObject getAccessTokenInternal(String code, String state, Boolean isApp) {
        Map<String, String> variableMap = new HashMap<>();
        if (isApp) {
            variableMap.put("appid", wxAppAppId);
            variableMap.put("secret", wxAppSecret);
        } else {
            variableMap.put("appid", wxWebAppId);
            variableMap.put("secret", wxWebSecret);
        }
        variableMap.put("code", code);
        variableMap.put("grant_type", "authorization_code");
        String str = restTemplate.getForObject(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code",
                String.class, variableMap);
        return JSONObject.parseObject(str);
    }

    @Override
    String getOpenId(UserVo userVo) {
        return userVo.getWechatOpenId();
    }

    @Override
    boolean setOpenId(UserVo userVo, User user, String openId) {
        if (StringUtils.hasText(userVo.getWechatOpenId())) {
            return false;
        }
        user.setWechatOpenId(openId);
        return true;
    }

}
