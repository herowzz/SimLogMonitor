package com.github.herowzz.simlogmonitor.watcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public final class WatcherServerIndexPage {

	private static Logger logger = LoggerFactory.getLogger(WatcherServerIndexPage.class);

	private static Configuration cfg = new Configuration(Configuration.getVersion());
	private static String filePath = WatcherServerIndexPage.class.getClassLoader().getResource("template").getPath();

	static {
		try {
			cfg.setDirectoryForTemplateLoading(new File(filePath));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		} catch (Exception e) {
			logger.error("can't find filePath:" + filePath, e);
		}
	}

	public static ByteBuf getContent(String webSocketLocation) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(baos)) {
			Template template = cfg.getTemplate("index.html");
			Map<String, String> root = new HashMap<>();
			root.put("webSocketLocation", webSocketLocation);
			template.process(root, writer);
			byte[] fileByteArray = baos.toByteArray();
			return Unpooled.copiedBuffer(fileByteArray);
		} catch (Exception e) {
			logger.error("getContent error", e);
			return Unpooled.copiedBuffer(e.getMessage(), CharsetUtil.UTF_8);
		}
	}

}
