package com.svnlan.NettyWebchat.initializer;

import com.svnlan.utils.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Service;


/**
 * @Author:
 * @Description: 服务启动
 * @Date:
 */
@Service
public class NettyServer {
    public void initNetty(){
        new Thread(() -> new NettyServer().start()).start();
    }
    public void start(){
        LogUtil.info("正在启动websocket服务器");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            LogUtil.info("availableProcessors, " + Runtime.getRuntime().availableProcessors());
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new WebSocketChannelInitializer());
            //设置高低水位
            bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024,64 * 1024));
            Channel channel = bootstrap.bind(81).sync().channel();
            LogUtil.info("webSocket服务器启动成功："+channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LogUtil.error(e,"运行出错");
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            LogUtil.info("websocket服务器已关闭");
        }
    }

}
