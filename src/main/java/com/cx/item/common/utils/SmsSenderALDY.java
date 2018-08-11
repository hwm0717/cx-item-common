package com.cx.item.common.utils;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @Author : Heavin
 * @Des : 阿里大鱼的短信发送封装工具
 * @Date :  21:03  2018/8/6
 */
public class SmsSenderALDY {


    private String smsServer;
    private String smsAppkey;
    private String smsAppsecret;
    private String smsType;
    private String smsSingName;

    /**
     * 阿里大鱼配置文件
     * @param smsServer  serverUrl
     * @param smsAppkey   key
     * @param smsAppsecret  授权
     * @param smsType  类型
     * @param smsSingName 签名
     */
    public SmsSenderALDY(String smsServer, String smsAppkey, String smsAppsecret, String smsType, String smsSingName) {
        this.smsServer = smsServer;
        this.smsAppkey = smsAppkey;
        this.smsAppsecret = smsAppsecret;
        this.smsType = smsType;
        this.smsSingName = smsSingName;
    }


    /**
     * 发送短信
     * @param phone  电话号码
     * @param smsTemplate  短信模板
     * @param paramString 参数
     * @throws Exception  异常
     */
    public void sendSms(String phone, String smsTemplate, String paramString) throws Exception {
        DefaultTaobaoClient client = new DefaultTaobaoClient(this.smsServer, this.smsAppkey, this.smsAppsecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType(this.smsType);
        req.setSmsFreeSignName(this.smsSingName);
        req.setRecNum(phone);
        if (!StringUtils.isEmpty(paramString)) {
            req.setSmsParamString(paramString);
        }

        req.setSmsTemplateCode(smsTemplate);
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        String respBody = null;

        rsp = (AlibabaAliqinFcSmsNumSendResponse) client.execute(req);
        respBody = rsp.getBody();
        ObjectMapper e = new ObjectMapper();
        JsonNode jsonNode = e.readTree(respBody);
        JsonNode jsonNodeChild = jsonNode.findValue("alibaba_aliqin_fc_sms_num_send_response");
        if (StringUtils.isEmpty(jsonNodeChild)) {
            // log.error("短信推送异常：{}" + respBody);
            throw new RuntimeException("短信推送异常:" + respBody);
        } else {
            JsonNode jsonNodeResult = jsonNodeChild.findValue("result");
            if (StringUtils.isEmpty(jsonNodeResult)) {
                //   log.error("短信推送异常：{}" + respBody);
                throw new RuntimeException("短信推送异常:" + respBody);
            } else {
                JsonNode jsonNodeErrCode = jsonNodeResult.get("err_code");
                JsonNode jsonNodeSucc = jsonNodeResult.get("success");
                int errCode = jsonNodeErrCode.asInt(1);
                boolean succ = jsonNodeSucc.asBoolean(false);
                if (errCode == 0 && succ) {

                } else {
                    throw new RuntimeException("短信推送异常:" + respBody);
                }
            }
        }
    }
}
