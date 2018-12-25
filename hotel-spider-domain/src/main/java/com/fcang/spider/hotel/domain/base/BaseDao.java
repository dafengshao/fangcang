package com.fcang.spider.hotel.domain.base;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T,K extends Serializable>{
   

    /**
     * 添加对象并设置ID到对象上(需开启事务)
     * @param t
     * @return 影响条数
     */
    int insertSelective(T t);

    /**
     * 删除对象,主键
     * @param key
     * @return 影响条数
     */
    int deleteByPrimaryKey(K key);

    /**
     * 删除对象,条件
     * @param condtion
     * @return 影响条数
     */
    int deleteByCondtion(T condtion);

    /**
     * 删除对象,条件
     * @param t
     * @return 影响条数
     */
    int deleteByArrayKey(K...k);


    /**
     * 更新对象,条件主键ID
     * @param t
     * @return 影响条数
     */
    int updateByPrimaryKeySelective(T t);

    /**
     * 查询对象,条件主键
     * @param key
     * @return
     */
    T selectByPrimaryKey(K key);

    /**
     * 查询对象,条件主键数组
     * @param key
     * @return
     */
    List<T> selectEntryArray(K...key);

    /**
     * 查询对象,只要不为NULL与空则为条件
     * @param t
     * @return
     */
    List<T> selectEntryList(T t);

    /**
     * 查询对象总数
     * @param t
     * @return
     */
    Integer selectEntryListCount(T t);


}
