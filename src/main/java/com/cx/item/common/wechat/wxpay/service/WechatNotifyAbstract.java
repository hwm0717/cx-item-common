package com.cx.item.common.wechat.wxpay.service;

import java.util.Map;
import java.util.Objects;

/**
 * 微信支付回调业务接口,回调的服务类需要实现该类实现业务逻辑
 * Created by hwm on 2018/6/28.
 */
public interface WechatNotifyAbstract {

    /**
     * 微信支付回调具体业务方法实现
     *
     * @param notifyMap
     */
     void wechatNotifyService(Map<String, Objects> notifyMap);

}
