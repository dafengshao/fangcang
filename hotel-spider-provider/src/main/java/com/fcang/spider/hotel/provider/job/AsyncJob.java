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

import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.CommonUtil;
import com.fcang.spider.hotel.core.JsoupUtil;
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
			CommonUtil.put("start", "2019-08-10");
			CommonUtil.put("end", "2019-08-11");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("cookie", "magicid=x3aXW7ZduAdgSSGNeeG9+H2yEk2JrrRPqfz3L3+IVK3BaLSQv4yIN4/TI76Mhhde; gad_city=be2e953e1ae09d16d9cc90a550611388; _RF1=171.223.96.253; _RSG=ZOahh6m9fH2fkugJnvkMjB; _RDG=284b2992d45f6b2c062b2a5b2b4a4c458e; _RGUID=24692701-abae-4623-bb56-bb9c53ab8ba9; MKT_Pagesource=PC; _ga=GA1.2.1607586121.1561730101; _gid=GA1.2.1736167368.1561730101; HotelCityID=2split%E4%B8%8A%E6%B5%B7splitShanghaisplit2019-06-28split2019-06-29split0; ASP.NET_SessionId=2bggvduxlmpzxzpybbcgsio5; _abtest_userid=4dd5c1df-c0be-406c-bb13-5ada31421a10; OID_ForOnlineHotel=15617300963743jbawu1561730116372102002; appFloatCnt=2; login_uid=74A5DE831C211C1FBD58200FD3D48B28; login_type=0; cticket=4C392FB46C4C31E1C4E2631F6C4742FD6EAC42C61F3793AB5552A69772A6CBAA; AHeadUserInfo=VipGrade=0&VipGradeName=%C6%D5%CD%A8%BB%E1%D4%B1&UserName=&NoReadMessageCount=0; ticket_ctrip=bJ9RlCHVwlu1ZjyusRi+ypZ7X2r4+yojhef2ULIE4QWpIzTGj60mKbg8hF1XzTcOvMFo4s4Qu6lKEq4PQ7szpex4ZNnpJ43LH6kfMl0F8qxf5j4Kve40SVPMujajyD0pDk5ZHYC/iQqFo7yG6JgWnVm6nH7MDzuGCEwLcYdW5PnKADc5RxHRmPrhzEzLwNVzsc8uJT0xmBgFU0mB/mg3lJ79lKtrYsAlZuQxHaeEwwogKv79ekApgytDuSg7T9JqLaHQLWOT9IoXQOzrt8D5L+Ds/h8TANWEw3F3aomCEq4=; DUID=u=AEA93E5730069A0954DA214F6AE17246&v=0; IsNonUser=F; UUID=78DBBB2F3B6B4121B3FA66B98CB4C55B; IsPersonalizedLogin=F; _bfa=1.1561730096374.3jbawu.1.1561730096374.1561730096374.1.5; _bfs=1.5; __zpspc=9.1.1561730100.1561730213.4%234%7C%7C%7C%7C%7C%23; _bfi=p1%3D102002%26p2%3D102002%26v1%3D5%26v2%3D4; _jzqco=%7C%7C%7C%7C1561730101318%7C1.553042818.1561730100907.1561730166560.1561730213590.1561730166560.1561730213590.undefined.0.0.4.4");
			JsoupUtil.PROXYINFOTHREADLOCAL.set(jsonObject);
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
			CommonUtil.put("start", "20190810");
			CommonUtil.put("end", "20190811");
			meituanHotelListSpiderBiz.runCityFromDB();
		}finally {
			lock2.release();
		}
	}
	@Scheduled(cron="* 0/10 * * * ? ")
	public void runMeituanDetailPage() {
		OwnerLock lock2 = jedisLocker.lock("runMeituanDetailPage"+i, 1000*3600);
		if(lock2==null) {
			return;
		}
		try {
			logger.info("meituanHotelListSpiderBiz.hanldMeituanDetailPage start");
			meituanHotelListSpiderBiz.hanldMeituanDetailPage("init");
			//meituanHotelListSpiderBiz.hanldMeituanDetailPage("error");
			//meituanHotelListSpiderBiz.hanldMeituanDetailPage("refresh_error");
		}finally {
			lock2.release();
		}
	}
	
	
}
