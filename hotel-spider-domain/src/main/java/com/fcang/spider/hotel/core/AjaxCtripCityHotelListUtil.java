package com.fcang.spider.hotel.core;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;

public class AjaxCtripCityHotelListUtil {
	static Logger logger = LoggerFactory.getLogger(AjaxCtripCityHotelListUtil.class);
	static String url = "http://hotels.ctrip.com/Domestic/Tool/AjaxHotelList.aspx";
	static Map<String, String> herders = new HashMap<>();
	static {
		herders.put("Referer", "http://hotels.ctrip.com/hotel/wuhan477#ctm_ref=hod_hp_sb_lst");
		herders.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		herders.put("Cookie", "_RGUID=02cc4dff-888c-4764-b2fd-e5a30e2e8df6; _RSG=aT4Z8.VJCB0steBECQvzBB; _RDG=286d8cf82a7c492d412281051417c943d1; _abtest_userid=f9073d77-5f27-4323-af04-81476e64eddc; _ga=GA1.2.776580256.1547047482; _gid=GA1.2.763056674.1547047482; MKT_Pagesource=PC; gad_city=be2e953e1ae09d16d9cc90a550611388; HotelCityID=477split%E6%AD%A6%E6%B1%89splitWuhansplit2019-01-10split2019-01-11split0; appFloatCnt=29; manualclose=1; _RF1=125.69.43.13; HotelDomesticVisitedHotels1=0=0,,0,0,,&6547475=0,0,4.5,377,/200d0v000000k1xs4D77E.jpg,&858509=0,0,4.3,1059,/20010n000000eoq87D00A.jpg,&8446971=0,0,4.3,417,/20070k000000crhezF4F0.jpg,&444966=0,0,4.5,5287,/20020i0000009gkue3ACF.jpg,&7104977=0,0,4.8,1488,/20060e00000075r4nD8FC.jpg,; ASP.NET_SessionId=ehl4ffnsqaltceovld4lisya; _bfa=1.1547047479551.2nb3gg.1.1547123773076.1547138029942.7.55; _bfs=1.4; OID_ForOnlineHotel=15470474795512nb3gg1547138540857102032; _gat=1; MKT_OrderClick=ASID=&CT=1547138543257&CURL=http%3A%2F%2Fhotels.ctrip.com%2Fhotel%2Fwuhan477%2Fp205&VAL={\"pc_vid\":\"1547047479551.2nb3gg\"}; _jzqco=%7C%7C%7C%7C1547138032478%7C1.1082763304.1547047482301.1547138045437.1547138543293.1547138045437.1547138543293.undefined.0.0.42.42; __zpspc=9.7.1547138032.1547138543.3%234%7C%7C%7C%7C%7C%23; _bfi=p1%3D102032%26p2%3D0%26v1%3D55%26v2%3D0");
	}
	public static BaseFullResponse<Collection<CtripHotelInfoDO>> ajaxCityHotelList(Integer page,Long cityId,String cityCode) {
<<<<<<< HEAD
		Date start = DateUtil.plusDaysToDate(new Date(),1);
		Date end = DateUtil.plusDaysToDate(new Date(),2);
=======
		Date start = DateUtil.plusDaysToDate(new Date(),6);
		Date end = DateUtil.plusDaysToDate(new Date(),7);
>>>>>>> branch 'master' of https://github.com/dafengshao/fangcang.git
		String startDateStr = DateFormatUtils.format(start,"yyyy-MM-dd");
		String endDateStr = DateFormatUtils.format(end,"yyyy-MM-dd");
		Map<String, String> parm = new HashMap<>();
		parm.put("StartTime", startDateStr);
		parm.put("DepTime", endDateStr);
		parm.put("checkIn", startDateStr);
		parm.put("checkOut", endDateStr);
		parm.put("cityId", cityId+"");
		parm.put("page", page+"");
		parm.put("cityPY", cityCode.toLowerCase());
		parm.put("IsOnlyAirHotel", "F");
		parm.put("requestTravelMoney", "F");
		parm.put("isusergiftcard", "F");
		parm.put("useFG", "F");
		parm.put("promotion", "F");
		parm.put("prepay", "F");
		parm.put("IsCanReserve", "F");
		parm.put("isfromlist", "T");
		parm.put("OrderBy", "99");
		parm.put("hasPKGHotel", "F");
		parm.put("priceRange", "-2");
		parm.put("htlPageView", "0");
		parm.put("markType", "0");
		parm.put("sid", "0");
		parm.put("a", "0");
		parm.put("contyped", "0");
		parm.put("contrast", "0");
		parm.put("allianceid", "0");
		parm.put("isHuaZhu", "False");
		parm.put("ubt_price_key", "htl_search_noresult_promotion");
		parm.put("hidTestLat", "0%7C0");
		parm.put("traceAdContextId", "v2_H4sIAAAAAAAAAD3Mu20CQRCAYchcg0N0kcVIs%2FPcIaSR0z5uY3pw7oBC6MItuB5Oh7n0%2B6X%2F4%2B9x%2F%2F2Gz5%2Bju4rM7dZmo4jMNqeLuJ9XV9xcmTXLzhnzxiKZdGdOyTYOthTJ345qsXkiQ1ai%2F6BM8dpLqHvil58OX1PrZRUcUEZbQDoS1MEO5rFUWZR7OB4vk43GpXCBpXUCqSJQLTqMhlYZjTK36%2BEJHduHE%2BoAAAA%3D");
		BaseFullResponse<Document> buildByUrl = JsoupUtil.buildByUrl(url, parm, herders , null);
		if(buildByUrl.isSuccess()) {
			Document data = buildByUrl.getData();
			String html = data.body().html();
			String hotelAmountStr = html.substring(html.indexOf("hotelAmount"), html.indexOf("sortHeader"));
			hotelAmountStr = hotelAmountStr.substring(hotelAmountStr.indexOf(":")+1, hotelAmountStr.indexOf(",\""));
			Integer hotelAmount = Integer.valueOf(hotelAmountStr);
			String hotelPositionJSON = html.substring(html.indexOf("hotelPositionJSON"), html.indexOf("biRecord"));
			hotelPositionJSON = hotelPositionJSON.substring(hotelPositionJSON.indexOf(":[")+1, hotelPositionJSON.indexOf("],\"")+1);
			hotelPositionJSON = hotelPositionJSON.replaceAll("\\\\", "|");
			logger.info("city:{},page:{},returnJSON:{}",cityId,page,hotelPositionJSON);
			JSONArray jsonArray =JSONObject.parseArray(hotelPositionJSON);
			String hotelListHtml = html.substring(html.indexOf("hotelList"), html.indexOf("paging")-1);
			hotelListHtml = hotelListHtml.substring(hotelListHtml.indexOf(":\"")+2);
			hotelListHtml = hotelListHtml.replaceAll("&quot;", "").replaceAll("\\\\", "");
			Document parse = Jsoup.parse(hotelListHtml);
			Elements elementsByClass = parse.getElementsByClass("hotel_item");
			Map<Long,CtripHotelInfoDO> map = new HashMap<>();
			elementsByClass.forEach(eleLi->{
				CtripHotelInfoDO ctripHotelInfoDO = new CtripHotelInfoDO();
				CtripHotelHtmlUtil.buildHotelFromLi(eleLi.parent(), ctripHotelInfoDO, eleLi);
				map.put(ctripHotelInfoDO.getId(), ctripHotelInfoDO);
			});
			jsonArray.forEach(obj->{
				JSONObject json = (JSONObject)obj;
				CtripHotelInfoDO hotel = map.get(json.getLong("id"));
				hotel.setNameCh(json.getString("name"));
				hotel.setStar(json.getString("star"));
				hotel.setStarDesc(json.getString("stardesc"));
				hotel.setHtladdress(json.getString("address"));
			});
			BaseFullResponse<Collection<CtripHotelInfoDO>> res = BaseFullResponse.success(map.values());
			res.setMessage(hotelAmountStr);
			return res;
		}else {
			return BaseFullResponse.failed(buildByUrl.getCode(),buildByUrl.getMessage());
		}
	}
	
