package com.cx.item.common.vo;

import com.cx.item.common.constants.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 通用响应VO
 */
 @ApiModel(value = "ResultPageVo", description = "页面通用响应VO")
public class ResultPageVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码 1:成功 0:失败，其他根据实际情况自定义在ResultCode中，同时每个接口的特殊返回码需要注释。", dataType = "int", required = true)
    private Integer status;

    @ApiModelProperty(value = "消息提示", dataType = "string")
    private String message = "";

    @ApiModelProperty(value = "响应数据", dataType = "object")
    private T data;

    @ApiModelProperty(value = "响应数据", dataType = "object")
    private Object spareData;

    private ResultPageVo(Integer status) {
        this.status = status;
    }

    private ResultPageVo(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private ResultPageVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResultPageVo(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResultPageVo<Void> success() {
        return new ResultPageVo<Void>(ResultCode.SUCCESS.status());
    }

    public static <T> ResultPageVo<T> success(T data) {
        return new ResultPageVo<T>(ResultCode.SUCCESS.status(), data);
    }

    public static <T> ResultPageVo<T> success(String message, T data) {
        return new ResultPageVo<T>(ResultCode.SUCCESS.status(), message, data);
    }

    public static ResultPageVo<Void> failure() {
        return new ResultPageVo<Void>(ResultCode.FAILURE.status());
    }

    public static ResultPageVo<Void> failure(String message) {
        return new ResultPageVo<Void>(ResultCode.FAILURE.status(), message);
    }

    public static <T> ResultPageVo<T> failure(T data) {
        return new ResultPageVo<T>(ResultCode.FAILURE.status(), data);
    }

    public static <T> ResultPageVo<T> failure(String message, T data) {
        return new ResultPageVo<T>(ResultCode.FAILURE.status(), message, data);
    }

    public static ResultPageVo<Void> create(ResultCode resultCode) {
        return new ResultPageVo<Void>(resultCode.status(), resultCode.message());
    }

    public static <T> ResultPageVo<T> create(ResultCode resultCode, T data) {
        return new ResultPageVo<T>(resultCode.status(), resultCode.message(), data);
    }

    public static ResultPageVo<Void> create(Integer status) {
        return new ResultPageVo<Void>(status);
    }

    public static ResultPageVo<Void> create(Integer status, String message) {
        return new ResultPageVo<Void>(status, message);
    }

    public static <T> ResultPageVo<T> create(Integer status, T data) {
        return new ResultPageVo<T>(status, data);
    }

    public static <T> ResultPageVo<T> create(Integer status, String message, T data) {
        return new ResultPageVo<T>(status, message, data);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Object getSpareData() {
        return spareData;
    }

    public void setSpareData(Object spareData) {
        this.spareData = spareData;
    }

    @Override
    public String toString() {
        return "ResultPageVo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", spareData=" + spareData +
                '}';
    }
}
