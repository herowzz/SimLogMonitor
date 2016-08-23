package com.github.herowzz.simlogmonitor;

import com.github.herowzz.simlogmonitor.collect.ColletSocketServer;
import com.github.herowzz.simlogmonitor.watcher.WatcherServer;

public class LogServer {

	private static int watcherPort = 8088;
	private static int collectPort = 9988;

	public static void main(String[] args) throws Exception {
		ColletSocketServer colletServer = new ColletSocketServer(collectPort);
		colletServer.start();

		WatcherServer watcherServer = new WatcherServer(watcherPort);
		watcherServer.start();
	}

}
