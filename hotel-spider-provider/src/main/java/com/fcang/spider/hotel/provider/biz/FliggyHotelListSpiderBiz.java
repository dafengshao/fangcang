package com.fcang.spider.hotel.provider.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.domain.pojo.AreaDO;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;
import com.fcang.spider.hotel.domain.pojo.FliggyHotelDO;
import com.fcang.spider.hotel.domain.service.AreaService;
import com.fcang.spider.hotel.domain.service.CityHotelAssemService;
import com.fcang.spider.hotel.domain.service.FliggyHotelService;
import com.fcang.spider.hotel.provider.util.FliggyHotelHtmlUtil;
import com.github.pagehelper.PageHelper;
@Component
public class FliggyHotelListSpiderBiz {
	static Logger logger = LoggerFactory.getLogger(FliggyHotelListSpiderBiz.class);
	
	@Autowired
	AreaService areaService;
	@Autowired
	FliggyHotelService fliggyHotelService;
	@Autowired
	FliggyHotelHtmlUtil fliggyHotelHtmlUtil;
	@Autowired
	CityHotelAssemService cityHotelAssemService;
	public void run(){
		AreaDO condtion = new AreaDO();
		//condtion.setLevel(4);
		condtion.setStatusCode("0");
		int startPageNo = 1;
		String batchCode =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		JsoupUtil.setBatchInfo(batchCode);
		while(true) {
			PageHelper.startPage(startPageNo, 50,false).setOrderBy("id asc");
			List<AreaDO> selectEntryList = areaService.selectEntryList(condtion);
			if(selectEntryList.isEmpty()) {
				break;
			}
			selectEntryList.forEach(area->{
				hanlderAjax(area);
			});
			startPageNo++;
		}
		
	}
	int pageSize = 50;
	private void hanlderAjax(AreaDO area) {
		Long cityId = area.getId();
		int length = cityId.toString().length();
		int i = 1;
		if(length<6) {
			i = (int)Math.pow(10,(6-length));
		}
		String cityCode = cityId*i+"";
		int pageNo = 1;
	
		while(true){
			int currentPage = pageNo;
			CityHotelAssemDO uniq = cityHotelAssemService.getUniq(1, cityId, cityCode, currentPage, pageSize,JsoupUtil.getBatchInfo());
			if(uniq!=null&&uniq.getReponseContent()!=null) {
				JSONObject parse = (JSONObject)JSONObject.parse(uniq.getReponseContent());
				JSONArray jsonArray = parse.getJSONArray("hotelList");
				if(jsonArray==null||jsonArray.isEmpty()) {
					break;
				}
			}
			if(uniq!=null&&uniq.getReponseStatus()>=1&&uniq.getReponseContent()!=null) {
				pageNo++;
				continue;
			}
			
			CityHotelAssemDO newAssem = new CityHotelAssemDO();
			if(uniq!=null) {
				newAssem.setId(uniq.getId());
			}
			newAssem.setReponseStatus(0);
			newAssem.setRequestType(1);
			newAssem.setCityCode(cityCode);
			newAssem.setCityId(cityId);
			newAssem.setCurrentPage(currentPage);
			newAssem.setPageSize(pageSize);
			newAssem.setTaskBatch(JsoupUtil.getBatchInfo());
			Document buildNormal = null;
			try {
				//Thread.sleep(2000);
				buildNormal = fliggyHotelHtmlUtil.buildNormal(cityCode, currentPage);
				boolean hasNext= true;
				if(buildNormal==null) {
					if(newAssem.getReponseStatus()==null) {
						newAssem.setReponseStatus(0);
					}
				}else {
					newAssem.setReponseStatus(1);
					String text = buildNormal.body().text();
					if(text.indexOf("hotelList")<0) {
						logger.error("cityCode:{},currentPage:{},body:{}",cityCode, currentPage,text);
						break;
					}
					if(currentPage>1) {
						JSONObject json = (JSONObject)JSONObject.parseObject(buildNormal.body().text());
						JSONObject jsonnew = new JSONObject();
						JSONArray jsonArray = json.getJSONArray("hotelList");
						if(jsonArray!=null&&!jsonArray.isEmpty()) {
							jsonnew.put("hotelList",jsonArray );
						}else {
							hasNext= false;
						}
						newAssem.setReponseContent(jsonnew.toJSONString());
					}else {
						newAssem.setReponseContent(text);
						int indexOf = newAssem.getReponseContent().indexOf("\"hotelList\":[]");
						//indexOf>0;//标识当前页空了,没有下页了
						hasNext = indexOf<0;
					}
					
					
				}
				cityHotelAssemService.insertOrUpdate(newAssem);
				if(!hasNext) {
					break;
				}
				
			}catch (Exception e) {
				if(buildNormal!=null) {
					newAssem.setReponseContent(buildNormal.body().text());
				}
				cityHotelAssemService.insertOrUpdate(newAssem);
				logger.error("cityCode:{},currentPage:{},document:{}",cityCode, currentPage,buildNormal==null?"none":buildNormal,e);
			}finally {
				pageNo++;
			}
			
			
		}
		
		
	}

