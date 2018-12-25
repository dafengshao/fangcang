package com.fcang.spider.hotel.provider.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.JoupUtil;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;

public class CtripHotelHtmlUtil {
	static final Logger  logger = LoggerFactory.getLogger(CtripHotelHtmlUtil.class);
	CtripHotelHtmlUtil(){}
	//"http://hotels.ctrip.com/hotel/wuhan477/p2"
	public static List<CtripHotelInfoDO> buildHotellist(String url) {
		BaseFullResponse<Document> buildByUrl = JoupUtil.buildByUrl(url,null, null,null);
		List<CtripHotelInfoDO> ctripHotelInfoDOs = new ArrayList<>();
		if(buildByUrl.isSuccess()) {
			
			Document data = buildByUrl.getData();
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
				Element h2 = elementLi.getElementsByTag("h2").get(0);
				String hostId = h2.attr("data-id");
				Element a = h2.getElementsByTag("a").get(0);
				Elements elementsByClass = a.getElementsByClass("label_selection");
				String hostName = a.attr("title");
				String hostUrl = a.attr("data-href");
				ctripHotelInfoDO.setOriginalId(hostId);
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
				//logger.info("ctripHotelInfoDO:{}",ctripHotelInfoDO);
				ctripHotelInfoDOs.add(ctripHotelInfoDO);
			});
		}else {
			logger.error("");
		}
		return ctripHotelInfoDOs;
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
			jsonArray.add(iele.attr("title"));
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
			jsonArray.add(aele.text());
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
				jsonArray.add(span.attr("class"));
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
