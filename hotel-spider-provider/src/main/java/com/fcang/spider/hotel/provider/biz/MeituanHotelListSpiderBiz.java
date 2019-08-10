package com.fcang.spider.hotel.provider.biz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.CommonUtil;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.core.ProxyInfo;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;
import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.domain.pojo.MeituanHotelDetailDO;
import com.fcang.spider.hotel.domain.pojo.MeituanHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CityHotelAssemService;
import com.fcang.spider.hotel.domain.service.CtripCityService;
import com.fcang.spider.hotel.domain.service.MeituanHotelDetailService;
import com.fcang.spider.hotel.domain.service.MeituanHotelInfoService;
import com.fcang.spider.hotel.provider.util.JedisLocker;
import com.fcang.spider.hotel.provider.util.JedisLocker.OwnerLock;
import com.github.pagehelper.PageHelper;

import redis.clients.jedis.commands.JedisCommands;
@Component
public class MeituanHotelListSpiderBiz {
	static Logger logger = LoggerFactory.getLogger(MeituanHotelListSpiderBiz.class);
	static String MEI_TUAN = "meituan";
	@Autowired
	private CtripCityService ctripCityService;
	@Autowired
	JedisCommands jedis;
	@Autowired
	JedisLocker jedisLocker;
	@Autowired
	ProxyProviderBiz proxyProviderBiz;
	@Autowired
	MeituanHotelInfoService meituanHotelInfoService;
	
