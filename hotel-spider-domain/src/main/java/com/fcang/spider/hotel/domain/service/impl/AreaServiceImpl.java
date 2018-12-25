package com.fcang.spider.hotel.domain.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.AreaDOMapper;
import com.fcang.spider.hotel.domain.pojo.AreaDO;
import com.fcang.spider.hotel.domain.service.AreaService;


/**
 * 
 * AreaServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-20 09:43:53
 **/

@Service("areaService")
public class AreaServiceImpl extends BaseServiceImpl<AreaDO, Long> implements AreaService{
	@Resource
	private AreaDOMapper areaDOMapper;

	public BaseDao<AreaDO, Long> getDao(){
		return areaDOMapper;
	}

}