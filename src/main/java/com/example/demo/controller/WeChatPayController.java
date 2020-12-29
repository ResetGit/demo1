package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.PrintAccountConfig;
import com.example.demo.config.WeChatConfig;
import com.example.demo.pojo.*;
import com.example.demo.service.Imp.PrintService;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.StoreService;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.HttpUtils;
import com.example.demo.util.OpenIdJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.demo.util.HttpUtils.getCurrentTimestamp;
import static com.github.wxpay.sdk.WXPayUtil.*;


/**
 * Date: 2020/5/2 17:52
 * Description:
 */
@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
class WeChatPayController {

//    @Autowired
//    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private WeChatConfig weChatH5Config;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private StoreService storeService;
//
//    @RequestMapping("/weChatPay")
//    public Map<String, String> getPrePayInfo(WeChatPayDto param, String openid,@RequestBody Map mappay) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        String openid2 = (String) mappay.get("openid");
//        String date = df.format(new Date());
//        Map<String,String> falsepay = new HashMap<>();
//        falsepay.put("error","参数出错");
//        System.out.println("进入方法");
//        String orderid = Sid.next();
//        param.setBody("点餐");
//        param.setTotalFee("1");
//        param.setOutTradeNo(orderid);
//        param.setSpbillCreateIp(WeChatPayUtil.getLocalIp());
//        param.setTradeType("JSAPI");         //小程序支付方式jsap
//
//        String payType = param.getPayType();
//        if ("11".equals(payType)) {
//            Map<String, String> resultMap = null;
//            try {
//                resultMap = weChatPayUtil.getPrePayInfo(param, openid,mappay);
//                resultMap.put("openid", (String) mappay.get("openid"));
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

