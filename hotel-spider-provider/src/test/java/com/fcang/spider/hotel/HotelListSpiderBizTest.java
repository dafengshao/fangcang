package com.fcang.spider.hotel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.provider.biz.FliggyHotelListSpiderBiz;
import com.fcang.spider.hotel.provider.biz.HotelListSpiderBiz;

public class HotelListSpiderBizTest extends BaseTest{
	@Autowired
	HotelListSpiderBiz hotelListSpiderBiz;
	@Autowired
	FliggyHotelListSpiderBiz fliggyHotelListSpiderBiz;
	@Test
	public void runTest() {
		hotelListSpiderBiz.run();
	}
	@Test
	public void runFromDBTest() {
		hotelListSpiderBiz.runFromDB();
	}
	@Test
	public void runFliggy() {
		fliggyHotelListSpiderBiz.run();
	}
}
