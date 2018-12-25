package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.CtripHotelPricePlanDOMapper;
import com.fcang.spider.hotel.domain.pojo.CtripHotelPricePlanDO;
import com.fcang.spider.hotel.domain.service.CtripHotelPricePlanService;


/**
 * 
 * CtripHotelPricePlanServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-18 09:47:59
 **/

@Service("ctripHotelPricePlanService")
public class CtripHotelPricePlanServiceImpl extends BaseServiceImpl<CtripHotelPricePlanDO, Long> implements CtripHotelPricePlanService{
	@Resource
	private CtripHotelPricePlanDOMapper ctripHotelPricePlanDOMapper;

	public BaseDao<CtripHotelPricePlanDO, Long> getDao(){
		return ctripHotelPricePlanDOMapper;
	}

}