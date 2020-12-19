package com.duing.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {
    public static void main(String[] args){
        //事件循环组对象
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        //客户端的启动引导对象
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(loopGroup)
                //设置通道的类型
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("客户端初始化完成了");

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1",9999).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            loopGroup.shutdownGracefully();
        }
    }
}
