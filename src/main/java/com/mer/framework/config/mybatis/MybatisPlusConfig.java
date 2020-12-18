package com.mer.framework.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.mer.framework.web.interceptor.MybatisInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author zhoaqi 15701556037
 * Description：MybatisPlus 配置文件
 */
@Configuration
@MapperScan("com.mer.project.dao")
public class MybatisPlusConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }


    /**
     * mybatis 自定义拦截器
     */
    @Bean
    public Interceptor getInterceptor() {
        return new MybatisInterceptor();
    }

}