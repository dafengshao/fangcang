package com.fcang.spider.hotel.domain.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.CtripHotelInfoDOMapper;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CtripHotelInfoService;


/**
 * 
 * CtripHotelInfoServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-18 09:47:59
 **/

@Service("ctripHotelInfoService")
public class CtripHotelInfoServiceImpl extends BaseServiceImpl<CtripHotelInfoDO, Long> implements CtripHotelInfoService{
	@Resource
	private CtripHotelInfoDOMapper ctripHotelInfoDOMapper;

	public BaseDao<CtripHotelInfoDO, Long> getDao(){
		return ctripHotelInfoDOMapper;
	}
	
	
	@Override
	public int insertSelective(CtripHotelInfoDO t) {
		t.setCreateTime(new Date());
		return super.insertSelective(t);
	}
	
	@Override
	public int updateByPrimaryKeySelective(CtripHotelInfoDO condition) {
		condition.setUpdateTime(new Date());
		return super.updateByPrimaryKeySelective(condition);
	}
	
	
}