package com.f;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger = LoggerFactory.getLogger(HelloWorldHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("receive msg: {}", msg);
        ctx.writeAndFlush(Unpooled.copiedBuffer("server message".getBytes()));
    }
}
