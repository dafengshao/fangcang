package com.fcang.spider.hotel.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fcang.spider.hotel.app.HotelSpiderProviderApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelSpiderProviderApplication.class)
public class BaseTest {
	Logger logger = LoggerFactory.getLogger(BaseTest.class);
	@Test
	public void mainTest(){
		logger.info("mainTest success");
	}
}

