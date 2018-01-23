package cn.worldwalker.game.wyqp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.worldwalker.game.wyqp.common.constant.Constant;
import cn.worldwalker.game.wyqp.server.initializer.WebSocketServerInitializer;

@Component("webSocketServer")
public class WebSocketServer implements Server{
	private Logger logger = Logger.getLogger(WebSocketServer.class);

    private int port = Constant.websocketPort;
    @Autowired
    private WebSocketServerInitializer serverInitializer;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    @Override
    public void start() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            logger.info("Starting WebSocketGameServer... Port: " + port);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
        	workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void restart() throws Exception {
        shutdown();
        start();
    }

    @Override
    public void shutdown() {
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }
}
