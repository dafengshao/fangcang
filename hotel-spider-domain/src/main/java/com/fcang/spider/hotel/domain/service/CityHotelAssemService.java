package com.fcang.spider.hotel.domain.service;

import com.fcang.spider.hotel.domain.base.BaseService;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;



/**
 * 
 * CityHotelAssemService业务层接口类
 * @author hewenfeng
 * @date 2018-12-23 15:47:07
 **/

public interface CityHotelAssemService extends BaseService<CityHotelAssemDO , Long> {
	public CityHotelAssemDO getUniq(int type ,Long cityId,String cityCode,int currentPage,int pageSize,String batchCode);
	public int insertOrUpdate(CityHotelAssemDO t);


}