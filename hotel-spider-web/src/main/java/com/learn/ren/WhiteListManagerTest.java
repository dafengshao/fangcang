package com.learn.ren;

import org.junit.Test;

public class WhiteListManagerTest {
@Test
	public void test() {
		WhiteListManager manager = new WhiteListManager();
		boolean addWhiteIpAddress = manager.addWhiteIpAddress("59.46.0.31");
		boolean addWhiteIpAddress2 = manager.addWhiteIpAddress("124.42.7.103");
		boolean addWhiteIpAddress3 = manager.addWhiteIpAddress("221.229.166.55");
		boolean addWhiteIpAddress4 = manager.addWhiteIpAddress("125.67.237.49");
		boolean addWhiteIpAddress5 = manager.addWhiteIpAddress("125.67.237.49");
		boolean whiteIpAddress = manager.isWhiteIpAddress("61.129.70.109");
		boolean whiteIpAddress2 = manager.isWhiteIpAddress("221.229.166.55");
		
	}


	public static void normal() {
		WhiteListNormalManager normang = new WhiteListNormalManager();
		WhiteListManager ipmn = new WhiteListManager();
		//59641
		System.out.println(System.currentTimeMillis()+"---start");
		while(true) {
			String randomIp = IpUtil.getRandomIp();
			boolean whiteIpAddress = normang.isWhiteIpAddress(randomIp);
			boolean addWhiteIpAddress = normang.addWhiteIpAddress(randomIp);
//			boolean whiteIpAddress1 = ipmn.isWhiteIpAddress(randomIp);
//			boolean addWhiteIpAddress1 = ipmn.addWhiteIpAddress(randomIp);
			if(normang.size()>59550) {
				System.out.println(System.currentTimeMillis()+"--normal---"+randomIp+"---"+normang.size()+"个");
			}
//			if(addWhiteIpAddress!=addWhiteIpAddress1) {
//				System.out.println(System.currentTimeMillis()+"--addWhiteIpAddress---"+randomIp+"---"+normang.size()+"个");
//			}
		}
		
	}
	public static void ipi() {
		WhiteListManager normang = new WhiteListManager();
		
		//68738个
		System.out.println(System.currentTimeMillis()+"---start");
		while(true) {
			String randomIp = IpUtil.getRandomIp();
			boolean whiteIpAddress = normang.isWhiteIpAddress(randomIp);
			boolean addWhiteIpAddress = normang.addWhiteIpAddress(randomIp);
			if(normang.size()>59550) {
				System.out.println(System.currentTimeMillis()+"--normal---"+randomIp+"---"+normang.size()+"个");
			}
		}
	}
	
	public static void main(String[] args) {
		//normal();
		ipi();
	}

}
