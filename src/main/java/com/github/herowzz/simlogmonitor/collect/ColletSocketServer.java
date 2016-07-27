package com.github.herowzz.simlogmonitor.collect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColletSocketServer extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ColletSocketServer.class);

	private final int port;
	private boolean closed = false;
	private ServerSocket serverSocket;
	private List<ColletSocketNode> socketNodeList = new ArrayList<ColletSocketNode>();

	public ColletSocketServer(int port) {
		this.port = port;
	}

	public void run() {
		try {
			logger.info("Collet Server start... port[{}]", port);
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
			while (!closed) {
				logger.info("Waiting to accept a new client.");
				Socket socket = serverSocket.accept();
				logger.info("Connected to client at " + socket.getRemoteSocketAddress());
				logger.info("Starting new socket node.");
				ColletSocketNode newSocketNode = new ColletSocketNode(this, socket);
				synchronized (socketNodeList) {
					socketNodeList.add(newSocketNode);
				}
				new Thread(newSocketNode).start();
			}
		} catch (Exception e) {
			if (closed) {
				logger.info("Exception in run method for a closed server. This is normal.");
			} else {
				logger.error("Unexpected failure in run method", e);
			}
		}
	}

	public boolean isClosed() {
		return closed;
	}

	public void close() {
		closed = true;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close serverSocket", e);
			} finally {
				serverSocket = null;
			}
		}

		logger.info("closing this server");
		synchronized (socketNodeList) {
			for (ColletSocketNode sn : socketNodeList) {
				sn.close();
			}
		}
		if (socketNodeList.size() != 0) {
			logger.warn("Was expecting a 0-sized socketNodeList after server shutdown");
		}

	}

	public void socketNodeClosing(ColletSocketNode sn) {
		logger.info("Removing {}", sn);

		synchronized (socketNodeList) {
			socketNodeList.remove(sn);
		}
	}

}
