package com.fcang.spider.hotel.domain.pojo;
import com.fcang.spider.hotel.domain.base.BaseDomain;


/**
 * 
 * 
 * @author hewenfeng
 * @date 2019-03-24 13:26:18
 **/
public class MeituanHotelDetailDO  extends BaseDomain  {

	/****/
	private String detailHtmlInfo;

	/****/
	private String imgJsonInfo;

	/****/
	private java.util.Date createTime;

	/****/
	private java.util.Date updateTime;

	/**init**/
	private String markStatus;



	/****/
	public void setDetailHtmlInfo(String detailHtmlInfo){
		this.detailHtmlInfo = detailHtmlInfo;
	}

	/****/
	public String getDetailHtmlInfo(){
		return this.detailHtmlInfo;
	}

	/****/
	public void setImgJsonInfo(String imgJsonInfo){
		this.imgJsonInfo = imgJsonInfo;
	}

	/****/
	public String getImgJsonInfo(){
		return this.imgJsonInfo;
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

	/**init**/
	public void setMarkStatus(String markStatus){
		this.markStatus = markStatus;
	}

	/**init**/
	public String getMarkStatus(){
		return this.markStatus;
	}

	@Override
	public String toString(){
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
}
