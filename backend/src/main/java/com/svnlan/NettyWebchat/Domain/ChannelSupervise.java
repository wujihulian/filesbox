package com.svnlan.NettyWebchat.Domain;

import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author:
 * @Description:
 */
public class ChannelSupervise {


    //游客
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Channel>> visitorRooms = new ConcurrentHashMap<>();
    //通用业务
    public static ConcurrentHashMap<String, ConcurrentHashMap<Channel, Long>> commonRooms = new ConcurrentHashMap<>();
    //扫码登录业务
    public static ConcurrentHashMap<String, ConcurrentHashMap<Channel, String>> scanLoginRooms = new ConcurrentHashMap<>();
    /** 聊天室内同步发送消息kafka */
    public static final String SEND_ROOMMESSAGE_Netty = "sendRoomMessageNetty";

    //服务标识 uuid
    public static String uuid = RandomUtil.getuuid();

    /**
     * @Description: 发送通用返回结果
     * @params:  [ctx, code, type]
     * @Return:  void
     * @Modified:
     */
    public static void sendCommonReturn(ChannelHandlerContext ctx, String code, String type) {
        CommonReturnMessage commonReturnMessage = new CommonReturnMessage(code, type);
        ChannelSupervise.sendToUser(ctx, JsonUtils.beanToJson(commonReturnMessage));
    }

    /**
     * @description: 发送通用返回结果
     * @param ctx
     * @param type
     * @param code
     * @param message
     * @return void
     */
    public static void sendCommonReturn(ChannelHandlerContext ctx, String type, String code, String message) {
        CommonReturnMessage commonReturnMessage = new CommonReturnMessage(code, type, message);
        ChannelSupervise.sendToUser(ctx, JsonUtils.beanToJson(commonReturnMessage));
    }

    public static String getRoomName(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    public static void sendToUser(ChannelHandlerContext ctx, String s) {
        sendToUser(ctx.channel(), s);
    }
    public static void sendToUser(Channel channel, String s){
        if (channel == null){
            return;
        }

        if (!channel.isWritable()){
            LogUtil.error("无法写了,"
                    + (channel.id() == null ? "" : channel.id().asLongText()) );
            return;
        }
        try {
            channel.writeAndFlush(new TextWebSocketFrame(s));
        } catch (Exception e){
            LogUtil.error(e, "netty消息发送失败, " + s);
        }
    }
}
