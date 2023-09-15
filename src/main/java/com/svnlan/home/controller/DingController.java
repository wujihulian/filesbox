package com.svnlan.home.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.DingCallbackCrypto;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.DingDto;
import com.svnlan.home.dto.LabelDto;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/28 14:54
 */
@RestController
public class DingController {


    @Value("${dingding.token}")
    private String dingToken;
    @Value("${dingding.aes.key}")
    private String dingAesKey;
    @Value("${dingding.app.key}")
    private String ownKey;

    @PostMapping("/api/disk/dingCallback2")
    public Map<String, String> eventCallback(HttpServletRequest request,
                                             @RequestParam(value = "msg_signature", required = false) String msg_signature,
                                             @RequestParam(value = "timestamp", required = false) String timeStamp,
                                             @RequestParam(value = "nonce", required = false) String nonce,
                                             @RequestBody(required = false) JSONObject json) throws DingCallbackCrypto.DingTalkEncryptException {
        DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(dingToken, dingAesKey, ownKey);
        Map<String, String> successMap = callbackCrypto.getEncryptedMap("success");
        LogUtil.info(successMap.toString());
        return successMap;
    }

    @PostMapping("/api/disk/dingCallback")
    public Map<String, String> callBack(
            @RequestParam(value = "msg_signature", required = false) String msg_signature,
            @RequestParam(value = "timestamp", required = false) String timeStamp,
            @RequestParam(value = "nonce", required = false) String nonce,
            @RequestBody(required = false) JSONObject json) {
        try {
            // 1. 从http请求中获取加解密参数

            // 2. 使用加解密类型
            // Constant.OWNER_KEY 说明：
            // 1、开发者后台配置的订阅事件为应用级事件推送，此时OWNER_KEY为应用的APP_KEY。
            // 2、调用订阅事件接口订阅的事件为企业级事件推送，
            //      此时OWNER_KEY为：企业的appkey（企业内部应用）或SUITE_KEY（三方应用）
            DingCallbackCrypto callbackCrypto = new DingCallbackCrypto(dingToken, dingAesKey, ownKey);
            String encryptMsg = json.getString("encrypt");
            String decryptMsg = callbackCrypto.getDecryptMsg(msg_signature, timeStamp, nonce, encryptMsg);

            // 3. 反序列化回调事件json数据
            JSONObject eventJson = JSON.parseObject(decryptMsg);
            String eventType = eventJson.getString("EventType");

            // 4. 根据EventType分类处理
            if ("check_url".equals(eventType)) {
                // 测试回调url的正确性
                LogUtil.info("测试回调url的正确性");
            } else if ("user_add_org".equals(eventType)) {
                // 处理通讯录用户增加事件
                LogUtil.info("发生了：" + eventType + "事件");
            } else {
                // 添加其他已注册的
                LogUtil.info("发生了：" + eventType + "事件");
            }

            // 5. 返回success的加密数据
            Map<String, String> successMap = callbackCrypto.getEncryptedMap("success");
            return successMap;

        } catch (DingCallbackCrypto.DingTalkEncryptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
