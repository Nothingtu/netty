package com.duing.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * 泛型需设置为FullHttpRequest
 */
public class MyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //DefaultFullHttpResponse是一个默认的http响应
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_0,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("hello ,my dear".getBytes())
        );
        HttpHeaders httpHeaders = defaultFullHttpResponse.headers();
        httpHeaders.add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.TEXT_PLAIN + ";charset=UTF-8");

        httpHeaders.add(HttpHeaderNames.CONTENT_LENGTH,defaultFullHttpResponse.content().readableBytes());
        channelHandlerContext.write(defaultFullHttpResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
