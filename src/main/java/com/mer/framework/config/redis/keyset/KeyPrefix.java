package com.mer.framework.config.redis.keyset;

/***
 * 设置key
 *
 * @author Administrator*/
public interface KeyPrefix {

	/**
	 * 设置过期时间
	 * @return
	 */
	 int expireSeconds();

	/**
	 * 前缀
	 * @return
	 */
	 String getPrefix();
	
}
