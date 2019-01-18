package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2019-01-28 14:12:52
 **/
public class MeituanHotelInfoDO  extends BaseDomain  {

	/****/
	private String nameCh;

	/****/
	private String originalUrl;

	/**搜索列表页号**/
	private Integer originalPage;

	/**行政区**/
	private String region;

	/****/
	private String city;

	/****/
	private Integer cityId;

	/**省份**/
	private String province;

	/****/
	private String htladdress;

	/**江湾、五角场商业区**/
	private String zonenames;

	/**很好**/
	private String hotelLevel;

	/**4.7**/
	private String avgScore;

	/**源自232位住客点评**/
	private String judgement;

	/**评论次数**/
	private Integer commentCount;

	/**价格xx起**/
	private String priceLow;

	/****/
	private String brand;

	/**品牌ID**/
	private Integer brandId;

	/**排序规则a-z**/
	private String hotelRes;

	/**["2012年装修", "2012年开业"],**/
	private String extInfo;

	/**联系方式**/
	private String contact;

	/**经济型**/
	private String starDesc;

	/****/
	private String star;

	/**备注 INIT_SUCCESS初始化成功**/
	private String mark;

	/****/
	private java.util.Date createTime;

	/****/
	private java.util.Date updateTime;

	/****/
	private Long cityHotelAssemId;



	/****/
	public void setNameCh(String nameCh){
		this.nameCh = nameCh;
	}

	/****/
	public String getNameCh(){
		return this.nameCh;
	}

	/****/
	public void setOriginalUrl(String originalUrl){
		this.originalUrl = originalUrl;
	}

	/****/
	public String getOriginalUrl(){
		return this.originalUrl;
	}

	/**搜索列表页号**/
	public void setOriginalPage(Integer originalPage){
		this.originalPage = originalPage;
	}

	/**搜索列表页号**/
	public Integer getOriginalPage(){
		return this.originalPage;
	}

	/**行政区**/
	public void setRegion(String region){
		this.region = region;
	}

	/**行政区**/
	public String getRegion(){
		return this.region;
	}

	/****/
	public void setCity(String city){
		this.city = city;
	}

	/****/
	public String getCity(){
		return this.city;
	}

	/****/
	public void setCityId(Integer cityId){
		this.cityId = cityId;
	}

	/****/
	public Integer getCityId(){
		return this.cityId;
	}

	/**省份**/
	public void setProvince(String province){
		this.province = province;
	}

	/**省份**/
	public String getProvince(){
		return this.province;
	}

	/****/
	public void setHtladdress(String htladdress){
		this.htladdress = htladdress;
	}

	/****/
	public String getHtladdress(){
		return this.htladdress;
	}

	/**江湾、五角场商业区**/
	public void setZonenames(String zonenames){
		this.zonenames = zonenames;
	}

	/**江湾、五角场商业区**/
	public String getZonenames(){
		return this.zonenames;
	}

	/**很好**/
	public void setHotelLevel(String hotelLevel){
		this.hotelLevel = hotelLevel;
	}

	/**很好**/
	public String getHotelLevel(){
		return this.hotelLevel;
	}

	/**4.7**/
	public void setAvgScore(String avgScore){
		this.avgScore = avgScore;
	}

	/**4.7**/
	public String getAvgScore(){
		return this.avgScore;
	}

	/**源自232位住客点评**/
	public void setJudgement(String judgement){
		this.judgement = judgement;
	}

	/**源自232位住客点评**/
	public String getJudgement(){
		return this.judgement;
	}

	/**评论次数**/
	public void setCommentCount(Integer commentCount){
		this.commentCount = commentCount;
	}

	/**评论次数**/
	public Integer getCommentCount(){
		return this.commentCount;
	}

	/**价格xx起**/
	public void setPriceLow(String priceLow){
		this.priceLow = priceLow;
	}

	/**价格xx起**/
	public String getPriceLow(){
		return this.priceLow;
	}

	/****/
	public void setBrand(String brand){
		this.brand = brand;
	}

	/****/
	public String getBrand(){
		return this.brand;
	}

	/**品牌ID**/
	public void setBrandId(Integer brandId){
		this.brandId = brandId;
	}

	/**品牌ID**/
	public Integer getBrandId(){
		return this.brandId;
	}

	/**排序规则a-z**/
	public void setHotelRes(String hotelRes){
		this.hotelRes = hotelRes;
	}

	/**排序规则a-z**/
	public String getHotelRes(){
		return this.hotelRes;
	}

	/**["2012年装修", "2012年开业"],**/
	public void setExtInfo(String extInfo){
		this.extInfo = extInfo;
	}

	/**["2012年装修", "2012年开业"],**/
	public String getExtInfo(){
		return this.extInfo;
	}

	/**联系方式**/
	public void setContact(String contact){
		this.contact = contact;
	}

	/**联系方式**/
	public String getContact(){
		return this.contact;
	}

	/**经济型**/
	public void setStarDesc(String starDesc){
		this.starDesc = starDesc;
	}

	/**经济型**/
	public String getStarDesc(){
		return this.starDesc;
	}

	/****/
	public void setStar(String star){
		this.star = star;
	}

	/****/
	public String getStar(){
		return this.star;
	}

	/**备注 INIT_SUCCESS初始化成功**/
	public void setMark(String mark){
		this.mark = mark;
	}

	/**备注 INIT_SUCCESS初始化成功**/
	public String getMark(){
		return this.mark;
	}

	/****/
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	/****/
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/****/
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	/****/
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/****/
	public void setCityHotelAssemId(Long cityHotelAssemId){
		this.cityHotelAssemId = cityHotelAssemId;
	}

	/****/
	public Long getCityHotelAssemId(){
		return this.cityHotelAssemId;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
