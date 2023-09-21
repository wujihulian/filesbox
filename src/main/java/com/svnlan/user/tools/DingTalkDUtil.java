package com.svnlan.user.tools;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.svnlan.common.Result;
import com.svnlan.user.domain.PostContent;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.util.ObjectUtils;

public class DingTalkDUtil {

    /**
     * @description: 调用钉钉接口发送消息
     * @param url
     * @param msg
     * @return com.svnlan.common.Result
     */
    public static Result sendMsg(String url, String msg) {
        return sendMsg(url, msg, null, null);
    }

    public static void sendMsg(PostContent postContent) {
        Result result = sendMsg(postContent.getUrl(), postContent.getMessage(), postContent.getMsgType(), postContent.getAt());
        LogUtil.info("dingTalkMsgSimple, " + JsonUtils.beanToJson(result));
    }

    public static Result sendMsg(String url, String msg, String msgType, String at) {
        Boolean success = Boolean.TRUE;
        String code = "200";
        String message = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(url);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype(StringUtil.isEmpty(msgType) ? "text" : msgType);
            switch (request.getMsgtype()){
                case "text":
                    OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                    text.setContent(msg);
                    request.setText(text);
                    break;
                case "markdown":
                    request.setMarkdown(msg);
                    break;
                case "link":
                    request.setLink(msg);
                    break;
                case "actionCard":
                    request.setActionCard(msg);
                    break;
                default:
                    request.setText(msg);
            }

            if (at != null){
                request.setAt(at);
            }
            OapiRobotSendResponse response = client.execute(request);
            Long errCode = response.getErrcode();
            if(!ObjectUtils.isEmpty(errCode) && !errCode.equals(0L)) {
                code = "DTFalse";
                message = String.format("错误码：%d，描述：%s", errCode, response.getErrmsg());
            }
        } catch (Exception e) {
            code = "Exception";
            message = "调用接口发生异常:" + ExceptionUtil.stackTraceToString(e);
        }
        if(!"200".equals(code)) {
            success = Boolean.FALSE;
        }
        Result result = new Result(success, code, message, null);
        return result;
    }

}
