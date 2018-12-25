package com.fcang.spider.hotel.domain.service;

import com.fcang.spider.hotel.domain.base.BaseService;
import com.fcang.spider.hotel.domain.pojo.FliggyHotelDO;



/**
 * 
 * FliggyHotelService业务层接口类
 * @author hewenfeng
 * @date 2018-12-20 23:29:41
 **/

public interface FliggyHotelService extends BaseService<FliggyHotelDO , Long> {
	
	public int insertOrUpdatePrice(FliggyHotelDO hotelDO);

	

}