	public static void main(String[] args) {
		String jss = "[{\"id\":\"6599478\",\"name\":\"武汉大学三环培训公寓(武大东湖店)\",\"lat\":\"30.536536\",\"lon\":\"114.381948\",\"url\":\"/hotel/6599478.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_1\",\"img\":\"//dimg11.c-ctrip.com/images//200p0c0000006o9eaD0FB_R_300_225.jpg\",\"address\":\"武昌区八一路483号三环学生公寓内，近武汉大学、东湖风景区、八一路与卓刀泉北路交叉处。 （ 街道口/武汉大学/南湖）\",\"score\":\"4.6\",\"dpscore\":\"97\",\"dpcount\":\"890\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"三环培训\",\"isSingleRec\":\"false\"},{\"id\":\"858509\",\"name\":\"武汉高铁凯瑞国际酒店\",\"lat\":\"30.434521151516\",\"lon\":\"114.4174183803\",\"url\":\"/hotel/858509.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_2\",\"img\":\"//dimg10.c-ctrip.com/images//20010n000000eoq87D00A_R_300_225.jpg\",\"address\":\"江夏区藏龙岛特1号，近汤逊湖。 （ 江夏大学城、藏龙岛、梁子湖）\",\"score\":\"4.3\",\"dpscore\":\"93\",\"dpcount\":\"1060\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"高铁凯瑞\",\"isSingleRec\":\"false\"},{\"id\":\"3742030\",\"name\":\"全季酒店(武汉光谷体育学院店)\",\"lat\":\"30.52400442611\",\"lon\":\"114.38340592393\",\"url\":\"/hotel/3742030.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_3\",\"img\":\"//dimg12.c-ctrip.com/images//fd/hotel/g6/M03/42/46/CggYtFae9SGAHCCPAAIzGl0A-FE664_R_300_225.jpg\",\"address\":\"洪山区珞瑜路459号，近卓刀泉北路。 （ 光谷科技中心（武昌高校区））\",\"score\":\"4.7\",\"dpscore\":\"98\",\"dpcount\":\"1154\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"全季酒店\",\"isSingleRec\":\"false\"},{\"id\":\"2603292\",\"name\":\"麗枫酒店(武汉理工大学店)\",\"lat\":\"30.518367881959\",\"lon\":\"114.35720716057\",\"url\":\"/hotel/2603292.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_4\",\"img\":\"//dimg10.c-ctrip.com/images//fd/hotel/g5/M0A/CF/35/CggYr1b52X6AXZCiAAFUOxp-Yrk788_R_300_225.jpg\",\"address\":\"洪山区雄楚大道与珞狮路交汇处（南国大家装），近武汉科技会展中心。 （ 街道口/武汉大学/南湖）\",\"score\":\"4.8\",\"dpscore\":\"98\",\"dpcount\":\"3024\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"麗枫酒店\",\"isSingleRec\":\"false\"},{\"id\":\"10901854\",\"name\":\"精途酒店(武汉汉口火车站西广场店)(原汉味一品酒店)\",\"lat\":\"30.623448\",\"lon\":\"114.260693\",\"url\":\"/hotel/10901854.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_5\",\"img\":\"//dimg13.c-ctrip.com/images//200h0v000000jukoi68C4_R_300_225.jpg\",\"address\":\"江汉区汉口火车站广场创业大厦内1楼（售票厅左侧)。 （ 汉口火车站、园博园）\",\"score\":\"4.4\",\"dpscore\":\"97\",\"dpcount\":\"349\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"精途\",\"isSingleRec\":\"false\"},{\"id\":\"6547475\",\"name\":\"武汉城市便捷酒店光谷软件园店\",\"lat\":\"30.479962\",\"lon\":\"114.410931\",\"url\":\"/hotel/6547475.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_6\",\"img\":\"//dimg12.c-ctrip.com/images//200d0v000000k1xs4D77E_R_300_225.jpg\",\"address\":\"洪山区东湖新技术开发区软件园东路1号12栋西区3楼，近关山大道。 （ 光谷科技中心（武昌高校区））\",\"score\":\"4.5\",\"dpscore\":\"97\",\"dpcount\":\"377\",\"star\":\"\",\"stardesc\":\"经济型\",\"shortName\":\"城市便捷光谷软件园店\",\"isSingleRec\":\"false\"},{\"id\":\"21338171\",\"name\":\"武汉维佳佰港大酒店\",\"lat\":\"30.507477\",\"lon\":\"114.332876\",\"url\":\"/hotel/21338171.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_7\",\"img\":\"//dimg11.c-ctrip.com/images//2h050u000000j51lv42BC_R_300_225.jpg\",\"address\":\"洪山区南湖丁字桥南路529号。 （ 街道口/武汉大学/南湖）\",\"score\":\"4.4\",\"dpscore\":\"94\",\"dpcount\":\"248\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"维佳佰港\",\"isSingleRec\":\"false\"},{\"id\":\"7104977\",\"name\":\"如家驿居酒店(武汉中山大道汉正街地铁站店)\",\"lat\":\"30.578181\",\"lon\":\"114.283819\",\"url\":\"/hotel/7104977.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_8\",\"img\":\"//dimg12.c-ctrip.com/images//20060e00000075r4nD8FC_R_300_225.jpg\",\"address\":\"硚口区中山大道263号，汉正街品牌服饰大楼对面，距6号线汉正街地铁站店B\\C出口50米。 （ 沿江大道/江汉路）\",\"score\":\"4.8\",\"dpscore\":\"99\",\"dpcount\":\"1488\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"如家驿居\",\"isSingleRec\":\"false\"},{\"id\":\"16981486\",\"name\":\"麗枫酒店(武汉广埠屯地铁站店)\",\"lat\":\"30.527738\",\"lon\":\"114.370361\",\"url\":\"/hotel/16981486.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_9\",\"img\":\"//dimg13.c-ctrip.com/images//200v0t000000iouidDFF9_R_300_225.jpg\",\"address\":\"洪山区珞喻路312号，（近地铁广埠屯D出口），近中国船舶总公司第七二二研究所。 （ 街道口/武汉大学/南湖）\",\"score\":\"4.8\",\"dpscore\":\"97\",\"dpcount\":\"478\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"麗枫酒店\",\"isSingleRec\":\"false\"},{\"id\":\"1286059\",\"name\":\"武汉金贵源洲际酒店\",\"lat\":\"30.563088561766\",\"lon\":\"114.34360944628\",\"url\":\"/hotel/1286059.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_10\",\"img\":\"//dimg11.c-ctrip.com/images//t1/hotel/1287000/1286059/df771a1ba9154180961d420eba25880b_R_300_225.jpg\",\"address\":\"武昌区公正路35号，近沙湖大道。 （ 楚河汉街/东湖风景区）\",\"score\":\"4.3\",\"dpscore\":\"92\",\"dpcount\":\"1685\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"金贵源洲际\",\"isSingleRec\":\"false\"},{\"id\":\"2062928\",\"name\":\"锦江之星(武汉江汉路地铁站大洋百货店)\",\"lat\":\"30.582564807044\",\"lon\":\"114.29543652172\",\"url\":\"/hotel/2062928.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_11\",\"img\":\"//dimg10.c-ctrip.com/images//200d0b0000005mo3dA64A_R_300_225.jpg\",\"address\":\"江汉区中山大道744号工艺大楼，近地铁2号线江汉路站B出口。 （ 沿江大道/江汉路）\",\"score\":\"4.5\",\"dpscore\":\"95\",\"dpcount\":\"3042\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"锦江之星\",\"isSingleRec\":\"false\"},{\"id\":\"1316347\",\"name\":\"汉庭酒店(武汉大学店)\",\"lat\":\"30.542536299066\",\"lon\":\"114.36289975848\",\"url\":\"/hotel/1316347.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_12\",\"img\":\"//dimg10.c-ctrip.com/images//200v0a0000004ktff6CC6_R_300_225.jpg\",\"address\":\"武昌区珞狮北路99号，武大正西门旁。 （ 街道口/武汉大学/南湖）\",\"score\":\"4.2\",\"dpscore\":\"92\",\"dpcount\":\"1041\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"汉庭酒店\",\"isSingleRec\":\"false\"},{\"id\":\"25504045\",\"name\":\"锦江都城酒店(武汉光谷大道店)\",\"lat\":\"30.502543\",\"lon\":\"114.427787\",\"url\":\"/hotel/25504045.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_13\",\"img\":\"//dimg10.c-ctrip.com/images//200b0z000000mvgtxD679_R_300_225.jpg\",\"address\":\"洪山区东湖高新区光谷&nbsp;大道与雄楚大道交汇处229号光谷ONE39号，近华科大，烽火科技，光谷生物城，华科大，光谷广场 。 （ 光谷科技中心（武昌高校区））\",\"score\":\"4.6\",\"dpscore\":\"97\",\"dpcount\":\"38\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"锦江都城\",\"isSingleRec\":\"false\"},{\"id\":\"1204284\",\"name\":\"武汉恒大酒店\",\"lat\":\"30.433784\",\"lon\":\"114.646905\",\"url\":\"/hotel/1204284.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_14\",\"img\":\"//dimg13.c-ctrip.com/images//200e0g000000865co44AE_R_300_225.jpg\",\"address\":\"江夏区豹澥镇红莲湖旅游度假区恒大金碧天下，红莲湖东北岸、金碧大道南侧。 （ 江夏大学城、藏龙岛、梁子湖）\",\"score\":\"4.9\",\"dpscore\":\"99\",\"dpcount\":\"1637\",\"star\":\"hotel_diamond05\",\"stardesc\":\"豪华型\",\"shortName\":\"恒大\",\"isSingleRec\":\"false\"},{\"id\":\"5128208\",\"name\":\"锦江之星品尚(武汉光谷大道灵杰路传媒学院店)\",\"lat\":\"30.43946722506\",\"lon\":\"114.4384992946\",\"url\":\"/hotel/5128208.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_15\",\"img\":\"//dimg13.c-ctrip.com/images//fd/hotel/g5/M09/AD/D8/CggYr1coya2ANvgvAAQApMu2xXA224_R_300_225.jpg\",\"address\":\"江夏区光谷大道灵杰路8号，近藏龙岛派出所，百度大厦斜对面。 （ 江夏大学城、藏龙岛、梁子湖）\",\"score\":\"4.8\",\"dpscore\":\"98\",\"dpcount\":\"1536\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"锦江之星品尚\",\"isSingleRec\":\"false\"},{\"id\":\"3803448\",\"name\":\"锦江之星(武汉光谷大道高新二路店)\",\"lat\":\"30.483630001671\",\"lon\":\"114.43116676053\",\"url\":\"/hotel/3803448.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_16\",\"img\":\"//dimg11.c-ctrip.com/images//200g080000002y0ijA6CD_R_300_225.jpg\",\"address\":\"武昌区东湖高新区关南工业园高新二路25号，近武汉光谷国际网球中心，关南园三路公交站。 （ 光谷科技中心（武昌高校区））\",\"score\":\"4.5\",\"dpscore\":\"95\",\"dpcount\":\"2236\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"锦江之星\",\"isSingleRec\":\"false\"},{\"id\":\"6108080\",\"name\":\"锦江之星品尚(武汉国际博览中心马鹦路地铁站店)\",\"lat\":\"30.539958\",\"lon\":\"114.273849\",\"url\":\"/hotel/6108080.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_17\",\"img\":\"//dimg12.c-ctrip.com/images//20040i0000009fv9j0FD5_R_300_225.jpg\",\"address\":\"汉阳区鹦鹉大道世贸锦绣长江四期11栋滨江大道188号。 （ 钟家村/归元寺/古琴台）\",\"score\":\"4.5\",\"dpscore\":\"97\",\"dpcount\":\"588\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"锦江之星\",\"isSingleRec\":\"false\"},{\"id\":\"1618768\",\"name\":\"麗枫酒店(武汉汉口火车站大武汉1911店)(原望旺城市旅店)\",\"lat\":\"30.622504417867\",\"lon\":\"114.27115530779\",\"url\":\"/hotel/1618768.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_18\",\"img\":\"//dimg10.c-ctrip.com/images//200u0l000000d1dbr28D0_R_300_225.jpg\",\"address\":\"江汉区发展大道226号，近汉口火车站。 （ 汉口火车站、园博园）\",\"score\":\"4.7\",\"dpscore\":\"99\",\"dpcount\":\"1230\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"麗枫酒店\",\"isSingleRec\":\"false\"},{\"id\":\"1417256\",\"name\":\"乐福全套间酒店(武汉市政府江滩店)\",\"lat\":\"30.594163562574\",\"lon\":\"114.30997338008\",\"url\":\"/hotel/1417256.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_19\",\"img\":\"//dimg10.c-ctrip.com/images//200w0j000000b6zvg1F21_R_300_225.jpg\",\"address\":\"江岸区沿江大道169号，武汉市政府，汉口江滩旁。 （ 沿江大道/江汉路）\",\"score\":\"4.7\",\"dpscore\":\"95\",\"dpcount\":\"2920\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"乐福全套间\",\"isSingleRec\":\"false\"},{\"id\":\"4026004\",\"name\":\"武汉豪泰尔酒店\",\"lat\":\"30.811115\",\"lon\":\"114.222006\",\"url\":\"/hotel/4026004.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_20\",\"img\":\"//dimg10.c-ctrip.com/images//20030g0000007vluuFAEA_R_300_225.jpg\",\"address\":\"黄陂区天河镇天河道西特一号，近天河医院。 （ 天河机场、盘龙城、奥特莱斯）\",\"score\":\"4.4\",\"dpscore\":\"93\",\"dpcount\":\"873\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"豪泰尔酒店\",\"isSingleRec\":\"false\"},{\"id\":\"21122218\",\"name\":\"优居酒店(武汉古田凯德广场店)\",\"lat\":\"30.602304\",\"lon\":\"114.197904\",\"url\":\"/hotel/21122218.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_21\",\"img\":\"//dimg11.c-ctrip.com/images//200c0y000000m73a25082_R_300_225.jpg\",\"address\":\"硚口区古田二路招商江湾国际A栋。 （ 汉西/宗关/凯德广场）\",\"score\":\"4.8\",\"dpscore\":\"97\",\"dpcount\":\"351\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"优居酒店\",\"isSingleRec\":\"false\"},{\"id\":\"779230\",\"name\":\"武汉森泰中洋酒店\",\"lat\":\"30.541407434026\",\"lon\":\"114.30326949225\",\"url\":\"/hotel/779230.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_22\",\"img\":\"//dimg13.c-ctrip.com/images//hotel/88000/87413/159079c2410b4cc3836293a84aa9b7e0_R_300_225.jpg\",\"address\":\"武昌区解放路239号，近黄鹤楼。 （ 黄鹤楼/户部巷/武昌火车站）\",\"score\":\"4.3\",\"dpscore\":\"90\",\"dpcount\":\"2575\",\"star\":\"hotel_diamond03\",\"stardesc\":\"舒适型\",\"shortName\":\"森泰中洋\",\"isSingleRec\":\"false\"},{\"id\":\"5774915\",\"name\":\"轻鱼E.U酒店(武汉中南财经政法大学店)\",\"lat\":\"30.483585\",\"lon\":\"114.393311\",\"url\":\"/hotel/5774915.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_23\",\"img\":\"//dimg10.c-ctrip.com/images//200m0a0000004ytab9160_R_300_225.jpg\",\"address\":\"洪山区南湖大道116号南湖时尚城K2栋109号1层大厅，近中南财经政法大学。 （ 光谷科技中心（武昌高校区））\",\"score\":\"4.5\",\"dpscore\":\"96\",\"dpcount\":\"862\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"轻鱼酒店\",\"isSingleRec\":\"false\"},{\"id\":\"7091331\",\"name\":\"纽宾凯怡都国际酒店（武汉光谷大学园路店）\",\"lat\":\"30.460115\",\"lon\":\"114.406943\",\"url\":\"/hotel/7091331.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_24\",\"img\":\"//dimg10.c-ctrip.com/images//200d0k000000b8u784BF9_R_300_225.jpg\",\"address\":\"江夏区东湖新技术开发区大学园路16号，近华师园。 （ 江夏大学城、藏龙岛、梁子湖）\",\"score\":\"4.6\",\"dpscore\":\"97\",\"dpcount\":\"648\",\"star\":\"hotel_diamond04\",\"stardesc\":\"高档型\",\"shortName\":\"\",\"isSingleRec\":\"false\"},{\"id\":\"688236\",\"name\":\"锦江之星(武汉复兴路地铁站首义广场店)(原张之洞路店)\",\"lat\":\"30.539957\",\"lon\":\"114.310478\",\"url\":\"/hotel/688236.html?isFull=F#ctm_ref=hod_sr_map_dl_txt_25\",\"img\":\"//dimg12.c-ctrip.com/images//200j0800000036dy0F2DD_R_300_225.jpg\",\"address\":\"武昌区张之洞路147号（人民医院左侧），近复兴路站D出口。 （ 黄鹤楼/户部巷/武昌火车站）\",\"score\":\"4.5\",\"dpscore\":\"96\",\"dpcount\":\"2521\",\"star\":\"hotel_diamond02\",\"stardesc\":\"经济型\",\"shortName\":\"锦江之星\",\"isSingleRec\":\"false\"}]";
		System.out.println(JSONObject.parse(jss));
	}
	
}
