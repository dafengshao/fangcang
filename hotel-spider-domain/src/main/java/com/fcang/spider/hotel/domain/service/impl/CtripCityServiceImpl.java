package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.CtripCityDOMapper;
import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.domain.service.CtripCityService;


/**
 * 
 * CtripCityServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-18 17:18:29
 **/

@Service("ctripCityService")
public class CtripCityServiceImpl extends BaseServiceImpl<CtripCityDO, Long> implements CtripCityService{
	@Resource
	private CtripCityDOMapper ctripCityDOMapper;

	public BaseDao<CtripCityDO, Long> getDao(){
		return ctripCityDOMapper;
	}
	@Override
	public int insertSelective(CtripCityDO t) {
		try {
			return super.insertSelective(t);
		}catch (DuplicateKeyException e) {
			
		}
		return 0;
	}
}