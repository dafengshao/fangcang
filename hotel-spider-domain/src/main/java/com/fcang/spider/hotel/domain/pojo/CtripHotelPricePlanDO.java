package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-18 09:47:59
 **/
public class CtripHotelPricePlanDO  extends BaseDomain  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6291802966798749817L;

	/****/
	private String roomOriginalId;

	/****/
	private String hotelOriginalId;

	/****/
	private String originalId;

	/****/
	private String title;

	/****/
	private String bedType;

	/****/
	private String breakfast;

	/****/
	private String broadband;

	/****/
	private String colPerson;

	/****/
	private String colPolicy;

	/****/
	private String price;

	/****/
	private java.util.Date checkDate;


	/****/
	public void setRoomOriginalId(String roomOriginalId){
		this.roomOriginalId = roomOriginalId;
	}

	/****/
	public String getRoomOriginalId(){
		return this.roomOriginalId;
	}

	/****/
	public void setHotelOriginalId(String hotelOriginalId){
		this.hotelOriginalId = hotelOriginalId;
	}

	/****/
	public String getHotelOriginalId(){
		return this.hotelOriginalId;
	}

	/****/
	public void setOriginalId(String originalId){
		this.originalId = originalId;
	}

	/****/
	public String getOriginalId(){
		return this.originalId;
	}

	/****/
	public void setTitle(String title){
		this.title = title;
	}

	/****/
	public String getTitle(){
		return this.title;
	}

	/****/
	public void setBedType(String bedType){
		this.bedType = bedType;
	}

	/****/
	public String getBedType(){
		return this.bedType;
	}

	/****/
	public void setBreakfast(String breakfast){
		this.breakfast = breakfast;
	}

	/****/
	public String getBreakfast(){
		return this.breakfast;
	}

	/****/
	public void setBroadband(String broadband){
		this.broadband = broadband;
	}

	/****/
	public String getBroadband(){
		return this.broadband;
	}

	/****/
	public void setColPerson(String colPerson){
		this.colPerson = colPerson;
	}

	/****/
	public String getColPerson(){
		return this.colPerson;
	}

	/****/
	public void setColPolicy(String colPolicy){
		this.colPolicy = colPolicy;
	}

	/****/
	public String getColPolicy(){
		return this.colPolicy;
	}

	/****/
	public void setPrice(String price){
		this.price = price;
	}

	/****/
	public String getPrice(){
		return this.price;
	}

	/****/
	public void setCheckDate(java.util.Date checkDate){
		this.checkDate = checkDate;
	}

	/****/
	public java.util.Date getCheckDate(){
		return this.checkDate;
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
