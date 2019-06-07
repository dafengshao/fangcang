package com.fcang.spider.hotel.provider.util;

import java.lang.reflect.Method;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;
@Component
public class JedisCommandsProxyConfiguration {
	static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	static {
		poolConfig.setMaxIdle(2);
		poolConfig.setMinIdle(2);
		poolConfig.setMaxTotal(4);
	}
    private JedisPool jedisPool = new JedisPool(poolConfig,"127.0.0.1", 6379);//,5000,"foobared");
	
	@Bean
	public JedisCommands jedis() {
		ProviderInvocationHandler handler = new ProviderInvocationHandler() {
			final Logger logger = LoggerFactory.getLogger(ProviderInvocationHandler.class);
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Jedis myJedis = null;
				try {
					if(method.getName().equals("toString")) {
						return this.toString();
					}
					if(method.getName().equals("shutdown")) {//keys * 危险指令 
						return "false";
					}
					myJedis = jedisPool.getResource();
					return method.invoke(myJedis, args);
				}catch (Exception e) {
					logger.error("jedis调用异常",e);
					throw e;
				}finally {
					if(myJedis!=null) {
						myJedis.close();
					}
				}
			}
		};
		
		return handler.getProxy(new Jedis());
	}
	
	@Bean
	public JedisLocker initJedisLocker(JedisCommands jedis) {
		return new JedisLocker(jedis);
	}
	
}
