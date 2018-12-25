package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-20 09:43:53
 **/
public class AreaDO  extends BaseDomain  {

	/****/
	private String title;

	/****/
	private String code;

	/****/
	private Integer level;

	/****/
	private String suggestName;

	/****/
	private String longCode;

	/****/
	private String statusCode;

	/****/
	private String mark;



	/****/
	public void setTitle(String title){
		this.title = title;
	}

	/****/
	public String getTitle(){
		return this.title;
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
	public void setLevel(Integer level){
		this.level = level;
	}

	/****/
	public Integer getLevel(){
		return this.level;
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
	public void setLongCode(String longCode){
		this.longCode = longCode;
	}

	/****/
	public String getLongCode(){
		return this.longCode;
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
