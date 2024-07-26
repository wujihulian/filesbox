package com.svnlan.home.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.exception.DingCallbackCrypto;
import com.svnlan.home.service.DingEventService;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.utils.LogUtil;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/19 10:50
 */
@RestController
public class DingCallbackController {

    @Resource
    DingEventService dingEventService;
    @Resource
    OptionTool optionTool;


    private final Logger LOGGER = LoggerFactory.getLogger("error");


    /**
     * 钉钉登录获取授权码回调接口
     */
    @PostMapping("/api/ding/even/callback")
    public Map<String, String> callBack(
            @RequestParam(value = "msg_signature", required = false) String msg_signature,
            @RequestParam(value = "timestamp", required = false) String timeStamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestBody(required = false) JSONObject json) {
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig();
        String AES_KEY = dingConfigVo.getAesKey();
        String AES_TOKEN = dingConfigVo.getAesToken();
        String OWNER_KEY = dingConfigVo.getCorpId();
        try {
            // 1. 从http请求中获取加解密参数

            // 2. 使用加解密类型
            // Constant.OWNER_KEY 说明：
            // 1、开发者后台配置的订阅事件为应用级事件推送，此时OWNER_KEY为应用的APP_KEY。
            // 2、调用订阅事件接口订阅的事件为企业级事件推送，
            //      此时OWNER_KEY为：企业的appkey（企业内部应用）或SUITE_KEY（三方应用）
            DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(AES_TOKEN, AES_KEY, OWNER_KEY);
            String encryptMsg = json.getString("encrypt");
            String decryptMsg = callbackCrypto.getDecryptMsg(msg_signature, timeStamp, nonce, encryptMsg);

            LogUtil.info("dingEventCallBack decryptMsg=" +decryptMsg);
            // 3. 反序列化回调事件json数据
            JSONObject eventJson = JSON.parseObject(decryptMsg);
            String eventType = eventJson.getString("EventType");


            // 4. 根据EventType分类处理
            if ("check_url".equals(eventType)) {
                // 测试回调url的正确性
                LOGGER.info("测试回调url的正确性");
            } else {
                // 添加已注册的
                LOGGER.info("other 发生了：" + eventType + "事件");
                dingEventService.dingEventCallback(eventJson);
            }

            // 5. 返回success的加密数据
            Map<String, String> successMap = callbackCrypto.getEncryptedMap("success");
            return successMap;

        } catch (DingCallbackCrypto.DingTalkEncryptException e) {
            LogUtil.error(e, "/api/ding/even/callback DingTalkEncryptException error");
        } catch (Exception e) {
            LogUtil.error(e, "/api/ding/even/callback error");
        }
        return null;
    }
}
