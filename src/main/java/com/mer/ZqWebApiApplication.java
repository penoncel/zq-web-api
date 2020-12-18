package com.mer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Program: zq-web-api
 * @Description: 启动类
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@SpringBootApplication
public class ZqWebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZqWebApiApplication.class, args);
	}

	/**
	 * 需要整改
	 * 1、aop 日志更新 ok
	 * 2、sql拦截器    ok
	 * 3、日志        ok
	 * 4、加解密
	 *
	 */
}
