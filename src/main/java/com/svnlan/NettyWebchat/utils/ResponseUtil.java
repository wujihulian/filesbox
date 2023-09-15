package com.svnlan.NettyWebchat.utils;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author:
 * @Description:
 */
public class ResponseUtil {

    /**
     * 获取400响应
     */
    public static FullHttpResponse get400Response(){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
    }

    /**
     * 获取200响应
     */
    public static FullHttpResponse get200Response(String content){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.getBytes()));
    }

    /**
     * 获取500响应
     */
    public static FullHttpResponse get500Response(){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer("服务器异常".getBytes()));
    }

    /**
     * 发送HTTP响应
     */
    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        // 返回应答给客户端
        if (response.status().code() != 200) {
            ByteBufUtil.writeUtf8(response.content(), response.status().toString());
        }

        //添加header描述length，避免客户端接收不到数据
        if(StringUtils.isEmpty(response.headers().get(HttpHeaderNames.CONTENT_TYPE))){
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        }
        if(StringUtils.isEmpty(response.headers().get(HttpHeaderNames.CONTENT_LENGTH))){
            response.headers().add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        }

        //解决跨域的问题
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS,"*");//允许headers自定义
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, PUT,DELETE");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");

        // 如果是非Keep-Alive，关闭连接
        if (! HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }else{
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.channel().writeAndFlush(response);
        }
    }

}