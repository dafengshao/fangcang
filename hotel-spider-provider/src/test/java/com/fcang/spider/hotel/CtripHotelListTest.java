package com.fcang.spider.hotel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.CtripHotelHtmlUtil;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.core.JsoupUtil;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;

public class CtripHotelListTest {//extends BaseTest{

	
	static String url = "http://hotels.ctrip.com/Domestic/Tool/AjaxHotelList.aspx";
	@SuppressWarnings("unchecked")
	public static BaseFullResponse<JSONArray> ajaxCityHotelList(Integer page,Long cityId) {
		Map<String, String> herders = new HashMap<>();
		Map<String, String> parm = new HashMap<>();
		Date start = DateUtil.plusDaysToDate(new Date(),5);
		Date end = DateUtil.plusDaysToDate(new Date(),6);
		String startDateStr = DateFormatUtils.format(start,"yyyy-MM-dd");
		String endDateStr = DateFormatUtils.format(end,"yyyy-MM-dd");
		parm.put("StartTime", startDateStr);
		parm.put("DepTime", endDateStr);
		parm.put("cityId", cityId+"");
		parm.put("page", page+"");
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(url, parm, herders , null);
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			String html = data.body().html();
			
			String hotelPositionJSON = html.substring(html.indexOf("hotelPositionJSON"), html.indexOf("biRecord"));
			hotelPositionJSON = hotelPositionJSON.substring(hotelPositionJSON.indexOf(":[")+1, hotelPositionJSON.indexOf("],\"")+1);
			//JSONArray jsonArray =JSONObject.parseArray(hotelPositionJSON);
			String hotelListHtml = html.substring(html.indexOf("hotelList"), html.indexOf("paging")-1);
			String substring = hotelListHtml.substring(hotelListHtml.length()-1000,hotelListHtml.length());
			hotelListHtml = hotelListHtml.substring(hotelListHtml.indexOf(":\"")+2);
			hotelListHtml = hotelListHtml.replaceAll("&quot;", "").replaceAll("\\\\", "");
			Document parse = Jsoup.parse(hotelListHtml);
			Elements elementsByClass = parse.getElementsByClass("hotel_item");
			elementsByClass.forEach(eleLi->{
				CtripHotelInfoDO ctripHotelInfoDO = new CtripHotelInfoDO();
				CtripHotelHtmlUtil.buildHotelFromLi(eleLi.parent(), ctripHotelInfoDO, eleLi);
				System.out.println(ctripHotelInfoDO);
			});
			return BaseFullResponse.success(null);
		}else {
			return BaseFullResponse.failed(buildByUrl.getCode(),buildByUrl.getMessage());
		}
	}
	
	public static void main(String[] args) {
		BaseFullResponse<JSONArray> ajaxCityHotelList = ajaxCityHotelList(22,477L);
	}
}
