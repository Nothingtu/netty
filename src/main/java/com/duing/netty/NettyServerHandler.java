package com.duing.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

//提供了不同状态时的方法
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //通道刚刚连接时调用的方法 业务使用中往往用作欢迎消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //直接写入字符串
        ctx.writeAndFlush("welcome to server of netty");
    }

    //数据读取完成时调用的方法
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        Unpooled是netty提供的Buf和字符串之间转换的工具类
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client ,你好",CharsetUtil.UTF_8));
    }

    //触发消息的读取的方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
        //打印输出客户端的远程地址
        System.out.println(ctx.channel().remoteAddress());
    }
}
