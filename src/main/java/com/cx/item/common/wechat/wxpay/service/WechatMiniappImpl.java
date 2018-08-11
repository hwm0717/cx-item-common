package com.cx.item.common.wechat.wxpay.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cx.item.common.exception.ParamsCheckException;
import com.cx.item.common.par.check.CheckParTarget;
import com.cx.item.common.par.check.CheckParTargetInstance;
import com.cx.item.common.utils.IpUtil;
import com.cx.item.common.utils.StringUtil;
import com.cx.item.common.wechat.wxpay.util.PayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序支付工具类
 * Created by hwm on 2018/6/27.
 */
public class WechatMiniappImpl extends WechatAbstractService {

    private static Logger log = LoggerFactory.getLogger(WechatMiniappImpl.class);

    private static String notifySys = "notifySys";

    /**
     *
     小程序支付交互
     商户系统和微信支付系统主要交互：
     1、小程序内调用登录接口，获取到用户的openid,api参见公共api【小程序登录API】
     2、商户server调用支付统一下单，api参见公共api【统一下单API】
     3、商户server调用再次签名，api参见公共api【再次签名】
     4、商户server接收支付通知，api参见公共api【支付结果通知API】
     5、商户server查询支付结果，api参见公共api【查询订单API】
     */

    protected WechatMiniappImpl() {

    }

    /**
     * 生成预支付订单号
     *
     * @return miniappPayConfig 参数
     */
    @Override
    public Map<String, String> wechatPay() {

        CheckParTarget checkParTarget = CheckParTargetInstance.getInstance(this.wechatPayConfig, "openid", "key", "appid", "mchid", "notifyUrl", "payPrice", "body", "outTradeNo", "request");
        checkParTarget.checkParIsNullThrowError();

        synchronized (wechatPayConfig) {
            try {

                // 防止商户订单号重复，每次下单是在后面添加6位随机数,订单号和随机数用_分割
                String tradeNo = StrUtil.format("{}_{}", wechatPayConfig.getOutTradeNo(), StringUtil.getStringRandom(6));

                //生成的随机字符串
                String nonce_str = StringUtil.getStringRandom(32);
                //获取客户端的ip地址
                String spbill_create_ip = IpUtil.getIpAddr(wechatPayConfig.getRequest());
                // 支付金额,元转分
                String payPrice = PayUtil.yuanTofen(wechatPayConfig.getPayPrice());
                // 支付方式小程序取值如下：JSAPI
                String trade_type = "JSAPI";

                //组装参数，用户生成统一下单接口的签名
                Map<String, String> packageParams = new HashMap();
                packageParams.put("appid", wechatPayConfig.getAppid());
                packageParams.put("mch_id", wechatPayConfig.getMchid());
                packageParams.put("nonce_str", nonce_str);
                packageParams.put("body", wechatPayConfig.getBody());
                packageParams.put("out_trade_no", tradeNo);//商户订单号
                packageParams.put("total_fee", payPrice);//支付金额，这边需要转成字符串类型，否则后面的签名会失败(单位分)
                packageParams.put("spbill_create_ip", spbill_create_ip);
                packageParams.put("notify_url", wechatPayConfig.getNotifyUrl());//支付成功后的回调地址
                packageParams.put("trade_type", trade_type);//支付方式小程序取值如下：JSAPI
                packageParams.put("openid", wechatPayConfig.getOpenid());

                String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

                //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
                String mysign = PayUtil.sign(prestr, wechatPayConfig.getKey(), "utf-8").toUpperCase();
                log.debug(StrUtil.format("====>微信支付签名参数，验证签名还需要在打印参数后加上（&key= + key） packageParams={}", prestr));
                log.debug(StrUtil.format("<====微信支付签名结果mysign={}", mysign));


                //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
                StringBuilder xml = new StringBuilder();
                xml.append("<xml>").append("<appid>").append(wechatPayConfig.getAppid()).append("</appid>")
                        .append("<body><![CDATA[").append(wechatPayConfig.getBody()).append("]]></body>")
                        .append("<mch_id>").append(wechatPayConfig.getMchid()).append("</mch_id>")
                        .append("<nonce_str>").append(nonce_str).append("</nonce_str>")
                        .append("<notify_url>").append(wechatPayConfig.getNotifyUrl()).append("</notify_url>")
                        .append("<openid>").append(wechatPayConfig.getOpenid()).append("</openid>")
                        .append("<out_trade_no>").append(tradeNo).append("</out_trade_no>")
                        .append("<spbill_create_ip>").append(spbill_create_ip).append("</spbill_create_ip>")
                        .append("<total_fee>").append(payPrice).append("</total_fee>")
                        .append("<trade_type>").append(trade_type).append("</trade_type>")
                        .append("<sign>").append(mysign).append("</sign>")
                        .append("</xml>");

                log.debug("====>调试模式_统一下单接口 请求XML数据：" + xml.toString());

                //调用统一下单接口，并接受返回的结果
                String result = PayUtil.httpRequest(wx_pay_url, "POST", xml.toString());

                log.debug("<====调试模式_统一下单接口 返回XML数据：" + result);

                // 将解析结果存储在HashMap中
                Map map = PayUtil.doXMLParse(result);
                log.debug("<====调试模式_统一下单接口 返回XML数据解析成map：" + map.toString());

                String return_code = (String) map.get("return_code");//返回状态码

                Map<String, String> response = new HashMap();//返回给小程序端需要的参数
                if ("SUCCESS".equals(return_code)) {
                    String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                    response.put("nonceStr", nonce_str);
                    response.put("package", "prepay_id=" + prepay_id);
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    response.put("timeStamp", String.valueOf(timeStamp));//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                    //拼接签名需要的参数
                    String stringSignTemp = "appId=" + wechatPayConfig.getAppid() + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                    //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                    String paySign = PayUtil.sign(stringSignTemp, wechatPayConfig.getKey(), "utf-8").toUpperCase();

                    response.put("paySign", paySign);
                    response.put("signType", "MD5");

                    log.debug("<====调试模式_统一下单接口结果验证成功返回结果：" + response.toString());
                }

                return response;
            } catch (Exception e) {
                throw new ParamsCheckException(e);
            }
        }
    }