	static final  String LIST_URL = "https://hotel.fliggy.com/hotel_list3.htm?_input_charset=&_output_charset=&searchBy=&market=0&previousChannel=&pageSize=50&city=%d&currentPage=%d";
	//https://hotel.fliggy.com/ajax/hotelList.htm?pageSize=20&currentPage=1&totalItem=124&startRow=0&endRow=19&city=820100&tid=null&market=0&previousChannel=&u=null&detailLinkCity=820100&cityName=%E6%BE%B3%E9%97%A8&checkIn=2018-12-23&checkOut=2018-12-24&browserUserAgent=Mozilla%2F5.0%20(Windows%20NT%2010.0%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F71.0.3578.98%20Safari%2F537.36&userClientIp=171.223.99.39&userSessionId=x0aFFGIWTUwCAd7UjEKEB%2Bon&offset=0&keywords=null&priceRange=R0&dangcis=&brands=162&services=&order=DEFAULT&dir=DESC&client=11.1.231.195&tagids=null&searchPoiName=null&pByRadiusLng=-10000&pByRadiusLat=-10000&radius=-1&pByRectMinLat=-1&pByRectMinLng=-1&pByRectMaxLat=-1&pByRectMaxLng=-1&lowPrice=-1&highPrice=-1&filterByKezhan=false&searchBy=&searchByTb=false&businessAreaId=-9999&skipKeywords=false&district=-9999&backCash=false&shids=null&activity=null&filterDoubleEleven=false&filterByRoomTickets=false&filterHxk=false&filterCxk=false&filterByRoomTicketsAndNeedLogin=false&filterByRoomTicketsAndNeedBuyTicket=false&activityCode=null&searchId=null&userId=null&hotelTypes=null&filterByB2g=false&filterByAgreement=false&bizNo=null&bizType=null&region=0&newYearSpeOffer=false&laterPay=false&sellerId=null&sellerIds=null&isMemberPrice=false&isLaterPayActivity=false&isFilterByTeHui=false&keyWordsType=&userUniqTag=null&iniSearchKW=false&poiNameFilter=&isFreeCancel=false&isInstantConfirm=false&activityCodes=&adultChildrenCondition=%26roomNum%3D1%26aNum_1%3D2%26cNum_1%3D0&poisearch=false&overseaMarket=false&roomNum=1&notFilterActivityCodeShotel=false&totalPage=7&pageLastItem=20&previousPage=1&nextPage=2&pageFirstItem=1&firstPage=true&lastPage=false&aNum_1=2&cNum_1=0&cAge_1_1=0&cAge_1_2=0&cAge_1_3=0&_input_charset=utf-8&laterPaySwitch&isNotFilterActivityCodeShotel&_ksTS=1545414364296_2816&callback=jsonp2817
	private void hanlderHtml(AreaDO area) {
		Long id = area.getId();
		int length = id.toString().length();
		int i = 1;
		if(length<6) {
			i = (int)Math.pow(10,(6-length));
		}
		Long cityId = id*i;
		int pageNo = 1;
		AreaDO condtion = new AreaDO();
		condtion.setId(id);
		while(true) {
			String cityUrl = String.format(LIST_URL, cityId,pageNo++);
			List<FliggyHotelDO> fliggyHotelList = fliggyHotelHtmlUtil.buildFliggyHotelList(cityUrl);
			if(!CollectionUtils.isEmpty(fliggyHotelList)) {
				fliggyHotelList.forEach(hotel->{
					try {
						hotel.setCityId(cityId);
						hotel.setUpdateTime(new Date());
						fliggyHotelService.insertSelective(hotel);
					}catch (DuplicateKeyException e) {
						
					}
				});
//				if(fliggyHotelList.size()==50) {
//					continue;
//				}
				condtion.setStatusCode("over_pageno_"+pageNo);
				int updateByPrimaryKeySelective = areaService.updateByPrimaryKeySelective(condtion);
				logger.info("over_pageno.areaService.updateByPrimaryKeySelective:{}",updateByPrimaryKeySelective);
			}else {
				condtion.setStatusCode("no_hotel");
				int updateByPrimaryKeySelective = areaService.updateByPrimaryKeySelective(condtion);
				logger.info("no_hotel.areaService.updateByPrimaryKeySelective:{}",updateByPrimaryKeySelective);
				break;
			}
		}
	}
	
	public static void main(String[] args) { 
	    System.out.println(new SimpleDateFormat("yyyy-MM-dda").format(new Date())); 
	  }
}
