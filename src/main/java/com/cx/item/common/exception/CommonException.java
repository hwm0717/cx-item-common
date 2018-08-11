package com.cx.item.common.exception;


import com.cx.item.common.constants.ResultCode;

/**
 * 通用异常基类
 */
public class CommonException extends RuntimeException {
    private Integer status;

    private String msg;

    public CommonException() {
        super();
    }

    public CommonException(Integer status) {
        this.status = status;
    }

    public CommonException(String msg) {
        super(msg);
        this.status = ResultCode.FAILURE.status();
        this.msg = msg;
    }

    public CommonException(Integer status, String msg) {
        super(msg);
        this.status = status;
        this.msg = msg;
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(Integer status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public CommonException(Integer status, String msg, Throwable cause) {
        super(msg, cause);
        this.status = status;
        this.msg = msg;
    }

    public CommonException(ResultCode resultCode) {
        super(resultCode.message());
        this.status = resultCode.status();
    }

    public CommonException(ResultCode resultCode, Throwable cause) {
        super(resultCode.message(), cause);
        this.status = resultCode.status();
        this.msg = resultCode.message();
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

}
