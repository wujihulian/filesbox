package com.svnlan.NettyWebchat.service.impl;

import com.svnlan.utils.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author:
 * @Description: 心跳检测
 * @Date:
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE){
                LogUtil.info("关闭这个不活跃通道！, " + ctx.channel().id().asLongText());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
