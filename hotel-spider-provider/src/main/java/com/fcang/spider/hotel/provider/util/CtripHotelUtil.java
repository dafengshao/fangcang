package com.fcang.spider.hotel.provider.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CtripHotelUtil {
	static Logger logger = LoggerFactory.getLogger(CtripHotelUtil.class);
	/**爬虫要爬的数据*/
	public final static String hotelIdsKey = "ctrip:hotel:id";
	/**解析房型价格计划成功的携程酒店ID*/
	public final static String htmlRoomPlan_ok_hotelIdsKey = "ctrip:id:room:ok";
	
	/**需要爬取price的酒店ID*/
	public final static String ctrip_price_hotelid = "ctrip:price:hotelid";
	
	/**已经爬取price的酒店ID和日期*/
	public final static String ctrip_price_ok = "ctrip:price:ok:";
	
	public final static String ctrip_price_statistics = "ctrip:price:statistics";
	
	/**redis中酒店日期价格 状态 -1 为数据已经爬取 其他原因导致的错误  晚点再试 **/
	//public final static String price_day_stauts_init = "-1";
	/**redis中酒店日期价格 状态  0解析异常  **/
	//public final static String price_day_stauts_ex = "0";
	/**redis中酒店日期价格 状态 1入库成功  **/
	public final static String price_day_stauts_ok = "1";
	/**酒店页面无法访问*/
	public final static String price_day_stauts_ctrip_url_error = "3";
	
	/**酒店没有获取到房型或者已经订完*/
	public final static String price_day_stauts_ctrip_url_noroom = "4";
	/**redis中酒店日期价格 状态 2html文件错误 **/
	public final static String price_day_stauts_file_error = "2";
	
	public final static String preHotelCtrip = "http://hotels.ctrip.com/hotel/";
	
	
	public final static String simpleDatePattern = "yyyy-MM-dd";
	/**yyyy-MM-dd*/
	public final static DateTimeFormatter formarrter = DateTimeFormatter.ofPattern(simpleDatePattern);
	
	public final static String file_error = "file_error";
	
	
	/**例70613150832 日期为分数*/
	public static double getHotelIdScores(){
		String format2 = timeFormart();
		double score = Double.valueOf(format2);
		
		return score;
	}

	public static String timeFormart() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String format2 = format.format(date);
		format2 =format2.substring(3);
		return format2;
	}
	
	public static double getHotelIdScoresError() {
		String format2 = timeFormart();
		format2 = format2.substring(0,format2.length()-2)+"00";
		double score = Double.valueOf(format2);
		return score;
	}
	
	/**only for http://hotels.ctrip.com/hotel/396453.html*/
	public static String buildFileName(String url) {
		int htmlIndexOf = url.indexOf(".html");
		String path = url;
		if(htmlIndexOf>0){
			path = url.substring(0, htmlIndexOf);
		}
		int startInexof = path.lastIndexOf("/");
		if(startInexof>0){
			path = path.substring(startInexof+"/".length());
		}
		if(path.length()>2){
			path = path.substring(0, 2)+"/"+path;
		}else{
			path = path+"/"+path;
		}
		return path+".html";
	}
	
	public static String buildCtripHotelURL(String hotelId, String startDate, String endDate) {
		StringBuilder sb = new StringBuilder(preHotelCtrip);
		sb.append(hotelId).append(".html").append(buildCtripHotelURL(startDate, endDate));
		String url = sb.toString();
		return url;
	}
	public static String buildCtripHotelURL(String startDate, String endDate) {
		StringBuilder sb = new StringBuilder("?checkIn=")
		.append(startDate).append("&checkOut=")
		.append(endDate).append("#ctm_ref=hod_hp_hot_dl_n_1_3");
		String url = sb.toString();
		return url;
	}
	
	public static void main(String[] args) {}
	/**正整数  */
	public final static String reg = "^[0-9]*[1-9][0-9]*$";
	public static  boolean match(String text,String reg){
		return Pattern.compile(reg).matcher(text).find();
	}
	
	
}
