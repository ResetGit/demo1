//package com.example.demo.util;
//
//import com.example.demo.common.idworker.Sid;
//import com.example.demo.config.WeChatConfig;
//import com.example.demo.pojo.WeChatPayDto;
//import com.google.common.collect.Maps;
//import com.github.wxpay.sdk.WXPayUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.net.InetAddress;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import static com.example.demo.util.HttpUtils.getCurrentTimestamp;
//import static com.github.wxpay.sdk.WXPayUtil.*;
//
//
///**
// * @Created by Wu Chenxuan
// * @Date 2020/4/27
// * @Description
// */
//@Slf4j
//@Component
//public class WeChatPayUtil {
//
//    @Autowired
//    private WeChatConfig weChatConfig;
//
//    public Map<String, String> getPrePayInfo(WeChatPayDto miniDTO, String openid, @RequestBody Map mappay) throws Exception {
//        Map<String, String> map = Maps.newHashMap();
//        String orderid = Sid.next();
//        String appid = (String)mappay.get("appid");
//        String key = (String) mappay.get("key");
//        String openid2 = (String) mappay.get("openid");
////        String scene_info = {"h5_info": {"type":"Wap","wap_url": "https://pay.qq.com","wap_name": "腾讯充值"}};
//        map.put("appid", appid);
//        map.put("mch_id", (String) mappay.get("mch_id"));
//        map.put("nonce_str", WXPayUtil.generateNonceStr());
//        map.put("body", miniDTO.getBody());
//        map.put("spbill_create_ip", miniDTO.getSpbillCreateIp());
//        map.put("out_trade_no", miniDTO.getOutTradeNo());
//        map.put("total_fee", miniDTO.getTotalFee());
//        map.put("trade_type", miniDTO.getTradeType());
//        map.put("sign_type","MD5");
//        map.put("notify_url", "https://www.lssell.cn/tt3/diancan/weChatPay");
//        map.put("openid",openid2);
//        map.put("scene_info", "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"https://www.lssell.cn/tt3/diancan/\",\"wap_name\": \"点餐\"}}");
////        map.put("openid", openId);
//        System.out.println("map"+map);
//        String sign = generateSignature(map, key);// 生成签名 PAY_API_SECRET=微信支付相关API调用时使用的秘钥
//        map.put("sign", sign);  // 参数配置 我直接写成"sign"
//        String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信统一下单URL
//        String xml = mapToXml(map);
//        //请求微信统一下单接口
//        String xmlStr = HttpUtils.httpRequest(unifiedorderUrl, "POST", xml);
//        Map map1 = HttpUtils.doXMLParse(xmlStr);
//        String return_code = (String) map1.get("return_code");//返回状态码
//        String result_code = (String) map1.get("result_code");//返回状态码
//        String err_code = (String) map1.get("err_code");//返回状态码
//        String err_code_des = (String) map1.get("err_code_des");//返回状态码
//        log.info(xmlStr);
//        if (return_code.equals("SUCCESS") || return_code.equals(result_code)) {
//            // 业务结果
//            System.out.println(map1);
//            String prepay_id = (String) map1.get("prepay_id");//返回的预付单信息
//            Map<String, String> payMap = new HashMap<>();
//            payMap.put("appId", appid);  // 参数配置
//            payMap.put("timeStamp", getCurrentTimestamp()+"");  //时间
//            payMap.put("nonceStr", generateNonceStr());  // 获取随机字符串
//            payMap.put("signType", "MD5");
//            payMap.put("package", "prepay_id=" + prepay_id);
//            String paySign = generateSignature(payMap, key); //第二次生成签名
//            payMap.put("paySign", paySign);
//            payMap.put("prepayId", prepay_id);
//            return payMap;   //返回给前端，让前端去调支付 ，完成后你去调支付查询接口，看支付结果，处理业务。
//        } else {
//            //打印失败日志
//        }
//        return null;
//
//    }
//
//    /**
//     * 获取当前机器的ip
//     *
//     * @return String
//     */
//    public static String getLocalIp() {
//        InetAddress ia = null;
//        String localip = null;
//        try {
//            ia = ia.getLocalHost();
//            localip = ia.getHostAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return localip;
//
//    }
//
//    public static String getouttradeno(){
//        String id = "Wx";
//        String date = new SimpleDateFormat("yyMMdd").format(new Date());
//        int machineId = 1;//最大支持1-9个集群机器部署
//        int hashCodeV = UUID.randomUUID().toString().hashCode();
//        if(hashCodeV < 0) {//有可能是负数
//            hashCodeV = - hashCodeV;
//        }
//        return id + date + machineId + String.format("%014d", hashCodeV);
//    }
//
//
//}
