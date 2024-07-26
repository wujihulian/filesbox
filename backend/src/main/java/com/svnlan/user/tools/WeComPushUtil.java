package com.svnlan.user.tools;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WeComPushUtil {


    private static final String SECRET = "96b1f94b-eadc-441c-b8cb-87a93d7a4904hlw";

    /**
     * 配置企业微信的webhook
     * https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=96b1f94b-eadc-441c-b8cb-87a93d7a4904hlw
     */
    private static final String WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";

    private static final String dingTemplate = "**告警名称** : %s\n\n**告警级别** : %s\n\n**设备信息** : %s\n\n" +
            "**告警内容** : %s\n\n**告警时间** : %s\n\n";
    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        sendGroup(SECRET,"火灾","S2","铁塔","大火熊熊燃气啊");
    }

    /**
     *
     * @param token   企业微信机器人token
     * @param warningName   告警名称
     * @param level 告警级别
     * @param deviceInfo  设备信息
     * @param content  告警内容
     */
    public static void sendGroup(String token,String warningName,String level,String deviceInfo,String content){
        try {
            JSONObject reqBody = getReqBody(String.format(dingTemplate, warningName, level, deviceInfo, content, format1.format(new Date())));
            callWeChatBot(reqBody.toString(),token);
        }catch (Exception e){

        }
    }
    public static void sendGroup(String token,String msg){
        try {
            JSONObject reqBody = getReqBody(msg);
            callWeChatBot(reqBody.toString(),token);
        }catch (Exception e){
            LogUtil.error(e, "sendGroup error");
        }
    }

    /**
     * 发送MarKDown消息
     *
     * @param msg 需要发送的消息
     * @return
     * @throws Exception
     */
    public static JSONObject getReqBody(String msg) throws Exception {
        JSONObject markdown = new JSONObject();
        markdown.put("content", msg);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "markdown");
        reqBody.put("markdown", markdown);
        reqBody.put("safe", 0);
        return reqBody;

    }
    /**
     * 调用群机器人
     *
     * @param reqBody 接口请求参数
     * @throws Exception 可能有IO异常
     */
    public static String callWeChatBot(String reqBody,String botUrl) throws Exception {
        log.info("请求参数：" + reqBody);

        // 构造RequestBody对象，用来携带要提交的数据；需要指定MediaType，用于描述请求/响应 body 的内容类型
        MediaType contentType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(contentType, reqBody);

        // 调用群机器人
        String respMsg = okHttp(body, (botUrl.indexOf("http") >= 0) ? botUrl : (WEBHOOK + botUrl));

        if ("0".equals(respMsg.substring(11, 12))) {
            log.info("向群发送消息成功！");
        } else {
            log.info("请求失败！");
            // 发送错误信息到群
            sendTextMsg("群机器人推送消息失败，错误信息：\n" + respMsg);
        }
        return respMsg;
    }

    /**
     * @param body 携带需要提交的数据
     * @param url  请求地址
     * @return
     * @throws Exception
     */
    public static String okHttp(RequestBody body, String url) throws Exception {
        // 构造和配置OkHttpClient
        OkHttpClient client;
        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS) // 设置读取超时时间
                .build();

        // 构造Request对象
        Request request = new Request.Builder().url(url).post(body).addHeader("cache-control", "no-cache") // 响应消息不缓存
                .build();

        // 构建Call对象，通过Call对象的execute()方法提交异步请求
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 请求结果处理
        byte[] datas = response.body().bytes();
        String respMsg = new String(datas);
        log.info("返回结果：" + respMsg);

        return respMsg;
    }
    /**
     * 发送文字消息
     *
     * @param msg 需要发送的消息
     * @return
     * @throws Exception
     */
    public static String sendTextMsg(String msg) throws Exception {
        JSONObject text = new JSONObject();
        text.put("content", msg);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "text");
        reqBody.put("text", text);
        reqBody.put("safe", 0);

        return callWeChatBot(reqBody.toString(),SECRET);
    }

}
