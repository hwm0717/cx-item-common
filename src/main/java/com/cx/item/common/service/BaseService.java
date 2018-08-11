package com.cx.item.common.service;

import com.cx.item.common.par.PageParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by hwm on 2018/5/22.
 */
public interface BaseService<M, E> {

    PageInfo<M> getPageList(PageParam pageParam, M record);

    PageInfo<M> getPageList(int pageNum, int pageSize, M record);

    int deleteByPrimaryKey(Integer id);

    /**
     * 获取id需要重写此方法，默认返回0
     *
     * @param record
     * @return
     */
    int insert(M record);

    /**
     * 获取id需要重写此方法，默认返回0
     *
     * @param record
     * @return
     */
    int insertSelective(M record);

    List<M> selectByExample(E example);

    M selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(M record);

    int updateByPrimaryKey(M record);
}
