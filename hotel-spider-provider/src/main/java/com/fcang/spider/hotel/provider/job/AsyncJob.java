package com.fcang.spider.hotel.provider.job;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fcang.spider.hotel.provider.biz.FliggyHotelInfoBiz;
import com.fcang.spider.hotel.provider.biz.FliggyHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.util.JedisLocker;
import com.fcang.spider.hotel.provider.util.JedisLocker.OwnerLock;
@Component
public class AsyncJob {
	private static final Logger logger = LoggerFactory.getLogger(AsyncJob.class);
	
  
	@Autowired
	Executor providerSimpleAsync;
	@Autowired
	FliggyHotelListSpiderBiz fliggyHotelListSpiderBiz;
	@Autowired
	FliggyHotelInfoBiz fliggyHotelInfoBiz;
	@Autowired
	JedisLocker jedisLocker;
	
	static final Lock lock = new ReentrantLock();
	static final Lock lockDetail = new ReentrantLock();
	
	@Scheduled(cron="0 0/5 * * * *")    //"0/5 * *  * * ? "每5秒执行一次  "0 15 2 ? * *"    每天早上2：15触发 
	public void runCityHotelList() {
		try {
			boolean tryLock = lock.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("runCityHotelList start");
			fliggyHotelListSpiderBiz.run();
			logger.info("runCityHotelList over");
		}finally {
			lock.unlock();
		}
		
	}
	
	@Scheduled(cron="0 0/2 * * * *") 
	public void runDetail() {
		try {
			boolean tryLock = lockDetail.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("fliggyHotelInfoBiz.run start");
			fliggyHotelInfoBiz.run();
			logger.info("fliggyHotelInfoBiz.run over");
		}finally {
			lockDetail.unlock();
		}
		
	}
	
	
}
