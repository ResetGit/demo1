package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * Date: 2020/5/2 17:53
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechatpay")
public class WeChatConfig {

    /**
     * 微信appId
     */
    private String WECHAT_APPID;

    /**
     * 微信商户号
     */
    private String WECHAT_MACH_ID;

    /**
     * key
     */
    private String WECHAT_key;

    private String appsecret;


    /**
     * 支付类型，小程序用:JSAPI
     */
    private String tradeType;

    /**
     * 回掉地址
     */
    private String NOTIFYURL;

    /**
     * 统一下单地址
     */
    private String UNIFIED_ORDER_URL;

    private String shopname;

    /**
     * 获取openid地址
     */
    private String Openid_URL = "https://api.weixin.qq.com/sns/jscode2session";
}
