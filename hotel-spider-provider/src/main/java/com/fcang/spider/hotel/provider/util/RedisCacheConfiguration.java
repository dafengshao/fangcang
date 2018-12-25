package com.fcang.spider.hotel.provider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {
    Logger logger = LoggerFactory.getLogger(RedisCacheConfiguration.class);

    @Bean("jedis.config")
    public JedisPoolConfig jedisPoolConfig(//
                                           @Value("${spring.redis.pool.min-idle}") int minIdle, //
                                           @Value("${spring.redis.pool.max-idle}") int maxIdle, //
                                           @Value("${spring.redis.pool.max-wait}") int maxWaitMillis) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        // 是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
        return config;
    }

    @Bean
    public JedisPool jedisPool(//
                               @Qualifier("jedis.config") JedisPoolConfig config, //
                               @Value("${spring.redis.host}") String host, //
                               @Value("${spring.redis.timeout}") int timeout, //
                               @Value("${spring.redis.password}") String password, //
                               @Value("${spring.redis.port}") int port) {
        return new JedisPool(config, host, port, timeout, password);
    }

}