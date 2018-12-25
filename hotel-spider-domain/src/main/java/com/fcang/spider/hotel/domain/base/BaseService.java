package com.fcang.spider.hotel.domain.base;

import java.io.Serializable;
import java.util.List;



public interface BaseService<T, K extends Serializable> {


    /**
     * 添加对象并且设置主键ID值(需要事务支持)
     *
     * @param t
     * @return
     */
    int insertSelective(T t);

    /**
     * 删除对象,主键
     *
     * @param key
     *            主键数组
     * @return 影响条数
     */
    int deleteByPrimaryKey(K key);
    
    int deleteByArrayKey(K ... keys);

    /**
     * 按条件删除对象
     *
     * @param condtion
     * @return 影响条数
     */
    int deleteByCondtion(T condtion);

    /**
     * 更新对象,条件主键Id
     *
     * @param condtion
     *            更新对象
     * @return 影响条数
     */
    int updateByPrimaryKeySelective(T condtion);


    /**
     * 查询对象,条件主键
     *
     * @param key
     * @return 实体对象
     */
    T selectByPrimaryKey(K key);

    /**
     * 查询对象列表,主键数组
     *
     * @param key
     * @return 对象列表
     */
    List<T> selectEntryArray(K... key);

    /**
     * 查询对象,只要不为NULL与空则为条件
     *
     * @param condtion
     *            查询条件
     * @return 对象列表
     */
    List<T> selectEntryList(T condtion);

    /**
     * 查询对象总数
     * @param condtion 查询条件
     * @return 对象总数
     */
    Integer selectEntryListCount(T condtion);

  
}
