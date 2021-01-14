package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.BossConfig;
import com.example.demo.config.printjk;
import com.example.demo.mapper.OrderComboMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.*;
import com.example.demo.service.Imp.PrintService;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.HttpUtils;
import com.example.demo.util.OpenIdJson;
import com.example.demo.util.PayUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;
import com.example.demo.util.RequestMethod;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.demo.util.HttpUtils.*;
import static com.github.wxpay.sdk.WXPayUtil.generateNonceStr;
import static com.github.wxpay.sdk.WXPayUtil.mapToXml;

@RestController
public class RechargeController {
//    static PrintUtil4 p = new PrintUtil4();
    @Autowired
    private BossConfig alipayConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderComboService orderComboService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderMasterAliService orderMasterAliService;

    @Autowired
    private OrderDetailAliService orderDetailAliService;

    @Autowired
    private OrderComboMapper orderComboMapper;

    @Autowired
    private StoreService storeService;

    @CrossOrigin
    @RequestMapping("orderh5")
    public Object doPost(HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) throws ServletException, IOException {
        String order = Sid.next();      //随机生成订单
        String money = httpRequest.getParameter("money");             //总价
        String user = httpRequest.getParameter("user");
        String[] map = money.split(":");
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("https://www.lssell.cn/tt3/h5/");
        alipayRequest.setNotifyUrl(alipayConfig.getZfbNOTIFYURL());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"" + order + "\"," +
                " \"total_amount\":\"" + map[1] + "\"," +
                " \"subject\":\"" + user + ":" + map[0] + ":" + map[2] + ":" + map[3] + "\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"," +
                "      \"goods_detail\":[{" +
                "\"goods_name\":\"" + user + "\"," +
                "        }]" +
                " }");//填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayConfig.getCharset());
//        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();
        return form;
    }

    /**
     * 支付宝服务器异步通知url
     *
     * @throws Exception
     */
    @RequestMapping(value = "/notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取支付宝发送过来的信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        //循环获取到所有的值
        for (String str : requestParams.keySet()) {
            String name = str;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(), alipayConfig.getCharset(), alipayConfig.getSign_type());
        //boolean类型signVerified为true时 则验证成功
        if (signVerified) {
            //获取到支付的状态 TRADE_SUCCESS则支付成功
            String trade_status = request.getParameter("trade_status");
            if (trade_status.equals("TRADE_SUCCESS")) {
                String user = request.getParameter("subject");
                String gmtcreate = request.getParameter("gmt_create");
                String out_trade_no = request.getParameter("out_trade_no");
                Example e = new Example(OrderCombo.class);
                Example.Criteria criteria = e.createCriteria();
                criteria.andEqualTo("orderid",out_trade_no);
                List<OrderCombo> list = orderComboMapper.selectByExample(e);
                String buyer_pay_amount = request.getParameter("buyer_pay_amount");
                String[] username = user.split(":");

                if(list.size()==0) {
                    Map kh = new HashMap();
                    kh.put("username", username[0]);
                    kh.put("day", username[2]);
                    kh.put("shopnumber", username[3]);
                    userService.updateByObject("editiscombo", kh);
                    Map role = new HashMap();
                    List<User> userList = userService.getListByObject("getByName", kh.get("username"));
                    role.put("user_id", userList.get(0).getId());
                    role.put("role_id", "2");
                    List<UserRole> userList1 = userRoleService.getListByObject("getByUserId", userList.get(0).getId());
                    if (userList1.size() == 0) {
                        userRoleService.addByObject("addUserRole", role, true);
                    }
                    Map map = new HashMap();
                    map.put("comboname", username[1]);
                    map.put("username", username[0]);
                    map.put("shopnumber", username[3]);
                    map.put("gmtcreate", gmtcreate);
                    map.put("outtradeno", out_trade_no);
                    map.put("comboprice", buyer_pay_amount);
                    orderComboService.addOrderCombo(map);
                }

            } else {

            }
        }
        //签名验证失败
        else {

        }
    }


    @RequestMapping("/codeopenid")
    public Object codeopenid(@RequestBody Map map) throws Exception {
        String result = "";
        System.out.println(map);
        result = HttpUtil.doGet(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + map.get("appid") + "&secret=" + map.get("secret") + "&code="
                        + map.get("code")
                        + "&grant_type=authorization_code", null);
        System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result, OpenIdJson.class);
        System.out.println("openid=" + openIdJson.getOpenid());
        return openIdJson.getOpenid();
    }

    /**
     * 微信充值
     *
     * @return
     */
    @RequestMapping("/wxczgzh")
    public Map wxPrepayx(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderid = Sid.next();

        String money = (String) map.get("money");
        String user = (String) map.get("user");
        String openid = (String) map.get("openid");
        System.out.println(map);
        String pricelist[] = money.split(":");
        String price = null;
        String pricelist2= pricelist[1].replace(".",":");
        String[] listjg = pricelist2.split(":");
        if ("0".equals(listjg[0])) {
            price = listjg[1] + "0";
        } else {
            price = listjg[0] + "00";
        }

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
        Map map1 = new HashMap();
        map1.put("appid", alipayConfig.getWECHAT_APPID());
        map1.put("mch_id", alipayConfig.getWECHAT_MACH_ID());
        map1.put("nonce_str", WXPayUtil.generateNonceStr());
        map1.put("body", "充值账号:"+user);
        map1.put("sign_type", alipayConfig.getWxsign_type());
        map1.put("spbill_create_ip", ip);
        map1.put("out_trade_no", orderid);
        map1.put("total_fee", price);
        map1.put("trade_type", "JSAPI");
        map1.put("attach", money+":"+user);
        map1.put("notify_url", alipayConfig.getNotifyurl());
        map1.put("openid", openid);
        String sign = WXPayUtil.generateSignature(map1, alipayConfig.getWECHAT_key());// 生成签名 PAY_API_SECRET=微信支付相关API调用时使用的秘钥
        map1.put("sign", sign);  // 参数配置 我直接写成"sign"
        String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信统一下单URL
        String xml = mapToXml(map1);
        //请求微信统一下单接口
        String xmlStr = HttpUtils.httpRequest(unifiedorderUrl, "POST", xml);
        Map map2 = HttpUtils.doXMLParse(xmlStr);
        String return_code = (String) map2.get("return_code");//返回状态码
        String result_code = (String) map2.get("result_code");//返回状态码
        String err_code = (String) map2.get("err_code");//返回状态码
        String err_code_des = (String) map2.get("err_code_des");//返回状态码
        if (return_code.equals("SUCCESS") || return_code.equals(result_code)) {
            // 业务结果
            String prepay_id = (String) map2.get("prepay_id");//返回的预付单信息
            Map payMap = new HashMap();
            payMap.put("appId", alipayConfig.getWECHAT_APPID());  // 参数配置
            payMap.put("timeStamp", getCurrentTimestamp() + "");  //时间
            payMap.put("nonceStr", generateNonceStr());  // 获取随机字符串
            payMap.put("package", "prepay_id=" + prepay_id);
            payMap.put("signType", "MD5");
            String paySign = WXPayUtil.generateSignature(payMap, alipayConfig.getWECHAT_key()); //第二次生成签名
            payMap.put("paySign", paySign);
            payMap.put("prepayId", prepay_id);
            System.out.println(payMap);
            return payMap;   //返回给前端，让前端去调支付 ，完成后你去调支付查询接口，看支付结果，处理业务。
        } else {
            //打印失败日志
        }
        return null;

    }

    /**
     * 微信支付成功回调函数
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/weixinotify")
    public void wxNotifyx(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        Map map = PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String sign = PayUtil.sign(validStr, alipayConfig.getWECHAT_key(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
                // 得到返回的参数
                String orderNo = (String) map.get("out_trade_no");
                Example e = new Example(OrderCombo.class);
                Example.Criteria criteria = e.createCriteria();
                criteria.andEqualTo("orderid",orderNo);
                List<OrderCombo> list = orderComboMapper.selectByExample(e);
                String body = (String) map.get("attach");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date());
                String[] username = body.split(":");
                if(list.size()==0) {
                    Map kh = new HashMap();
                    kh.put("username", username[4]);
                    kh.put("day", username[2]);
                    kh.put("shopnumber", username[3]);
                    userService.updateByObject("editiscombo", kh);
                    Map role = new HashMap();
                    List<User> userList = userService.getListByObject("getByName", kh.get("username"));
                    role.put("user_id", userList.get(0).getId());
                    role.put("role_id", "2");
                    List<UserRole> userList1 = userRoleService.getListByObject("getByUserId", userList.get(0).getId());
                    if (userList1.size() == 0) {
                        userRoleService.addByObject("addUserRole", role, true);
                    }
                    Map map1 = new HashMap();
                    map1.put("comboname", username[0]);
                    map1.put("username", username[4]);
                    map1.put("gmtcreate", date);
                    map1.put("outtradeno", orderNo);
                    map1.put("shopnumber", username[3]);
                    map1.put("comboprice", username[1]);
                    orderComboService.addOrderCombo(map1);
                    System.out.println("=====================================================");
                    System.out.println(map);
                    System.out.println("支付成功");
                    System.out.println("=====================================================");
                    /**回调逻辑代码编写*/
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                }
            } else {
                System.out.println("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }


    //=============================================================================================================================
    //微信支付的接口



    /**
     * 生成订单「微信内部浏览器」
     *
     * @return
     */
    @RequestMapping("/testweChatPay")
    public Map wxPrepay(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderid = Sid.next();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        String appid = (String) map.get("appid");
        String mch_id = (String) map.get("mch_id");
        System.out.println("appid为+" + appid);
        String money = (String) map.get("money");             //总价
        String remarks = (String) map.get("remarks");         //备注
        String theTable = (String) map.get("thetable");       //桌号
        String shopname = (String) map.get("storeName");         //用户店名
        String userName = (String) map.get("userName");         //打印机账号
        String key = (String) map.get("key");                   //微信密钥
        String key2 = (String) map.get("key2");                   //打印机key
        String sn = (String) map.get("sn");                     //打印机sn
        String storeid = (String) map.get("storeId");           //店铺id
        String printetoken = (String)map.get("printertoken");
        List<Store> storeList = storeService.getListByObject("getByStoreName", storeid);
        System.out.println("总价" + money);
        String pricelist = money.replace(".", ":");
        String price = null;
        String[] listjg = pricelist.split(":");
        if ("0".equals(listjg[0])) {
            price = listjg[1] + "0";
        } else {
            price = listjg[0] + "00";
        }
        //创建订单
        OrderMaster masterAli = new OrderMaster();
        masterAli.setPayStatus(0);
        masterAli.setOrderStatus(0);
        masterAli.setOrderAmount(Float.parseFloat(money));
        masterAli.setBuyerOpenid((String) map.get("openid"));
        masterAli.setAppid(storeList.get(0).getWxAppId());
        masterAli.setOrderId(orderid);
        masterAli.setMsg(remarks);
        masterAli.setZh(theTable);
        masterAli.setShopname(storeList.get(0).getName());
        masterAli.setStoreId(storeid);
        masterAli.setSn(sn);
        masterAli.setUserKey(key);
        masterAli.setCreateTime(date);
        masterAli.setUpdateTime(date);
        masterAli.setUserName(userName);
        masterAli.setPrintertoken(printetoken);
        orderMasterService.saveOrder(masterAli);       //创建订单

        String data = (String) map.get("data");
        JSONArray array = JSONArray.parseArray(data);
        List<Map> arraylist = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            Map map2 = new HashMap();
            String list = array.getString(i).replace("{", "");
            String list2 = list.replace("}", "");
            String list3 = list2.replace("\"", "");
            String list4 = list3.replace(",", ":");
            String list5[] = list4.split(":");
            for (int j = 0; j < list5.length; j++) {
                int num = j % 2;
                if (num == 0) {
                    map2.put(list5[j], list5[j + 1]);
                }
            }
            arraylist.add(map2);
        }
        System.out.println(arraylist);
        System.out.println("=======================================================");
//
        for (int i = 0; i < arraylist.size(); i++) {
            if (!arraylist.get(i).get("quantity").equals("0")) {
                OrderDetail orderDetailAli = new OrderDetail();
                String productId = (String) arraylist.get(i).get("productId");
                String productName = (String) arraylist.get(i).get("productName");
                String quantity = (String) arraylist.get(i).get("quantity");
                String productPrice = (String) arraylist.get(i).get("productPrice");
                System.out.println("测试" + arraylist.get(i));
                orderDetailAli.setProductId(productId);
                orderDetailAli.setAppid(storeList.get(0).getZfbAppId());
                orderDetailAli.setOrderId(orderid);
                orderDetailAli.setProductName(productName);
                orderDetailAli.setProductPrice(Float.parseFloat(productPrice.toString()));
                orderDetailAli.setProductQuantity(Integer.parseInt(quantity.toString()));
                orderDetailService.saveOrderDetail(orderDetailAli);
            }
        }
        System.out.println("=======================================================");

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
        System.out.println("key为" + key);
        Map map1 = new HashMap();
        map1.put("appid", "wxa3d82c2926894876");
        map1.put("mch_id", "1605530736");
        map1.put("sub_mch_id",mch_id);
        map1.put("nonce_str", WXPayUtil.generateNonceStr());
        map1.put("body", "点餐-支付");
        map1.put("sign_type", "MD5");
        map1.put("spbill_create_ip", ip);
        map1.put("out_trade_no", orderid);
        map1.put("total_fee", price);
        map1.put("trade_type", "JSAPI");
        map1.put("attach", storeid);
        map1.put("notify_url", "https://www.lssell.cn/tt3/diancan/weChatPay1");
        map1.put("openid", map.get("openid"));
        String sign = WXPayUtil.generateSignature(map1, key2);// 生成签名 PAY_API_SECRET=微信支付相关API调用时使用的秘钥
        map1.put("sign", sign);  // 参数配置 我直接写成"sign"
        System.out.println("第一次验签" + sign);
        String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信统一下单URL
        System.out.println("接收到的数据为：" + map1);
        String xml = mapToXml(map1);
        System.out.println("第一次数据" + xml);
        //请求微信统一下单接口
        String xmlStr = HttpUtils.httpRequest(unifiedorderUrl, "POST", xml);
        Map map2 = HttpUtils.doXMLParse(xmlStr);
        String return_code = (String) map2.get("return_code");//返回状态码
        String result_code = (String) map2.get("result_code");//返回状态码
        String err_code = (String) map2.get("err_code");//返回状态码
        String err_code_des = (String) map2.get("err_code_des");//返回状态码
        System.out.println("第二次数据" + xmlStr);
        System.out.println("生成的数据" + map2);
        if (return_code.equals("SUCCESS") || return_code.equals(result_code)) {
            // 业务结果
            String prepay_id = (String) map2.get("prepay_id");//返回的预付单信息
            Map payMap = new HashMap();
            payMap.put("appId", appid);  // 参数配置
            payMap.put("timeStamp", getCurrentTimestamp() + "");  //时间
            payMap.put("nonceStr", generateNonceStr());  // 获取随机字符串
            payMap.put("package", "prepay_id=" + prepay_id);
            payMap.put("signType", "MD5");
            String paySign = WXPayUtil.generateSignature(payMap, key2); //第二次生成签名
            System.out.println("第二次验签" + paySign);
            payMap.put("paySign", paySign);
            payMap.put("prepayId", prepay_id);
            System.out.println(payMap);
            return payMap;   //返回给前端，让前端去调支付 ，完成后你去调支付查询接口，看支付结果，处理业务。
        } else {
            //打印失败日志
        }
        return null;

    }

    /**
     * 微信支付 回调函数
     */
    /**
     * 微信支付成功回调函数
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/weChatPay1")
    public void weChatPay1(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        Map map = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String stroid = (String) map.get("attach");
            List<Store> storeList = storeService.getListByObject("getByStoreName", stroid);
            String sign = PayUtil.sign(validStr, storeList.get(0).getWxKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
            // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(sign.equals(map.get("sign"))){
                int b1 = 14;
                int b2 = 6;
                int b3 = 3;
                int b4 = 6;
                String orderid = (String) map.get("out_trade_no");
                System.out.println(orderid);
                if(orderMasterService.queryOrderById(orderid).getPayStatus()==0) {
                    orderMasterService.updateOrderById(orderid);
                    for (int i = 0; i < 2; i++) {

                        List<OrderDetail> orderDetails = orderDetailService.queryOrderDetailByOrderId(orderid);
                        RequestMethod.getInstance().init(orderMasterService.queryOrderById(orderid).getUserName(),orderMasterService.queryOrderById(orderid).getUserKey());

//                        String content = "";
//                        //打印内容
//                        content += "<CB>";
//                        content += orderMasterService.queryOrderById(orderid).getShopname();
//                        content += "</CB><BR>";
//                        content += "<CB>";
//                        content += "桌号:";
//                        content += orderMasterService.queryOrderById(orderid).getZh();
//                        content += "号";
//                        content += "</CB><BR>";
//                        content += "名称　　　　　 单价  数量 金额<BR>";
//                        content += "--------------------------------<BR>";
//                        for (OrderDetail orderDetail : orderDetails) {
//                            //遍历出来的数据
//                            String title = orderDetail.getProductName();
//                            String price = orderDetail.getProductPrice().toString();
//                            String quantity = orderDetail.getProductQuantity().toString();
//                            String total = null;
//                            BigDecimal total2 = new BigDecimal("0");
//                            BigDecimal a1 = new BigDecimal(price);
//                            BigDecimal a2 = new BigDecimal(quantity);
//                            total2 = a1.multiply(a2);
//                            //打印机的工具类
//                            price = printjk.addSpace(price, b2);
//                            quantity = printjk.addSpace(quantity, b3);
//                            total = printjk.addSpace(total2.toString(), b4);
//
//                            String otherStr = " " + price + quantity + " " + total;
//
//                            int tl = 0;
//                            try {
//                                tl = title.getBytes("GBK").length;
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            int spaceNum = (tl / b1 + 1) * b1 - tl;
//                            if (tl < b1) {
//                                for (int k = 0; k < spaceNum; k++) {
//                                    title += " ";
//                                }
//                                title += otherStr;
//                            } else if (tl == b1) {
//                                title += otherStr;
//                            } else {
//                                List<String> list = null;
//                                if (printjk.isEn(title)) {
//                                    list = printjk.getStrList(title, b1);
//                                } else {
//                                    list = printjk.getStrList(title, b1 / 2);
//                                }
//                                String s0 = printjk.titleAddSpace(list.get(0));
//                                title = s0 + otherStr + "<BR>";// 添加 单价 数量 总额
//                                String s = "";
//                                for (int k = 1; k < list.size(); k++) {
//                                    s += list.get(k);
//                                }
//                                try {
//                                    s = printjk.getStringByEnter(b1, s);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                title += s;
//                            }
//
//                            content += title + "<BR>";
//                        }
//                        content += "备注：";
//                        content += orderMasterService.queryOrderById(orderid).getMsg();
//                        content += "<BR>";
//                        //content += orderDetails.toString();
//                        content += "--------------------------------<BR>";
//                        content += "合计：" + orderMasterService.queryOrderById(orderid).getOrderAmount() + "元<BR>";
//
//                        content += "点餐时间：";
//                        content += orderMasterService.queryOrderById(orderid).getCreateTime();
//                        content += "<BR>";
//                        PrintService.print(orderMasterService.queryOrderById(orderid).getSn(), "http://api.feieyun.cn/Api/Open/", orderMasterService.queryOrderById(orderid).getUserName(), orderMasterService.queryOrderById(orderid).getUserKey(), content);
                        //字符串拼接
                        StringBuilder sb1=new StringBuilder();
                        sb1.append("@@2<center>"+orderMasterService.queryOrderById(orderid).getShopname()+"\r\n</center>");
                        sb1.append("@@2<center>桌号:"+orderMasterService.queryOrderById(orderid).getZh()+"号\r\n</center>");
                        sb1.append("--------------------------------\r\n");
                        sb1.append("<table>");
                        sb1.append("<tr>");
                        sb1.append("<td>");
                        sb1.append("菜品");
                        sb1.append("</td>");
                        sb1.append("<td>");
                        sb1.append("单价");
                        sb1.append("</td>");
                        sb1.append("<td>");
                        sb1.append("数量");
                        sb1.append("</td>");
                        sb1.append("<td>");
                        sb1.append("总计");
                        sb1.append("</td>");
                        sb1.append("</tr>");
                        for(OrderDetail orderDetail : orderDetails){
                            String title = orderDetail.getProductName();
                            String price = orderDetail.getProductPrice().toString();
                            String quantity = orderDetail.getProductQuantity().toString();
                            BigDecimal total2 = new BigDecimal("0");
                            BigDecimal a1 = new BigDecimal(price);
                            BigDecimal a2 = new BigDecimal(quantity);
                            total2 = a1.multiply(a2);
                            sb1.append("<tr>");
                            sb1.append("<td>"+title+"</td>");
                            sb1.append("<td>"+price+"</td>");
                            sb1.append("<td>"+quantity+"</td>");
                            sb1.append("<td>"+total2+"</td>");
                            sb1.append("</tr>");
                        }
                        sb1.append("</table>");
                        sb1.append("--------------------------------\r\n");
                        sb1.append("备注："+orderMasterService.queryOrderById(orderid).getMsg()+"\r\n");
                        sb1.append("合计：￥"+orderMasterService.queryOrderById(orderid).getOrderAmount() +"元\r\n");
                        sb1.append("点餐时间："+orderMasterService.queryOrderById(orderid).getCreateTime() +"\r\n");
                        String print = RequestMethod.getInstance().printIndex(orderMasterService.queryOrderById(orderid).getPrintertoken(),orderMasterService.queryOrderById(orderid).getSn(),sb.toString(),Sid.next());
                    }
                }
                System.out.println("=====================================================");
                System.out.println(map);
                System.out.println("支付成功");
                System.out.println("=====================================================");
                /**回调逻辑代码编写*/
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            } else {
                System.out.println("微信支付回调失败!签名不一致");
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }



    //打印测试
    @RequestMapping("testprint2")
    public void print2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int b1 = 14;
        int b2 = 6;
        int b3 = 3;
        int b4 = 6;
        String orderid = "210107138366412652544";
        orderMasterService.updateOrderById(orderid);

        for (int i = 0; i < 2; i++) {

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
                //遍历出来的数据
                String title = orderDetail.getProductName();
                String price = orderDetail.getProductPrice().toString();
                String quantity = orderDetail.getProductQuantity().toString();
                String total = null;
                BigDecimal total2 = new BigDecimal("0");
                BigDecimal a1 = new BigDecimal(price);
                BigDecimal a2 = new BigDecimal(quantity);
                total2 = a1.multiply(a2);
                //打印机的工具类
                price = printjk.addSpace(price, b2);
                quantity = printjk.addSpace(quantity, b3);
                total = printjk.addSpace(total2.toString(), b4);

                String otherStr =  " " + price + quantity+ " " + total;

                int tl = 0;
                try {
                    tl = title.getBytes("GBK").length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int spaceNum = (tl / b1 + 1) * b1 - tl;
                if (tl < b1) {
                    for (int k = 0; k < spaceNum; k++) {
                        title += " ";
                    }
                    title += otherStr;
                } else if (tl == b1) {
                    title += otherStr;
                } else {
                    List<String> list = null;
                    if (printjk.isEn(title)) {
                        list = printjk.getStrList(title, b1);
                    } else {
                        list = printjk.getStrList(title, b1 / 2);
                    }
                    String s0 = printjk.titleAddSpace(list.get(0));
                    title = s0 + otherStr + "<BR>";// 添加 单价 数量 总额
                    String s = "";
                    for (int k = 1; k < list.size(); k++) {
                        s += list.get(k);
                    }
                    try {
                        s = printjk.getStringByEnter(b1, s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    title += s;
                }

                content += title + "<BR>";
            }
            content += "备注：";
            content += orderMasterService.queryOrderById(orderid).getMsg();
            content += "<BR>";
            //content += orderDetails.toString();
            content += "--------------------------------<BR>";
            content += "合计：" + orderMasterService.queryOrderById(orderid).getOrderAmount() + "元<BR>";

            content += "点餐时间：";
            content += orderMasterService.queryOrderById(orderid).getCreateTime();
            content += "<BR>";
            PrintService.print(orderMasterService.queryOrderById(orderid).getSn(), "http://api.feieyun.cn/Api/Open/", orderMasterService.queryOrderById(orderid).getUserName(), orderMasterService.queryOrderById(orderid).getUserKey(), content);

        }
    }

}


