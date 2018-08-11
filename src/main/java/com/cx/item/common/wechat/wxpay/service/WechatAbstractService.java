package com.cx.item.common.wechat.wxpay.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cx.item.common.wechat.wxpay.model.WechatPayConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hwm on 2018/6/28.
 */
public abstract class WechatAbstractService {

    /**
     * 支付参数
     */
    protected WechatPayConfig wechatPayConfig;

    /**
     * code 获取 openid 和 session_key信息url
     */
    private static String getSessionKeyAndOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 统一下单请求地址
     */
    protected String wx_pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 使用 code 获取 openid 和 session_key 等信息
     *
     * @param code
     * @param appId
     * @param appSecret
     * @return
     */
    public static Map<String, String> getSessionKeyAndOpenid(String code, String appId, String appSecret) {

        Map<String, String> requestUrlParam = new HashMap();
        requestUrlParam.put("appid", appId);    //开发者设置中的appId
        requestUrlParam.put("secret", appSecret);    //开发者设置中的appSecret
        requestUrlParam.put("js_code", code);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(sendPost(getSessionKeyAndOpenidUrl, requestUrlParam));

        Map<String, String> map = new HashMap();
        if (StrUtil.isEmpty(jsonObject.getString("openid"))) {
            return null;
        }

        map.put("openid", jsonObject.getString("openid"));
        map.put("session_key", jsonObject.getString("session_key"));

        return map;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param paramMap 请求参数
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 微信支付,微信各种支付方式实现方法可能不一样
     * 说明：https://developers.weixin.qq.com/miniprogram/dev/api/api-login.html
     *
     * @return
     */
    public abstract Map<String, String> wechatPay();

    /**
     * 微信支付回调方法
     * 说明：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_7&index=8
     *
     * @param wechatNotifyAbstract
     * @throws Exception
     */
    public abstract void wechatNotify(WechatNotifyAbstract wechatNotifyAbstract) throws Exception;
}
