package com.mer.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Program: zq-web-api
 * @Description: 日志
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
public class WriteFrom {
	public static String writerEx(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
