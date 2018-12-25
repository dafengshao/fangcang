package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-19 23:28:48
 **/
public class FliggyCityDO  extends BaseDomain  {

	/****/
	private Integer region;

	/****/
	private String cityCode;

	/****/
	private String displayName;

	/****/
	private String suggestName;

	/****/
	private Integer level;

	/****/
	private String levelDisplayName;



	/****/
	public void setRegion(Integer region){
		this.region = region;
	}

	/****/
	public Integer getRegion(){
		return this.region;
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
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	/****/
	public String getDisplayName(){
		return this.displayName;
	}

	/****/
	public void setSuggestName(String suggestName){
		this.suggestName = suggestName;
	}

	/****/
	public String getSuggestName(){
		return this.suggestName;
	}

	/****/
	public void setLevel(Integer level){
		this.level = level;
	}

	/****/
	public Integer getLevel(){
		return this.level;
	}

	/****/
	public void setLevelDisplayName(String levelDisplayName){
		this.levelDisplayName = levelDisplayName;
	}

	/****/
	public String getLevelDisplayName(){
		return this.levelDisplayName;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