	@Autowired
	private CityHotelAssemService cityHotelAssemService;
	static String filePath = "C:\\Users\\47246\\github\\hotel-spider\\doc\\meituan_city.json";
	public void addCity() {
		String readLine  = null;
		try {
			BufferedReader in;
			in = new BufferedReader(new FileReader(new File(filePath)));
			readLine = in.readLine();
			in.close();
		} catch (Exception e) {
			logger.error("",e);
		}finally {
			
		}
		JSONArray array = JSONObject.parseArray(readLine);
		array.forEach(obj->{
			JSONObject json = (JSONObject)obj;
			Long id = json.getLong("id");
			String text = json.getString("name");
			CtripCityDO city = new CtripCityDO();
			city.setId(id);
			city.setCity(text);
			city.setCode(json.getString("pinyin"));
			city.setType(2);
			city.setMark(MEI_TUAN);
			ctripCityService.insertSelective(city);
		});
		logger.info("addCity over");
	}
	static String cityUrl = "http://hotel.meituan.com/";
	//https://ihotel.meituan.com/hbsearch/HotelSearch?utm_medium=pc&version_name=999.9&cateId=20
	//&attr_28=129&uuid=5800CE72372C9CD81BA91141310CDECDA53B0202312905176F979ABEE9D8BC46%401564796250414
	//&cityId=59&offset=0&limit=20&startDay=20190803&endDay=20190803&q=&sort=defaults&hotelStar=6
	//&X-FOR-WITH=0K5EDARojgsjLMse7o0UzH6dtEkcVQDiWPYqz2YbVlq6T%2BH9MR0ggp0x8h282bFwpcLvWaVsxUj21Uww473a%2FciHUJhvUDwxczDHzEnyaEmWzUwtKfBXtMd99LzuFiddD%2FQ3hHFKe6tUfU9Ieytulw%3D%3D
	static String ajaxCityComUrl = "https://ihotel.meituan.com/hbsearch/HotelSearch"
			+ "?utm_medium=pc&version_name=999.9&cateId=20&attr_28=129"
			+ "&uuid=uuidValue"
			+ "&cityId=cityIdValue&offset=offsetValue&limit=40&startDay=dayValue&endDay=dayValue&q=&sort=defaults&X-FOR-WITH=0K5EDARojgsjLMse7o0UzH6dtEkcVQDiWPYqz2YbVlq6T%2BH9MR0ggp0x8h282bFwpcLvWaVsxUj21Uww473a%2FciHUJhvUDwxczDHzEnyaEmWzUwtKfBXtMd99LzuFiddD%2FQ3hHFKe6tUfU9Ieytulw%3D%3D";
	static String uuid  = "5800CE72372C9CD81BA91141310CDECDA53B0202312905176F979ABEE9D8BC46";
	public void runCityFromDB() {
		runAssemMeituanHotelDetail();
		CtripCityDO condtion = new CtripCityDO();
		condtion.setMark("meituan_update");
		condtion.setTableName("meituan_city");
		condtion.setType(2);
		List<CtripCityDO> selectEntryList = ctripCityService.selectEntryList(condtion);
		selectEntryList.forEach(city->{
			OwnerLock tryLock = jedisLocker.lock(MEI_TUAN+":"+city.getCode(),100);
			if(tryLock==null) {
				return;
			}
			String newuuid = uuid +"%40"+System.currentTimeMillis();
			
			String ajaxCityUrl = ajaxCityComUrl.replace("uuidValue", newuuid);
			try {
				runAjaxCity(city,ajaxCityUrl);
			}catch (Exception e) {
				logger.error("ajaxCityUrl:{}",e);
			}finally {
				tryLock.release();
			}
			
			
		});
	}
	 //更新酒店批量信息，不会更新单个酒店  ；但是之前调用了runAssemMeituanHotelDetail，来处批量信息	
	private void runAjaxCity(CtripCityDO city,String ajaxCityUrl) {
		logger.info("run city:{}",city);
		int i = 1;
		int pageSize = 40;
		while(i<=150) {
			int currentPage = i;
			i++;
			String key  = MEI_TUAN+":"+DateFormatUtils.format(new Date(), "yyyyMMdd")+":"+city.getCode();
			String hget = jedis.hget(key, currentPage+"");
			if(hget!=null) {
				Integer valueOf = Integer.valueOf(hget);
				if(valueOf>=40) {
					continue;
				}
				if(valueOf<=5) {
					break;
				}
			}
			
			int offset = (currentPage-1)*40;
		
			Date plusDaysToDate = DateUtil.plusDaysToDate(new Date(), 1);
			String day = (String)CommonUtil.get("start");
			if(day==null) {
				day = DateFormatUtils.format(plusDaysToDate, "yyyyMMdd");
			}	
					
			String newUrl = ajaxCityUrl.replaceAll("offsetValue", offset+"")
					.replaceAll("cityIdValue", city.getId()+"")
					.replace("dayValue", day)
					;
			logger.info("offset:{},day:{}",offset,day);
			logger.info("buildByUrl:{}",newUrl);
			BaseFullResponse<Document> buildByUrlRes = JsoupUtil.buildByUrl(newUrl, null, null, null );
			if(!buildByUrlRes.isSuccess()) {
				logger.error("fail,{},buildByUrlRes:{}",newUrl,buildByUrlRes);
				continue;
			}
			Document data = buildByUrlRes.getData();
			String text = data.body().text();
			JSONObject json = (JSONObject)JSONObject.parse(text);
			JSONArray jsonArray = json.getJSONObject("data").getJSONArray("searchresult");
			CityHotelAssemDO assem = new CityHotelAssemDO();
			assem.setCityCode(city.getCode());
			assem.setCityName(city.getCity());
			assem.setCityId(city.getId());
			assem.setCurrentPage(currentPage);
			assem.setPageSize(pageSize);
			assem.setRequestUrl(ajaxCityUrl);
			assem.setRequestType(3);
			assem.setReponseContent(jsonArray.toJSONString());
			assem.setReponseStatus(1);
			assem.setMark(MEI_TUAN);
			cityHotelAssemService.insertOrUpdate(assem);
			jedis.hset(key, currentPage+"",jsonArray.size()+"");
			if(jsonArray.size()<=5) {
				break;
			}
			
		}
		
	}
	@Autowired
	Executor providerSimpleAsync;
	public void runAssemMeituanHotelDetail() {
		providerSimpleAsync.execute(()->{
			CityHotelAssemDO condtion = new CityHotelAssemDO();
			condtion.setReponseStatus(1);
			condtion.setMark("meituan");
			while(!Thread.interrupted()) {
				PageHelper.startPage(1,10,false);
				List<CityHotelAssemDO> selectEntryList = cityHotelAssemService.selectEntryList(condtion );
				if(CollectionUtils.isEmpty(selectEntryList)) {
					logger.info("runAssemMeituanHotelDetail:over");
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}
				selectEntryList.forEach(assem->{
					boolean succ = hanld(assem);
					if(succ) {
						CityHotelAssemDO update = new CityHotelAssemDO();
						update.setId(assem.getId());
						update.setReponseStatus(2);
						cityHotelAssemService.updateByPrimaryKeySelective(update);
					}
				});
			}
		});
		
		
	}

