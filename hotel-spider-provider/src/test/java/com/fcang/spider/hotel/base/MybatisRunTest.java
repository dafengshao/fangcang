package com.fcang.spider.hotel.base;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MybatisRunTest extends BaseTest{
	@Autowired
	MyBatis2Java myBatis2Java;
	@Test
	public void run() {
		myBatis2Java.run();
	}
}
