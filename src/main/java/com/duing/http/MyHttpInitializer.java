package com.duing.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class MyHttpInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //数据的编码解码
        pipeline.addLast("codec",new HttpServerCodec());

        //聚合压缩器
        pipeline.addLast("compressor",new HttpContentCompressor());

        //聚合完整的消息 参数代表可以处理的最大的参数
        pipeline.addLast("aggregator",new HttpObjectAggregator(512*1024));

        pipeline.addLast(new MyHttpHandler());
    }
}
