package com.svnlan.NettyWebchat.service;

import io.netty.channel.ChannelHandlerContext;

/**
 * @description: 扫码登录
 */
public interface NettyLoginService {

    void connect(ChannelHandlerContext ctx, String channelName);

    void disconnect(ChannelHandlerContext ctx);

    void receiveMessage(ChannelHandlerContext ctx, String message);

}
