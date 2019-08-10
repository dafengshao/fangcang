package com.fcang.spider.hotel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.provider.biz.HotelMappingBiz;

public class HotelMappingBizTest extends BaseTest{
	@Autowired
	HotelMappingBiz hotelMappingBiz;
	@Test
	public void testRun() {
		hotelMappingBiz.run();
	}

}
