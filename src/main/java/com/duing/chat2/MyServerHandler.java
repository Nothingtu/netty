package com.duing.chat2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.channel.Channel;

import java.util.Iterator;

//另一种自定义的处理器 继承自SimpleChannelInboundHandler父类，是ChannelInboundHandler的子类
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    //ChannelGroup本质是一个set集合，用来管理通道的
    // GlobalEventExecutor全局事件执行器单例
    private  static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //通道是活跃的时触发的方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("welcome to my place!!!\r\n");
        ctx.flush();
    }

    //通常被用来处理用户下线的方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("用户" + ctx.channel().remoteAddress() + "下线了\r\n");
    }

    //连接断开时执行的方法，最后一个执行的方法
    //此方法会自动将channel从ChannelGroup channels 中移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("用户" + ctx.channel().remoteAddress() + "断开了连接\r\n");
    }

    //刚刚建立连接时被执行的方法，第一个执行的方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将连接的通道注入channels中
        channels.add(ctx.channel());
        System.out.println("用户" + ctx.channel().remoteAddress() + "连接成功了\r\n");
    }

    //读取客户端的信息，并处理将信息推送给其他的客户
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel selfChannel = ctx.channel();

        System.out.println("服务端接收到了" + selfChannel.remoteAddress() + ":" + s + "\r\n");

        Iterator<Channel> it = channels.iterator();
        while (it.hasNext()) {
            Channel channel = it.next();

            if (selfChannel != channel) {
                //广播该消息
                channel.writeAndFlush("[服务器] - " + selfChannel.remoteAddress()
                        + "发送消息：" + s + "\n");
//                channel.writeAndFlush(selfChannel.remoteAddress() + ":" + s);
                continue;
            }
            String answer = null;
            if (s.length() == 0) {
                answer = "please speak loudly !!!\r\n";
            } else {
                answer = "Did you said :" + s + "?\r\n";
            }
            selfChannel.writeAndFlush(answer);
        }
    }

    //处理异常的方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭上下文，即通道
        ctx.close();
    }
}