    @RequestMapping("/weChatPayh5")
    @ResponseBody
    public Map<String, String> getPrePayInfoH5(HttpServletRequest request) {
        try{
            System.out.println("进入h5支付");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());
            System.out.println(date);
            String order = Sid.next();      //随机生成订单
            String money = request.getParameter("money");             //总价
            String remarks = request.getParameter("remarks");         //备注
            String theTable = request.getParameter("thetable");       //桌号
            String shopname = request.getParameter("storeName");         //用户店名
            String userName = request.getParameter("userName");         //打印机账号
            String key = request.getParameter("key");                   //打印机key
            String sn = request.getParameter("sn");                     //打印机sn
            String storeid = request.getParameter("storeId");           //店铺id
            List<Store> storeList = storeService.getListByObject("getByStoreName",storeid);
            System.out.println("总价"+money);
            String pricelist =money.replace(".",":");
            String price = null;
            String[] listjg = pricelist.split(":");
            if("0".equals(listjg[0])){
                price=listjg[1]+"0";
            }else {
                price=listjg[0]+"00";
            }
            //创建订单
            OrderMaster masterAli = new OrderMaster();
            masterAli.setPayStatus(0);
            masterAli.setOrderStatus(0);
            masterAli.setOrderAmount(Float.parseFloat(money));
            masterAli.setBuyerOpenid("暂无");
            masterAli.setAppid(storeList.get(0).getWxAppId());
            masterAli.setOrderId(order);
            masterAli.setMsg(remarks);
            masterAli.setZh(theTable);
            masterAli.setShopname(storeList.get(0).getName());
            masterAli.setStoreId(storeid);
            masterAli.setSn(sn);
            masterAli.setUserKey(key);
            masterAli.setCreateTime(date);
            masterAli.setUpdateTime(date);
            masterAli.setUserName(userName);
            orderMasterService.saveOrder(masterAli);       //创建订单

            String data = request.getParameter("data");
            JSONArray array = JSONArray.parseArray(data);
            List<Map> arraylist = new ArrayList<>();
            for (int i=0;i<array.size();i++){
                Map map = new HashMap();
                String list = array.getString(i).replace("{","");
                String list2 = list.replace("}","");
                String list3 = list2.replace("\"","");
                String list4 = list3.replace(",",":");
                String list5[] = list4.split(":");
                for(int j=0;j<list5.length;j++){
                    int num = j%2;
                    if(num==0){
                        map.put(list5[j],list5[j+1]);
                    }
                }
                arraylist.add(map);
            }
            System.out.println(arraylist);
            System.out.println("=======================================================");
//
            for(int i=0;i<arraylist.size();i++){
                if(!arraylist.get(i).get("quantity").equals("0")) {
                    OrderDetail orderDetailAli = new OrderDetail();
                    String productId = (String) arraylist.get(i).get("productId");
                    String productName = (String) arraylist.get(i).get("productName");
                    String quantity = (String) arraylist.get(i).get("quantity");
                    String productPrice = (String) arraylist.get(i).get("productPrice");
                    System.out.println("测试"+arraylist.get(i));
                    orderDetailAli.setProductId(productId);
                    orderDetailAli.setAppid(storeList.get(0).getZfbAppId());
                    orderDetailAli.setOrderId(order);
                    orderDetailAli.setProductName(productName);
                    orderDetailAli.setProductPrice(Float.parseFloat(productPrice.toString()));
                    orderDetailAli.setProductQuantity(Integer.parseInt(quantity.toString()));
                    orderDetailService.saveOrderDetail(orderDetailAli);
                }
            }
            System.out.println("=======================================================");

            Map<String, String> map = Maps.newHashMap();

            map.put("appid", storeList.get(0).getWxAppId());
            map.put("mch_id", storeList.get(0).getWxMachid());
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", "点餐");
            map.put("spbill_create_ip", "8.129.121.95");
            map.put("out_trade_no", order);
            map.put("total_fee", price);
            map.put("trade_type", "MWEB");
            map.put("attach",storeid);
            map.put("notify_url", weChatConfig.getNOTIFYURL());
            map.put("scene_info", "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"https://www.lssell.cn/tt3/diancan/\",\"wap_name\": \"点餐\"}}");
            String sign = generateSignature(map, storeList.get(0).getWxKey());// 生成签名 PAY_API_SECRET=微信支付相关API调用时使用的秘钥
            map.put("sign", sign);
            System.out.println("map"+map);
            String xml = WXPayUtil.mapToXml(map);//将所有参数(map)转xml格式
            String unifiedorderUrl = weChatConfig.getUNIFIED_ORDER_URL(); // 微信统一下单URL
            String xmlStr = HttpUtils.httpRequest(unifiedorderUrl, "POST", xml);

            //以下内容是返回前端页面的json数据
            String mweb_url = "";//跳转链接
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map1 = WXPayUtil.xmlToMap(xmlStr);
                mweb_url = (String) map1.get("mweb_url");
                //支付完返回浏览器跳转的地址，如跳到查看订单页面
                String redirect_url = "www.lssell.cn/tt3/diancan/login.html";
                String redirect_urlEncode =  URLEncoder.encode(redirect_url,"utf-8");//对上面地址urlencode
                mweb_url = mweb_url + "&redirect_url=" + redirect_urlEncode;//拼接返回地址
            }

            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("mweb_url", mweb_url);
            System.out.println(payMap);
            return  payMap;
        } catch (Exception e){
            log.info("微信支付异常");
        }
        return null;
    }

    /**
     * 微信支付 回调函数
     */
    @PostMapping("/weChatPay")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "UTF-8");
            Map<String, String> map = xmlToMap(result);//xml转map 微信SDK自带
            String stroid = (String) map.get("attach");
            List<Store> storeList = storeService.getListByObject("getByStoreName",stroid);
            if (WXPayUtil.isSignatureValid(map, storeList.get(0).getWxKey())) {
                String resXml = "";
                if ("SUCCESS".equals((String) map.get("result_code"))) {
                    String orderid = (String) map.get("out_trade_no");
                    System.out.println(orderid);
                    orderMasterService.updateOrderById(orderid);

                    for (int i=0;i<2;i++) {

                        List<OrderDetail> orderDetails = orderDetailService.queryOrderDetailByOrderId(orderid);
                        String content = "";
                        //打印内容
                        content += "<CB>";
                        content += orderMasterService.queryOrderById(orderid).getShopname();
                        content += "</CB><BR>";
                        content += "<CB>";
                        content += "桌号:";
                        content += orderMasterService.queryOrderById(orderid).getZh();
                        content += "号";
                        content += "</CB><BR>";
                        content += "名称　　　　　 单价  数量 金额<BR>";
                        content += "--------------------------------<BR>";
                        for (OrderDetail orderDetail : orderDetails) {
                            content += orderDetail.getProductName() + "     " + orderDetail.getProductPrice() + "    " + orderDetail.getProductQuantity() + "    " + orderDetail.getProductPrice() * orderDetail.getProductQuantity() + "<BR>";
                        }
                        content += "备注：";
                        content += orderMasterService.queryOrderById(orderid).getMsg();
                        content += "<BR>";
                        //content += orderDetails.toString();
                        content += "--------------------------------<BR>";
                        content += "合计：" + orderMasterService.queryOrderById(orderid).getOrderAmount() + "元<BR>";

                        content += "订餐时间：";
                        content += orderMasterService.queryOrderById(orderid).getCreateTime();
                        content += "<BR>";
                        PrintService.print(orderMasterService.queryOrderById(orderid).getSn(), "http://api.feieyun.cn/Api/Open/", orderMasterService.queryOrderById(orderid).getUserName(), orderMasterService.queryOrderById(orderid).getUserKey(), content);

                    }
                    resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    out.write(resXml.getBytes());
                    out.flush();
                    out.close();
                    response.getWriter().write(resXml);
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    out.write(resXml.getBytes());
                    out.flush();
                    out.close();
                }
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } else {
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.flush();
                out.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.flush();
            out.close();
        }
    }





    /**
     * @Description 微信浏览器内微信支付/公众号支付(JSAPI)
     * @param request
     * @return Map
     */
    @RequestMapping(value = "/orders")
    public @ResponseBody Map<String, String> orders(HttpServletRequest request, HttpServletResponse response) {
        try {

            String openId = request.getParameter("openid");
            String appid = request.getParameter("appid");
            String mch_id = request.getParameter("mch_id");
            String key = request.getParameter("key");
            System.out.println(openId);
            System.out.println(key);
            // 拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            // 获取请求ip地址
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip.indexOf(",") != -1) {
                String[] ips = ip.split(",");
                ip = ips[0].trim();
            }

            paraMap.put("appid",appid); // 商家平台ID
            paraMap.put("body", "店铺-薯条"); // 商家名称-销售商品类目、String(128)
            paraMap.put("mch_id", mch_id); // 商户ID
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr()); // UUID
            paraMap.put("openid", openId);
            paraMap.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));// 订单号,每次都不同
            paraMap.put("spbill_create_ip", ip);
            paraMap.put("total_fee", "1"); // 支付金额，单位分
            paraMap.put("trade_type", "JSAPI"); // 支付类型
            paraMap.put("notify_url", "https://www.lssell.cn/tt3/diancan/order/weChatPay");// 此路径是微信服务器调用支付结果通知路径随意写
            String sign = WXPayUtil.generateSignature(paraMap, key);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);// 将所有参数(map)转xml格式

            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

            System.out.println("xml为：" + xml);

            // String xmlStr = HttpRequest.sendPost(unifiedorder_url,
            // xml);//发送post请求"统一下单接口"返回预支付id:prepay_id

            String xmlStr = HttpUtils.httpRequest(unifiedorder_url, "GET", xml);

            System.out.println("xmlStr为：" + xmlStr);

            // 以下内容是返回前端页面的json数据
            String prepay_id = "";// 预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }

            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appid);
            payMap.put("timeStamp", getCurrentTimestamp() + "");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, key);
            payMap.put("paySign", paySign);
            //将这个6个参数传给前端
            return payMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * @Title: callBack
//     * @Description: 支付完成的回调函数
//     * @param:
//     * @return:
//     */
//    @RequestMapping("/notify")
//    public String callBack(HttpServletRequest request, HttpServletResponse response) {
//        // System.out.println("微信支付成功,微信发送的callback信息,请注意修改订单信息");
//        InputStream is = null;
//        try {
//
//            is = request.getInputStream();// 获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
//            String xml = WXPayUtil.InputStream2String(is);
//            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);// 将微信发的xml转map
//
//            System.out.println("微信返回给回调函数的信息为："+xml);
//
//            if (notifyMap.get("result_code").equals("SUCCESS")) {
//                String ordersSn = notifyMap.get("out_trade_no");// 商户订单号
//                String amountpaid = notifyMap.get("total_fee");// 实际支付的订单金额:单位 分
//                BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);// 将分转换成元-实际支付金额:元
//
//                /*
//                 * 以下是自己的业务处理------仅做参考 更新order对应字段/已支付金额/状态码
//                 */
//                System.out.println("===notify===回调方法已经被调！！！");
//
//            }
//
//            // 告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
//            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return null;
//    }
//
//}
}
