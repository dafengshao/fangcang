package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-18 17:18:29
 **/
public class CtripCityDO  extends BaseDomain  {

	private static final long serialVersionUID = 7960746959974607621L;

	/****/
	private String city;

	/****/
	private String code;

	/****/
	private String province;
	

	private Integer type;

	/****/
	public void setCity(String city){
		this.city = city;
	}

	/****/
	public String getCity(){
		return this.city;
	}

	/****/
	public void setCode(String code){
		this.code = code;
	}

	/****/
	public String getCode(){
		return this.code;
	}

	/****/
	public void setProvince(String province){
		this.province = province;
	}

	/****/
	public String getProvince(){
		return this.province;
	}
	/**1携程 、2飞猪*/
	public Integer getType() {
		return type;
	}
	/**1携程 、2飞猪*/
	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
