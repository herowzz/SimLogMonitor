package com.github.herowzz.simlogmonitor.manager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public abstract class ChannelManager {

	public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static void add(ChannelHandlerContext ctx) {
		channelGroup.add(ctx.channel());
	}

	public static void remove(ChannelHandlerContext ctx) {
		channelGroup.remove(ctx.channel());
	}

	public static void sendAll(TextWebSocketFrame tws) {
		channelGroup.writeAndFlush(tws);
	}
}
