package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.MeituanHotelInfoDOMapper;
import com.fcang.spider.hotel.domain.pojo.MeituanHotelInfoDO;
import com.fcang.spider.hotel.domain.service.MeituanHotelInfoService;


/**
 * 
 * MeituanHotelInfoServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2019-01-28 14:12:52
 **/

@Service("meituanHotelInfoService")
public class MeituanHotelInfoServiceImpl extends BaseServiceImpl<MeituanHotelInfoDO, Long> implements MeituanHotelInfoService{
	@Resource
	private MeituanHotelInfoDOMapper meituanHotelInfoDOMapper;

	public BaseDao<MeituanHotelInfoDO, Long> getDao(){
		return meituanHotelInfoDOMapper;
	}

}