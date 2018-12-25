package com.fcang.spider.hotel.provider.biz;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.JoupUtil;
import com.fcang.spider.hotel.core.ProxyInfo;
@Component
public class ProxyProviderBiz {
	static Logger logger = LoggerFactory.getLogger(ProxyProviderBiz.class);

	static String PROXY_URL_ANONY = "http://31f.cn/anonymous-proxy/";
	static String PROXY_URL_HIGH = "http://31f.cn/anonymous-proxy/";
	
	static final String XICI_DAILI_URL = "https://www.xicidaili.com/nn/";
	
	static final String PROXY_API = "http://d.jghttp.golangapi.com/getip?num=1&type=2&pro=&city=0&yys=0&port=1&pack=3298&ts=1&ys=0&cs=0&lb=1&sb=0&pb=4&mr=2&regions=";
	
	
	
	public static ProxyInfo getProxyInfoRandom() {
		BaseFullResponse<Document> buildByUrl = JoupUtil.buildByUrl(PROXY_API, null, null,null);
		String text = buildByUrl.getData().body().text();
		JSONObject jsonObject = (JSONObject)JSONObject.parse(text);
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		if(!jsonArray.isEmpty()) {
			ProxyInfo info= new ProxyInfo();
			JSONObject object = (JSONObject)jsonArray.get(0);
			info.setHost(object.getString("ip"));
			info.setPort(object.getInteger("port"));
			return info;
		}else {
			logger.error("没有获取到代理ip");
		}
		return getProxyInfoRandom();
	}
	
}
