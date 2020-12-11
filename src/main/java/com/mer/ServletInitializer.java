package com.mer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * @Program: zq-web-api
 * @Description: war启动包
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ZqWebApiApplication.class);
	}

}


