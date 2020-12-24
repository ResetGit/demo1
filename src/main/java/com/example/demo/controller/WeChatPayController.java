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
import com.example.demo.util.WeChatPayUtil;
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

    @Autowired
    private WeChatPayUtil weChatPayUtil;

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
            System.out.println(map);
            String stroid = (String) map.get("attach");
            System.out.println(stroid);
            List<Store> storeList = storeService.getListByObject("getByStoreName",stroid);
            if (WXPayUtil.isSignatureValid(map, storeList.get(0).getWxKey())) {
                String resXml = "";
                if ("SUCCESS".equals((String) map.get("result_code"))) {
                    System.out.println(map);
                    String orderid = (String) map.get("out_trade_no");
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
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    out.write(resXml.getBytes());
                    out.flush();
                    out.close();
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } else {
                System.out.println("通知签名验证失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
