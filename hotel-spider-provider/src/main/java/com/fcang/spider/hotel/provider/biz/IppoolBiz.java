package com.fcang.spider.hotel.provider.biz;

public interface IppoolBiz {
	String getProxyipFromPool();
	
	void removeProxyIp(Integer score);
}
