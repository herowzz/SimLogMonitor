package com.github.herowzz.simlogmonitor.watcher;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.herowzz.simlogmonitor.manager.ChannelManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WatcherServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(WatcherServerHandler.class);

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		logger.info("新连接:{}", address.getHostString());
		ChannelManager.add(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		logger.info("断开连接:{}", address.getHostString());
		ChannelManager.remove(ctx);
	}
	
}
