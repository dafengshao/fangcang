package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.CtripHotelRoomDOMapper;
import com.fcang.spider.hotel.domain.pojo.CtripHotelRoomDO;
import com.fcang.spider.hotel.domain.service.CtripHotelRoomService;


/**
 * 
 * CtripHotelRoomServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-18 09:47:59
 **/

@Service("ctripHotelRoomService")
public class CtripHotelRoomServiceImpl extends BaseServiceImpl<CtripHotelRoomDO, Long> implements CtripHotelRoomService{
	@Resource
	private CtripHotelRoomDOMapper ctripHotelRoomDOMapper;

	public BaseDao<CtripHotelRoomDO, Long> getDao(){
		return ctripHotelRoomDOMapper;
	}

}