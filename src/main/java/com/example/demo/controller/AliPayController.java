package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayOpenAppQrcodeCreateResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.AlipayConfig;
import com.example.demo.config.PrintAccountConfig;
import com.example.demo.config.WeChatConfig;
import com.example.demo.mapper.SellerInfoMapper;
import com.example.demo.pojo.OrderDetailAli;
import com.example.demo.pojo.OrderMasterAli;
import com.example.demo.pojo.ShanghuInfo;
import com.example.demo.service.OrderDetailAliService;
import com.example.demo.service.OrderMasterAliService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/Ali")
@AllArgsConstructor
@Slf4j
class AliPayController {

    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private PrintAccountConfig printAccountConfig;
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private OrderMasterAliService orderMasterAliService;
    @Autowired
    private OrderDetailAliService orderDetailAliService;
    @Autowired
    private SellerInfoMapper sellerInfoMapper;

    /**
     * 授权码
     * @param
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping("/getInfo")
    public Object getInfo(@RequestBody JSONObject list) throws AlipayApiException {
        //使用支付宝小程序的固定方法获取auth_code
        String auth_code = (String) list.get("auth_code");
        System.out.println("auth_code:"+auth_code);
        System.out.println(alipayConfig.getUrl());
        System.out.println(alipayConfig.getApp_private_key());
        System.out.println(alipayConfig.getAlipay_public_key());
        if(auth_code==null||auth_code.length()==0) {
            return "请求参数auth_code不能为空";
        }else {
            //String serverUrl, String appId, String privateKey, String format,String charset, String alipayPublicKey, String signType
            //实例化客户端 参数：正式环境URL,Appid,商户私钥 PKCS8格式,字符编码格式,字符格式,支付宝公钥,签名方式
            AlipayClient  alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            // 值为authorization_code时，代表用code换取
            request.setGrantType("authorization_code");
            //授权码，用户对应用授权后得到的
            request.setCode(auth_code);
            //这里使用execute方法
            AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
            //刷新令牌，上次换取访问令牌时得到。见出参的refresh_token字段
            request.setRefreshToken(response.getAccessToken());
            //返回成功时 就将唯一标识返回
            if(response.isSuccess()){
                System.out.println("调用成功");
                //我这里只返回了一个字段给前端用
                Map<String,Object> map=new HashMap<>();
                map.put("userid", response.getUserId());
                return map;
            } else {
                return "调用失败";
            }
        }
    }

    /**
     * 用户唯一标识id
     * @param
     * @return
     */
    @RequestMapping("/orderpay")
    public Object pay(@RequestBody JSONObject list){
        System.out.println(list);
        String total_amount = (String)list.get("total_amount"); //总价
        String subject = (String)list.get("subject");           //标题
        String buyer_id = (String)list.get("buyer_id");         //支付宝id
        String msg = (String)list.get("msg");                   //备注
        String theTable = (String)list.get("theTable");                   //桌号
        float amount = Float.parseFloat(total_amount);
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        //设置请求参数
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        JSONObject json=new JSONObject();
        String out_trade_no = Sid.next();
        //订单号
        json.put("out_trade_no",out_trade_no);
        //金额 这里的金额是以元为单位的可以不转换但必须是字符串
        json.put("total_amount",total_amount);
        //描述
        json.put("subject","点餐");
        //用户唯一标识id 这里必须使用buyer_id 参考文档 userid
        json.put("buyer_id",buyer_id);
        //对象转化为json字符串
        String jsonStr=json.toString();
        //商户通过该接口进行交易的创建下单
        request.setBizContent(jsonStr);
        //回调地址 是能够访问到的域名加上方法名
        request.setNotifyUrl(alipayConfig.getNotifyurl());
        try {
            //使用的是execute
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            String trade_no = response.getTradeNo();
            OrderMasterAli masterAli = new OrderMasterAli();
            masterAli.setAppId(alipayConfig.getAppid());
            masterAli.setBuyerId(buyer_id);
            masterAli.setMsg(msg);
            masterAli.setOrderAmount(amount);
            masterAli.setOrderStatus(0);
            masterAli.setPayStatus(0);
            masterAli.setZh(theTable);
            masterAli.setOrderId(out_trade_no);         //暂时没有开通小程序支付先随机生成,有了直接用trade_no
            orderMasterAliService.saveOrder(masterAli);       //创建订单

            String array = list.get("data").toString();
            JsonArray jsonArray = JsonParser.parseString(array).getAsJsonArray();
            for(int i=0;i<jsonArray.size();i++){
                JsonElement je = jsonArray.get(i);
                OrderDetailAli orderDetailAli = new OrderDetailAli();
                JsonElement productId = je.getAsJsonObject().get("productId");
                JsonElement productName = je.getAsJsonObject().get("productName");
                JsonElement num = je.getAsJsonObject().get("quantity");
                JsonElement productPrice = je.getAsJsonObject().get("productPrice");
                orderDetailAli.setProductId(productId.toString());
                orderDetailAli.setAppid(alipayConfig.getAppid());
                orderDetailAli.setOrderId(out_trade_no);        //暂时没有开通小程序支付先随机生成，有了直接用trade_no
                orderDetailAli.setProductName(productName.toString().replace("\"",""));
                orderDetailAli.setProductPrice(Float.parseFloat(productPrice.toString()));
                orderDetailAli.setProductQuantity(Integer.parseInt(num.toString()));
                orderDetailAliService.saveOrderDetail(orderDetailAli);
            }
            Map<String,String> map = new HashMap<>();
            map.put("trade_no",trade_no);
            return map;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
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
                String trade_no = request.getParameter("trade_no");
                orderMasterAliService.updateOrderById(trade_no);


//                for (int i=0;i<2;i++) {
//                    printAccountConfig.getSn();
//                    OrderMaster orderMaster = new OrderMaster();
//                    //PayResponse payResponse = this.payService.create(orderMaster);
//
//                    SellerInfo sellerInfo = new SellerInfo();
//                    //sellerInfo.setUsername("aa");
//
//                    List<OrderDetailAli> orderDetails = orderDetailAliService.queryOrderDetailByOrderId(trade_no);
//                    String content = "";
//                    //打印内容
//                    content += "<CB>";
//                    content += ShopName;
//                    content += "</CB><BR>";
//                    content += "<CB>";
//                    content += "桌号:";
//                    content += orderMasterAliService.queryOrderById(trade_no).getZh();
//                    content += "号";
//                    content += "</CB><BR>";
//                    content += "名称　　　　　 单价  数量 金额<BR>";
//                    content += "--------------------------------<BR>";
//                    for (OrderDetailAli orderDetail : orderDetails) {
//                        content += orderDetail.getProductName() + "     " + orderDetail.getProductPrice() + "    " + orderDetail.getProductQuantity() + "    " + orderDetail.getProductPrice() * orderDetail.getProductQuantity() + "<BR>";
//                    }
//                    content += "备注：";
//                    content += orderMasterAliService.queryOrderById(trade_no).getMsg();
//                    content += "<BR>";
//                    //content += orderDetails.toString();
//                    content += "--------------------------------<BR>";
//                    content += "合计：" + orderMasterAliService.queryOrderById(trade_no).getOrderAmount() + "元<BR>";
//
//                    content += "订餐时间：";
//                    content += orderMasterAliService.queryOrderById(trade_no).getCreateTime();
//                    content += "<BR>";
//                    PrintService.print(printAccountConfig.getSn(), printAccountConfig.getUrl(), printAccountConfig.getUser(), printAccountConfig.getUkey(), content);
//
//                    log.info("【SN】={}", printAccountConfig.getSn());
//                    log.info("【URL】={}", printAccountConfig.getUrl());
//                    log.info("【USER】={}", printAccountConfig.getUser());
//                    log.info("【UKEY】={}", printAccountConfig.getUkey());
//                    log.info("【订单详情】={}", orderDetails);
//
//                    List<SellerInfo> sellers = this.sellerInfoMapper.select(sellerInfo);
//                }
            }else {
                System.out.println("支付失败");
            }
        }
        //签名验证失败
        else {
            System.out.println(AlipaySignature.getSignCheckContentV1(params));
        }
    }


    /**
     * 退款
     * @param
     */
    @RequestMapping("/alipayRefound")
    public void alipayRefound(@RequestBody JSONObject list) {
        String outTradeNo = (String) list.get("outTradeNo");
        String total_amount = (String)list.get("total_amount");
        String subject = (String)list.get("subject");
        //初始化
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        //构造退款的参数
        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        //订单号 这里的订单号是自己用算法生成的 和支付宝系统的订单号TradeNo只能二选一
        model.setOutTradeNo(outTradeNo);
        //系统订单号
        //model.setTradeNo(tradeNo);
        //金额
        model.setRefundAmount(total_amount);
        //退款原因
        model.setRefundReason(subject);
        //退款请求
        AlipayTradeRefundRequest request=new AlipayTradeRefundRequest();
        //参数set到请求里
        request.setBizModel(model);
        try {
            //退款返回
            AlipayTradeRefundResponse response=alipayClient.execute(request);
            System.out.println(response.getBody()+"\n");//获取到body这个订单的所有信息
            System.out.println(response.getMsg()); //这里打印的是退款的信息 是否退款成功的原因
        } catch (AlipayApiException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println("支付宝退款错误");
        }
    }

    @RequestMapping("/aliQRcode")
    public Object aliQrcode(String zh) throws AlipayApiException {
//        Integer zh = (Integer) map.get("zh");
        System.out.println(zh);
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        AlipayOpenAppQrcodeCreateRequest request = new AlipayOpenAppQrcodeCreateRequest();
        request.setBizContent("{" +
                "\"url_param\":\""+alipayConfig.getUrlparam()+"\"," +
                "\"query_param\":\"zh="+zh+"\"," +
                "\"describe\":\"点餐\"" +
                "}");
        AlipayOpenAppQrcodeCreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            return response;
        } else {
            System.out.println("调用失败");
            return "调用失败";
        }
    }
    @CrossOrigin
    @RequestMapping("orderh5")
    public String doPost(HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) throws ServletException, IOException {
        System.out.println("进入h5支付");
        String order = Sid.next();      //随机生成订单
        String money = httpRequest.getParameter("money");             //总价
        System.out.println("总价"+money);
        String remarks = httpRequest.getParameter("remarks");         //备注
        String theTable = httpRequest.getParameter("theTable");       //桌号
        String buyer_id = httpRequest.getParameter("buyer_id");         //支付宝用户唯一id
        //创建订单
        OrderMasterAli masterAli = new OrderMasterAli();
        masterAli.setPayStatus(0);
        masterAli.setOrderStatus(0);
        masterAli.setOrderAmount(Float.parseFloat(money));
        masterAli.setBuyerId("暂无");
        masterAli.setAppId(alipayConfig.getAppid());
        masterAli.setOrderId(order);
        masterAli.setMsg("暂无");
        masterAli.setZh("暂无");
        orderMasterAliService.saveOrder(masterAli);       //创建订单
//
        String data = httpRequest.getParameter("data");
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
        System.out.println("=======================================================");
//
        for(int i=0;i<arraylist.size();i++){
            if(!arraylist.get(i).get("quantity").equals("0")) {
                OrderDetailAli orderDetailAli = new OrderDetailAli();
                String productId = (String) arraylist.get(i).get("productId");
                String productName = (String) arraylist.get(i).get("productName");
                String quantity = (String) arraylist.get(i).get("quantity");
                String productPrice = (String) arraylist.get(i).get("productPrice");
                System.out.println("测试"+arraylist.get(i));
                System.out.println(arraylist.get(i).get("productName"));
                System.out.println(arraylist.get(i).get("productId"));
                System.out.println(arraylist.get(i).get("quantity"));
                System.out.println(arraylist.get(i).get("productPrice"));
                orderDetailAli.setProductId(productId);
                orderDetailAli.setAppid(alipayConfig.getAppid());
                orderDetailAli.setOrderId(order);        //暂时没有开通小程序支付先随机生成，有了直接用trade_no
                orderDetailAli.setProductName(productName);
                orderDetailAli.setProductPrice(Float.parseFloat(productPrice.toString()));
                orderDetailAli.setProductQuantity(Integer.parseInt(quantity.toString()));
                orderDetailAliService.saveOrderDetail(orderDetailAli);
            }
        }
        System.out.println("=======================================================");

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("https://www.lssell.cn/tt3/diancan/pages/confirm/confirm");
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyurl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\""+order+"\"," +
                " \"total_amount\":\""+money+"\"," +
                " \"subject\":\"点餐\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
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


}
