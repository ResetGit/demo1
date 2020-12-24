package com.example.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.BossConfig;
import com.example.demo.pojo.User;
import com.example.demo.pojo.UserRole;
import com.example.demo.service.OrderComboService;
import com.example.demo.service.UserRoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.HttpUtils;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.wxpay.sdk.WXPayUtil.generateSignature;
import static com.github.wxpay.sdk.WXPayUtil.xmlToMap;

@RestController
public class RechargeController {

    @Autowired
    private BossConfig alipayConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderComboService orderComboService;

    @Autowired
    private UserRoleService userRoleService;


    @RequestMapping("testprint")
    public Object print(){

        return "测试";
    }

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
        alipayRequest.setReturnUrl("https://www.lssell.cn/tt3/diancan/login.html");
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyurl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\""+order+"\"," +
                " \"total_amount\":\""+map[1]+"\"," +
                " \"subject\":\""+user+":"+map[0]+":"+map[2]+":"+map[3]+"\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"," +
                "      \"goods_detail\":[{" +
                "\"goods_name\":\""+user+"\"," +
                "        }]" +
                " }");//填充业务参数
        String form="";
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
     * @throws Exception
     */
    @RequestMapping(value="/notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取支付宝发送过来的信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        //循环获取到所有的值
        for(String str:requestParams.keySet()) {
            String name =str;
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
        if(signVerified) {
            //获取到支付的状态 TRADE_SUCCESS则支付成功
            String trade_status =request.getParameter("trade_status");
            if (trade_status.equals("TRADE_SUCCESS")){
                System.out.println("支付成功");
                String user = request.getParameter("subject");
                String gmtcreate = request.getParameter("gmt_create");
                String out_trade_no = request.getParameter("out_trade_no");
                String buyer_pay_amount = request.getParameter("buyer_pay_amount");
                String[] username = user.split(":");
                System.out.println(username);
                System.out.println(username[3]);
                Map kh = new HashMap();
                kh.put("username",username[0]);
                kh.put("day",username[2]);
                kh.put("shopnumber",username[3]);
                userService.updateByObject("editiscombo",kh);
                Map role = new HashMap();
                List<User> userList = userService.getListByObject("getByName",kh.get("username"));
                role.put("user_id",userList.get(0).getId());
                role.put("role_id","2");
                List<UserRole> userList1 = userRoleService.getListByObject("getByUserId",userList.get(0).getId());
                if(userList1.size()==0) {
                    userRoleService.addByObject("addUserRole", role, true);
                }
                Map map = new HashMap();
                map.put("comboname",username[1]);
                map.put("username",username[0]);
                map.put("shopnumber",username[3]);
                map.put("gmtcreate",gmtcreate);
                map.put("outtradeno",out_trade_no);
                map.put("comboprice",buyer_pay_amount);
                orderComboService.addOrderCombo(map);


            }else {
                System.out.println("支付失败");
            }
        }
        //签名验证失败
        else {
            System.out.println(AlipaySignature.getSignCheckContentV1(params));
        }
    }

    @CrossOrigin
    @RequestMapping("weChatPayh5")
    @ResponseBody
    public Map<String, String> getPrePayInfoH5(HttpServletRequest request) {
        try{
            Map<String, String> map = Maps.newHashMap();
            String orderid = Sid.next();
            String money = request.getParameter("money");
            String user = request.getParameter("user");
            String[] name = money.split(":");
            String money1 = name[1];
            System.out.println(money1);
            String pricelist =money1.replace(".",":");
            String price = null;
            String[] list = pricelist.split(":");
            if("0".equals(list[0])){
                price=list[1]+"0";
            }else {
                price=list[0]+"00";
            }
            System.out.println(price);
            map.put("appid", alipayConfig.getWECHAT_APPID());
            map.put("mch_id", alipayConfig.getWECHAT_MACH_ID());
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", ""+user+":"+name[0]+":"+name[2]+":"+name[3]+"");
//            map.put("body", "1");
            map.put("attach",""+user+":"+name[0]+":"+name[2]+":"+name[3]);
            map.put("spbill_create_ip", "8.129.121.95");
            map.put("out_trade_no", orderid);
            map.put("total_fee", price);
            map.put("trade_type", "MWEB");
            map.put("notify_url", alipayConfig.getWxNOTIFYURL());
            map.put("scene_info", "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"https://www.lssell.cn/tt3/diancan/\",\"wap_name\": \"点餐\"}}");
            String sign = generateSignature(map, alipayConfig.getWECHAT_key());// 生成签名 PAY_API_SECRET=微信支付相关API调用时使用的秘钥
            map.put("sign", sign);
            String xml = WXPayUtil.mapToXml(map);//将所有参数(map)转xml格式
            String unifiedorderUrl = alipayConfig.getUNIFIED_ORDER_URL(); // 微信统一下单URL
            String xmlStr = HttpUtils.httpRequest(unifiedorderUrl, "POST", xml);

            //以下内容是返回前端页面的json数据
            String mweb_url = "";//跳转链接
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map1 = xmlToMap(xmlStr);
                mweb_url = (String) map1.get("mweb_url");
                //支付完返回浏览器跳转的地址，如跳到查看订单页面
                String redirect_url = "lssell.cn/tt3/diancan/login.html";
                String redirect_urlEncode =  URLEncoder.encode(redirect_url,"utf-8");//对上面地址urlencode
                mweb_url = mweb_url + "&redirect_url=" + redirect_urlEncode;//拼接返回地址
            }

            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("mweb_url", mweb_url);
            return  payMap;
        } catch (Exception e){

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
            if (WXPayUtil.isSignatureValid(map, alipayConfig.getWECHAT_key())) {
                String resXml = "";
                if ("SUCCESS".equals((String) map.get("result_code"))) {
                    String orderNo = (String) map.get("out_trade_no");
                    String body = map.get("attach");
                    System.out.println(map);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = df.format(new Date());
                    String[] username = body.split(":");
                    Map kh = new HashMap();
                    kh.put("username",username[0]);
                    kh.put("day",username[2]);
                    kh.put("shopnumber",username[3]);
                    System.out.println(username);
                    System.out.println("=================");
                    System.out.println(username[3]);
                    userService.updateByObject("editiscombo",kh);
                    Map role = new HashMap();
                    List<User> userList = userService.getListByObject("getByName",kh.get("username"));
                    role.put("user_id",userList.get(0).getId());
                    role.put("role_id","2");
                    List<UserRole> userList1 = userRoleService.getListByObject("getByUserId",userList.get(0).getId());
                    if(userList1.size()==0) {
                        userRoleService.addByObject("addUserRole", role, true);
                    }
                    Map map1 = new HashMap();
                    map1.put("comboname",username[1]);
                    map1.put("username",username[0]);
                    map1.put("gmtcreate",date);
                    map1.put("outtradeno",orderNo);
                    map1.put("shopnumber",username[3]);
                    map1.put("comboprice",username[2]);
                    orderComboService.addOrderCombo(map1);
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
