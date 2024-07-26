package com.svnlan.jwt.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.EnWebChatConfigVo;
import com.svnlan.utils.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 作为平台时 微信登录
 *
 * @author lingxu 2023/05/15 14:07
 */
@Slf4j
@RestController
@RequestMapping("api/platform/wechat")
public class WechatLoginEndpoint {
    @Value("${wechat.web.wx_appid}")
    private String wxWebAppId;
    @Value("${wechat.web.wx_appSecret}")
    private String wxWebSecret;

    @Value("${wechat.app.wx_appid}")
    private String wxAppAppId;
    @Value("${wechat.app.wx_appSecret}")
    private String wxAppSecret;

    @Value("${enWechat.corpId}")
    private String corpId;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    OptionTool optionTool;


    @SneakyThrows
    @GetMapping("accessToken")
    public JSONObject getAccessToken(String code, String state, Boolean isApp) {

        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("appid", isApp ? wxAppAppId : wxWebAppId);
        variableMap.put("secret", isApp ? wxAppAppId : wxWebSecret);
        variableMap.put("code", code);
        variableMap.put("grant_type", "authorization_code");
        String str = restTemplate.getForObject(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code",
                String.class, variableMap);
        log.info("getAccessToken str => {}", str);
        return JSONObject.parseObject(str);
    }

    @GetMapping("appId")
    public String getAppId(String type) {
        if (SecurityTypeEnum.WECHAT.getCode().equals(type)) {
            return wxWebAppId;
        } else if (SecurityTypeEnum.EN_WECHAT.getCode().equals(type)) {
            EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
            return (!ObjectUtils.isEmpty(config) && !ObjectUtils.isEmpty(config.getCorpId())) ? config.getCorpId() : corpId;
        } else if (SecurityTypeEnum.WECHAT_APP.getCode().equals(type)) {
            return wxAppAppId;
        }
        LogUtil.error("暂不支持的安全设置类型type=" + type);
        throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
    }
}
