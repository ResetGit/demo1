package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "boss")
public class BossConfig {
    private String url;     //支付宝网关（固定）
    private String appid;
    private String app_private_key; //开发者私钥
    private String format;  //固定json
    private String charset; //编码集，支持 GBK/UTF-8
    private String alipay_public_key;   //支付宝公钥
    private String sign_type;   //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2
    private String notifyurl;
    private String urlparam;
}
