package com.fcang.spider.hotel.provider.biz;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Files;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.core.BaseFullResponse;
import com.fcang.spider.hotel.core.ConstantVar;
import com.fcang.spider.hotel.core.DateUtil;
import com.fcang.spider.hotel.core.JoupUtil;
import com.fcang.spider.hotel.core.ProxyInfo;
import com.fcang.spider.hotel.domain.pojo.CtripHotelInfoDO;
import com.fcang.spider.hotel.domain.service.CtripHotelInfoService;
import com.fcang.spider.hotel.domain.service.CtripHotelRoomService;
import com.github.pagehelper.PageHelper;

@Component
public class CtripHotelRoomBiz {
	private static final int DETAULT_AFTER = 12;
	Logger logger = LoggerFactory.getLogger(CtripHotelRoomBiz.class);
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	@Autowired	
	PhantomjsManagerBiz phantomjsManagerBiz;
	// /usr/hotel-spider/html/
	@Value("${FILE_PATH}")
	public String filePath ;
	@Value("${WIN_FILE_PATH}")
	public String winFilePath ;
	@Autowired
	Executor phantomjsAsyncExecutor;
	@Autowired
	CtripHotelInfoService ctripHotelInfoService;
	@Autowired
	CtripHotelRoomService ctripHotelRoomService;
	public void runDownHtmlFile() throws InterruptedException {
		Date date = new Date();
		CtripHotelInfoDO condtion = new CtripHotelInfoDO();
		//condtion.setCityCode("wuhan");
		condtion.setMark(ConstantVar.INIT_SUCCESS);
		//update_time越新越晚处理
		String orderby = "update_time asc,create_time desc";
		if(PhantomjsLoader.isWindows) {
			orderby = "update_time desc,create_time desc";
		}
		PageHelper.startPage(1,50,false).setOrderBy(orderby);
		List<CtripHotelInfoDO> selectEntryList = ctripHotelInfoService.selectEntryList(condtion );
		PageHelper.clearPage();
		if(CollectionUtils.isEmpty(selectEntryList)) {
			logger.info("ASSEM_SUCCESS状态，下载完成");
			return;
		}
		CountDownLatch latch = new CountDownLatch(selectEntryList.size());
		JSONArray array = new JSONArray();
		for(CtripHotelInfoDO hotelDO :selectEntryList) {
			if(array.size()==5) {
				runArray(date, latch, (JSONArray)array.clone());
				array.clear();
			}
			array.add(toJSON(hotelDO));
		}
		runArray(date, latch, (JSONArray)array.clone());
		latch.await();
	}
	private void runArray(Date date, CountDownLatch latch, JSONArray array) {
		JSONObject json = new JSONObject();
		json.put("checkin", buildDate(date,DETAULT_AFTER));
		json.put("checkout",buildDate(date,DETAULT_AFTER+1));
		if(PhantomjsLoader.isWindows) {
			json.put("filePath", winFilePath+json.getString("checkin")+"/");
		}else {
			json.put("filePath", filePath+json.getString("checkin")+"/");
		}
		json.put("list", array);
		phantomjsAsyncExecutor.execute(()->{
			runLoadHTML(getProxyInfo()	,json,latch);
		});
	}
	private String buildDate(Date date,int day) {
		return DateFormatUtils
				.format(DateUtil.plusDaysToDate(date , day), YYYY_MM_DD);
	}
	static ProxyInfo getProxyInfo() {
		return null;
	}
	static JSONObject toJSON(CtripHotelInfoDO hotelDO) {
		JSONObject json = new JSONObject();
		json.put("id", hotelDO.getId());
		return json;
	}
	
	public void runLoadHTML(ProxyInfo proxy,JSONObject json,CountDownLatch latch) {
		
		JSONArray jsonArray = json.getJSONArray("list");
		try {
			String loadHTML = phantomjsManagerBiz.loadHTML(proxy,json);
			logger.info("phantomjs return:{}",loadHTML);
			
			for(Object obj:jsonArray) {
				JSONObject idJSON = (JSONObject)obj;
				Long id = idJSON.getLong("id");
				String idstr = id.toString();
				idstr = buildSubPath(idstr);
				String path = json.getString("filePath")+idstr+"/"+id+".html";
				File file = new File(path);
				CtripHotelInfoDO condtion = new CtripHotelInfoDO();
				condtion.setId(id);
				condtion.setUpdateTime(new Date());
				if(file.exists()) {
					this.parseFileTransformHotelInfo(file);
					//condtion.setMark(ConstantVar.FILE_DOWN_SUCCESS);
					logger.info("ok,{}",path);
				}else {
					logger.info("fail,{}",path);
					ctripHotelInfoService.updateByPrimaryKeySelective(condtion);
				}
				
			}
			
			
		}catch (Exception e) {
			logger.error("",e);
		}finally {
			for(int i=0;i<jsonArray.size();i++) {
				latch.countDown();
			}
		}
	}
	private String buildSubPath(String idstr) {
		return  idstr.substring(idstr.length()-2, idstr.length());
	}
	
