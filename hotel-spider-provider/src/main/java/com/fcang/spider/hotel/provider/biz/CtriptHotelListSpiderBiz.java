package com.fcang.spider.hotel.provider.biz;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.AjaxCtripCityHotelListUtil;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.ConstantVar;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CtripCityService;
import com.fcang.spider.hotel.domain.service.CtripHotelInfoService;
import com.fcang.spider.hotel.provider.util.JedisLocker;
import com.fcang.spider.hotel.provider.util.JedisLocker.OwnerLock;
import com.github.pagehelper.PageHelper;

import redis.clients.jedis.commands.JedisCommands;

@Component
public class CtriptHotelListSpiderBiz {
	
	static final  Logger LOGGER = LoggerFactory.getLogger(CtriptHotelListSpiderBiz.class);
	//武汉 荆州 洪湖 十堰 恩施  孝感 神农架  宜昌 潜江
	static final String[] CITY_PAGE_URL = {"武汉","荆州","洪湖","十堰","恩施","孝感","神农架","宜昌","潜江"};
	static final String CITY_PAGE = "http://hotels.ctrip.com/hotel/%s%d/p%d";
	@Autowired
	CtripCityService ctripCityService ;
	
	@Autowired
	CtripHotelInfoService ctripHotelInfoService;
	
	@Autowired
	JedisCommands jedis;
	@Autowired
	JedisLocker jedisLocker;

	
	public void runCityFromDB() {
		CtripCityDO condtion = new CtripCityDO();
		//condtion.setCode("Nanning");
		PageHelper.startPage(1, 1000, false).setOrderBy("type asc,id desc");
		List<CtripCityDO> selectEntryList = ctripCityService.selectEntryList(condtion);
		PageHelper.clearPage();
		int i = 0;
		for (CtripCityDO city : selectEntryList) {
			OwnerLock lock = null;
			try {
				lock = jedisLocker.lock(city.getCode(), 3000);
				if (lock == null) {
					continue;
				}
				LOGGER.info("run,city:{}",city);
				runCity(city);
			} catch (Exception e) {
				LOGGER.error("exception city:{}", city, e);
			} finally {
				if (lock != null) {
					lock.release();
				}
			}
		}
	}
	
	public void runCity(CtripCityDO cityDO) {
		int i = 1;
		int totel = 5000;
		CtripCityDO city = new CtripCityDO();
		city.setId(cityDO.getId());
		while(i<=totel) {
			int currentPage = i;
			i++;
			String redisCityKey = "city:"+cityDO.getId()+"";
			String hget = jedis.hget(redisCityKey, currentPage+"");
			if("25".equals(hget)) {
				continue;
			}
			String totelStr = jedis.hget(redisCityKey, "totel");
			if(totelStr!=null) {
				totel = Integer.valueOf(totelStr);
			}
			//BaseFullResponse<List<CtripHotelInfoDO>> res = CtripHotelHtmlUtil.buildLocalHotellist(pageListUrl);
			BaseFullResponse<Collection<CtripHotelInfoDO>> res = AjaxCtripCityHotelListUtil.ajaxCityHotelList(currentPage, cityDO.getId(),cityDO.getCode());
			if(!res.isSuccess()) {
				jedis.hset(redisCityKey, currentPage+"", "0");
				continue;
			}
			Collection<CtripHotelInfoDO> jsonArray = res.getData();
			if(totelStr==null) {
				Integer pages = Integer.valueOf(res.getMessage());
				jedis.hset(redisCityKey, "totel", ""+(pages%25==0?pages/25:pages/25+1));
			}
			if(!CollectionUtils.isEmpty(jsonArray)) {
				if(jsonArray.size()!=25) {
					LOGGER.error("少于25个酒店，city:{}，currentPage:{}",cityDO.getId(),currentPage);
				}
				jsonArray.forEach(hotel->{
					hotel.setCity(cityDO.getCity());
					hotel.setCityId(cityDO.getId());
					hotel.setCityCode(cityDO.getCode());
					hotel.setOriginalPage(currentPage);
					hotel.setMark(ConstantVar.INIT_SUCCESS);
					hotel.setUpdateTime(DateUtil.plusDaysToDate(new Date(), -40));
					ctripHotelInfoService.insertOrUpdate(hotel);
				});
				city.setType(currentPage);
				ctripCityService.updateByPrimaryKeySelective(city);
				jedis.hset(redisCityKey,currentPage+"", jsonArray.size()+"");
			}else {
				
				break;
			}
			
			
		}
	}
	
}
