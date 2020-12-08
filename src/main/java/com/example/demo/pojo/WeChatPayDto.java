package com.example.demo.pojo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * Date: 2020/5/2 17:51
 * Description: 微信:小程序支付
 */
@Data
public class WeChatPayDto implements Serializable {

    /**
     * 商品描述
     */
    private String body;

    /**
     * 订单号
     */
    @NotNull(message = "缺少请求参数")
    private String outTradeNo;

    /**
     * 金额
     */
    private String totalFee;

    /**
     * 终端IP
     */
    private String spbillCreateIp;

    /**
     * 支付类型
     */
    @NotBlank(message = "支付类型不能为空")
    private String payType = "11";

    private String device_info;

    private String tradeType;
}
