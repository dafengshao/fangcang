package com.fcang.spider.hotel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.provider.biz.MeituanHotelListSpiderBiz;

public class MeituanHotelListSpiderBizTest extends BaseTest{
	@Autowired
	MeituanHotelListSpiderBiz meituanHotelListSpiderBiz;

	@Test
	public void testAddCity() {
		meituanHotelListSpiderBiz.addCity();
	}
}
