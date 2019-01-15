package com.fcang.spider.hotel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.provider.biz.FliggyHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.CtriptHotelListSpiderBiz;

public class HotelListSpiderBizTest extends BaseTest{
	@Autowired
	CtriptHotelListSpiderBiz hotelListSpiderBiz;
	@Autowired
	FliggyHotelListSpiderBiz fliggyHotelListSpiderBiz;

	@Test
	public void runFromDBTest() {
		hotelListSpiderBiz.runCityFromDB();
	}
	@Test
	public void runFliggy() {
		fliggyHotelListSpiderBiz.run();
	}
}
