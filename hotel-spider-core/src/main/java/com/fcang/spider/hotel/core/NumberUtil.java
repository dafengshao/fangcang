package com.fcang.spider.hotel.core;

import java.util.Random;

public class NumberUtil {

	public static int getRan4() {
		return (int)((Math.random()*9+1)*1000);
	}
	
	public static int random(int r) {
		return new Random().nextInt(r);
	}
	
	public static void main(String[] args) throws InterruptedException {
		while(true) {
			System.out.println(random(11));
			Thread.sleep(300);
		}
	}
}
