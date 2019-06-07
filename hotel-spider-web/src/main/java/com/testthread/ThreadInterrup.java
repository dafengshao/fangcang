package com.testthread;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;



public class ThreadInterrup {

	public static void interrup() throws InterruptedException {
		Thread newThread = new Thread(()->{
		synchronized (ThreadInterrup.class) {
			try {
				System.out.println("main thread is interrupted? before sleep："+ Thread.currentThread().isInterrupted());
				TimeUnit.HOURS.sleep(1);
			}catch (InterruptedException e) {
				System.out.println("main thread is interrupted? "+ Thread.currentThread().isInterrupted());
				System.out.println("i will be interrupted still.");
			}
		}
		});
		newThread.start();
		newThread.interrupt();
		TimeUnit.SECONDS.sleep(5);
		System.out.println(newThread.isInterrupted());
		System.out.println(newThread.getState());
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		//interrup();
		BigDecimal a = new BigDecimal("999");
		change(a);
		System.out.println(a.toString());
		JSONObject json = new JSONObject();
		json.put("org", 1);
		change(json);
		System.out.println(json.toString());
	}
	
	static void change(String str) {
		str+="join";
	}
	
	static void change(BigDecimal str) {
		str=new BigDecimal("111");
	}
	static void change(JSONObject json) {
		json=new JSONObject();
		json.put("or1231g", 22);
	}
}
