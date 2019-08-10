package com.fcang.spider.hotel.provider.biz;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.domain.pojo.HotelMappingDO;
import com.fcang.spider.hotel.domain.service.HotelMappingService;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HotelMappingBiz {
	@Autowired
	HotelMappingService hotelMappingService;
	
	String init = "init";
	String empty = "empty";
	String ok = "ok";
	String error = "error";
	String exception = "exception";
	public List<HotelMappingDO> getInitList() {
		HotelMappingDO condtion = new HotelMappingDO();
		condtion.setMark(init);
		PageHelper.startPage(1, 20).setCount(false);
		return  hotelMappingService.selectEntryList(condtion );
	}
	public void run() {
		List<HotelMappingDO> list  = null;
		do {
			list = getInitList();
			list.forEach(action->{
				hanlder(action);
			});
		}while(!list.isEmpty());
		
	}
	/**
	 * Method: GetHotelDetail
MasterHotelId: 396356
CheckInDate: 2019-06-07
CheckOutDate: 2019-06-08
CountryID: 1
	 */
	String httpPostUrl = "http://b2b.vipdlt.com/Booking/Ajax/SearchHotel.ashx?v=0.8603939287700262";
	static Map<String,String> headers = new HashMap<>();
	static {
		headers.put("Cookie","_RSG=ZOahh6m9fH2fkugJnvkMjB; _RDG=284b2992d45f6b2c062b2a5b2b4a4c458e; _RGUID=24692701-abae-4623-bb56-bb9c53ab8ba9; MipHuName=D3EFBB5022885AEF; autoLogin=T; hotel_user_token=4306E962-D34E-4E0B-A813-99E8ACFA3F3D; _RF1=125.69.40.177; ASP.NET_SessionSvc=MTAuOC4xODkuNTl8OTA5MHxqaW5xaWFvfGRlZmF1bHR8MTU1NzgxNDI4NjA3OQ; ASP.NET_SessionId=xsvfdnn5qwihmvkbnqdzh1la; CurrentLanguage=SimpChinese; backToHotel=http%3A//b2b.vipdlt.com/Booking/HotelDetailNew.aspx%3Fmasterhotelid%3D1592153%26checkIn%3D2019-06-08%26checkOut%3D2019-06-09; DonotShowNotice=F; randomkey=af5905dd-2b7c-4a31-86f5-641fa439de35; clinetIp=125.69.40.177; MipHuid=7C86B27404E1AD6F; ebkneworder=; ebk_basedata=Huid=7C86B27404E1AD6F&IsSpecialSupplier=C5504E123F084683&isDivCloseNotice=0; huid=7C86B27404E1AD6F; huname=B899FB7B02005825A09A1BE855EBF0FDB17E37118E845A88D1C3F75843DDF9AC7849047FF7680464; hotel_user_token=A3A0654CD832498FC9E87A82AC8023494B067CD12CE890B07B905C34188CA18E1B521E418A5F6BB3; ebk_login=IsNewVersion=T; huid=6756526; _bfi=p1%3D10320669739%26p2%3D10320669714%26v1%3D42%26v2%3D41; _bfa=1.1559732339502.1v17eo.1.1559902329399.1559957452315.6.43; _bfs=1.5");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.108 Safari/537.36");
		headers.put("Origin", "http://b2b.vipdlt.com");
		headers.put("Referer", "http://b2b.vipdlt.com/Booking/Int_HotelDetailNew.aspx?masterhotelid=215&checkIn=2019-06-07&checkOut=2019-06-08&countryID=1");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Host", "b2b.vipdlt.com");
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");
	}
	private void hanlder(HotelMappingDO mapping) {
		Map<String, String> datas = new HashMap<>();
		datas.put("Method","GetHotelDetail");
		datas.put("MasterHotelId",mapping.getId().toString());
		datas.put("CheckInDate",LocalDate.now().toString());
		datas.put("CheckOutDate",LocalDate.now().plus(1, ChronoUnit.DAYS).toString());
		datas.put("CountryID","1");
		Long id = mapping.getId();
		log.info("hanlder id:{}",id);
		BaseFullResponse<Document> buildByUrlRes = 
				JsoupUtil.buildByUrl(httpPostUrl, datas 	,headers,null);
		if(!buildByUrlRes.isSuccess()) {
			update(mapping.getId(),error);
			return;
		}
		Document doc = buildByUrlRes.getData();
		if(doc==null) {
			update(mapping.getId(),empty);
			return;
		}
		Element body = doc.body();
		String string = body.text();
		JSONObject parseObject = JSONObject.parseObject(string);
		JSONObject data = parseObject.getJSONObject("Data");
		if(data==null) {
			update(mapping.getId(),empty);
			return;
		}
		JSONObject hotelBasicInfo = data.getJSONObject("HotelBasicInfo");
		if(hotelBasicInfo==null) {
			update(mapping.getId(),empty);
			return;
		}
		JSONObject hotelMappingUrl = hotelBasicInfo.getJSONObject("HotelMappingUrl");
		if(hotelMappingUrl==null) {
			update(mapping.getId(),empty);
			return;
		}
		HotelMappingDO map = buildHotelMappingDO(hotelMappingUrl);
		map.setId(id);
		map.setHotelMappingUrl(hotelMappingUrl.toJSONString());
		hotelMappingService.updateByPrimaryKeySelective(map );
	}
	private  HotelMappingDO  buildHotelMappingDO(JSONObject hotelMappingUrl) {
		Set<Entry<String, Object>> entrySet = hotelMappingUrl.entrySet();
		Iterator<Entry<String, Object>> iterator = entrySet.iterator();
		HotelMappingDO hotelMappingDO = new HotelMappingDO();
		try {
			while(iterator.hasNext()) {
				Entry<String, Object> next = iterator.next();
				String value = (String)next.getValue();
				String key = next.getKey();
				setKeyValue(hotelMappingDO,key,value);
			}
			hotelMappingDO.setMark(ok);
		}catch (Exception e) {
			hotelMappingDO.setMark(exception);
		}
		return hotelMappingDO;
	}

	private void setKeyValue(HotelMappingDO hotelMappingDO, String key, String value) {
		switch (key) {
		case "agoda":
			hotelMappingDO.setAgodaId(value);
			break;
		case "alitrip":
			value = value.substring(value.indexOf("shid=")+5);
			value = value.substring(0,value.indexOf('&'));
			hotelMappingDO.setAlitripId(value);
			break;
		case "booking":
			hotelMappingDO.setBookingId(value);
			break;
		case "ctrip":
			//ctrip: "http://hotels.ctrip.com/hotel/396356.html"
			value = value.replaceAll(".html", "")
			.replaceAll("http://hotels.ctrip.com/hotel/", "");
			hotelMappingDO.setCtripId(value);
			break;
		case "dianping":
			//http://www.dianping.com/shop/
			value = value.replaceAll("http://www.dianping.com/shop/", "");
			hotelMappingDO.setDianpingId(value);
			break;
		case "elong":
			value = value.replaceAll("http://hotel.elong.com/", "");
			value = value.substring(value.indexOf('/')+1);
			value= value.replaceAll("/", "");
			hotelMappingDO.setElongId(value);
			break;
		case "meituan":
			if(value.indexOf("deal")>=0) {
				value = value.replaceAll("http://i.meituan.com/awp/h5/hotel/poi/deal.html", "");
				value = value.replaceAll("[?poiId=]", "");
				value = value.substring(0,value.indexOf('&'));
			}else {
				value = value.replaceAll("http://hotel.meituan.com/item/", "");
				value = value.substring(0,value.indexOf('?'));
			}
			
			
			hotelMappingDO.setMeituanId(value);
			break;
		case "qunar":
			//value = value.replaceAll("http://hotel.qunar.com/city/hongkong_city/dt-", "");
			value = value.substring(value.indexOf("dt-")+3,value.indexOf('?'));
			value= value.replaceAll("/", "");
			hotelMappingDO.setQunarId(value);
			break;
		case "tongcheng":
			value = value.replaceAll("http://www.ly.com/HotelInfo-", "");
			value = value.replaceAll(".html", "");
			hotelMappingDO.setTongchengId(value);
			break;
		case "tuniu":
			value = value.replaceAll("http://hotel.tuniu.com/detail/", "");
			value = value.substring(0,value.indexOf('?'));
			hotelMappingDO.setTuniuId(value);
			break;
		default:
			break;
		}
	}
	private void update(Long id, String mark) {
		HotelMappingDO condtion = new HotelMappingDO();
		condtion.setId(id);
		condtion.setMark(mark);
		hotelMappingService.updateByPrimaryKeySelective(condtion );
	}
	
	
}
