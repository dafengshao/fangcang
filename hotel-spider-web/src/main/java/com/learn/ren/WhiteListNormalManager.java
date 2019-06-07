package com.learn.ren;

import java.util.Set;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;

public class WhiteListNormalManager {

	Set<String> setIps = new ConcurrentHashSet<>();
	public boolean addWhiteIpAddress(String ip) {
		return setIps.add(ip);
	}
	public boolean isWhiteIpAddress(String ip) {
		return setIps.contains(ip);
	}
	
	public int size() {
		return setIps.size();
	}
	
}
