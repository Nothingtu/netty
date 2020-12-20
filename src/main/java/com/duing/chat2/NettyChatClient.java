package com.duing.chat2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class NettyChatClient {
    public static void main(String[] args){
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer());

        System.out.println("客户端初始化完成");

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1",7878).sync();
//            future.channel().closeFuture().sync();
            while(true){
                //键盘输入的获取方式
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String msg = br.readLine();
                future.channel().writeAndFlush(msg + "\r\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            loopGroup.shutdownGracefully();
        }
    }
}
