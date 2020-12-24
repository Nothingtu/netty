package com.duing.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

//泛型代表的处理数据的单位
//TextWebSocketFrame 文本信息帧
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("[服务器]接收到了:" + textWebSocketFrame.text());

        TextWebSocketFrame textWebSocketFrame1 = new TextWebSocketFrame("hello ,there is test");

        channelHandlerContext.channel().writeAndFlush(textWebSocketFrame1);
    }
}
