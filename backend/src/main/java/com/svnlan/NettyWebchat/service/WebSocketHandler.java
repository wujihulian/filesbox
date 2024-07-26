package com.svnlan.NettyWebchat.service;

import com.svnlan.NettyWebchat.Common.CommonHandler;
import com.svnlan.NettyWebchat.Domain.ChannelSupervise;
import com.svnlan.NettyWebchat.Domain.ClientInfo;
import com.svnlan.enums.BusinessTypeEnum;
import com.svnlan.utils.BeanUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

/**
 * @Author:
 * @Description:
 * @Date: 
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<Object>{
    private WebSocketServerHandshaker handShaker;

    //连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        LogUtil.info("客户端加入连接：" + ctx.channel().id().asLongText());
//        ChannelSupervise.addChannel(ctx.channel());
    }
    //读信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FullHttpRequest){
                //以http请求形式接入，但是走的是websocket
                LogUtil.info("websocket WebSocketHandler FullHttpRequest");
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }else if (msg instanceof WebSocketFrame){
                //处理websocket客户端的消息
                LogUtil.info("websocket WebSocketHandler WebSocketFrame");
                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
            }else {
                LogUtil.info("websocket WebSocketHandler other");
//            ReferenceCountUtil.release(msg)
            };
        } catch (Exception e){
            LogUtil.error(e, "read0出错");
        }
    }

    //断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
//        LogUtil.info("客户端断开连接："+ctx.channel());
        ClientInfo clientInfo = (ClientInfo) ctx.channel().attr(AttributeKey.valueOf("clientInfo")).get();
        LogUtil.info("断开连接, " + JsonUtils.beanToJson(clientInfo));

        try {
            if (clientInfo == null){
                return;
            }
            //扫码登录
            if(BusinessTypeEnum.SCAN_LOGIN.getCode().equals(clientInfo.getBusinessType())) {
                //
                getLoginService().disconnect(ctx);
            }
        } catch (Exception e) {
            LogUtil.error(e, "断开处理失败" + JsonUtils.beanToJson(clientInfo));
        }
    }

    private NettyLoginService getLoginService() {
        BeanUtil beanUtil = SpringUtil.getBean(BeanUtil.class);
        return beanUtil.getNettyLoginService();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    /**
     * 唯一的一次http请求，用于创建websocket
     * */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        LogUtil.info("request info : " + req.decoderResult().isSuccess() + ", " + req.headers().get("Upgrade")
            + ", " + req.headers().get("Connection")
        );
        if (!req.decoderResult().isSuccess()) {
            //\\\\(不是)若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
//                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            //处理http请求
            ctx.fireChannelRead(req);
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8081/websocket/webchat/xx", null, true, 2048);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            System.out.println("handleHttpRequest url....." + req.uri());
            if (req.uri().indexOf("/websocket/webchat/scanLogin/") == 0){ //扫码登录
                String channelName = ChannelSupervise.getRoomName(req.uri());
                NettyLoginService nettyLoginService = getLoginService();
                CommonHandler.setBaseClientInfo(ctx, req, channelName, false, BusinessTypeEnum.SCAN_LOGIN.getCode());
                nettyLoginService.connect(ctx, channelName);
            }

            handShaker.handshake(ctx.channel(), req);
        }
    }



    /**
     * 拒绝不合法的请求，并返回错误信息
     * */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        LogUtil.info("webSocketLog handlerWebSocketFrame");
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            LogUtil.info("webSocketLog CloseWebSocketFrame");
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame || frame instanceof PongWebSocketFrame) {
            LogUtil.info("webSocketLog PingWebSocketFrame or PongWebSocketFrame");
//            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 其他格式帧
        ClientInfo clientInfo;
        try {
            clientInfo = CommonHandler.getClientInfo(ctx.channel());
            LogUtil.info("webSocketLog clientInfo=" + JsonUtils.beanToJson(clientInfo));
        } catch (Exception e){
            LogUtil.error(e.getMessage(), "非text消息, 客户端信息获取失败. ");
            clientInfo = new ClientInfo();
        }
        if (!(frame instanceof TextWebSocketFrame)) {

            try {
                LogUtil.info(String.format(
                        "%s frame types not supported", frame.getClass().getName()) + ", " + JsonUtils.beanToJson(clientInfo));
            } catch (Exception e){
                LogUtil.error(e.getMessage(), "非text消息, ");
            }
            return;
        }
        if (clientInfo.getRoomName() == null){
            return;
        }
        // 返回应答消息
        String message = ((TextWebSocketFrame) frame).text();
        LogUtil.info("服务端收到：" + message);
        if (message.equals("1")){ //保活
            ChannelSupervise.sendToUser(ctx, "2");
            return;
        }

        String businessType = clientInfo.getBusinessType();
        if(businessType.equals(BusinessTypeEnum.SCAN_LOGIN.getCode())) { //扫码登录
            //
            getLoginService().receiveMessage(ctx, message);
        }

    }
}
