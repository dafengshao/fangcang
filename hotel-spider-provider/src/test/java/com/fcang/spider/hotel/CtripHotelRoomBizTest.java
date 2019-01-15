package com.fcang.spider.hotel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.provider.biz.CtripHotelRoomBiz;

public class CtripHotelRoomBizTest extends BaseTest{
	@Autowired
	CtripHotelRoomBiz ctripHotelRoomBiz;
	@Test
	public void runDownHtmlFile() throws InterruptedException {
		ctripHotelRoomBiz.runDownHtmlFile();
	}
}
