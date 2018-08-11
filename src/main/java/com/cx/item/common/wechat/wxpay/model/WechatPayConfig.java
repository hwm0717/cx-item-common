package com.cx.item.common.wechat.wxpay.model;

import com.cx.item.common.par.check.CheckParInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hwm on 2018/6/28.
 */
public class WechatPayConfig implements CheckParInterface {

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
    // response
    HttpServletResponse response;

//    /**
//     * 小程序支付构造器
//     *
//     * @param openid
//     * @param key
//     * @param appid
//     * @param mchid
//     * @param notifyUrl
//     * @param payPrice
//     * @param body
//     * @param outTradeNo
//     * @param request
//     * @param response
//     */
    public WechatPayConfig(MiniappPayConfig miniappPayConfig) {
        this.openid = miniappPayConfig.getOpenid();
        this.key = miniappPayConfig.getKey();
        this.appid = miniappPayConfig.getAppid();
        this.mchid = miniappPayConfig.getMchid();
        this.notifyUrl = miniappPayConfig.getNotifyUrl();
        this.payPrice = miniappPayConfig.getPayPrice();
        this.body = miniappPayConfig.getBody();
        this.outTradeNo = miniappPayConfig.getOutTradeNo();
        this.request = miniappPayConfig.getRequest();
        this.response = miniappPayConfig.getResponse();
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

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
