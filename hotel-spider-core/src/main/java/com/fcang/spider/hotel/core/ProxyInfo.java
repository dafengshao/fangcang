package com.fcang.spider.hotel.core;

import java.util.concurrent.atomic.AtomicInteger;

public class ProxyInfo {

	String host;
	int port;
	AtomicInteger count = new AtomicInteger(0);
	AtomicInteger failCount = new AtomicInteger(0);
	String type;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAndIncrement() {
		return count.getAndIncrement();
	}
	public int getAndIncrementFailCount() {
		return failCount.getAndIncrement();
	}
}
