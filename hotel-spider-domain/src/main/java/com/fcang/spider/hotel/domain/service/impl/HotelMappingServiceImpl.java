package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.HotelMappingDOMapper;
import com.fcang.spider.hotel.domain.pojo.HotelMappingDO;
import com.fcang.spider.hotel.domain.service.HotelMappingService;


/**
 * 
 * HotelMappingServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2019-06-07 16:17:35
 **/

@Service("hotelMappingService")
public class HotelMappingServiceImpl extends BaseServiceImpl<HotelMappingDO, Long> implements HotelMappingService{
	@Resource
	private HotelMappingDOMapper hotelMappingDOMapper;

	public BaseDao<HotelMappingDO, Long> getDao(){
		return hotelMappingDOMapper;
	}

}