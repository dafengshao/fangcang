package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.FliggyCityDOMapper;
import com.fcang.spider.hotel.domain.pojo.FliggyCityDO;
import com.fcang.spider.hotel.domain.service.FliggyCityService;


/**
 * 
 * FliggyCityServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-19 23:28:48
 **/

@Service("fliggyCityService")
public class FliggyCityServiceImpl extends BaseServiceImpl<FliggyCityDO, Long> implements FliggyCityService{
	@Resource
	private FliggyCityDOMapper fliggyCityDOMapper;

	public BaseDao<FliggyCityDO, Long> getDao(){
		return fliggyCityDOMapper;
	}

}