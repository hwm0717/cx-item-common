package com.cx.item.common.service.impl;


import cn.hutool.core.text.StrFormatter;
import com.cx.item.common.mapper.BaseMapper;
import com.cx.item.common.par.PageParam;
import com.cx.item.common.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * @param <M> model对象
 * @param <E> example对象
 * @param <D> dao对象
 */
public abstract class BaseServiceImpl<M, E, D> implements BaseService<M, E> {

    private static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    protected BaseMapper baseMapper;

    /**
     * 设置base对象
     *
     * @param dao
     */
    @Resource
    public final void setMapper(final D dao) {
        this.baseMapper = (BaseMapper) dao;
    }

    @Override
    public PageInfo<M> getPageList(PageParam pageParam, M record) {

        log.debug(StrFormatter.format("{}{}{}", this.getClass().getName(), pageParam.toString(), record.toString()));

        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<M> pageList = baseMapper.getPageList(record);

        return new PageInfo(pageList);
    }

    @Override
    public PageInfo<M> getPageList(int pageNum, int pageSize, M record) {

        log.debug(StrFormatter.format("{}(pageNum={}, pageSize={}){}", this.getClass().getName(), pageNum, pageSize, record.toString()));

        PageHelper.startPage(pageNum, pageSize);
        List<M> pageList = baseMapper.getPageList(record);

        return new PageInfo(pageList);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        log.debug(StrFormatter.format("{}(id={})", this.getClass().getName(), id));
        return baseMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(M record) {
        log.debug(this.getClass().getName() + record.toString());
        baseMapper.insert(record);
        return 0;
    }

    @Override
    public int insertSelective(M record) {
        log.debug(this.getClass().getName() + record.toString());
        baseMapper.insertSelective(record);
        return 0;
    }

    @Override
    public List<M> selectByExample(E example) {
        log.debug(this.getClass().getName() + example.toString());
        return baseMapper.selectByExample(example);
    }

    @Override
    public M selectByPrimaryKey(Integer id) {
        log.debug(StrFormatter.format("{}(id={})", this.getClass().getName(), id));
        return (M) baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(M record) {
        log.debug(this.getClass().getName() + record.toString());
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(M record) {
        log.debug(this.getClass().getName() + record.toString());
        baseMapper.updateByPrimaryKey(record);
        return 0;
    }
}
