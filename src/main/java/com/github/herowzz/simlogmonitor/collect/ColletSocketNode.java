package com.github.herowzz.simlogmonitor.collect;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.LoggerFactory;

import com.github.herowzz.simlogmonitor.manager.ChannelManager;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ColletSocketNode implements Runnable {

	Socket socket;
	LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	ObjectInputStream ois;
	SocketAddress remoteSocketAddress;

	Logger logger;
	boolean closed = false;
	ColletSocketServer socketServer;

	public ColletSocketNode(ColletSocketServer socketServer, Socket socket) {
		this.socketServer = socketServer;
		this.socket = socket;
		remoteSocketAddress = socket.getRemoteSocketAddress();
		logger = context.getLogger(ColletSocketNode.class);
	}

	public void run() {

		try {
			ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (Exception e) {
			logger.error("Could not open ObjectInputStream to " + socket, e);
			closed = true;
		}

		ILoggingEvent event;
		Logger remoteLogger;

		try {
			while (!closed) {
				event = (ILoggingEvent) ois.readObject();
				remoteLogger = context.getLogger(event.getLoggerName());
				remoteLogger.callAppenders(event);

				String message = event.getFormattedMessage();
				LocalDateTime dateTime = new Timestamp(event.getTimeStamp()).toLocalDateTime();
				String dateTimeStr = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
				StringBuilder logMsgBuild = new StringBuilder();
				logMsgBuild.append("[").append(dateTimeStr).append("] [").append(event.getLevel()).append("] ").append(message);
				TextWebSocketFrame tws = new TextWebSocketFrame(logMsgBuild.toString());
				ChannelManager.sendAll(tws);
			}
		} catch (java.io.EOFException e) {
			logger.info("Caught java.io.EOFException closing connection.");
		} catch (java.net.SocketException e) {
			logger.info("Caught java.net.SocketException closing connection.");
		} catch (IOException e) {
			logger.info("Caught java.io.IOException: " + e);
			logger.info("Closing connection.");
		} catch (Exception e) {
			logger.error("Unexpected exception. Closing connection.", e);
		}

		socketServer.socketNodeClosing(this);
		close();
	}

	void close() {
		if (closed) {
			return;
		}
		closed = true;
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				logger.warn("Could not close connection.", e);
			} finally {
				ois = null;
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + remoteSocketAddress.toString();
	}

}
