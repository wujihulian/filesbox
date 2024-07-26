package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.EnWebChatConfigVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.qq.aes.EnAesException;
import com.svnlan.utils.qq.aes.ConstantUtil;
import com.svnlan.utils.qq.aes.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EnWebchatController {

    @Autowired
    OptionTool optionTool;

    /**
     * 企业微信回调
     * 3.1 支持Http Get请求验证URL有效性
     * 3.2 支持Http Post请求接收业务数据
     *
     * @return
     */
    @RequestMapping(value = "/api/enWebChat/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public Object CompanyWeChatChangeNotice(HttpServletRequest request, @RequestBody(required = false) String body) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String jsonString = JSONObject.toJSONString(parameterMap);
        EnWebChatConfigVo config = optionTool.getEnWebChatConfig();
        LogUtil.info("企业微信回调参数：{},  解析参数：{}", jsonString, body);

        if (body == null) {
            Object result = this.verificationUrl(request, config);
            return result;
        }
        Map<String, String> resultMap = this.getRequestParameter(request, body, config);
        LogUtil.info("enWebChatCallBack resultMap=" + JsonUtils.beanToJson(resultMap));
        return "success";
    }

    /**
     * 验证回调URL
     *
     * @param request
     * @return
     */
    public Object verificationUrl(HttpServletRequest request, EnWebChatConfigVo config) {
        LogUtil.info("=========验证URL有效性开始=========");
        String sEchoStr; //需要返回的明文
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(config.getToken(), config.getAesKey(), config.getCorpId());
            String msgSignature = request.getParameter("msg_signature");
            String timeStamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");

            LogUtil.info("企业微信加密签名: {},时间戳: {},随机数: {},加密的字符串: {}", msgSignature, timeStamp, nonce, echostr);
            sEchoStr = wxcpt.VerifyURL(msgSignature,
                    timeStamp,
                    nonce,
                    echostr);
            LogUtil.info("给企业微信返回的明文,{}", sEchoStr);
            LogUtil.info("=========验证URL有效性结束=========");
            return sEchoStr;

        } catch (EnAesException e) {
            LogUtil.error(e, "验证URL失败，错误原因请查看异常:{}");

        }
        return null;
    }

    /**
     * 企业微信回调参数解析
     *
     * @param request
     * @param body
     * @return
     */
    public Map<String, String> getRequestParameter(HttpServletRequest request, String body, EnWebChatConfigVo config) {
        LogUtil.info("=========参数解析开始=========");

        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(config.getToken(), config.getAesKey(), config.getCorpId());
            String msgSignature = request.getParameter("msg_signature");
            String timeStamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");

            LogUtil.info("企业微信加密签名: {},时间戳: {},随机数: {}", msgSignature, timeStamp, nonce);
            String sMsg = wxcpt.DecryptMsg(msgSignature, timeStamp, nonce, body);
            Map<String, String> resultMap = new HashMap<>(16);
            resultMap = ConstantUtil.parseXmlToMap(sMsg, resultMap);
            LogUtil.info("decrypt密文转为map结果为{}"+ resultMap);
            LogUtil.info("=========参数解析结束=========");
            return resultMap;
        } catch (EnAesException e) {
            LogUtil.error("密文参数解析失败，错误原因请查看异常:{}", e.getMessage());
        }
        return null;
    }

}