    @Override
    public void wechatNotify(WechatNotifyAbstract wechatNotifyAbstract) throws Exception {

        CheckParTarget checkParTarget = CheckParTargetInstance.getInstance(this.wechatPayConfig, "key");
        checkParTarget.checkParIsNullThrowError();

        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) this.wechatPayConfig.getRequest().getInputStream()));
        String line = null;
        //sb为微信返回的xml
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        // 返回结果
        StringBuilder resXml = new StringBuilder();

        // 解析报文
        Map map = PayUtil.doXMLParse(sb.toString());
        if (CollUtil.isEmpty(map)){
            log.info("=================微信支付回调接受报文为空=================");
            return;
        }
        log.debug("====>接收到的报文解析结果：" + map.toString());

        String returnCode = String.valueOf(map.get("return_code"));
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = PayUtil.sign(validStr, this.wechatPayConfig.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if (sign.equals(map.get("sign"))) {
                /**此处添加自己的业务逻辑代码start**/

                synchronized (notifySys) {
                    log.debug("===============微信付款成功==============");
                    // ------------------------------
                    log.debug("=============处理业务开始===============");
                    // ------------------------------
                    log.debug("============此处处理订单状态，结合自己的订单数据完成订单状态的更新==============");
                    // 返回的订单号去掉后面的6位随机数
                    String out_trade_no = String.valueOf(map.get("out_trade_no")).split("_")[0]; // 订单号
                    map.put("out_trade_no", out_trade_no);
                    wechatNotifyAbstract.wechatNotifyService(map);

                    log.debug("<====订单支付成功,订单状态以改为已支付状态");
                    // ------------------------------
                    log.debug("====================处理业务完毕===================");
                    // ------------------------------
                }

                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
                resXml.append("<xml>").append("<return_code><![CDATA[SUCCESS]]></return_code>")
                        .append("<return_msg><![CDATA[OK]]></return_msg>").append("</xml> ");
            }
        } else {
            resXml.append("<xml>").append("<return_code><![CDATA[FAIL]]></return_code>")
                    .append("<return_msg><![CDATA[报文为空]]></return_msg>").append("</xml> ");
            log.debug("===============微信付款回调成功，判断签名失败==============");
            log.debug(StrUtil.format("======微信回调参数【{}】", map.toString()));
        }
        log.debug(resXml.toString());
        log.debug("<==========微信支付回调数据结束=======================");

        BufferedOutputStream out = new BufferedOutputStream(this.wechatPayConfig.getResponse().getOutputStream());
        out.write(resXml.toString().getBytes());
        out.flush();
        out.close();
    }



}
