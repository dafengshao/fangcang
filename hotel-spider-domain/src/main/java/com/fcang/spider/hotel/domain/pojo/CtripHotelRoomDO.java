package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-18 09:47:59
 **/
public class CtripHotelRoomDO  extends BaseDomain  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7565699457210793254L;

	/****/
	private String hotelOriginalId;

	/****/
	private String originalId;

	/****/
	private String title;

	/**所有房型设施**/
	private String hrdAllfac;

	/**建筑面积、楼层**/
	private String hrdInfoBase;


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

	/**所有房型设施**/
	public void setHrdAllfac(String hrdAllfac){
		this.hrdAllfac = hrdAllfac;
	}

	/**所有房型设施**/
	public String getHrdAllfac(){
		return this.hrdAllfac;
	}

	/**建筑面积、楼层**/
	public void setHrdInfoBase(String hrdInfoBase){
		this.hrdInfoBase = hrdInfoBase;
	}

	/**建筑面积、楼层**/
	public String getHrdInfoBase(){
		return this.hrdInfoBase;
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
