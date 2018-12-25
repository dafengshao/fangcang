package com.fcang.spider.hotel.domain.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.FliggyHotelDOMapper;
import com.fcang.spider.hotel.domain.pojo.FliggyHotelDO;
import com.fcang.spider.hotel.domain.service.FliggyHotelService;


/**
 * 
 * FliggyHotelServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-20 23:29:41
 **/

@Service("fliggyHotelService")
public class FliggyHotelServiceImpl extends BaseServiceImpl<FliggyHotelDO, Long> implements FliggyHotelService{
	@Resource
	private FliggyHotelDOMapper fliggyHotelDOMapper;

	public BaseDao<FliggyHotelDO, Long> getDao(){
		return fliggyHotelDOMapper;
	}

	public int insertOrUpdatePrice(FliggyHotelDO hotelDO) {
		FliggyHotelDO selectByPrimaryKey = super.selectByPrimaryKey(hotelDO.getId());
		if(selectByPrimaryKey==null) {
			return super.insertSelective(hotelDO);
		}else {
			FliggyHotelDO upd = new FliggyHotelDO();
			upd.setId(hotelDO.getId());
			if(hotelDO.getPrice()!=null&&hotelDO.getPrice()>0) {
				upd.setPrice(hotelDO.getPrice());
				upd.setPriceInfo(hotelDO.getPriceInfo());
				upd.setPriceLg(hotelDO.getPriceLg());
			}
			upd.setCity(hotelDO.getCity());
			upd.setCityId(hotelDO.getCityId());
			upd.setCityCode(hotelDO.getCityCode());
			return super.updateByPrimaryKeySelective(upd);
		}
	}

}