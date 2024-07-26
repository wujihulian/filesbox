package com.svnlan.jwt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.service.EnWechatService;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.domain.User;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.EnWebChatConfigVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信相关
 *
 * @author lingxu 2023/05/09 15:41
 */
@Slf4j
@Service
public class EnWechatServiceImpl extends ThirdBaseService implements EnWechatService {

    @Value("${enWechat.corpId}")
    private String enCorpId;

    @Value("${enWechat.Secret}")
    private String enSecret;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    OptionTool optionTool;

    public static final String EN_WECHAT_ACCESS_TOKEN_KEY = "accessToken:enWechat:%s:secret:%s";

    /**
     * 获取企业微信 accessToken
     */

    @Override
    public String getAccessToken(Long tenantId) {

        EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
        String corpId = (!ObjectUtils.isEmpty(config) && !ObjectUtils.isEmpty(config.getCorpId())) ? config.getCorpId() : enCorpId;
        String secret = (!ObjectUtils.isEmpty(config) && !ObjectUtils.isEmpty(config.getSecret())) ? config.getSecret() : enSecret;

        LogUtil.info(String.format("企业微信登录  corpId：%s  secret：%s", corpId,secret));
        String accessToken = stringRedisTemplate.opsForValue().get(String.format(EN_WECHAT_ACCESS_TOKEN_KEY, corpId,secret));
        if (!StringUtils.hasText(accessToken)) {
            synchronized (this) {
                accessToken = stringRedisTemplate.opsForValue().get(String.format(EN_WECHAT_ACCESS_TOKEN_KEY, corpId,secret));
                if (!StringUtils.hasText(accessToken)) {

                    JSONObject jsonObj = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + secret, JSONObject.class);
                    Assert.notNull(jsonObj, "请求企业微信 token 失败");

                    if (Objects.equals(jsonObj.getInteger("errcode"), 0)
                            && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
                        accessToken = jsonObj.getString("access_token");
                        stringRedisTemplate.opsForValue().set(String.format(EN_WECHAT_ACCESS_TOKEN_KEY, corpId,secret), accessToken, 7100, TimeUnit.SECONDS);
                    } else {
                        throw new SvnlanRuntimeException(String.valueOf(jsonObj.getInteger("errcode")), jsonObj.getString("errmsg"), null);
                    }
                }
            }
        }
        return accessToken;
    }

    @Override
    ThirdUser getThirdUser(String accessToken, String code) {
        log.info("企业微信 获取用户信息开始 -----------");
        // 获取用户的 code

        JSONObject jsonObj = getUserTicket(accessToken, code);
        String userId = jsonObj.getString("userid");
        log.info("userId => {}", userId);
        String userTicket = jsonObj.getString("user_ticket");
        ;
        log.info("userTicket => {}", userTicket);
        JSONObject userInfoObj;
        if (StringUtils.hasText(userTicket)) {
            // 企业微信内部应用授权网页登录的方式
            // 获取用户比较详细的信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //设置请求参数
            HashMap<String, Object> map = new HashMap<>();
            map.put("user_ticket", userTicket);
            HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(map, headers);

            userInfoObj = restTemplate.postForObject("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserdetail?access_token="
                    + accessToken, request, JSONObject.class);
            log.info("企业微信内部应用 userInfoObj =>{}", userInfoObj);
        } else {
            // 第三方网页 企业微信扫码登录
            // 需要查询通过通讯录管理查询成员信息
            userInfoObj = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="
                    + accessToken + "&userid=" + userId, JSONObject.class);
            log.info("第三方网站 userInfoObj =>{}", userInfoObj);
        }
        Assert.notNull(userInfoObj, "企业微信 获取用户信息失败");
        if (!Objects.equals(userInfoObj.getInteger("errcode"), 0)
                || !Objects.equals(userInfoObj.getString("errmsg"), "ok")) {
            throw new SvnlanRuntimeException(String.valueOf(userInfoObj.getInteger("errcode")), userInfoObj.getString("errmsg"), null);
        }

        EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
        String corpId = (!ObjectUtils.isEmpty(config) && !ObjectUtils.isEmpty(config.getCorpId())) ? config.getCorpId() : enCorpId;
        return new ThirdUser.ThirdUserBuilder()
                .nick(userInfoObj.getString("name"))
                .avatar(userInfoObj.getString("avatar"))
                .mobile(userInfoObj.getString("mobile"))
                .sex(userInfoObj.getString("gender"))
                .email(userInfoObj.getString("biz_mail"))
                .openId(corpId + "_" + userInfoObj.getString("userid"))
                .build();
    }

    /**
     * 获取用户的 userTicket
     */
    private JSONObject getUserTicket(String accessToken, String code) {
        JSONObject jsonObj = restTemplate.getForObject("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=" + accessToken + "&code=" + code, JSONObject.class);
        Assert.notNull(jsonObj, "请求企业微信 用户信息 失败");
        log.info("jsonObj => {}", jsonObj);
        if (!Objects.equals(jsonObj.getInteger("errcode"), 0)
                || !Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            throw new SvnlanRuntimeException(String.valueOf(jsonObj.getInteger("errcode")), jsonObj.getString("errmsg"), null);
        }
        // 如果没有 userid 说明是非企业成员
        Assert.hasText(jsonObj.getString("userid"), "非企业成员不能登录");

        return jsonObj;
    }

    @Override
    public ThirdUserInitializeConfig getInitializeConfig() {
        return super.getInitializeConfig(SecurityTypeEnum.EN_WECHAT);
    }

    @Override
    public Pair<String, String> beforeLoginWithWechat(String type) {
        EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
        String corpId = (!ObjectUtils.isEmpty(config) && !ObjectUtils.isEmpty(config.getCorpId())) ? config.getCorpId() : enCorpId;
        return beforeLogin(type, () -> corpId, config.getAgentId());
    }

    @Override
    String getOpenId(UserVo userVo) {
        return userVo.getEnWechatOpenId();
    }

    @Override
    boolean setOpenId(UserVo userVo, User user, String openId) {
        if (StringUtils.hasText(userVo.getEnWechatOpenId())) {
            return false;
        }
        user.setEnWechatOpenId(openId);
        return true;
    }


    @Override
    public JSONObject doBind(UserVo userVo, String code, String state, Long userId) {
        Assert.isTrue(!StringUtils.hasText(userVo.getEnWechatOpenId()), "已绑定企业微信，请先进行解绑");
        log.info("开始企业微信绑定 --------");
        String accessToken = getAccessToken(userVo.getTenantId());
        ThirdUser thirdUser = getThirdUser(accessToken, code);
        // 校验对应的openId 是否已经绑定了账号
        checkBeforeBind(() -> "enWechatOpenId", thirdUser.openId, userId);
        populateAndUpdateUser(userVo, thirdUser);
        return afterExecuteUser(userVo.getUserID());
    }

    @Override
    public Map<String, String> checkAndExecuteUserLogin(String accessToken, String code, String state) {
        return executeUser(accessToken, SecurityTypeEnum.EN_WECHAT, state, code);
    }
}
















