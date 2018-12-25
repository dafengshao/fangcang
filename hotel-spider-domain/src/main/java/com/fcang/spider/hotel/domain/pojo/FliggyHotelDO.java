package com.fcang.spider.hotel.domain.pojo;
import com.alibaba.fastjson.JSONObject;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-24 18:52:17
 **/
public class FliggyHotelDO  extends BaseDomain  {

	/****/
	private String title;

	/****/
	private Long cityId;

	/****/
	private String cityCode;

	/****/
	private String city;

	/**是否信用住1是；0否**/
	private Integer creditIcon;

	/****/
	private String originalUrl;

	/**商业区**/
	private String businessArea;

	/****/
	private String address;

	/****/
	private String star;

	/**星级**/
	private String starInfo;

	/**单位分**/
	private Integer price;

	/**价格json对象**/
	private String priceInfo;

	/****/
	private String priceLg;

	/**非常好**/
	private String scoreEstimate;

	/** 用户评价2095条**/
	private String commentVolume;

	/**4.7分**/
	private String scoreValue;

	/****/
	private String statusCode;

	/****/
	private String htmlContent;

	/****/
	private String mark;
	
	private String brand;

	/****/
	private java.util.Date updateTime;

	/**基础结果集ID**/
	private Long hotelAssemId;



	/****/
	public void setTitle(String title){
		this.title = title;
	}

	/****/
	public String getTitle(){
		return this.title;
	}

	/****/
	public void setCityId(Long cityId){
		this.cityId = cityId;
	}

	/****/
	public Long getCityId(){
		return this.cityId;
	}

	/****/
	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
	}

	/****/
	public String getCityCode(){
		return this.cityCode;
	}

	/****/
	public void setCity(String city){
		this.city = city;
	}

	/****/
	public String getCity(){
		return this.city;
	}

	/**是否信用住1是；0否**/
	public void setCreditIcon(Integer creditIcon){
		this.creditIcon = creditIcon;
	}

	/**是否信用住1是；0否**/
	public Integer getCreditIcon(){
		return this.creditIcon;
	}

	/****/
	public void setOriginalUrl(String originalUrl){
		this.originalUrl = originalUrl;
	}

	/****/
	public String getOriginalUrl(){
		return this.originalUrl;
	}

	/**商业区**/
	public void setBusinessArea(String businessArea){
		this.businessArea = businessArea;
	}

	/**商业区**/
	public String getBusinessArea(){
		return this.businessArea;
	}

	/****/
	public void setAddress(String address){
		this.address = address;
	}

	/****/
	public String getAddress(){
		return this.address;
	}

	/****/
	public void setStar(String star){
		this.star = star;
	}

	/****/
	public String getStar(){
		return this.star;
	}

	/**星级**/
	public void setStarInfo(String starInfo){
		this.starInfo = starInfo;
	}

	/**星级**/
	public String getStarInfo(){
		return this.starInfo;
	}

	/**单位分**/
	public void setPrice(Integer price){
		this.price = price;
	}

	/**单位分**/
	public Integer getPrice(){
		return this.price;
	}

	/**价格json对象**/
	public void setPriceInfo(String priceInfo){
		this.priceInfo = priceInfo;
	}

	/**价格json对象**/
	public String getPriceInfo(){
		return this.priceInfo;
	}

	/****/
	public void setPriceLg(String priceLg){
		this.priceLg = priceLg;
	}

	/****/
	public String getPriceLg(){
		return this.priceLg;
	}

	/**非常好**/
	public void setScoreEstimate(String scoreEstimate){
		this.scoreEstimate = scoreEstimate;
	}

	/**非常好**/
	public String getScoreEstimate(){
		return this.scoreEstimate;
	}

	/** 用户评价2095条**/
	public void setCommentVolume(String commentVolume){
		this.commentVolume = commentVolume;
	}

	/** 用户评价2095条**/
	public String getCommentVolume(){
		return this.commentVolume;
	}

	/**4.7分**/
	public void setScoreValue(String scoreValue){
		this.scoreValue = scoreValue;
	}

	/**4.7分**/
	public String getScoreValue(){
		return this.scoreValue;
	}

	/****/
	public void setStatusCode(String statusCode){
		this.statusCode = statusCode;
	}

	/****/
	public String getStatusCode(){
		return this.statusCode;
	}

	/****/
	public void setHtmlContent(String htmlContent){
		this.htmlContent = htmlContent;
	}

	/****/
	public String getHtmlContent(){
		return this.htmlContent;
	}

	/****/
	public void setMark(String mark){
		this.mark = mark;
	}

	/****/
	public String getMark(){
		return this.mark;
	}

	/****/
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	/****/
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**基础结果集ID**/
	public void setHotelAssemId(Long hotelAssemId){
		this.hotelAssemId = hotelAssemId;
	}

	/**基础结果集ID**/
	public Long getHotelAssemId(){
		return this.hotelAssemId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
	
	public static FliggyHotelDO toFliggyHotelDO(JSONObject json) {
		FliggyHotelDO hotel = new FliggyHotelDO();
		//酒店ID
		hotel.setId(json.getLong("shid"));
		//中文名称
		hotel.setTitle(json.getString("name"));
		//地址
		hotel.setAddress(json.getString("address"));
		//商务区
		hotel.setBusinessArea(json.getString("businessAreas"));
		//信用住
		hotel.setCreditIcon(json.getBooleanValue("laterPay")?1:0);
		//城市
		hotel.setCity(json.getString("cityName"));
		//评论次数
		hotel.setCommentVolume(json.getString("rateNum"));
		//json信息
		hotel.setHtmlContent(json.toJSONString());
		//价格描述
		hotel.setPriceLg(json.getString("priceDesp"));
		//价格json
		hotel.setPriceInfo(json.getString("priceWithoutTax"));
		//具体价格，单位分
		hotel.setPrice(json.getInteger("price"));
		//酒店评分
		hotel.setScoreValue(json.getString("rateScore"));
		//酒店评价描述
		hotel.setScoreEstimate(json.getString("rateScoreDesc"));
		//品牌json，可以参照
		hotel.setBrand(json.getString("inRightMapHotelTitle"));
		//星级
		JSONObject levelJsonObject = json.getJSONObject("level");
		hotel.setStarInfo(levelJsonObject.toJSONString());
		hotel.setStar(levelJsonObject.getString("star"));
		
		return hotel;
	}
}
