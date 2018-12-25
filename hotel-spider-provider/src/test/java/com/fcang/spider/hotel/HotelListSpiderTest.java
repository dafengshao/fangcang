package com.fcang.spider.hotel;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.base.BaseTest;
import com.fcang.spider.hotel.domain.mapper.CtripCityDOMapper;
import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.provider.util.CityUtil;
import com.fcang.spider.hotel.provider.util.FliggyHotelHtmlUtil;

public class HotelListSpiderTest extends BaseTest{
	final static String CITY_JSON_FILE = "C:\\Users\\47246\\github\\hotel-spider\\phantomjs-2.1.1-windows\\ctrip\\citycode.json";
	final static String FEI_ZHU_CITY_JSON_FILE = "C:\\Users\\47246\\github\\hotel-spider\\phantomjs-2.1.1-windows\\json\\feizhu.json";
	Logger logger = LoggerFactory.getLogger(HotelListSpiderTest.class);
	@Autowired
	CtripCityDOMapper ctripCityDOMapper;
	@Test
	public void cityCtripTest() throws IOException{
		JSONObject cityJSON = CityUtil.getCtripCityJSON(CITY_JSON_FILE);
		Set<Entry<String, Object>> entrySet = cityJSON.entrySet();
		entrySet.forEach(ent->{
			JSONArray jsonArray = (JSONArray)ent.getValue();
			final String key = ent.getKey();
			jsonArray.forEach(st->{
				JSONObject json = (JSONObject)st;
				String string = json.getString("display");
				String[] data = json.getString("data").split(string);
				
				CtripCityDO t = new CtripCityDO();
				t.setId(Long.valueOf(data[1].substring(1)));
				t.setCity(string);
				t.setMark(key);
				t.setCode(data[0].substring(0, data[0].length()-1));
				try {
					ctripCityDOMapper.insertSelective(t);
				} catch (DuplicateKeyException e) {
					logger.info("city:{}，已经存在",string);
				}
			});
			
		});
	}
	
	@Autowired
	FliggyHotelHtmlUtil fliggyHotelHtmlUtil;
	/**
	 * 飞猪城市
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Test
	public void cityFlyTest() throws IOException, InterruptedException{
		while(true) {
			try {
				fliggyHotelHtmlUtil.runCity();
			}catch (Exception e) {
				
			}
			Thread.sleep(20000);
		}
	}
	@Test
	public void areaTest() {
		fliggyHotelHtmlUtil.runArea();
	}
}
