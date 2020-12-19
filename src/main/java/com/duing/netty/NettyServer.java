package com.duing.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyServer {
    public static void main(String[] args){
        //创建时间循环组的主从对象
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //服务端的启动引导对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置bootstrap的相关配置
        bootstrap.group(bossGroup,workerGroup)
                //设置通道的类型
                .channel(NioServerSocketChannel.class)
                //处理服务端的通道
                .handler(new LoggingHandler(LogLevel.INFO))
                //处理客户端的通道
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //尾部插入法，增加自定义的处理器
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("服务端初始化完成");


        try {
            //设置异步启动并绑定端口
            ChannelFuture future = bootstrap.bind(9999).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.getStackTrace();
        }finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
