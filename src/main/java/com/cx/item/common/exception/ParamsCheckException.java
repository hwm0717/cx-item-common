package com.cx.item.common.exception;


import com.cx.item.common.constants.ResultCode;

/**
 * 参数校验异常
 */
public class ParamsCheckException extends CommonException {

    public ParamsCheckException() {
        super();
    }

    public ParamsCheckException(Integer status) {
        super(status);
    }

    public ParamsCheckException(String msg) {
        super(msg);
    }

    public ParamsCheckException(Integer status, String msg) {
        super(status, msg);
    }

    public ParamsCheckException(Throwable cause) {
        super(cause);
    }

    public ParamsCheckException(Integer status, Throwable cause) {
        super(status, cause);
    }

    public ParamsCheckException(Integer status, String msg, Throwable cause) {
        super(status, msg, cause);
    }

    public ParamsCheckException(ResultCode resultCode) {
        super(resultCode);
    }

    public ParamsCheckException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }
}
