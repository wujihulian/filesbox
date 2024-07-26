package com.svnlan.NettyWebchat;

import com.svnlan.NettyWebchat.initializer.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


/**
 * @Author:
 * @Description:
 * @Date:
 */
@Profile({"dev","test", "pre", "pro"})
@SpringBootApplication
//@EnableAsync
public class NettyApplication implements CommandLineRunner  {
//    public static void main(String[] args) {
//        SpringApplication.run(NettyApplication.class);
//        System.out.println("SpringBoot start");
//    }

    @Override
    public void run(String... args) {
        new NettyServer().initNetty();
//        new NettyServer().start();
    }
}
