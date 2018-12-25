package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2018-12-23 15:47:07
 **/
public class CityHotelAssemDO  extends BaseDomain  {

	/**1飞猪； 2携程**/
	private Integer requestType;

	/****/
	private String requestUrl;

	/**0异常，1成功，负数标识失败次数**/
	private Integer reponseStatus;

	/****/
	private String cityName;

	/****/
	private Long cityId;

	/****/
	private String cityCode;

	/****/
	private String reponseContent;

	/****/
	private Integer currentPage;

	/****/
	private Integer pageSize;

	/**json备注**/
	private String mark;

	/****/
	private java.util.Date updateTime;
	
	
	private String taskBatch;



	/**1飞猪； 2携程**/
	public void setRequestType(Integer requestType){
		this.requestType = requestType;
	}

	/**1飞猪； 2携程**/
	public Integer getRequestType(){
		return this.requestType;
	}

	/****/
	public void setRequestUrl(String requestUrl){
		this.requestUrl = requestUrl;
	}

	/****/
	public String getRequestUrl(){
		return this.requestUrl;
	}

	/**0异常，1成功，负数标识失败次数**/
	public void setReponseStatus(Integer reponseStatus){
		this.reponseStatus = reponseStatus;
	}

	/**0异常，1成功，负数标识失败次数**/
	public Integer getReponseStatus(){
		return this.reponseStatus;
	}

	/****/
	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	/****/
	public String getCityName(){
		return this.cityName;
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
	public void setReponseContent(String reponseContent){
		this.reponseContent = reponseContent;
	}

	/****/
	public String getReponseContent(){
		return this.reponseContent;
	}

	/****/
	public void setCurrentPage(Integer currentPage){
		this.currentPage = currentPage;
	}

	/****/
	public Integer getCurrentPage(){
		return this.currentPage;
	}

	/****/
	public void setPageSize(Integer pageSize){
		this.pageSize = pageSize;
	}

	/****/
	public Integer getPageSize(){
		return this.pageSize;
	}

	/**json备注**/
	public void setMark(String mark){
		this.mark = mark;
	}

	/**json备注**/
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

	public String getTaskBatch() {
		return taskBatch;
	}

	public void setTaskBatch(String taskBatch) {
		this.taskBatch = taskBatch;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
