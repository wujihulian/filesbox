package com.svnlan.NettyWebchat.initializer;

import com.svnlan.NettyWebchat.service.WebSocketHandler;
import com.svnlan.NettyWebchat.service.impl.HeartBeatServerHandler;
import com.svnlan.NettyWebchat.service.impl.HttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(2, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    //主业务线程池
    private static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(DEFAULT_EVENT_LOOP_THREADS);
    //耗时业务线程池
    private static final EventExecutorGroup largeConsumptionExecutorGroup = new DefaultEventExecutorGroup(DEFAULT_EVENT_LOOP_THREADS);


//        private static final ChannelTrafficShapingHandler shapingHandler = new ChannelTrafficShapingHandler(2000);
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(1024 * 20));//聚合器，使用websocket会用到
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
        ch.pipeline().addLast("compress", new WebSocketServerCompressionHandler());
        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(0, 0, 120, TimeUnit.SECONDS));
        ch.pipeline().addLast(new HeartBeatServerHandler());
//        ch.pipeline().addLast(shapingHandler);
//                ch.pipeline().addLast("handler", new BaseEventHandler());
        ch.pipeline().addLast(eventExecutorGroup,  new WebSocketHandler());//自定义的业务handler
        ch.pipeline().addLast(eventExecutorGroup,  new HttpHandler());//自定义的业务handler
    }
}
