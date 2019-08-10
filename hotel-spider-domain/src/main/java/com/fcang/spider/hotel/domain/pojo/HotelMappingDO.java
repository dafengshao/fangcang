package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2019-06-07 16:17:35
 **/
public class HotelMappingDO  extends BaseDomain  {

	/****/
	private String ctripId;

	/****/
	private String meituanId;

	/****/
	private String agodaId;

	/****/
	private String alitripId;

	/****/
	private String bookingId;

	/****/
	private String dianpingId;

	/****/
	private String elongId;

	/****/
	private String qunarId;

	/****/
	private String tongchengId;

	/****/
	private String tuniuId;

	/****/
	private String hotelMappingUrl;

	/****/
	private String mark;



	/****/
	public void setCtripId(String ctripId){
		this.ctripId = ctripId;
	}

	/****/
	public String getCtripId(){
		return this.ctripId;
	}

	/****/
	public void setMeituanId(String meituanId){
		this.meituanId = meituanId;
	}

	/****/
	public String getMeituanId(){
		return this.meituanId;
	}

	/****/
	public void setAgodaId(String agodaId){
		this.agodaId = agodaId;
	}

	/****/
	public String getAgodaId(){
		return this.agodaId;
	}

	/****/
	public void setAlitripId(String alitripId){
		this.alitripId = alitripId;
	}

	/****/
	public String getAlitripId(){
		return this.alitripId;
	}

	/****/
	public void setBookingId(String bookingId){
		this.bookingId = bookingId;
	}

	/****/
	public String getBookingId(){
		return this.bookingId;
	}

	/****/
	public void setDianpingId(String dianpingId){
		this.dianpingId = dianpingId;
	}

	/****/
	public String getDianpingId(){
		return this.dianpingId;
	}

	/****/
	public void setElongId(String elongId){
		this.elongId = elongId;
	}

	/****/
	public String getElongId(){
		return this.elongId;
	}

	/****/
	public void setQunarId(String qunarId){
		this.qunarId = qunarId;
	}

	/****/
	public String getQunarId(){
		return this.qunarId;
	}

	/****/
	public void setTongchengId(String tongchengId){
		this.tongchengId = tongchengId;
	}

	/****/
	public String getTongchengId(){
		return this.tongchengId;
	}

	/****/
	public void setTuniuId(String tuniuId){
		this.tuniuId = tuniuId;
	}

	/****/
	public String getTuniuId(){
		return this.tuniuId;
	}

	/****/
	public void setHotelMappingUrl(String hotelMappingUrl){
		this.hotelMappingUrl = hotelMappingUrl;
	}

	/****/
	public String getHotelMappingUrl(){
		return this.hotelMappingUrl;
	}

	/****/
	public void setMark(String mark){
		this.mark = mark;
	}

	/****/
	public String getMark(){
		return this.mark;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
