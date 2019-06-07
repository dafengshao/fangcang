package com.testthread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

public class BooleanLock {

	
	private Thread currentThread ;
	private boolean locked;
	private Set<Thread> blockedSet = new HashSet<>();
	
	public void lock() throws InterruptedException {
		synchronized (this) {
			while(locked) {
				blockedSet.add(Thread.currentThread());
				try {
					this.wait();
				} catch (InterruptedException e) {
					blockedSet.remove(Thread.currentThread());
					throw e;
				}
			}
			locked = true;
			if(Thread.currentThread()!=currentThread) {
				currentThread=Thread.currentThread();
			}
			if(currentThread!=null) {
				blockedSet.remove(currentThread);
			}
		}
	}
	
	public void lock(long timeout) throws InterruptedException,TimeoutException {
		if(timeout<=0) {
			throw new IllegalArgumentException("timeout<=0");
		}
		synchronized (this) {
			long endTime = System.currentTimeMillis()+timeout;
			while(locked) {
				long currentTime = System.currentTimeMillis();
				if(currentTime>endTime) {
					blockedSet.remove(Thread.currentThread());
					throw new TimeoutException("timeout");
				}
				blockedSet.add(Thread.currentThread());
				this.wait(timeout);
				
			}
			locked = true;
			currentThread=Thread.currentThread();
			blockedSet.remove(currentThread);
		}
	}
	
	public void unlock() {
		synchronized (this) {
			if(locked&&Thread.currentThread()==currentThread) {
				locked = false;
				this.notifyAll();
			}
		}
	}
	
	
	
}
