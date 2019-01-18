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

import com.fcang.spider.hotel.provider.biz.CtripHotelRoomBiz;
import com.fcang.spider.hotel.provider.biz.CtriptHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.FliggyHotelInfoBiz;
import com.fcang.spider.hotel.provider.biz.FliggyHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.MeituanHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.util.JedisLocker;
@Component
public class AsyncJob {
	private static final Logger logger = LoggerFactory.getLogger(AsyncJob.class);
	
	@Bean
	public TaskScheduler taskScheduler() {
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.setPoolSize(3);
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
	//@Scheduled(cron="0 26 1 ? * *")   //"0/5 * *  * * ? "每5秒执行一次  "0 15 2 ? * *"    每天早上2：15触发 
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
	
	
	
	/**
	 * 携程城市酒店列表
	 */
	//@Scheduled(cron="0/5 * * * * *") 
	public void runFromDB() {
		try {
			boolean tryLock = lockDetail.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("hotelListSpiderBiz.runFromDB start");
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
	//@Scheduled(cron="0/5 * * * * ? ")
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
	static final Lock runCtripParseHtmlFileLock = new ReentrantLock();
	//@Scheduled(cron="0/5 * * * * ? ")
	public void runCtripParseHtmlFile() {
		try {
			boolean tryLock = runCtripParseHtmlFileLock.tryLock();
			if(!tryLock) {
				return;
			}
			logger.info("ctripHotelRoomBiz.runCtripParseHtmlFile start");
			while(!Thread.interrupted()) {
				ctripHotelRoomBiz.runByHarddisk();
			}
			logger.info("ctripHotelRoomBiz.runCtripParseHtmlFile over");
		} catch (Exception e) {
			logger.error("",e);
		}finally {
			runCtripParseHtmlFileLock.unlock();
		}
		
	}
	@Autowired
	MeituanHotelListSpiderBiz  meituanHotelListSpiderBiz;
	//@Scheduled(cron="0/5 * * * * ? ")
	public void runMeituanCity() {
		meituanHotelListSpiderBiz.runCityFromDB();
	}
	
	@Scheduled(cron="0/5 * * * * ? ")
	public void runMeituanHotelDetail() {
		meituanHotelListSpiderBiz.runMeituanHotelDetail();
	}
	
}