	//html文件解析
	//开业时间 联系电话 省份 区
	//房型
	@Deprecated
	public void runCtripParseHtmlFile() {
		
		CtripHotelInfoDO condtion = new CtripHotelInfoDO();
		condtion.setMark(ConstantVar.FILE_DOWN_SUCCESS);
		String orderby = "update_time asc,create_time desc";
		PageHelper.startPage(1, 20,false).setOrderBy(orderby);
		List<CtripHotelInfoDO> selectEntryList = ctripHotelInfoService.selectEntryList(condtion );
		if(CollectionUtils.isEmpty(selectEntryList)) {
			logger.info("FILE_DOWN_SUCCESS状态的酒店,暂无");
			return;
		}
		selectEntryList.forEach(hotel->{
			Long id = hotel.getId();
			String buildSubPath = buildSubPath(id+"");
			Date updateTime = hotel.getUpdateTime();
			String checkin = buildDate(updateTime, DETAULT_AFTER);
			String thisFilePath = PhantomjsLoader.isWindows?winFilePath:filePath;
			String currentHotelFile = thisFilePath + checkin+"/"+buildSubPath+"/"+id+".html";
			File file = new File(currentHotelFile);
			boolean exists = file.exists();
			if(!exists) {
				currentHotelFile = thisFilePath + buildDate(updateTime, DETAULT_AFTER-1)+"/"+buildSubPath+"/"+id+".html";
				file = new File(currentHotelFile);
			}
			if(!exists) {
				updateHotelMark(id, ConstantVar.INIT_SUCCESS);
				return;
			}
			parseHotelHtmlFile(file);
		});
	}
	public void runByHarddisk() {
		//"2018-12-30","2018-12-31","2019-01-02",
		String[] dates = {"2019-01-18","2019-01-21","2019-01-22"};
		String pathname = PhantomjsLoader.isWindows?winFilePath:filePath;
		for(String day :dates) {
			
			File file = new File(pathname+"/"+day);
			
			parseHotelHtmlFile(file);
		}
		
	}
	
	
	private void parseHotelHtmlFile(File file) {
		if(!file.exists()) {
			return;
		}
		logger.info("file:{}",file.getPath());
		if(file.isDirectory()) {
			
			File[] listFiles = file.listFiles();
			if(listFiles.length<=0) {
				Files.delete(file);
				return;
			}
			for(File htmlFile :listFiles) {
				parseHotelHtmlFile(htmlFile);
			}
			return;
		}
		parseFileTransformHotelInfo(file);
	}
	public void parseFileTransformHotelInfo(File file) {
		String name = file.getName();
		if(name.startsWith("_")) {
			return;
		}
		BaseFullResponse<Document> buildByFile = JoupUtil.buildByFile(file, "utf-8");
		if(!buildByFile.isSuccess()) {
			return;
		}
		Long id = Long.valueOf(file.getName().replaceAll(".html", ""));
		Document data = buildByFile.getData();
		Elements elementsByAttributeValue = data.getElementsByAttributeValue("name", "location");
		if(elementsByAttributeValue.isEmpty()) {
			Files.delete(file);
			updateHotelMark(id, ConstantVar.FILE_NONE_HOTEL_INFO);
			return;
		}
		Elements elementsByAttribute = data.getElementsByAttribute("data-hotelid");
		if(elementsByAttribute.isEmpty()) {
			Files.delete(file);
			updateHotelMark(id, ConstantVar.FILE_NONE_HOTEL_INFO);
			return;
		}else {
			String attr = elementsByAttribute.get(0).attr("data-hotelid");
			if(!attr.equals(id+"")) {
				Files.delete(file);
				updateHotelMark(id, ConstantVar.FILE_NONE_HOTEL_INFO);
				return;
			}
		}
		CtripHotelInfoDO updateHotel = new CtripHotelInfoDO();
		updateHotel.setId(id);
		String attr2 = elementsByAttributeValue.get(0).attr("content");
		String province = attr2.substring(0, attr2.indexOf(";")).replace("province=", "");
		updateHotel.setProvince(province);
		//房型
		//
		/*Elements elementsByAttribute = data.getElementsByAttribute("data-baseroominfo");
		if(elementsByAttribute.isEmpty()) {
			elementsByAttribute.forEach(ele->{
				String baseInfo = ele.attr("data-baseroominfo");
				String name = ele.attr("data-baseroomname");
				CtripHotelRoomDO ctripHotelRoomDO = new CtripHotelRoomDO();
				ctripHotelRoomDO.setHrdInfoBase(baseInfo);
				ctripHotelRoomDO.setTitle(name);
				ctripHotelRoomService.insertSelective(ctripHotelRoomDO);
			});
		}else {
			//没有房型
		}*/
		//开业时间 联系电话 省份 区
		
		Elements pathBar = data.getElementsByClass("path_bar2");
		Elements labelSelection = data.getElementsByClass("label_selection");
		//是否精选
		updateHotel.setSelection(labelSelection.isEmpty()?0:1);
		//打分
//		updateHotel.setHotelValue(hotelValue);
//		updateHotel.setJudgement(judgement);
//		updateHotel.setPriceLow(priceLow);
//		updateHotel.setRecommend(recommend);
//		updateHotel.setGiftcardAvailable(giftcardAvailable);
//		updateHotel.setHotelIco(hotelIco);
//		updateHotel.setHotelLevel(hotelLevel);
//		updateHotel.setHotelRes(hotelRes);
//		updateHotel.setSpecialLabel(specialLabel);
//		updateHotel.setZonenames(zonenames);
		if(!pathBar.isEmpty()) {
			Elements hs = pathBar.get(0).getElementsByTag("h1");
			if(hs.isEmpty()||StringUtils.isEmpty(hs.get(0).text())) {
				Files.delete(file);
				updateHotelMark(id, ConstantVar.FILE_NONE_HOTEL_INFO);
				return;
			}else {
				Elements elementsByTag = pathBar.get(0).getElementsByTag("a");
				if(!elementsByTag.isEmpty()) {
					elementsByTag.forEach(ele->{
						String attr = ele.attr("href");
						if(attr.indexOf("brand")>=0) {
							updateHotel.setBrand(ele.text());
						}
						if(attr.indexOf("location")>=0) {
							updateHotel.setRegion(ele.text());
						}
					});
				}
			}
			
		}
		Element htltags = data.getElementById("htltags");
		String extInfo = "";
		if(htltags!=null&&!StringUtils.isEmpty(htltags.text())) {
			extInfo = htltags.text()+",";
		}
		Elements elementsByClass = data.getElementsByClass("htl_room_txt");
		if(!elementsByClass.isEmpty()) {
			Element element = elementsByClass.get(0);
			Elements elementsByTag = element.getElementsByTag("p");
			if(!elementsByTag.isEmpty()) {
				Element element2 = elementsByTag.get(0);
				String text = element2.text();
				extInfo =extInfo+ text.replaceAll("资质备案", "").replaceAll("联系方式", "").replaceAll(" ", "").replaceAll("&nbsp;", "");
			}
			Element elementById = element.getElementById("J_realContact");
			if(elementById!=null) {
				String attr = elementById.attr("data-real");
				if(attr.indexOf("<a")>=0) {
					attr = attr.substring(0,attr.indexOf("<a"));
				}
				updateHotel.setContact(attr);
			}
		}
		if(!StringUtils.isEmpty(extInfo)) {
			if(extInfo.length()>200) {
				extInfo = extInfo.substring(0,200)+"...";
			}
			updateHotel.setExtInfo(extInfo);
		}
		updateHotel.setMark(ConstantVar.FILE_PARSE_SUCCESS);
		ctripHotelInfoService.updateByPrimaryKeySelective(updateHotel);
		Files.delete(file);
		logger.info("ok,{}",file.getAbsolutePath());
		logger.info("ok,updateHotel:{}",updateHotel);
//		String newFileName = file.getAbsolutePath().replace(id+"", "_"+id);
//		boolean renameTo = file.renameTo(new File(newFileName));
//		if(!renameTo) {
//			
//		}
	}
	private void updateHotelMark(Long id,String mark) {
		CtripHotelInfoDO updateHotel = new CtripHotelInfoDO();
		updateHotel.setId(id);
		updateHotel.setMark(mark);
		if(mark.equals(ConstantVar.FILE_PARSE_SUCCESS)) {
			updateHotel.setUpdateTime(new Date());
		}else if(mark.equals(ConstantVar.INIT_SUCCESS)){
			//重新下载
			updateHotel.setUpdateTime(DateUtil.plusDaysToDate(new Date() , -4));
		}
		ctripHotelInfoService.updateByPrimaryKeySelective(updateHotel);
	}
	
	
}
