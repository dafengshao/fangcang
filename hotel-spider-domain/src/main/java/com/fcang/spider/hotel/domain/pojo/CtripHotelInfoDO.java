package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-18 15:01:38
 **/
public class CtripHotelInfoDO  extends BaseDomain  {

	private static final long serialVersionUID = -7596471432485338517L;

	/****/
	private String nameCh;


	/****/
	private String originalUrl;

	/**搜索列表页号**/
	private Integer originalPage;

	/****/
	private String region;
	private String city;

	/**是否精选 1是；0否**/
	private Integer selection;

	/**hotel_strategymedal携程战略合作酒店，拥有优质服务、优良品质及优惠房价，供携程会员专享预订;ico_quality_gold:确认订单更快速，入住过程更顺利，携程服务品质认证。**/
	private String hotelIco;

	/****/
	private String htladdress;

	/**江湾、五角场商业区**/
	private String zonenames;

	/**浪漫情侣**/
	private String specialLabel;

	/**公共区域免费WIFI**/
	private String iconList;

	/**很好**/
	private String hotelLevel;

	/**4.7**/
	private String hotelValue;

	/**98%用户推荐**/
	private String totalJudgementScore;

	/**源自232位住客点评**/
	private String judgement;

	/**“性价比高”“服务周到”**/
	private String recommend;

	/****/
	private String priceLow;

	/**1可礼品卡支付 0不可**/
	private Integer giftcardAvailable;

	/****/
	private Long cityId;

	/****/
	private String cityCode;

	/**省份**/
	private String province;

	/****/
	private String hotelRes;
	private String brand;

	private String mark;

	private String  extInfo;
	
	private String  contact;
	private String  star;
	private String  starDesc;
	
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

	/****/
	public void setCity(String city){
		this.city = city;
	}

	/****/
	public String getCity(){
		return this.city;
	}

	/**是否精选 1是；0否**/
	public void setSelection(Integer selection){
		this.selection = selection;
	}

	/**是否精选 1是；0否**/
	public Integer getSelection(){
		return this.selection;
	}

	/**hotel_strategymedal携程战略合作酒店，拥有优质服务、优良品质及优惠房价，供携程会员专享预订;ico_quality_gold:确认订单更快速，入住过程更顺利，携程服务品质认证。**/
	public void setHotelIco(String hotelIco){
		this.hotelIco = hotelIco;
	}

	/**hotel_strategymedal携程战略合作酒店，拥有优质服务、优良品质及优惠房价，供携程会员专享预订;ico_quality_gold:确认订单更快速，入住过程更顺利，携程服务品质认证。**/
	public String getHotelIco(){
		return this.hotelIco;
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

	/**浪漫情侣**/
	public void setSpecialLabel(String specialLabel){
		this.specialLabel = specialLabel;
	}

	/**浪漫情侣**/
	public String getSpecialLabel(){
		return this.specialLabel;
	}

	/**公共区域免费WIFI**/
	public void setIconList(String iconList){
		this.iconList = iconList;
	}

	/**公共区域免费WIFI**/
	public String getIconList(){
		return this.iconList;
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
	public void setHotelValue(String hotelValue){
		this.hotelValue = hotelValue;
	}

	/**4.7**/
	public String getHotelValue(){
		return this.hotelValue;
	}

	/**98%用户推荐**/
	public void setTotalJudgementScore(String totalJudgementScore){
		this.totalJudgementScore = totalJudgementScore;
	}

	/**98%用户推荐**/
	public String getTotalJudgementScore(){
		return this.totalJudgementScore;
	}

	/**源自232位住客点评**/
	public void setJudgement(String judgement){
		this.judgement = judgement;
	}

	/**源自232位住客点评**/
	public String getJudgement(){
		return this.judgement;
	}

	/**“性价比高”“服务周到”**/
	public void setRecommend(String recommend){
		this.recommend = recommend;
	}

	/**“性价比高”“服务周到”**/
	public String getRecommend(){
		return this.recommend;
	}

	/****/
	public void setPriceLow(String priceLow){
		this.priceLow = priceLow;
	}

	/****/
	public String getPriceLow(){
		return this.priceLow;
	}

	/**1可礼品卡支付 0不可**/
	public void setGiftcardAvailable(Integer giftcardAvailable){
		this.giftcardAvailable = giftcardAvailable;
	}

	/**1可礼品卡支付 0不可**/
	public Integer getGiftcardAvailable(){
		return this.giftcardAvailable;
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

	/**省份**/
	public void setProvince(String province){
		this.province = province;
	}

	/**省份**/
	public String getProvince(){
		return this.province;
	}

	/****/
	public void setHotelRes(String hotelRes){
		this.hotelRes = hotelRes;
	}

	/****/
	public String getHotelRes(){
		return this.hotelRes;
	}


	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	/**
	 * 开业时间 联系方式
	 * @return
	 */
	public String getExtInfo() {
		return extInfo;
	}
	/**
	 * 开业时间
	 * @return
	 */
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	/** 联系方式*/
	public String getContact() {
		return contact;
	}
	/** 联系方式*/
	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getStarDesc() {
		return starDesc;
	}

	public void setStarDesc(String starDesc) {
		this.starDesc = starDesc;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
}
