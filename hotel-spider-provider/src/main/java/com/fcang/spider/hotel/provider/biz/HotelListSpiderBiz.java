package com.fcang.spider.hotel.provider.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CtripCityService;
import com.fcang.spider.hotel.domain.service.CtripHotelInfoService;
import com.fcang.spider.hotel.provider.util.CtripHotelHtmlUtil;

@Component
public class HotelListSpiderBiz {
	static final  Logger LOGGER = LoggerFactory.getLogger(HotelListSpiderBiz.class);
	//武汉 荆州 洪湖 十堰 恩施  孝感 神农架  宜昌 潜江
	static final String[] CITY_PAGE_URL = {"武汉","荆州","洪湖","十堰","恩施","孝感","神农架","宜昌","潜江"};
	static final String CITY_PAGE = "http://hotels.ctrip.com/hotel/%s%d/p%d";
	@Autowired
	CtripCityService ctripCityService ;
	@Autowired
	CtripHotelInfoService ctripHotelInfoService;
	public void run(String pageUrl ,int pageNo) {
		String pageListUrl = String.format(pageUrl, pageNo);
		List<CtripHotelInfoDO> buildHotellist = CtripHotelHtmlUtil.buildHotellist(pageListUrl);
		if(CollectionUtils.isEmpty(buildHotellist)) {
			LOGGER.info("pageListUrl:{},酒店列表为空",pageListUrl);
		}
	}
	public void run() {
		for(String city:CITY_PAGE_URL) {
			runCity(city);
		}
	}
	
	public void runFromDB() {
		CtripCityDO condtion = new CtripCityDO();
		List<CtripCityDO> selectEntryList = ctripCityService.selectEntryList(condtion);
		int i = 0;
		for(CtripCityDO city:selectEntryList) {
			i++;
			if(i<=2) {//跳过北京
				continue;
			}
			
			runCity(city);
		}
	}
	public void runCity(String city) {
		CtripCityDO condtion = new CtripCityDO();
		condtion.setCity(city);
		List<CtripCityDO> selectEntryList = ctripCityService.selectEntryList(condtion );
		if(CollectionUtils.isEmpty(selectEntryList)||selectEntryList.size()>1) {
			LOGGER.info("city:{},不存在，或者存在多个",city);
			return;
		}
		CtripCityDO cityDO= selectEntryList.get(0);
		 runCity(cityDO);
	}
	public void runCity(CtripCityDO cityDO) {
		int i = 1;
		while(true) {
			final int currentPage = i;
			String pageListUrl = String.format(CITY_PAGE,cityDO.getCode(),cityDO.getId(), currentPage);
			LOGGER.info("pageListUrl:{}",pageListUrl);
			List<CtripHotelInfoDO> buildHotellist = CtripHotelHtmlUtil.buildHotellist(pageListUrl);
			if(CollectionUtils.isEmpty(buildHotellist)) {
				break;
			}
			buildHotellist.forEach(hotel->{
				hotel.setCity(cityDO.getCity());
				hotel.setCityId(cityDO.getId());
				hotel.setCityCode(cityDO.getCode());
				hotel.setOriginalPage(currentPage);
				while(true) {
					try {
						ctripHotelInfoService.insertSelective(hotel);
						Thread.sleep(500);
					} catch (DuplicateKeyException e) {
						LOGGER.info("hotel:{},已经存在",hotel.getNameCh());
					} catch (Exception e) {
						continue;
					}
					break;
				}
			});
			i++;
		}
	}
	
}
