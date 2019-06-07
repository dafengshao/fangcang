package com.fcang.spider.hotel.domain.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.MeituanHotelDetailDOMapper;
import com.fcang.spider.hotel.domain.pojo.MeituanHotelDetailDO;
import com.fcang.spider.hotel.domain.service.MeituanHotelDetailService;


/**
 * 
 * MeituanHotelDetailServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2019-03-24 13:26:18
 **/

@Service("meituanHotelDetailService")
public class MeituanHotelDetailServiceImpl extends BaseServiceImpl<MeituanHotelDetailDO, Long> implements MeituanHotelDetailService{
	@Resource
	private MeituanHotelDetailDOMapper meituanHotelDetailDOMapper;

	Logger logger = LoggerFactory.getLogger(MeituanHotelDetailServiceImpl.class);
	public BaseDao<MeituanHotelDetailDO, Long> getDao(){
		return meituanHotelDetailDOMapper;
	}
	
	@Override
	public int insertSelective(MeituanHotelDetailDO t) {
		t.setCreateTime(new Date());
		try {
			return super.insertSelective(t);
		}catch (DuplicateKeyException e) {
			logger.warn("insertSelective MeituanHotelDetailDO记录已经存在了");
		}
		return 0;
	}

}