package com.fcang.spider.hotel.provider.biz;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.ProxyInfo;
import com.fcang.spider.hotel.provider.util.CtripHotelUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import redis.clients.jedis.Jedis;
@Component
public class PhantomjsManagerBiz{
	
	static Logger LOGGER = LoggerFactory.getLogger(PhantomjsManagerBiz.class);
	PhantomjsLoader loader = new PhantomjsLoader();

	@Value("${JS_PATH}")
	public String jsPath ;
	@Value("${WIN_JS_PATH}")
	public String winJsPath ;
	
	public static final String COMMAND_EXE ="phantomjs ";
	

	
	
	public static final  String PROXY_STR = " --proxy=";
	volatile LoadingCache<String, String> cookieCache;
	
	public String loadHTML(ProxyInfo proxy,JSONObject json) {
		try {
			
			String commande = COMMAND_EXE+(PhantomjsLoader.isWindows?winJsPath:jsPath);
			if(proxy!=null) {
				String ipproxy = getProxyIp(proxy);
				 commande = commande+PROXY_STR+ipproxy;
			}
			return loader.loadCtripHTMLContent(commande, json, "gb2312");
		} catch (Exception e) {
			LOGGER.error("loadHTML exception json={}",json,e);
		}finally {
			
		}
		return null;
	}

	private String getProxyIp(ProxyInfo proxy) {
		return proxy.getHost()+":"+proxy.getPort();
	}

	
	public static void main(String[] args) {
		Random random  = new Random();
		int i = 0;
		while(i++<1000){
			System.out.println(random.nextInt(21));
		}
	}
	
}
