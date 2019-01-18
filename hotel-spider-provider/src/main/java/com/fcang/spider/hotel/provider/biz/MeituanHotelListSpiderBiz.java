package com.fcang.spider.hotel.provider.biz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.core.ProxyInfo;
import com.fcang.spider.hotel.domain.pojo.CityHotelAssemDO;
import com.fcang.spider.hotel.domain.pojo.CtripCityDO;
import com.fcang.spider.hotel.domain.pojo.MeituanHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CityHotelAssemService;
import com.fcang.spider.hotel.domain.service.CtripCityService;
import com.fcang.spider.hotel.domain.service.MeituanHotelInfoService;
import com.fcang.spider.hotel.provider.util.JedisLocker;
import com.fcang.spider.hotel.provider.util.JedisLocker.OwnerLock;
import com.github.pagehelper.PageHelper;

import redis.clients.jedis.commands.JedisCommands;
@Component
public class MeituanHotelListSpiderBiz {
	Logger logger = LoggerFactory.getLogger(MeituanHotelListSpiderBiz.class);
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
	
	static String ajaxCityComUrl = "https://ihotel.meituan.com/hbsearch/HotelSearch"
			+ "?utm_medium=pc&version_name=999.9&cateId=20&attr_28=129"
			+ "&uuid=uuidValue"
			+ "&cityId=cityIdValue&offset=offsetValue&limit=40&startDay=dayValue&endDay=dayValue&q=&sort=defaults";
	public void runCityFromDB() {
		CtripCityDO condtion = new CtripCityDO();
		condtion.setMark(MEI_TUAN);
		condtion.setType(2);
		List<CtripCityDO> selectEntryList = ctripCityService.selectEntryList(condtion);
		selectEntryList.forEach(city->{
			String stri = cityUrl+city.getCode();
			OwnerLock tryLock = jedisLocker.lock(MEI_TUAN+":"+city.getCode(),2000);
			if(tryLock==null) {
				return;
			}
			try {
//				BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(stri, null, null, null);
//				if(!buildByUrl.isSuccess()) {
//					return;
//				}
				String uuid  = "F2B91CF795D64A3D0C319A642329B3A6B29F464F6F28561622670EB6AE44DC90";
				uuid = uuid +"%40"+System.currentTimeMillis();
				
				String ajaxCityUrl = ajaxCityComUrl.replace("uuidValue", uuid);
				runAjaxCity(city,ajaxCityUrl);
				/*Document data = buildByUrl.getData();
				Elements elementsByTag = data.getElementsByTag("script");
				elementsByTag.forEach(ele->{
					String text = ele.html();
					if(text.indexOf("window.__INITIAL_STATE__")<0) {
						return;
					}
					int uuidIndexOf = text.indexOf("uuid")+7;
					if(uuidIndexOf<=7) {
						logger.error("error,{}",text.substring(0,5000));
						return;
					}
					int baseIndexOf = text.indexOf("base")-5;
					
				});*/
			}finally {
				tryLock.release();
			}
			
			
		});
	}
	 	
	private void runAjaxCity(CtripCityDO city,String ajaxCityUrl) {
		logger.info("run city:{}",city);
		int i = 1;
		int pageSize = 40;
		while(i<=3000) {
			int currentPage = i;
			i++;
			/*CityHotelAssemDO condtion = new CityHotelAssemDO();
			condtion.setCityId(city.getId());
			condtion.setCurrentPage(currentPage);
			condtion.setRequestType(3);
			Integer selectEntryListCount = cityHotelAssemService.selectEntryListCount(condtion );
			if(selectEntryListCount!=null&&selectEntryListCount>=1) {
				continue;
			}*/
			String hget = jedis.hget(MEI_TUAN+":"+city.getCode(), currentPage+"");
			if(hget!=null) {
				Integer valueOf = Integer.valueOf(hget);
				if(valueOf>=40) {
					continue;
				}
				if(valueOf<=10) {
					break;
				}
			}
			
			int offset = (currentPage-1)*40;
		
			Date plusDaysToDate = DateUtil.plusDaysToDate(new Date(), 1);
			String day = DateFormatUtils.format(plusDaysToDate, "yyyyMMdd");
			String newUrl = ajaxCityUrl.replaceAll("offsetValue", offset+"")
					.replaceAll("cityIdValue", city.getId()+"")
					.replace("dayValue", day)
					;
			logger.info("offset:{},day:{}",offset,day);
			logger.info("buildByUrl:{}",newUrl);
			ProxyInfo proxy = null;//proxyProviderBiz.getProxyInfoRandom();
			//proxy.setHost("136.228.128.6");
			//proxy.setPort(58708);
			BaseFullResponse<Document> buildByUrlRes = JsoupUtil.buildByUrl(newUrl, null, null, proxy );
			if(!buildByUrlRes.isSuccess()) {
				if(buildByUrlRes.getCode().equals("IOException"))proxyProviderBiz.score(proxy, -100);
				logger.error("fail,{},buildByUrlRes:{}",newUrl,buildByUrlRes);
				continue;
			}
			Document data = buildByUrlRes.getData();
			String text = data.body().text();
			JSONObject json = (JSONObject)JSONObject.parse(text);
			if(json.getJSONObject("error")!=null) {
				proxyProviderBiz.score(proxy, -10);
				logger.error("error:{}",text);
				break;
			}
			proxyProviderBiz.score(proxy, 1);
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
			jedis.hset(MEI_TUAN+":"+city.getCode(), currentPage+"",jsonArray.size()+"");
			if(jsonArray.size()<=10) {
				break;
			}
			
		}
		
	}

	public void runMeituanHotelDetail() {
		CityHotelAssemDO condtion = new CityHotelAssemDO();
		condtion.setReponseStatus(1);
		while(true) {
			PageHelper.startPage(1,10,false);
			List<CityHotelAssemDO> selectEntryList = cityHotelAssemService.selectEntryList(condtion );
			if(CollectionUtils.isEmpty(selectEntryList)) {
				break;
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
			logger.error("hanld exception",e);
			return false;
		}
		return true;
	}
}
