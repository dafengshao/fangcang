package com.learn.ren;

import java.util.Set;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;

public class WhiteListManager {

	Set<IpInfo> setIps = new ConcurrentHashSet<>();
	public boolean addWhiteIpAddress(String ip) {
		IpInfo ipInfo = new IpInfo(ip);
		return setIps.add(ipInfo);
	}
	public boolean isWhiteIpAddress(String ip) {
		return setIps.contains(new IpInfo(ip));
	}
	public int size() {
		return setIps.size();
	}
}
