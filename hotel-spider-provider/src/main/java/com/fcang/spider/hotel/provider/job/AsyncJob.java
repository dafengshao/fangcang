package com.fcang.spider.hotel.provider.job;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.fcang.spider.hotel.core.CommonUtil;
import com.fcang.spider.hotel.provider.biz.CtripHotelRoomBiz;
import com.fcang.spider.hotel.provider.biz.CtriptHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.FliggyHotelInfoBiz;
import com.fcang.spider.hotel.provider.biz.FliggyHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.MeituanHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.util.JedisLocker;
import com.fcang.spider.hotel.provider.util.JedisLocker.OwnerLock;
@Component
public class AsyncJob {
	private static final Logger logger = LoggerFactory.getLogger(AsyncJob.class);
	
	@Bean
	public TaskScheduler taskScheduler() {
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.setPoolSize(4);
	    return taskScheduler;
	}
  
	@Autowired
	Executor providerSimpleAsync;
	@Autowired
	FliggyHotelListSpiderBiz fliggyHotelListSpiderBiz;
	@Autowired
	FliggyHotelInfoBiz fliggyHotelInfoBiz;
	@Autowired
	CtriptHotelListSpiderBiz hotelListSpiderBiz;
	@Autowired
	CtripHotelRoomBiz ctripHotelRoomBiz;
	
	@Autowired
	JedisLocker jedisLocker;
	static final Lock lock = new ReentrantLock();
	static final Lock lockDetail = new ReentrantLock();
	/**
	 * 飞猪酒店列表
	 */
	//@Scheduled(cron="0/5 * * * * *") 
	public void runFeizhuCityHotelList() {
		try {
			boolean tryLock = lock.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("runFeizhuCityHotelList start");
			fliggyHotelListSpiderBiz.run();
			logger.info("runFeizhuCityHotelList over");
		}finally {
			lock.unlock();
		}
		
	}
	
	
	
	
	/**
	 * 携程城市酒店列表
	 * 0 0 0/1 * * ?
	 * 0 0 0/2 * * ?
	 * 0/5 * * * * *
	 */
		
	
	@Scheduled(cron="0/5 * * * * *") 
	public void runCtripCityFromDB() {
		try {
			boolean tryLock = lockDetail.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("hotelListSpiderBiz.runFromDB start");
			CommonUtil.put("start", "2019-06-07");
			CommonUtil.put("end", "2019-06-08");
			hotelListSpiderBiz.runCityFromDB();
			logger.info("hotelListSpiderBiz.runFromDB over");
		}finally {
			lockDetail.unlock();
		}
		
	}
	static final Lock runCtripHtmlDownloadLock = new ReentrantLock();
	/**
	 * 携程酒店详情
	 */
	@Scheduled(cron="0/5 * * * * *") 
	public void runCtripHtmlDownload() {
		try {
			boolean tryLock = runCtripHtmlDownloadLock.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("ctripHotelRoomBiz.runDownHtmlFile start");
			while(!Thread.interrupted()) {
				ctripHotelRoomBiz.runDownHtmlFile();
			}
			logger.info("ctripHotelRoomBiz.runDownHtmlFile over");
		} catch (Exception e) {
			logger.error("",e);
		}finally {
			runCtripHtmlDownloadLock.unlock();
		}
		
	} 
	
	@Autowired
	MeituanHotelListSpiderBiz  meituanHotelListSpiderBiz;
	final static Long i = System.currentTimeMillis();
	@Scheduled(cron="0/5 * * * * ? ")
	public void runMeituanCity() {
		OwnerLock lock2 = jedisLocker.lock("runMeituanCity"+i, 1000*3600*50);
		if(lock2==null) {
			return;
		}
		try {
			logger.info("meituanHotelListSpiderBiz.runMeituanCity start");
			CommonUtil.put("start", "20190607");
			CommonUtil.put("end", "20190608");
			meituanHotelListSpiderBiz.runCityFromDB();
		}finally {
			lock2.release();
		}
	}
	@Scheduled(cron="0/5 * * * * ? ")
	public void runMeituanDetailPage() {
		OwnerLock lock2 = jedisLocker.lock("runMeituanDetailPage"+i, 1000*3600);
		if(lock2==null) {
			return;
		}
		try {
			logger.info("meituanHotelListSpiderBiz.hanldMeituanDetailPage start");
			meituanHotelListSpiderBiz.hanldMeituanDetailPage();
		}finally {
			lock2.release();
		}
	}
	
	
}
