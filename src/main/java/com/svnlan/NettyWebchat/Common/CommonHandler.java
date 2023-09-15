package com.svnlan.NettyWebchat.Common;

import com.svnlan.NettyWebchat.Domain.ClientInfo;
import com.svnlan.enums.BusinessTypeEnum;
import com.svnlan.utils.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

/**
 * @Author:
 * @Description:
 */
public class CommonHandler {
    public static void setBaseClientInfo(ChannelHandlerContext ctx, FullHttpRequest req, String channelName, Boolean isCommon) {
        //
        setBaseClientInfo(ctx, req, channelName, isCommon, BusinessTypeEnum.COMMON.getCode());
    }

    public static void setBaseClientInfo(ChannelHandlerContext ctx, FullHttpRequest req, String channelName, Boolean isCommon,
                                         String businessType) {
        //记录roomName
        AttributeKey<ClientInfo> attributeKey;
        if (AttributeKey.exists("clientInfo")) {
            attributeKey = AttributeKey.valueOf("clientInfo");
        } else {
            attributeKey = AttributeKey.newInstance("clientInfo");
        }
        //客户端信息写入
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setRoomName(channelName);
        clientInfo.setUserAgent(req.headers().get("user-agent"));
        clientInfo.setReferer(req.headers().get("referer"));
        clientInfo.setIp(IpUtil.getIp(req, ctx));
        clientInfo.setLoginTime(System.currentTimeMillis());
        if (isCommon){
            Long userId = getUserIdFromChannelName(channelName);
            clientInfo.setUserId(userId);
        }
        clientInfo.setCommon(isCommon);
        clientInfo.setBusinessType(businessType);

        ctx.channel().attr(attributeKey).set(clientInfo);
    }

    private static Long getUserIdFromChannelName(String channelName) {
        String hexUserId = channelName.split("_")[0];
        return Long.valueOf(hexUserId, 16);
    }

    public static ClientInfo getClientInfo(Channel channel) {
        ClientInfo clientInfo;
        AttributeKey<ClientInfo> attributeKey = AttributeKey.valueOf("clientInfo");
        try {
            clientInfo = channel.attr(attributeKey).get();
        } catch (Exception e){
            LogUtil.error(e, "获取clientInfo 失败");
            clientInfo = new ClientInfo();
        }
        if (clientInfo == null){
            clientInfo = new ClientInfo();
        }
        return clientInfo;
    }
    public static ClientInfo getClientInfo(ChannelHandlerContext ctx) {
        return getClientInfo(ctx.channel());
    }

}