	private boolean hanld(CityHotelAssemDO assem) {
		String reponseContent = assem.getReponseContent();
		if(reponseContent==null) {
			return false;
		}
		try {
			JSONArray parseArray = JSONObject.parseArray(reponseContent);
			if(parseArray==null) {
				return false;
			}
			parseArray.forEach(obj->{
				JSONObject json = (JSONObject)obj;
				String addr = json.getString("addr")==null?"":json.getString("addr");
				Integer brandId = json.getInteger("brandId");
				String commentsCountDesc = json.getString("commentsCountDesc");
				String hotelStar = json.getString("hotelStar");
				String name = json.getString("name");
				String lowestPrice = json.getString("lowestPrice");
				String scoreIntro = json.getString("scoreIntro");
				String avgScore = json.getString("avgScore");
				String areaName = json.getString("areaName");
				Long poiid = json.getLong("poiid");
				MeituanHotelInfoDO meituanHotelInfoDO = new MeituanHotelInfoDO();
				meituanHotelInfoDO.setId(poiid);
				meituanHotelInfoDO.setNameCh(name);
				meituanHotelInfoDO.setStarDesc(hotelStar);
				int indexOf = addr.indexOf('区');
				if(indexOf>0) {
					meituanHotelInfoDO.setRegion(addr.substring(0, indexOf+1));
				}
				meituanHotelInfoDO.setZonenames(areaName);
				meituanHotelInfoDO.setHtladdress(addr);
				meituanHotelInfoDO.setBrandId(brandId);
				meituanHotelInfoDO.setCity(assem.getCityName());
				meituanHotelInfoDO.setCityId(assem.getCityId().intValue());
				meituanHotelInfoDO.setJudgement(commentsCountDesc);
				meituanHotelInfoDO.setAvgScore(avgScore);
				meituanHotelInfoDO.setHotelLevel(scoreIntro);
				meituanHotelInfoDO.setPriceLow(lowestPrice);
				JSONObject forwrdInfo = json.getJSONObject("forward");
				String commentCount = forwrdInfo.getString("commentCount");
				String poiExtendsInfosDesc = forwrdInfo.getString("poiExtendsInfosDesc");
				meituanHotelInfoDO.setCommentCount(Integer.valueOf(commentCount));
				meituanHotelInfoDO.setExtInfo(poiExtendsInfosDesc);
				meituanHotelInfoDO.setCityHotelAssemId(assem.getId());
				meituanHotelInfoDO.setCreateTime(new Date());
				try {
					meituanHotelInfoService.insertSelective(meituanHotelInfoDO);
				}catch (DuplicateKeyException e) {
					
				}
			});
		}catch (Exception e) {
			logger.error("hanld exception,assem.id:{},assem.ReponseContent:{}",assem.getId(),assem.getReponseContent(),e);
			return false;
		}
		return true;
	}
	public void hanldMeituanDetailPage(String init) {
		MeituanHotelInfoDO condtion = new MeituanHotelInfoDO();
		condtion.setMark(init);
		String orderby = PhantomjsLoader.isWindows?"id desc":"id asc";
		while(!Thread.interrupted()) {
			PageHelper.startPage(1,4,false).setOrderBy(orderby);
			List<MeituanHotelInfoDO> selectEntryList = meituanHotelInfoService.selectEntryList(condtion);
			if(CollectionUtils.isEmpty(selectEntryList)) {
				logger.info("hanldMeituanDetailPage.over");
				try {
					Thread.sleep(1000*60*3);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				continue;
			}
			selectEntryList.forEach(meituanHotelInfoDB->{
				hanldDetail(meituanHotelInfoDB);
			});
		}
	
}
	/*public void hanldMeituanDetailPage() {
		hanldMeituanDetailPage("init");
	}*/
//https://hotel.meituan.com/2387965
	@Autowired
	MeituanHotelDetailService meituanHotelDetailService;
	private void hanldDetail(MeituanHotelInfoDO meituanHotelInfoDO) {
		Long id = meituanHotelInfoDO.getId();
		String url = "https://hotel.meituan.com/"+id;
		JsoupUtil.setKV("cookie", "__mta=222780964.1563988511753.1563988511753.1564793593988.2; iuuid=5800CE72372C9CD81BA91141310CDECDA53B0202312905176F979ABEE9D8BC46; ci=59; cityname=%E6%88%90%E9%83%BD; _lxsdk_cuid=16c24fa44c09e-068dc1f86e9d7e-e343166-104040-16c24fa44c1c8; _lxsdk=5800CE72372C9CD81BA91141310CDECDA53B0202312905176F979ABEE9D8BC46; uuid=4602a564745841ae943a.1563984841.1.0.0; __mta=222780964.1563988511753.1563988511753.1563988511753.1; hotel_city_id=59; hotel_city_info=%7B%22id%22%3A59%2C%22name%22%3A%22%E6%88%90%E9%83%BD%22%2C%22pinyin%22%3A%22chengdu%22%7D; IJSESSIONID=5qm8729k5m8xdsjvy1yr2sfy; _lxsdk_s=16c54f7269b-2c2-e29-189%7C%7C6");
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
		BaseFullResponse<Document> buildByUrlRes = JsoupUtil.buildByUrl(url, null, headers, null );
		if(!buildByUrlRes.isSuccess()) {
			logger.error("fail,{},buildByUrlRes:{}",url,buildByUrlRes);
			//updateHotelInfoDO.setMark("error");
			//meituanHotelInfoService.updateByPrimaryKeySelective(updateHotelInfoDO);
			return;
		}
		Document document = buildByUrlRes.getData();
		Element poiDetail = document.getElementById("poiDetail");
		MeituanHotelInfoDO updateHotelInfoDO = new MeituanHotelInfoDO();
		updateHotelInfoDO.setId(id);
		if(poiDetail==null) {
			String doc = document.toString();
			if(doc.contains("refresh")&&doc.contains("www.meituan.com/error/")) {
				updateHotelInfoDO.setMark("refresh_error");
			}else {
				updateHotelInfoDO.setMark("error");
			}
			meituanHotelInfoService.updateByPrimaryKeySelective(updateHotelInfoDO);
			logger.error("fail,{},poiDetail==null",url);
			return;
		}
		
		Elements elementsByClass = poiDetail.getElementsByClass("mb10");
		Iterator<Element> iterator = elementsByClass.iterator();
		while(iterator.hasNext()) {
			Element next = iterator.next();
			if(next.childNodeSize()>1) {
				continue;
			}
			String text = next.text().trim();
			if(text.contains("电话")) {
				if(StringUtils.isEmpty(text)) {
					return;
				}
				updateHotelInfoDO.setMark("init_1");
				updateHotelInfoDO.setContact(text.replaceAll("电话：", ""));
				updateHotelInfoDO.setUpdateTime(new Date());
				meituanHotelInfoService.updateByPrimaryKeySelective(updateHotelInfoDO);
				break;
			}
		}
		MeituanHotelDetailDO meituanHotelDetail = new MeituanHotelDetailDO();
		meituanHotelDetail.setId(id);
		meituanHotelDetail.setDetailHtmlInfo(poiDetail.toString());
		meituanHotelDetail.setMarkStatus("init_1");
		meituanHotelDetailService.insertSelective(meituanHotelDetail );
	}
	
	public static void main(String[] args) {
		String url = "https://hotel.meituan.com/193183852";
		JsoupUtil.setKV("cookie", "hotel_city_id=1; hotel_city_info=%7B%22id%22%3A1%2C%22name%22%3A%22%E5%8C%97%E4%BA%AC%22%2C%22pinyin%22%3A%22beijing%22%7D; IJSESSIONID=1gnmv664abbxh1813wg69a4vj3; iuuid=403014E92EC807E170DFDD7ED097EB001E6B4F2EE39E4ACF2CB3730873BEC2EC; latlng=30.543696%2C104.058906%2C1564563999535; ci=59; cityname=%E6%88%90%E9%83%BD; _lxsdk_cuid=16c47481006c8-0e63c1c1b6b078-e343166-104040-16c47481006c8; _lxsdk=403014E92EC807E170DFDD7ED097EB001E6B4F2EE39E4ACF2CB3730873BEC2EC; uuid=f548ba38f6714ee38214.1564564001.1.0.0; _lxsdk_s=16c4748100a-7c-303-2e3%7C%7C3");
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
		//headers.put("Referer","https://hotel.meituan.com/beijing/");
		BaseFullResponse<Document> buildByUrlRes = JsoupUtil.buildByUrl(url, null, headers , null );
		if(!buildByUrlRes.isSuccess()) {
			logger.error("fail,{},buildByUrlRes:{}",url,buildByUrlRes);
			//updateHotelInfoDO.setMark("error");
			//meituanHotelInfoService.updateByPrimaryKeySelective(updateHotelInfoDO);
			return;
		}
		Document document = buildByUrlRes.getData();
		Element poiDetail = document.getElementById("poiDetail");
		System.out.println(poiDetail);
	}

	private static void remove(Element eel) {
		if(eel==null) {
			return;
			
		}
		if(eel.childNodeSize()==0) {
			eel.remove();
			return;
		}
		eel.children().forEach(ele->{
			remove(ele);
		});
	}
}
