package com.fcang.spider.hotel.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;

public class CtripHotelHtmlUtil {
	static final Logger  logger = LoggerFactory.getLogger(CtripHotelHtmlUtil.class);
	CtripHotelHtmlUtil(){}
	//"http://hotels.ctrip.com/hotel/wuhan477/p2"
	public static BaseFullResponse<List<CtripHotelInfoDO>> buildHotellist(String url) {
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(url,null, null,null);
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			List<CtripHotelInfoDO> ctripHotelInfoDOs =	paseHtmlDocument( data);
			return BaseFullResponse.success(ctripHotelInfoDOs);
		}else {
			logger.error("");
		}
		return BaseFullResponse.failed("");
	}
	
	public static BaseFullResponse<List<CtripHotelInfoDO>> buildLocalHotellist(String url) {
		Map<String, String> headers = new HashMap();
		headers.put("url", url);
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl("http://127.0.0.1:8020",null , headers,null);
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			List<CtripHotelInfoDO> ctripHotelInfoDOs =	paseHtmlDocument( data);
			return BaseFullResponse.success(ctripHotelInfoDOs);
		}else {
			logger.error("");
		}
		return BaseFullResponse.failed("");
	}
	public static BaseFullResponse<List<CtripHotelInfoDO>> buildHotellist(File file) {
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByFile(file,"utf-8");
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			List<CtripHotelInfoDO> ctripHotelInfoDOs =	paseHtmlDocument( data);
			return BaseFullResponse.success(ctripHotelInfoDOs);
		}else {
			logger.error("");
		}
		return BaseFullResponse.failed("");
	}
	private static List<CtripHotelInfoDO> paseHtmlDocument(Document data) {
		List<CtripHotelInfoDO> ctripHotelInfoDOs = new ArrayList<>();
		Element textNoresult = data.getElementById("textNoresult");
		if(textNoresult!=null) {
			return ctripHotelInfoDOs;
		}
		Element elementById = data.getElementById("hotel_list");
		//酒店列表
		Elements jhotelListBaseCell = elementById.getElementsByClass("J_HotelListBaseCell");
		jhotelListBaseCell.forEach(cellEle->{
			CtripHotelInfoDO ctripHotelInfoDO = new CtripHotelInfoDO();
			Element elementLi = cellEle.getElementsByClass("hotel_item_name").get(0);
			buildHotelFromLi(cellEle, ctripHotelInfoDO, elementLi);
			//logger.info("ctripHotelInfoDO:{}",ctripHotelInfoDO);
			ctripHotelInfoDOs.add(ctripHotelInfoDO);
		});
		return ctripHotelInfoDOs;
	}
	public static void buildHotelFromLi(Element cellEle, CtripHotelInfoDO ctripHotelInfoDO, Element elementLi) {
		Element h2 = elementLi.getElementsByTag("h2").get(0);
		String hostId = h2.attr("data-id");
		Element a = h2.getElementsByTag("a").get(0);
		Elements elementsByClass = a.getElementsByClass("label_selection");
		String hostName = a.attr("title");
		String hostUrl = a.attr("data-href");
		ctripHotelInfoDO.setId(Long.valueOf(hostId));
		ctripHotelInfoDO.setOriginalUrl(hostUrl);
		ctripHotelInfoDO.setNameCh(hostName);
		ctripHotelInfoDO.setSelection(elementsByClass.isEmpty()?0:1);
		JSONArray buildHotelIco = buildHotelIco(elementLi);
		if(!buildHotelIco.isEmpty()) {
			ctripHotelInfoDO.setHotelIco(buildHotelIco.toJSONString());
		}
		buildHotelAddress(ctripHotelInfoDO,elementLi);
		buildHotelMedalList(ctripHotelInfoDO,elementLi);
		buildHotelIconList(ctripHotelInfoDO,elementLi);
		buildHotelItemJudge(ctripHotelInfoDO,cellEle);
		//hotel_price_icon
		buildHotelPriceIcon(ctripHotelInfoDO,cellEle);
	}

	private static void buildHotelPriceIcon(CtripHotelInfoDO ctripHotelInfoDO, Element cellEle) {
		Element elementsByClassFirst = getElementsByClassFirst(cellEle,"hotel_price_icon");
		if(elementsByClassFirst==null) {
			return;
		}
		
		elementsByClassFirst = getElementsByClassFirst(elementsByClassFirst,"hotel_price");
		if(elementsByClassFirst!=null) {
			ctripHotelInfoDO.setPriceLow(elementsByClassFirst.text());
		}
		//可礼品卡支付
		elementsByClassFirst = getElementsByClassFirst(cellEle,"giftcard_available");
		if(elementsByClassFirst!=null) {
			ctripHotelInfoDO.setGiftcardAvailable(1);
		}else {
			ctripHotelInfoDO.setGiftcardAvailable(0);
		}
		
	}

	private static void buildHotelItemJudge(CtripHotelInfoDO ctripHotelInfoDO, Element cellEle) {
		Element elementsByClassFirst = getElementsByClassFirst(cellEle,"hotel_item_judge");
		if(elementsByClassFirst==null) {
			return;
		}
		elementsByClassFirst = getElementsByClassFirst(elementsByClassFirst,"hotel_judge");
		if(elementsByClassFirst==null) {
			return;
		}
		Elements children = elementsByClassFirst.children();
		if(!children.isEmpty()) {
			children.forEach(span -> {
				boolean hasLevel = span.hasClass("hotel_level");
				boolean hasValue = span.hasClass("hotel_value");
				boolean hasJudgement = span.hasClass("hotel_judgement");
				boolean hasRecommend = span.hasClass("recommend");
				String text = span.text();
				if (hasLevel) {
					ctripHotelInfoDO.setHotelLevel(text);
				}
				if (hasValue) {
					ctripHotelInfoDO.setHotelValue(text);
				}
				if (hasJudgement) {
					ctripHotelInfoDO.setJudgement(text);
				}
				if (hasRecommend) {
					ctripHotelInfoDO.setRecommend(span.html());
				}
			});
		}
	}

	private static void buildHotelIconList(CtripHotelInfoDO ctripHotelInfoDO,Element elementLi) {
		JSONArray jsonArray = new JSONArray();
		Elements elementsByClass = elementLi.getElementsByClass("icon_list");
		if(elementsByClass.isEmpty()) {
			return ;
		}
		Element element = elementsByClass.get(0);
		Elements elementsByTag = element.getElementsByTag("i");
		elementsByTag.forEach(iele->{
			if(!StringUtils.isEmpty(iele.attr("title"))) {
				jsonArray.add(iele.attr("title"));
			}
		});
		if(jsonArray.isEmpty()) {
			return;
		}
		ctripHotelInfoDO.setIconList(jsonArray.toJSONString());
	}

	private static void buildHotelMedalList(CtripHotelInfoDO ctripHotelInfoDO, Element elementLi) {
		Elements elementsByClass = elementLi.getElementsByClass("medal_list");
		if(elementsByClass.isEmpty()) {
			return;
		}
		Elements specialLabels = elementsByClass.get(0).getElementsByClass("special_label");
		if(specialLabels.isEmpty()) {
			return;
		}
		JSONArray jsonArray = new JSONArray();	
		Elements elementsByTag = specialLabels.get(0).getElementsByTag("i");
		elementsByTag.forEach(iele->{
			jsonArray.add(iele.text());
		});
		if(!jsonArray.isEmpty()) {
			ctripHotelInfoDO.setSpecialLabel(jsonArray.toJSONString());
		}
	}

	private static void buildHotelAddress(CtripHotelInfoDO ctripHotelInfoDO, Element elementLi) {
		Element element = elementLi.getElementsByClass("hotel_item_htladdress").get(0);
		String text = element.text();
		JSONArray jsonArray = new JSONArray();	
		element.getElementsByTag("a").forEach(aele->{
			String tex = aele.text();
			if(!StringUtils.isEmpty(tex)&&!"地图".equals(tex)&&!"街景".equals(tex)) {
				jsonArray.add(tex);
			}
			
		});
		ctripHotelInfoDO.setHtladdress(text);
		if(!jsonArray.isEmpty()) {
			ctripHotelInfoDO.setZonenames(jsonArray.toJSONString());
		}
	}

	private static JSONArray buildHotelIco(Element elementLi) {
		Elements elementsByClass = elementLi.getElementsByClass("hotel_ico");
		JSONArray jsonArray = new JSONArray();
		if(!elementsByClass.isEmpty()) {
			Element element = elementsByClass.get(0);
			element.getElementsByTag("span").forEach(span->{
				
				String cl = span.attr("class");
				if(!StringUtils.isEmpty(cl)&&!"hotel_ico".equals(cl)) {
				jsonArray.add(cl);
				}
			});
			
		}
		return jsonArray;
	}
	
	public static Element getElementsByClassFirst(Element elementLi,String className) {
		Elements elementsByClass = elementLi.getElementsByClass(className);
		if(elementsByClass.isEmpty()) {
			return null;
		}
		return elementsByClass.get(0);
	}
	
}
