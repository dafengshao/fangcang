package com.fcang.spider.hotel.domain.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fcang.spider.hotel.domain.base.BaseDao;
import com.fcang.spider.hotel.domain.base.BaseServiceImpl;
import com.fcang.spider.hotel.domain.mapper.CityHotelAssemDOMapper;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;
import com.fcang.spider.hotel.domain.service.CityHotelAssemService;


/**
 * 
 * CityHotelAssemServiceImpl业务层实现类
 * @author hewenfeng
 * @date 2018-12-23 15:47:07
 **/

@Service("cityHotelAssemService")
public class CityHotelAssemServiceImpl extends BaseServiceImpl<CityHotelAssemDO, Long> implements CityHotelAssemService{
	@Resource
	private CityHotelAssemDOMapper cityHotelAssemDOMapper;

	public BaseDao<CityHotelAssemDO, Long> getDao(){
		return cityHotelAssemDOMapper;
	}

	public CityHotelAssemDO getUniq(int type ,Long cityId,String cityCode,int currentPage,int pageSize,String batchCode) {
		CityHotelAssemDO condtion = new CityHotelAssemDO();
		condtion.setCityCode(cityCode);
		condtion.setCityId(cityId);
		condtion.setCurrentPage(currentPage);
		condtion.setPageSize(pageSize);
		condtion.setRequestType(type);
		if(batchCode==null) {
			condtion.setTaskBatch("0");
		}else {
			condtion.setTaskBatch(batchCode);
		}
		/*JSONObject json = (JSONObject) JSONObject.toJSON(area);
		json.put("cityCode", cityCode)	;
		json.put("currentPage", currentPage)	;
		json.put("response_status", status)	;
		logger.warn("fliggy_hotel_response={}",json.toJSONString());*/
		List<CityHotelAssemDO> selectEntryList = this.selectEntryList(condtion );
		if(CollectionUtils.isEmpty(selectEntryList)) {
			return null;
		}else {
			return selectEntryList.get(0);
		}
	}
	
	@Override
	public int insertOrUpdate(CityHotelAssemDO t) {
		t.setUpdateTime(new Date());
		if(t.getId()!=null) {
			return super.updateByPrimaryKeySelective(t);
		}
		if(t.getTaskBatch()==null) {
			t.setTaskBatch("0");
		}
		try {
			return super.insertSelective(t);
		}catch (DuplicateKeyException e) {
			return 0;
		}
		
	}
}