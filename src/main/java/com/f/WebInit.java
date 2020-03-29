package com.f;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Objects;

@WebListener("WebInit")
public class WebInit implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(WebInit.class);
    private NioServerSocketChannel nioServerSocketChannel;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (Objects.nonNull(nioServerSocketChannel)) {
            nioServerSocketChannel.disconnect();
            nioServerSocketChannel = null;
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Thread server = new Thread(() -> {
            NioEventLoopGroup boss = new NioEventLoopGroup();
            NioEventLoopGroup worker = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            try {
                ChannelFuture channelFuture = serverBootstrap.group(boss, worker)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childHandler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(
                                        new StringDecoder(),
                                        new HelloWorldHandler());
                            }
                        })
                        .bind("127.0.0.1", 49159).sync();
                nioServerSocketChannel = (NioServerSocketChannel) channelFuture.channel();
                logger.info("netty server 启动成功....");
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });
        server.setDaemon(true);
        server.setName("tcp server");
        server.start();
    }
}
