package com.github.herowzz.simlogmonitor.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class WatcherServer {

	private static Logger logger = LoggerFactory.getLogger(WatcherServer.class);

	private final int port;

	public WatcherServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 2048);
			b.option(ChannelOption.SO_RCVBUF, 2048);
			b.option(ChannelOption.SO_SNDBUF, 2048);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.option(ChannelOption.SO_LINGER, 1);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.childOption(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new LoggingHandler(LogLevel.INFO));
			b.childHandler(new WatcherChannelInitializer());

			Channel ch = b.bind(port).sync().channel();
			logger.info("Watcher Server start... port[{}]", port);

			ch.closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
