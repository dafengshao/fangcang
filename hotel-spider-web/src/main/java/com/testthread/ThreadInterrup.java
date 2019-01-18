package com.testthread;

import java.util.concurrent.TimeUnit;

public class ThreadInterrup {

	public static void main(String[] args) {
		System.out.println("main thread is interrupted? "+ Thread.interrupted());
		Thread.currentThread().interrupt();
		
		System.out.println("main thread is interrupted? "+ Thread.currentThread().isInterrupted());
		
		try {
			TimeUnit.SECONDS.sleep(1);
		}catch (InterruptedException e) {
			System.out.println("i will be interrupted still.");
		}
	}
}
