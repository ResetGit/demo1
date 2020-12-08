//package com.example.demo.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.example.demo.common.idworker.Sid;
//import com.example.demo.config.PrintAccountConfig;
//import com.example.demo.config.WeChatConfig;
//import com.example.demo.pojo.OrderMaster;
//import com.example.demo.pojo.WeChatPayDto;
//import com.example.demo.util.HttpUtil;
//import com.example.demo.util.OpenIdJson;
//import com.example.demo.util.WeChatPayUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.wxpay.sdk.WXPayUtil;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import lombok.AllArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//
///**
// * Date: 2020/5/2 17:52
// * Description:
// */
//@RestController
//@RequestMapping("/order")
//@AllArgsConstructor
//@Slf4j
//class WeChatPayController {
//
//    @Autowired
//    private WeChatPayUtil weChatPayUtil;
//
//    @Autowired
//    private WeChatConfig weChatConfig;
//
//    @Autowired
//    private WeChatH5Config weChatH5Config;
//
//    @Autowired
//    private OrderMasterService orderMasterService;
//
//    @Autowired
//    private OrderDetailService orderDetailService;
//
//    @Autowired
//    private PrintAccountConfig printAccountConfig;
//
//    @PostMapping("/weChatPay")
//    @SneakyThrows
//    public Map<String, String> getPrePayInfo(WeChatPayDto param, @RequestBody JSONObject list, String openid) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        String date = df.format(new Date());
//        Map<String,String> falsepay = new HashMap<>();
//        falsepay.put("error","参数出错");
//        System.out.println("进入方法");
//        System.out.println(list);
//        String orderid = Sid.next();
//        String body = (String) list.get("body");    //内容
//        String msg = (String) list.get("remarks");      //备注
//        String theTable = (String) list.get("theTable");
//        String total_fee = (String) list.get("total_fee");  //单价
//        param.setBody(body);
//        param.setTotalFee(total_fee);
//        param.setOutTradeNo(orderid);
//        param.setSpbillCreateIp(WeChatPayUtil.getLocalIp());
//        param.setTradeType("JSAPI");         //小程序支付方式jsapi
//        System.out.println("weChatPayDto"+param);
//        String money = (String)list.get("money");   //总价
//        String code = (String)list.get("code");
//        System.out.println("code"+code);
//        String result = "";
//        result = HttpUtil.doGet(
//                "https://api.weixin.qq.com/sns/jscode2session?appid="
//                        + weChatConfig.getWECHAT_APPID() + "&secret="
//                        + weChatConfig.getAppsecret() + "&js_code="
//                        + code
//                        + "&grant_type=authorization_code", null);
//        ObjectMapper mapper = new ObjectMapper();
//        OpenIdJson openIdJson = mapper.readValue(result,OpenIdJson.class);
//
//        OrderMaster orderMaster = new OrderMaster();//保存用户订单
//        orderMaster.setOrderId(orderid);
//        orderMaster.setBuyerOpenid(openIdJson.getOpenid());
//        orderMaster.setOrderAmount(Float.parseFloat(money));
//        orderMaster.setOrderStatus(0);
//        orderMaster.setPayStatus(0);
//        orderMaster.setMsg(msg);
//        orderMaster.setCreateTime(date);
//        orderMaster.setUpdateTime(date);
//        orderMaster.setZh(theTable);
//        orderMaster.setStoreId("");                //店铺id暂无
//        orderMaster.setAppid(weChatConfig.getWECHAT_APPID());
//        orderMasterService.saveOrder(orderMaster);
//
//        String array = list.get("data").toString();
//        JsonArray jsonArray = JsonParser.parseString(array).getAsJsonArray();
//        System.out.println(jsonArray);
//        String del = "\"";
//        for (int i=0;i<jsonArray.size();i++){
//            JsonElement je = jsonArray.get(i);
//            JsonElement productId = je.getAsJsonObject().get("productId");
//            JsonElement productName = je.getAsJsonObject().get("productName");
//            JsonElement num = je.getAsJsonObject().get("quantity");
//            JsonElement productPrice = je.getAsJsonObject().get("productPrice");
//            OrderDetail saveorderDetail = new OrderDetail();
//            saveorderDetail.setOrderId(orderid);
//            saveorderDetail.setCreateTime(date);
//            saveorderDetail.setUpdateTime(date);
//            saveorderDetail.setProductId(productId.toString().replace(del,""));
//            saveorderDetail.setProductName(productName.toString().replace(del,""));
//            saveorderDetail.setProductPrice(Float.parseFloat(productPrice.toString().replace(del,"")));
//            saveorderDetail.setProductQuantity(Integer.parseInt(num.toString().replace(del,"")));
//            saveorderDetail.setAppid(weChatConfig.getWECHAT_APPID());
//            orderDetailService.saveOrderDetail(saveorderDetail);    //生成订单号
//        }
//
//        System.out.println(result.toString());
//        System.out.println("openid="+openIdJson.getOpenid());
//        System.out.println(param);
//        String payType = param.getPayType();
//        if ("11".equals(payType)) {
//            Map<String, String> resultMap = null;
//            try {
//                resultMap = weChatPayUtil.getPrePayInfo(param, openIdJson.getOpenid());
//                resultMap.put("openid",openid);
//                resultMap.put("status","200");
//                log.info(resultMap.toString());
//            } catch (Exception e) {
//                log.error("生成微信预支付订单失败", e);
//            }
//            // 处理公司业务
//            return resultMap;
//
//        }
//
//        return falsepay;
//    }
//
//    //退款
//    @PostMapping("/weChatRefund")
//    @SneakyThrows
//    public Map<String,String> weChatRefund(@RequestBody JSONObject list){
//        String out_trade_no = (String) list.get("out_trade_no");
//        String total_fee = (String) list.get("total_fee");
//        String refund_fee = (String) list.get("total_fee");
//        Map<String,String> map = new HashMap<>();
//        map.put("appid",weChatConfig.getWECHAT_APPID());
//        map.put("mch_id",weChatConfig.getWECHAT_MACH_ID());
//        map.put("nonce_str", WXPayUtil.generateNonceStr());
//        map.put("out_trade_no",out_trade_no);
//        map.put("out_refund_no",UUID.randomUUID().toString().replace("-",""));
//        map.put("total_fee",total_fee);
//        map.put("refund_fee",refund_fee);
//        String sign = WXPayUtil.generateSignature(map,weChatConfig.getWECHAT_key());
//        map.put("sign",sign);
//        String xml = mapToXml(map);
//        String xmlStr = HttpUtils.httpRequest("https://api.mch.weixin.qq.com/secapi/pay/refund", "POST", xml);
//        Map map1 = HttpUtils.doXMLParse(xmlStr);
//        String return_code = (String) map1.get("return_code");
//        String return_msg = (String) map1.get("return_msg");
//        log.info(xmlStr);
//        if(return_code.equals("SUCCESS")){
//            Map<String,String> map2 = new HashMap<>();
//            return map;
//        }else {
//            return map;
//        }
//
//    }
//
//    @PostMapping("/weChatPayh5")
//    @SneakyThrows
//    public Map<String, String> getPrePayInfoH5(WeChatPayDto param, @RequestBody JSONObject list, String openid) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        String date = df.format(new Date());
//        String orderid = WeChatPayUtil.getouttradeno();
//        Map<String,String> falsepay = new HashMap<>();
//        falsepay.put("error","参数出错");
//        System.out.println("进入方法1");
//        System.out.println(list);
//        String body = (String) list.get("body");           //内容
//        String msg = (String) list.get("remarks");         //备注
//        String theTable = (String) list.get("theTable");
//        String total_fee = (String) list.get("total_fee");  //单价
//        param.setBody(body);
//        param.setTotalFee(total_fee);
//        param.setOutTradeNo(orderid);
//        param.setTradeType("MWEB");
//        param.setSpbillCreateIp(WeChatPayUtil.getLocalIp());
//        System.out.println("weChatPayDto"+param);
//        String money = (String)list.get("money");   //总价
//        String code = (String)list.get("code");
//        System.out.println("code"+code);
//        String result = "";
//        result = HttpUtil.doGet(
//                "https://api.weixin.qq.com/sns/jscode2session?appid="
//                        + weChatH5Config.getWECHAT_APPID() + "&secret="
//                        + weChatH5Config.getAppsecret() + "&js_code="
//                        + code
//                        + "&grant_type=authorization_code", null);
//        ObjectMapper mapper = new ObjectMapper();
//        OpenIdJson openIdJson = mapper.readValue(result,OpenIdJson.class);
//
////        OrderMaster orderMaster = new OrderMaster();//保存用户订单
////        orderMaster.setOrderId(orderid);
////        orderMaster.setBuyerOpenid(openIdJson.getOpenid());
////        orderMaster.setOrderAmount(Float.parseFloat(money));
////        orderMaster.setOrderStatus(0);
////        orderMaster.setPayStatus(0);
////        orderMaster.setMsg(msg);
////        orderMaster.setCreateTime(date);
////        orderMaster.setUpdateTime(date);
////        orderMaster.setZh(theTable);
////        orderMaster.setAppid(weChatH5Config.getWECHAT_APPID());
////        orderMasterService.saveOrder(orderMaster);
//
//        String array = list.get("data").toString();
//        JsonArray jsonArray = JsonParser.parseString(array).getAsJsonArray();
//        System.out.println(jsonArray);
//        String del = "\"";
////        for (int i=0;i<jsonArray.size();i++){
////            JsonElement je = jsonArray.get(i);
////            JsonElement productId = je.getAsJsonObject().get("productId");
////            JsonElement productName = je.getAsJsonObject().get("productName");
////            JsonElement num = je.getAsJsonObject().get("quantity");
////            JsonElement productPrice = je.getAsJsonObject().get("productPrice");
////            OrderDetail saveorderDetail = new OrderDetail();
////            saveorderDetail.setOrderId(orderid);
////            saveorderDetail.setCreateTime(date);
////            saveorderDetail.setUpdateTime(date);
////            saveorderDetail.setProductId(productId.toString().replace(del,""));
////            saveorderDetail.setProductName(productName.toString().replace(del,""));
////            saveorderDetail.setProductPrice(Float.parseFloat(productPrice.toString().replace(del,"")));
////            saveorderDetail.setProductQuantity(Integer.parseInt(num.toString().replace(del,"")));
////            saveorderDetail.setAppid(weChatH5Config.getWECHAT_APPID());
////            orderDetailService.saveOrderDetail(saveorderDetail);    //生成订单号
////        }
//
//        System.out.println(result.toString());
//        System.out.println("openid="+openIdJson.getOpenid());
//
//        String payType = param.getPayType();
//        if ("11".equals(payType)) {
//            Map<String, String> resultMap = null;
//            try {
//                resultMap = weChatPayUtil.getPrePayInfo(param, openIdJson.getOpenid());
//                resultMap.put("openid",openid);
//                resultMap.put("status","200");
//                log.info(resultMap.toString());
//            } catch (Exception e) {
//                log.error("生成微信预支付订单失败", e);
//            }
//            // 处理公司业务
//            return resultMap;
//
//        }
//
//        return falsepay;
//    }
//
//
//}
