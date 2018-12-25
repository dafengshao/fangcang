package com.fcang.spider.hotel.domain.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseServiceImpl<T, K extends Serializable> implements BaseService<T, K> {
	@Autowired
	SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 获取DAO操作类
	 */
	public abstract BaseDao<T, K> getDao();
	@Override
	public int insertSelective(T t) {
		return getDao().insertSelective(t);
	}
	@Override
	public int deleteByPrimaryKey(K key) {
		return getDao().deleteByPrimaryKey(key);
	}
	@Override
	public int deleteByArrayKey(K... keys) {
		return getDao().deleteByArrayKey(keys);
	}
	@Override
	public int deleteByCondtion(T condition) {
		return getDao().deleteByCondtion(condition);
	}
	@Override
	public int updateByPrimaryKeySelective(T condition) {
		return getDao().updateByPrimaryKeySelective(condition);
	}

	@Override
	public T selectByPrimaryKey(K key) {
		return getDao().selectByPrimaryKey(key);
	}
	@Override
	public List<T> selectEntryArray(K... key) {
		return getDao().selectEntryArray(key);
	}
	@Override
	public List<T> selectEntryList(T condition) {
		return getDao().selectEntryList(condition);
	}
	@Override
	public Integer selectEntryListCount(T condition) {
		return getDao().selectEntryListCount(condition);
	}


	
	

}
