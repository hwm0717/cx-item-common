package com.cx.item.common.par;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class PageParam implements Serializable{

    @ApiModelProperty(value = "当前页", dataType = "int")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页的数量", dataType = "int")
    private Integer pageSize = 10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
