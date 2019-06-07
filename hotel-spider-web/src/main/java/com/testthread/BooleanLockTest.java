package com.testthread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class BooleanLockTest {

	
	public class Model {
		int value = 0;
		public int getValue(){
			return value;
		}
		public void add() {
			value++;
		}
	}
		
	public static void main(String[] args) throws InterruptedException {
		BooleanLockTest t = new BooleanLockTest();
		Model m = t.new Model();
		BooleanLock blk = new BooleanLock();
		IntStream.range(0, 40).mapToObj((i)->new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(1);
				blk.lock(1L);
				m.add();
				blk.unlock();
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			} catch (TimeoutException e) {
				System.out.println("timeout");
			}
		})).forEach(Thread::start);
		TimeUnit.SECONDS.sleep(5);
		System.out.println(m.getValue());
	}
}

