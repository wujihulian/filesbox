package com.svnlan.NettyWebchat.service.impl;

import com.svnlan.NettyWebchat.Common.SpringManager;
import com.svnlan.NettyWebchat.utils.ResponseUtil;
import com.svnlan.NettyWebchat.utils.RequestTransUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
//            boolean isKeepAlive = HttpUtil.isKeepAlive(req);
//        FullHttpRequest fullHttpRequest = (FullHttpRequest) req;
        fullHttpRequest.retain();
        MockHttpServletRequest servletRequest = RequestTransUtil.transRequest2Spring(fullHttpRequest);
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        try {
            SpringManager.getInstance().getDispatcherServlet().service(servletRequest, servletResponse);
            FullHttpResponse nettyResponse = RequestTransUtil.transResponse2Netty(servletResponse);
            ResponseUtil.sendHttpResponse(ctx, fullHttpRequest, nettyResponse);
        } catch (Exception e) {
            ResponseUtil.sendHttpResponse(ctx, fullHttpRequest, ResponseUtil.get500Response());
        }
//        NettyWebchatService webchatService = SpringUtil.getBean(NettyWebchatService.class);
//        webchatService.aaa(ctx, fullHttpRequest);
    }
}
