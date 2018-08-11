package com.cx.item.common.constants;

/**
 * 响应码
 */
public enum ResultCode {

    //带消息的响应码
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),//其他失败，全部走这里  请注意
    TOKEN_ERROR(10, "token验证失败"),
    PERMISSION_ERROR(11, "权限不足"),
    ACCESS_LIMIT_ERROR(12, "ACCESS_LIMIT_ERROR"),
    REPEAT_REQUEST_ERROR(13, "重复请求");

    private Integer status;

    private String message;

    ResultCode(Integer status) {
        this.status = status;
    }

    ResultCode(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer status() {
        return status;
    }

    public String message() {
        return message;
    }

}
