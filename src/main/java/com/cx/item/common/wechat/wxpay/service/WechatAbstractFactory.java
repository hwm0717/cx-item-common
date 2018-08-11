package com.cx.item.common.wechat.wxpay.service;

import com.cx.item.common.wechat.wxpay.model.MiniappPayConfig;
import com.cx.item.common.wechat.wxpay.model.WechatPayConfig;

/**
 * 微信工具类创建工厂
 * Created by hwm on 2018/6/28.
 */
public class WechatAbstractFactory {

    /**
     * 创建微信小程序支付对象
     *
     * @param miniappPayConfig
     * @return
     */
    public static WechatAbstractService getMiniappPayInstance(MiniappPayConfig miniappPayConfig) {

        WechatAbstractService wechatAbstractService = new WechatMiniappImpl();
        wechatAbstractService.wechatPayConfig = new WechatPayConfig(miniappPayConfig);

        return wechatAbstractService;
    }

}
