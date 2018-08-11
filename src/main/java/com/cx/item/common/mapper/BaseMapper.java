package com.cx.item.common.mapper;

import java.util.List;

/**
 * Created by hwm on 2018/5/22.
 */
public interface BaseMapper<M, E> {

    List<M> getPageList(M record);

    int deleteByPrimaryKey(Integer id);

    int insert(M record);

    int insertSelective(M record);

    List<M> selectByExample(E example);

    M selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(M record);

    int updateByPrimaryKey(M record);
}