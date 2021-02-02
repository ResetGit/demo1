package com.example.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.Methods;
import com.example.demo.pojo.OrderDetail;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.util.HttpClientUtils;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.MD5Utils;
import com.google.gson.Gson;
import com.example.demo.util.RequestMethod;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class test {
    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @RequestMapping("testprint")
    public Object print(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = "1035175370"; //应用id
        String key = "1fd935b682ec6068beda33d1c3a8a846"; //应用密钥
        String machine = "4004708352";   //machine设备终端号
        String token = "74db0856c5a34902bac1a8c281f97c5e";

        String orderid = "210107138366412652544";

        RequestMethod.getInstance().init(id,key);

        double ZMoney=0;
        double YMoney=20;
        double SMoney=500;

        List<OrderDetail> orderDetails = orderDetailService.queryOrderDetailByOrderId(orderid);
        //字符串拼接
        StringBuilder sb=new StringBuilder();
        sb.append("@@2<center>"+orderMasterService.queryOrderById(orderid).getShopname()+"\r\n</center>");
        sb.append("@@2<center>桌号:"+orderMasterService.queryOrderById(orderid).getZh()+"号\r\n</center>");
        sb.append("--------------------------------\r\n");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td>");
        sb.append("菜品");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("单价");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("数量");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("总计");
        sb.append("</td>");
        sb.append("</tr>");
        for(OrderDetail orderDetail : orderDetails){
            String title = orderDetail.getProductName();
            String price = orderDetail.getProductPrice().toString();
            String quantity = orderDetail.getProductQuantity().toString();
            BigDecimal total2 = new BigDecimal("0");
            BigDecimal a1 = new BigDecimal(price);
            BigDecimal a2 = new BigDecimal(quantity);
            total2 = a1.multiply(a2);
            sb.append("<tr>");
            sb.append("<td>"+title+"</td>");
            sb.append("<td>"+price+"</td>");
            sb.append("<td>"+quantity+"</td>");
            sb.append("<td>"+total2+"</td>");
            sb.append("</tr>");
                }
        sb.append("</table>");
        sb.append("--------------------------------\r\n");
        sb.append("备注："+orderMasterService.queryOrderById(orderid).getMsg()+"\r\n");
        sb.append("合计：￥"+orderMasterService.queryOrderById(orderid).getOrderAmount() +"元\r\n");
        sb.append("点餐时间："+orderMasterService.queryOrderById(orderid).getCreateTime() +"\r\n");
        String print = RequestMethod.getInstance().printIndex(token,machine,sb.toString(),Sid.next());
        return print;

    }

    @RequestMapping("testid")
    public Object testid(@RequestBody Map map) throws Exception {
        String id = (String)map.get("id"); //应用id
        String key = (String)map.get("key"); //应用密钥
       RequestMethod.getInstance().init(id,key);
        String f= RequestMethod.getAccessToken();
        Map map1 = new HashMap();
        Gson gson = new Gson();
        map1 = gson.fromJson(f,map1.getClass());
        return map1;
    }

    @RequestMapping("testpay")
    public Object testpay() throws IOException {
        String privateKey = "wq933c65e136ada75b8601d13e6a8qbb";
        byte[] privatebyte = privateKey.getBytes("utf-8");
        System.out.println(privatebyte);
        String mchId = "1605530736";
        String mchSerialNo = "6B969F9D3A30FC30957ADA996AAE6D93919BA419";
        String apiV3Key = "wq933c65e136ada75b8601d13e6a8qbb";
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3秘钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),apiV3Key.getBytes("utf-8"));

        // 初始化httpClient
        CloseableHttpClient httpclient = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();
        return httpclient;
    }

    @RequestMapping("testali")
    public Object testali(){
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2021002120621412", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRYRxAeMiWp8GF07ExeUNZpeJ+ds9Y752BcSVSfZkEyJjWSnPkqA4kPRaxIken+kp10qI5V+pT82Ww7U5CUdtHGzBKh32R+50loWZ7s3/jLb9NDhFnyMG5zgcSuyXtXYJKMXgkMMtoq3FfCKLLTihzT+NB737nzwCRZI06uQbAC5tc4S9D9MWG9wsqDi/h0ALGDzePP8UOZ90ucLdYbsMYd2Ev978E0LZvtGgCRxp7hEHyzRsCLnC9WpDpEXSj4B/AZlYvC6ZqtcODRL2HjvdmJZMMCf7PCTmF+pp9bgkXoTPrxruSr7q0J2dC7WVbOmOBRgHrpcFJgs/BstPt5m9VAgMBAAECggEAVLxskiuUhzN6ef18n2CV4A/vZ1Tq4GSxwnvnjCBG62Hcq7KzwEgwDfFJY6+eYothkTFXeuMCYzJQSILZBZDepaB0UkyM5WTqIL/lkx554TmN8mTLwlliiWFMAB/zKFpDkekppLaIKRccuo63DJcevq+Gv6zPpF+kbhpUszCbfZ2hvbBrS2o8YNBxWEetrSLrxcemr7VDjFT6Hbu7rMrIebZZTNODKQxYdUzjXjzhJfUFAbtJJnumpcXuEfqmEhF89ZEitmLSHW82HdExU5Warbes2+41eLeaZHlTfhP1LPQpzop8wUa+1T6IVHpudZl6Xlnpv1YqSZBvK7iqu3359QKBgQDnES54FfDxL/PbZE1KtUO1SKvds7ORIARgtO1SKc5CX/sY7VMnigLgpYrTKL2XcbicB6yQw6ztu5vMMQxf3Yx2bvCxURnptpVjNHA0GQtNiULMgH6ajV/nUfrfMUpuDkzQsqNleWQ/as/yTkZV+SL75zV+/5hperf4Mf4dKOA3PwKBgQChEPTLfLXDH23q5M8p3IgrF3w2G6uZTD44tWM+hd+5rOtghPwaa96Bc1o2JzqVM3qvP7wU5exuNxSctcNj6uNF3uEQflceJulrDq3VUuIrdzs8GKteolxDgXPyY8eixw3F3Im8rWS0y1OE4rodExrlgjfBlOxdwQkYqTvkTpKoawKBgELH/3MrfDho0IS+52vJGamCOFHaBvLRmxJMGC7f8EHUtaCNrrPFunEsxwOiSbFEZ8OIoEIym38V8sBACMCIKnlsNNo6g0+9uk0ysZ8NAexF2G18UZrfczFKy0PIXieSsHa8umawf92oRDNWlhNw7sB5IUf0HRuXISZ1ZGkPEEZHAoGAc2N5bHqYoQRYM8reW2ghqXV2yDDEm9uSTAAu7HDuWc/3BLvg/HDBBE6Gl3s9LfymbPylGvepjUAqpSZWp7N0RO8qoC1S7kgEO21VvhlV7LvLlP87bl206avJTCKNuCEQzep0RtaV6iwsVmwz13aDc1754NZ3C6eqg59iR+q+iKcCgYEAiQIP53jBVDTxvglXN1zrFk9W62b0T6aLu1aFo42BdIMI6u2rjpROrNe6E8ME1RzlIs/SBWImPqadE+Ht/CRUWbpiGgGAW/aHzBdefFYT7UctClI3lNfY/PFgz+K9kPz/tprgfAHSf0KMOtYOGLwfVGTnUDf8vgCphZ0dMk7x7uA=",
                "json", "UTF-8", ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        return null;
    }



}
