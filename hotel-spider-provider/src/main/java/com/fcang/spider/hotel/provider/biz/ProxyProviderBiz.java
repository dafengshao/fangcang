package com.fcang.spider.hotel.provider.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.core.ProxyInfo;

import redis.clients.jedis.commands.JedisCommands;
@Component
public class ProxyProviderBiz {
	private static final String PROXY_IP = "proxy_ip";

	static Logger logger = LoggerFactory.getLogger(ProxyProviderBiz.class);

	static String PROXY_URL_ANONY = "http://31f.cn/anonymous-proxy/";
	static String PROXY_URL_HIGH = "http://31f.cn/anonymous-proxy/";
	
	static final String XICI_DAILI_URL = "https://www.xicidaili.com/nn/";
	
	static final String PROXY_API = "http://d.jghttp.golangapi.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&pack=3298&ts=1&ys=0&cs=0&lb=1&sb=0&pb=4&mr=2&regions=";
	
	@Autowired
	JedisCommands jedis;
	
	public ProxyInfo getProxyInfoRandom() {
		Set<String> zrangeByScore = jedis.zrangeByScore(PROXY_IP, 0D, 100D,0,10);
		if(CollectionUtils.isEmpty(zrangeByScore)) {
			throw new RuntimeException("代理ip没有了");
		}
		List<String> list = new ArrayList<>(zrangeByScore);
		String str = list.get(new Random().nextInt(zrangeByScore.size()));
		ProxyInfo info = new ProxyInfo();
		String[] split = str.split(":");
		info.setHost(split[0]);
		info.setPort(Integer.valueOf(split[1]));
		return info;
	}
	
	
	public void score(ProxyInfo info,int s) {
		if(info==null) {
			return;
		}
		jedis.zincrby(PROXY_IP, s, info.getHost()+":"+info.getPort());
	}
	
}
