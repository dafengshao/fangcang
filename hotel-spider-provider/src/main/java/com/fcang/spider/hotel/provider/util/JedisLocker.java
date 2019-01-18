package com.fcang.spider.hotel.provider.util;

import java.util.UUID;

import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

public class JedisLocker {
	private static final String LOCKKEY_PREFIX = "lock:";
	private JedisCommands jedis;
	private static final String DELANDEDQUAL_SCRIPT = "local key = KEYS[1] local value = KEYS[2] local rel = 0 local lockValue = redis.call('get', key) if lockValue == value then rel = redis.call('del', key) end return rel";

	//1分钟 发生死锁后多长时间自动恢复
	private static final int DEFAUL_LOCKTIME = 60 ;
	
	private static final int RETRY_TIME = 100;

	
	public JedisLocker(JedisCommands redisProxy) {
		this.jedis = redisProxy;
	}
	
	/** 不存在就设值，并且设置过期时间，原子操作 */
	public boolean setNxEx(String key, String value, int seconds) {
		SetParams setpa = new SetParams();
		setpa.ex(seconds);
		setpa.nx();
		String set = jedis.set(key, value,setpa);
		return "OK".equals(set);
	}

	/**
	 * 给指定参数加锁，需要调用release释放 *
	 * 
	 * @param key
	 *            锁定的key
	 * @param timeOut
	 *            如果已经被加锁，需等待的时间，等待超时将返回失败,单位ms
	 * @param retryTime 重试间隔,单位ms
	 * @return null失败，OwnerLock成功，返回结果需要在释放时使用
	 * @throws InterruptedException
	 */
	public OwnerLock tryLock(String key, long timeOut,long retryTime) {
		long re = RETRY_TIME;
		if(retryTime>0){
			re = retryTime;
		}
		long waitEndTime = System.currentTimeMillis() + (timeOut);
		OwnerLock lock = null;
		while((lock = this.tryLock(key))==null){
			long currTime = System.currentTimeMillis();
			if (waitEndTime < currTime) {// 加锁失败 等待超时
				return null;
			}
			try {
				Thread.sleep(re);
			} catch (InterruptedException e) {
				
			}
		}
		return lock;
	}

	/**
	 * @param key
	 *            指定参数加锁，需要调用release释放
	 * 
	 * @return null失败，OwnerLock成功，返回结果需要在释放时使用
	 */
	public OwnerLock tryLock(String key) {
		return lock(key, DEFAUL_LOCKTIME);
	}

	/** 给指定参数加锁,lockSeconds为锁定时间单位ms，需要调用release释放 */
	public OwnerLock lock(String key, int lockSeconds) {
		String lockKey = (LOCKKEY_PREFIX.concat(key));
		UUID uuid = UUID.randomUUID();
		OwnerLock lock = null;
		if (this.setNxEx(lockKey, uuid.toString(), lockSeconds)) {
			lock = new OwnerLock(lockKey, uuid.toString());
		}
		return lock;
	}

	/**
	 * 释放key,这个方法在加锁成功锁使用完毕以后调用，需要放到finally里
	 * 
	 * @param key
	 * @return
	 */
	public boolean release(OwnerLock ownerLock) {
		if (ownerLock == null) {
			return false;
		}
		int i = 0;
		while(i++<5){
			try{
				Long delOk = jedis.del(ownerLock.getKey());
				return delOk.longValue() == 1;
			}catch(Exception e){
				if(i>=4){
					throw new RuntimeException(e);
				}
				try {
					Thread.sleep(101);
				} catch (InterruptedException e1) {
					
				}
				
			}
			
		}
		return false;
		
	}

	public class OwnerLock {
		private String key;
		private String value;

		public OwnerLock(String key, String value) {
			this.key = key;
			this.value = value;

		}

		public boolean release() {
			return JedisLocker.this.release(this);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return super.toString()+"{key:"+key+",value:"+value+"}";
		}

	}

}
