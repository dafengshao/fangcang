package com.fcang.spider.hotel.domain.base;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 领域模型基类(常规公共字段)<br/>
 * 一律使用引用类型
 * @author 
 * @since 2014-03-01
 */
@Data
public class BaseDomain implements Serializable{
	
	private static final long serialVersionUID = -2671530029171920798L;
	protected Long id;
	protected Date createTime;
	protected Date updateTime;
	protected Integer infoStatus;
	protected String mark;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);
	}
	
	
}
