package com.cx.item.common.wechat.wxpay.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by hwm on 2018/6/28.
 */
public class MiniappPayConfig {

    // response
    HttpServletResponse response;
    // openid
    private String openid;
    // key为商户平台设置的密钥key
    private String key;
    // 微信分配的程序id
    private String appid;
    // 微信支付分配的商户号
    private String mchid;
    // 支付成功微信回调方法
    private String notifyUrl;
    // 支付金额
    private String payPrice;
    // 商品描述
    private String body;
    // 商户订单号
    private String outTradeNo;
    // request
    private HttpServletRequest request;

    public MiniappPayConfig() {
    }

    public MiniappPayConfig(String openid, String key, String appid, String mchid, String notifyUrl, String payPrice, String body, String outTradeNo, HttpServletRequest request) {
        this.openid = openid;
        this.key = key;
        this.appid = appid;
        this.mchid = mchid;
        this.notifyUrl = notifyUrl;
        this.payPrice = payPrice;
        this.body = body;
        this.outTradeNo = outTradeNo;
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
