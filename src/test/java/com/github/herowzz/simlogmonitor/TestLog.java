package com.github.herowzz.simlogmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class TestLog {

	private static Logger logger = LoggerFactory.getLogger(TestLog.class);

	public static void main(String[] args) throws Exception {
		loadLogFile();
		for (int i = 0; i < 100; i++) {
			logger.info("TestLog info " + i);
			Thread.sleep(3000);
		}
	}

	private static void loadLogFile() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		try {
			configurator.doConfigure("src/test/resources/test-logback.xml");
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

}
