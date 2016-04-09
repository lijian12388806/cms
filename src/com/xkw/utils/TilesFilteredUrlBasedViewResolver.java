package com.xkw.utils;

import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class TilesFilteredUrlBasedViewResolver extends UrlBasedViewResolver {
	
	private static Logger logger = LogManager
			.getLogger(TilesFilteredUrlBasedViewResolver.class);

	protected boolean canHandle(String viewName, Locale locale) {
        logger.info("viewName:" + viewName);
		return viewName.startsWith("tiles");
	}

}
