package com.fcang.spider.hotel.provider.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.CommonUtil;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.domain.pojo.AreaDO;
import com.fcang.spider.hotel.domain.pojo.FliggyCityDO;
import com.fcang.spider.hotel.domain.pojo.FliggyHotelDO;
import com.fcang.spider.hotel.domain.service.AreaService;
import com.fcang.spider.hotel.domain.service.FliggyCityService;
import com.fcang.spider.hotel.provider.biz.ProxyProviderBiz;
@Component
public class FliggyHotelHtmlUtil {
	static final Logger  logger = LoggerFactory.getLogger(FliggyHotelHtmlUtil.class);
	static String URL_FORM = "https://hotel.fliggy.com/hotel_list3.htm"
			+ "?_input_charset=&_output_charset=&searchBy=&market=0&previousChannel="
			+ "&cityName=%CE%E4%BA%BA%CA%D0&city=420100"
			+ "&pageSize=50&currentPage=";
	@Autowired
	FliggyCityService fliggyCityService;
	@Autowired
	ProxyProviderBiz proxyProviderBiz;
	@Autowired
	AreaService areaService;
	public static void buildHotellist() {
		Map<String,String> headers = new HashMap<>();
		headers.put("Cookie", "t=8f438a9345c98061691fe0f5acfb1eac; _tb_token_=e64675b36a3ee; cookie2=1e0737d7d5f5ebf986372e4fdcb88e1d; hng=CN%7Czh-cn%7CCNY; tracknick=%5Cu9000%5Cu4E86%5Cu4F11%5Cu7684%5Cu6D77%5Cu76D7; _l_g_=Ug%3D%3D; ck1=\\\"\\\"; unb=368723234; lgc=%5Cu9000%5Cu4E86%5Cu4F11%5Cu7684%5Cu6D77%5Cu76D7; cookie1=BdM0m8qWjiixi5iEbs8DK7EZUQsz57YAMaZD4jkdDPo%3D; login=true; cookie17=UNaPBxr9YlLV; _nk_=%5Cu9000%5Cu4E86%5Cu4F11%5Cu7684%5Cu6D77%5Cu76D7; uss=\\\"\\\"; chanelStat=\\\"NA==\\\"; chanelStatExpire=\\\"2019-03-28 19:23:51\\\"; UM_distinctid=169b497e1de7a-0a5e2f527a0b1e-5b402d18-e1000-169b497e1df656; cna=sL4EFXvq0BYCAd7UVxW1cZHy; uc1=cookie16=V32FPkk%2FxXMk5UvIbNtImtMfJQ%3D%3D&cookie21=Vq8l%2BKCLjA%2Bl&cookie15=UIHiLt3xD8xYTw%3D%3D&existShop=false&pas=0&cookie14=UoTZ50xbqRnsaw%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByEndrOoOJ6UgjqQ%3D&id2=UNaPBxr9YlLV&nk2=rq7bqHDDomg1hc2i&lg2=V32FPkk%2Fw0dUvg%3D%3D; csg=0ca965e8; skt=d477ab13a906a883; JSESSIONID=D5B9DF8EF813FDD005DA62287F690A24; x5sec=7b226873703b32223a223231346562653366383161616535363261313138366337373663333538336539434d4c3434755146454d6a416c6444476e667a6668514561437a4d324f4463794d7a497a4e447378227d; CNZZDATA1253581663=1669381930-1553507778-%7C1553509687; l=bBxbf6pHvAgR904FBOCNquIJfUQtKIRVguPRwhkJi_5d5_L_MubOlOn-gEp6Vj5P9CYB41JZdowTreFQ-yJf.; isg=BFlZfZcCYVAbBT3EUeiN8SB3aEXzTl4uwIqLYnsOkwD-gnsUwzbVaeyUhAZROuXQ");
		int i = 1;
		while(true) {
			String url =URL_FORM+(i++);
			BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(url,null, headers,null);
			if(buildByUrl.isSuccess()) {
				Document data = buildByUrl.getData();
				Elements elementsByClass = data.getElementsByClass("J_LazyZoom");
				int size = elementsByClass.size();
				if(size>0) {
					
				}
				if(size<50) {
					break;
				}
				logger.info(url+",over");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					
				}
			}
		}
	}
	
	static String abc = "0123456789qwertyuiopasdfghjklzxcvbnm";
	
	static final String CITY_URL = "https://hotel.alitrip.com/ajax/CitySuggest.do?_ksTS=1545232449294_5164&t=0&_input_charset=utf-8&q=";
	public void runCity() {
		int i = 0 ;
		for(;i<abc.length();i++) {
			char a = abc.charAt(i);
			String sul = CITY_URL+a;
			BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(sul, null, null,null);
			if(buildByUrl.isSuccess()) {
				Element body = buildByUrl.getData().body();
				String text = body.text();
				JSONObject jsonObject = (JSONObject)JSONObject.parse(text);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				buildCityAndInsert(jsonArray);
				//JSONArray jsonArrayNew = jsonObject.getJSONArray("resultNew");
				//buildCityAndInsert(jsonArrayNew);
			}
		}
		
	}
	private void buildCityAndInsert(JSONArray jsonArray) {
		if(jsonArray.isEmpty()) {
			return;
		}
		jsonArray.forEach(obj->{
			JSONObject json = (JSONObject)obj;
			FliggyCityDO javaObject = JSONObject.toJavaObject(json, FliggyCityDO.class);
			javaObject.setMark(json.toJSONString());
			try {
				fliggyCityService.insertSelective(javaObject);
			}catch (DuplicateKeyException e) {
				
			}
			
		});
	}
	static String AREA_URL = "http://www.ip33.com/area_code.html";
	public void runArea() {
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(AREA_URL, null, null,null);
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			Elements elementsByClass = data.getElementsByClass("ip");
			int initLevel = 4;
			elementsByClass.forEach(ele->{
				buildHtag(initLevel, ele,null);
			});
		}
	}
	private void buildHtag(int initLevel, Element ele,AreaDO parent) {
		Elements elementsByTag = ele.getElementsByTag("h"+initLevel);
		AreaDO thisArea = null;
		if(!elementsByTag.isEmpty()) {
			Element element = elementsByTag.get(0);
			thisArea = buildArea(initLevel, parent, element);
			try {
				areaService.insertSelective(thisArea);
			}catch (DuplicateKeyException e) {
				logger.info("exist,{}",thisArea);
			}
			
		}
		Elements elementsByTag3 = ele.getElementsByTag("ul");
		if(elementsByTag3.isEmpty()) {
			return;
		}
		Elements elementsByTag2 = elementsByTag3.get(0).getElementsByTag("li");
		if(elementsByTag2.isEmpty()) {
			return;
		}
		final AreaDO areaTemp = thisArea;
		logger.info(areaTemp.getLongCode());
		elementsByTag2.forEach(eleli->{
			AreaDO buildArea = buildArea(initLevel,areaTemp,eleli);
			try {
				areaService.insertSelective(buildArea);
			}catch (DuplicateKeyException e) {
				logger.info("exist,{}",buildArea);
			}
			buildHtag(initLevel+1, eleli, buildArea);
		});
	}
	private AreaDO buildArea(int initLevel, AreaDO parent, Element element) {
		AreaDO thisArea = new AreaDO();
		String text = element.text();
		//济南市 3701
		String[] split = text.split(" ");
		String name = split[0];
		String code = split[1];
		Long id = Long.valueOf(code);
		if(parent!=null&&parent.getId().equals(id)) {
			return parent;
		}
		thisArea.setId(id);
		thisArea.setCode(code);
		thisArea.setTitle(name);
		thisArea.setLevel(initLevel);
		if(parent!=null) {
			thisArea.setLongCode(parent.getLongCode()+"-"+code);
		}else {
			thisArea.setLongCode(code);
		}
		return thisArea;
	}
	
	static Map<String,String> headers = new HashMap<>();
	static {
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		//headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		headers.put("Referer", "https://hotel.fliggy.com");
		headers.put("Cookie", "cna=8HEnFYZpTU8CAavfYCrmql7J; t=b087c6c10b4d98736b3642691c2a2cef; _tb_token_=KJMkDhXC3fhiVnFShrq3; cookie2=19dd70cd4dba8b1a335d227b7154bafe; UM_distinctid=169d3f200b73e8-0747552ccd03e8-5b402d18-e1000-169d3f200b8441; chanelStat=\"NA==\"; chanelStatExpire=\"2019-04-03 21:31:36\"; CNZZDATA1253581663=1898299949-1554035585-https%253A%252F%252Fwww.fliggy.com%252F%7C1554040995; isg=BBAQyUIOeDMXuyQ3eiy8h0e14V6icfUk4e2yaQrh12s8RbDvs-iUsym0HU0Akqz7; l=bBPk-xDnv2L-cX0KBOCNCuI8L1_TjIRA_uPRwCmMi_5CF1Y1gSbOliUfIev6Vj5R_3Tp41JZdow9-etow; JSESSIONID=FD4843F96DE4598362CE1DCEA59BB00C");
		headers.put("DNT", "1");
		headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
	}
	
	
	public Document buildNormal(String cityCode,int currentPage) {
		int i= 0;
		BaseFullResponse<Document> buildByUrl = new BaseFullResponse<>();
		Date date = new Date();
		while(i++<6) {
//			ProxyInfo proxy = JsoupUtil.getProxy();
//			if(proxy==null||proxy.getAndIncrement()>100||proxy.getAndIncrementFailCount()>3) {
//				proxy = ProxyProviderBiz.getProxyInfoRandom();
//				JsoupUtil.setProxy(proxy);
//				JsoupUtil.setRandomUa();
//			}
			JsoupUtil.setRandomUa();
			
			String format = url.replaceAll("ppCityIdpp", cityCode)
					.replaceAll("ppCheckInpp",DateFormatUtils
							.format(DateUtil.plusDaysToDate(date, 2), "yyyy-MM-dd"))
					.replaceAll("ppCheckOutpp",DateFormatUtils
							.format(DateUtil.plusDaysToDate(date, 3), "yyyy-MM-dd"))
					.replaceAll("ppCurrentPagepp", currentPage+"")
					.replaceAll("ppIppp", "222.212.87.21")
					;//+param;
			headers.put("Referrer", "http://hotel.fliggy.com/hotel_list3.htm?city="+cityCode+"&"+System.currentTimeMillis());
			//headers.put("user-agent", JsoupUtil.getUserAgent());
			logger.info("buildNormal,url:{}",format);
			buildByUrl = JsoupUtil.buildByUrl(format
					, null, headers, null );
			if(!buildByUrl.isSuccess()) {
				logger.error("JsoupUtil.buildByUrl fail.url:{}/n,res:{}",url,buildByUrl);
				continue;
			}else{
				break;
			}
		}
		return buildByUrl.getData();
		
	}
	public List<FliggyHotelDO> buildFliggyHotelList(String url){
		logger.info(url);
		List<FliggyHotelDO> fliggyHotelDOs = new ArrayList<>();
		Document doc = null;//buildNormal(url);
		if(doc==null) {
			return fliggyHotelDOs;
		}
		Elements listRow = doc.getElementsByClass("J_ListRow");
		if(listRow.isEmpty()) {
			logger.info(doc.toString());
			return fliggyHotelDOs;
		}
		
		Element cityEle = CommonUtil.getElementsByClassFirst(doc, "city-input");
		String city = cityEle.val();
		String cityCode = cityEle.nextElementSibling().attr("value");
			
		
		listRow.forEach(eleListRow->{
			String attr = eleListRow.attr("data-shid");
			Long hotelId = Long.valueOf(attr);
			FliggyHotelDO hotel = new FliggyHotelDO();
			hotel.setId(hotelId);
			hotel.setHtmlContent(eleListRow.html());
			hotel.setCity(city);
			hotel.setCityCode(cityCode);
			try {
				String title = eleListRow.attr("data-name");
				hotel.setTitle(title);
				Elements elementsByClass = eleListRow.getElementsByClass("credit-icon");
				hotel.setCreditIcon(elementsByClass.isEmpty()?0:1);
				
				
				Element elementsByClassFirst = CommonUtil.getElementsByClassFirst(eleListRow, "box-price");
				if(elementsByClassFirst!=null) {
					Element priceLg = CommonUtil.getElementsByClassFirst(eleListRow, "pi-price-lg");
					if(priceLg!=null) {
						hotel.setPriceLg(priceLg.text());
					}else {
						Element noPriceEle = CommonUtil.getElementsByClassFirst(eleListRow, "no-price");
						if(noPriceEle!=null) {
							hotel.setPriceLg(noPriceEle.text());
						}
					}
				}
				Element commentScore = CommonUtil.getElementsByClassFirst(eleListRow, "comment-score");
				Element valueEle = CommonUtil.getElementsByClassFirst(commentScore, "value");
				Element estimateEle = CommonUtil.getElementsByClassFirst(commentScore, "estimate");
				hotel.setScoreValue(valueEle.text());
				hotel.setScoreEstimate(estimateEle.text());
				Element commentVolumeEle = CommonUtil.getElementsByClassFirst(commentScore, "volume");
				hotel.setCommentVolume(commentVolumeEle.text());
				Element h5 = CommonUtil.getElementsByClassFirst(eleListRow, "row-title");
				Element element = h5.getElementsByTag("a").get(0);
				String attr2 = element.attr("href");
				hotel.setOriginalUrl(attr2);
				Element groupStarIcons = CommonUtil.getElementsByClassFirst(h5, "groupStarIcons");
				if(groupStarIcons!=null) {
					groupStarIcons.classNames().forEach(clas->{
						if(!clas.equals("groupStarIcons")&&clas.startsWith("star-")) {
							hotel.setStarInfo(clas);
							return;
						}
					});
				}
				Element areaEle = CommonUtil.getElementsByClassFirst(eleListRow, "area");
				if(areaEle!=null) {
					JSONArray jsonArray = new JSONArray(); 
					areaEle.getElementsByTag("a").forEach(area->{
						jsonArray.add(area.text());
					});
					if(!jsonArray.isEmpty()) {
						hotel.setBusinessArea(jsonArray.toJSONString());
					}
				}
				Element addressEle = CommonUtil.getElementsByClassFirst(eleListRow, "address");
				if(addressEle!=null) {
					String text = addressEle.text();
					hotel.setAddress(text);
				}
			}catch (Exception e) {
				logger.error("document to hotel exception.",e);
			}
			fliggyHotelDOs.add(hotel);
		});
		return fliggyHotelDOs;
	}
	
	static String url = "https://hotel.fliggy.com/ajax/hotelList.htm?pageSize=50"
			+ "&city=ppCityIdpp&tid=null"
			+ "&market=0&previousChannel=&u=null&detailLinkCity=ppCityIdpp"
			+ "&checkIn=ppCheckInpp"
			+ "&checkOut=ppCheckOutpp"
			+ "&currentPage=ppCurrentPagepp"
			+"&userClientIp=ppIppp"
			+"&brands=";//品牌
	static String param = "&userSessionId=x0aFFGIWTUwCAd7UjEKEB%20Bon&offset=0&keywords=null"
			+ "&priceRange=R0&dangcis=&services=&order=DEFAULT&dir=DESC"
			+ "&tagids=null&searchPoiName=null&pByRadiusLng=-10000&pByRadiusLat=-10000&radius=-1&pByRectMinLat=-1"
			+ "&pByRectMinLng=-1&pByRectMaxLat=-1&pByRectMaxLng=-1&lowPrice=-1&highPrice=-1&filterByKezhan=false&searchBy="
			+ "&searchByTb=false&businessAreaId=-9999&skipKeywords=false&district=-9999&backCash=false&shids=null"
			+ "&activity=null&filterDoubleEleven=false&filterByRoomTickets=false&filterHxk=false&filterCxk=false"
			+ "&filterByRoomTicketsAndNeedLogin=false&filterByRoomTicketsAndNeedBuyTicket=false&activityCode=null"
			+ "&searchId=null&userId=null&hotelTypes=null&filterByB2g=false&filterByAgreement=false&bizNo=null&bizType=null"
			+ "&region=0&newYearSpeOffer=false&laterPay=true&sellerId=null&sellerIds=null&isMemberPrice=false"
			+ "&isLaterPayActivity=false&isFilterByTeHui=false&keyWordsType=&userUniqTag=null&iniSearchKW=false"
			+ "&poiNameFilter=&isFreeCancel=false&isInstantConfirm=false&activityCodes="
			+ "&adultChildrenCondition=%26roomNum%3D1%26aNum_1%3D2%26cNum_1%3D0&poisearch=false&overseaMarket=false&roomNum=1"
			+ "&notFilterActivityCodeShotel=false"
			+ "&previousPage=1&nextPage=2&aNum_1=2&cNum_1=0&cAge_1_1=0&cAge_1_2=0&cAge_1_3=0&_input_charset=utf-8"
			+ "&laterPaySwitch&isNotFilterActivityCodeShotel&_ksTS="+System.currentTimeMillis()+"_4168"
			+"&client=11.20.100.247";
	public static void main(String[] args) {
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl("https://hotel.fliggy.com/ajax/hotelList.htm?pageSize=50&city=420100&currentPage=1000", null, null, null);
		int indexOf = buildByUrl.getData().body().text().indexOf("\"hotelList\":[]");
		System.out.println(indexOf);
	}
}
