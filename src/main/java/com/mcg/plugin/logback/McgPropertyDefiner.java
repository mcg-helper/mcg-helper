package com.mcg.plugin.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.PropertyDefinerBase;

public class McgPropertyDefiner extends PropertyDefinerBase {

	private static Logger logger = LoggerFactory.getLogger(McgPropertyDefiner.class);
	
	@Override
	public String getPropertyValue() {
		String webPath = this.getClass().getClassLoader().getResource("/").getPath().replace("mcg/WEB-INF/classes/", "../");
		logger.info("mcg-helper日志输出路径：{}", webPath);
		return webPath;
	}
	
}
