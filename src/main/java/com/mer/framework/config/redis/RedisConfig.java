package com.mer.framework.config.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Program: zq-web-api
 * @Description: 读取redis配置类
 * @Author: 赵旗
 * @Create: 2020-12-09 12:08
 */
@Configuration
@Data
@Slf4j
public class RedisConfig {
	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;


	/**
	 * 封装获取连接池
	 * */
	@Bean
	public JedisPool redisPoolFactory()  throws Exception {
		log.info("JedisPool注入成功！！ IP：" + host + "  port:" + port+ "  DB:" + database);

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认 true
		jedisPoolConfig.setBlockWhenExhausted(true);
		//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
		jedisPoolConfig.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
		//是否启用pool的jmx管理功能, 默认true
		jedisPoolConfig.setJmxEnabled(true);
		//MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
		jedisPoolConfig.setJmxNamePrefix("pool");
		//是否启用后进先出, 默认true
		jedisPoolConfig.setLifo(true);

		//最大空闲连接数, 默认8个
		jedisPoolConfig.setMaxIdle(maxIdle);
		//最大连接数, 默认8个
		jedisPoolConfig.setMaxTotal(maxActive);
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		jedisPoolConfig.setMaxWaitMillis(-1);
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		jedisPoolConfig.setMinEvictableIdleTimeMillis(1800000);
		//最小空闲连接数, 默认0
		jedisPoolConfig.setMinIdle(minIdle);
		//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		jedisPoolConfig.setNumTestsPerEvictionRun(3);
		//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
		//最大的等待时间，
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1);

		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password,database);
		return jedisPool;
	}

}
