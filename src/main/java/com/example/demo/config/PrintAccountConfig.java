package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "print")
public class PrintAccountConfig {

    private String url;//不需要修改

    private String user;//*必填*：账号名

    private String ukey;//*必填*: 飞鹅云后台注册账号后生成的UKEY 【备注：这不是填打印机的KEY】

    private String sn;//*必填*：打印机编号，必须要在管理后台里添加打印机或调用API接口添加之后，才能调用API

    public PrintAccountConfig(){}
}
