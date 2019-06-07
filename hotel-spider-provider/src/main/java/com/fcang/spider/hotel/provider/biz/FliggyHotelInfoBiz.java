package com.fcang.spider.hotel.provider.biz;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;
import com.fcang.spider.hotel.domain.pojo.FliggyHotelDO;
import com.fcang.spider.hotel.domain.service.CityHotelAssemService;
import com.fcang.spider.hotel.domain.service.FliggyHotelService;
import com.github.pagehelper.PageHelper;
@Component
public class FliggyHotelInfoBiz {
	
	Logger logger = LoggerFactory.getLogger(FliggyHotelInfoBiz.class);
	@Autowired
	CityHotelAssemService cityHotelAssemService;
	@Autowired
	FliggyHotelService fliggyHotelService;
	
	@Autowired
	Executor providerSimpleAsync;

	public void run() {
		providerSimpleAsync.execute(()->{runCityHotelAssem();});
	}
	public void runCityHotelAssem() {
		int pageSize = 8;
		int pageNum = 1;
		CityHotelAssemDO condtion = new CityHotelAssemDO();
		condtion.setReponseStatus(1);
		condtion.setRequestType(1);
		while(!Thread.interrupted()){
			PageHelper.startPage(pageNum++ , pageSize,false).setOrderBy("update_time asc");
			List<CityHotelAssemDO> selectEntryList = cityHotelAssemService.selectEntryList(condtion );
			if(CollectionUtils.isEmpty(selectEntryList)) {
				try {
					Thread.sleep(20000L);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					
				}
				continue;
			}
			CountDownLatch latch = new CountDownLatch(selectEntryList.size());
			//开启多线程
			selectEntryList.forEach(cityAssem->
				providerSimpleAsync.execute(parseHotelAssem(cityAssem,latch))
			);
			try {
				latch.await();
			} catch (InterruptedException e) {
				
			}
		}
		
		
	}


	private Runnable parseHotelAssem(CityHotelAssemDO cityAssem,CountDownLatch latch) {
		return () -> {
			CityHotelAssemDO condtion = new CityHotelAssemDO();
			condtion.setId(cityAssem.getId());
			try {
				if(cityAssem.getReponseStatus()!=null&&cityAssem.getReponseStatus()<=0) {
					return;
				}
				String content = cityAssem.getReponseContent();
				if (content == null) {
					return;
				}
				JSONObject parseObject = JSONObject.parseObject(content, JSONObject.class);
				JSONArray jsonArray = parseObject.getJSONArray("hotelList");
				if (!CollectionUtils.isEmpty(jsonArray)) {
					jsonArray.forEach(json -> {
						JSONObject jsonObj = (JSONObject) json;
						FliggyHotelDO fhotel = FliggyHotelDO.toFliggyHotelDO(jsonObj);
						fhotel.setHotelAssemId(cityAssem.getId());
						fhotel.setStatusCode("success");
						fhotel.setUpdateTime(new Date());
						fhotel.setCityId(cityAssem.getCityId());
						fhotel.setCityCode(cityAssem.getCityCode());
						fliggyHotelService.insertOrUpdatePrice(fhotel);
					});
				}
				condtion.setReponseStatus(200);
				cityHotelAssemService.updateByPrimaryKeySelective(condtion);
			}catch (Exception e) {
				logger.error("",e);
				condtion.setReponseStatus(1);
				condtion.setUpdateTime(new Date());
				cityHotelAssemService.updateByPrimaryKeySelective(condtion);
			}finally {
				latch.countDown();
			}
			
			
		};
	}


	
	
	
}
