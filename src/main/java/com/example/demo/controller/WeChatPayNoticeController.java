//package com.example.demo.controller;
//
//import com.github.wxpay.sdk.WXPayUtil;
//import com.linmao.config.PrintAccountConfig;
//import com.linmao.config.WeChatConfig;
//import com.linmao.mapper.LoginMapper;
//import com.linmao.mapper.SellerInfoMapper;
//import com.linmao.pojo.OrderDetail;
//import com.linmao.pojo.OrderMaster;
//import com.linmao.pojo.SellerInfo;
//import com.linmao.pojo.ShanghuInfo;
//import com.linmao.service.OrderDetailService;
//import com.linmao.service.OrderMasterService;
//import com.linmao.service.imp.PrintService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tk.mybatis.mapper.entity.Example;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
////import com.linmao.service.PayService;
//
//@RestController
//@RequestMapping("/notice")
//@AllArgsConstructor
//@Slf4j
//public class WeChatPayNoticeController {
//
//    @Autowired
//    private WeChatConfig weChatConfig;
//
//    @Autowired
//    private SellerInfoMapper sellerInfoMapper;
//
//    @Autowired
//    LoginMapper loginMapper;
//
//    @Autowired
//    private OrderMasterService orderMasterService;
//
//    @Autowired
//    private PrintAccountConfig printAccountConfig;
//
//    @Autowired
//    private OrderDetailService orderDetailService;
//
//
//    @PostMapping("/weChatPay")
//    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Map orderMap = new HashMap();
//        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
//        String line = null;
//        StringBuilder sb = new StringBuilder();
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        String notityXml = sb.toString();
//        Map<String, String> resultMap = WXPayUtil.xmlToMap(notityXml);
//        String returnCode = (String) resultMap.get("return_code");//业务结果
//        Map resPrint = new HashMap();
//        String resXml = "";
//        String orderNo = resultMap.get("out_trade_no");//订单号
//        String sign = resultMap.get("sign");//获取微信签名
//        resultMap.remove("sign");//去除签名字段
//        String signNew = WXPayUtil.generateSignature(resultMap, weChatConfig.getWECHAT_key()); //重新签名
//        if (signNew.equals(sign)) {
//            if ("SUCCESS".equals(returnCode)) {
//                resPrint.put("return_code", "SUCCESS");
//                resPrint.put("return_msg", "ok");
//                resXml = WXPayUtil.mapToXml(resPrint);
//                orderMap.put("orderStatus", 1);
//                orderMap.put("orderNo", orderNo);
//                System.out.println(resultMap);
//                Example example = new Example(ShanghuInfo.class);
//                Example.Criteria criteria = example.createCriteria();
//                criteria.andEqualTo("appid",weChatConfig.getWECHAT_APPID());
//                String ShopName = "";
//                try {
//                    String shopname = loginMapper.selectByExample(example).get(0).getShopname();
//                    ShopName = shopname;
//                }catch (Exception e){
//                    ShopName = "NULL";
//                }
//                String orderId = resultMap.get("out_trade_no");
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//                String date = df.format(new Date());
//
//                orderMasterService.updateOrderById(orderId);
//                for (int i=0;i<2;i++) {
//                    printAccountConfig.getSn();
//
//                    OrderMaster orderMaster = new OrderMaster();
//                    //PayResponse payResponse = this.payService.create(orderMaster);
//
//                    SellerInfo sellerInfo = new SellerInfo();
//                    //sellerInfo.setUsername("aa");
//
//                    List<OrderDetail> orderDetails = this.orderDetailService.queryOrderDetailByOrderId(orderId);
//                    String content = "";
//
//                    //打印内容
//                    content += "<CB>";
//                    content += ShopName;
//                    content += "</CB><BR>";
//                    content +="<CB>";
//                    content +="桌号:";
//                    content += this.orderMasterService.queryOrderById(orderId).getZh();
//                    content +="号";
//                    content += "</CB><BR>";
//                    content += "名称　　　　　 单价  数量 金额<BR>";
//                    content += "--------------------------------<BR>";
//                    for (OrderDetail orderDetail : orderDetails) {
//                        content += orderDetail.getProductName() + "     " + orderDetail.getProductPrice() + "    " + orderDetail.getProductQuantity() + "    " + orderDetail.getProductPrice() * orderDetail.getProductQuantity() + "<BR>";
//                    }
//
////        content += "奶绿　　	10.0   1  10.0<BR>";
////        content += "百香双生　　　　10.0   1  10.0<BR>";
////        content += "可可奶霜　　　 	10.0  1   10.0<BR>";
////        content += "抹茶奶霜　　 	10.0 	1   10.0<BR>";
////        content += "幸运清茶　 	10.0 	1   10.0<BR>";
////        content += "天香茉莉 	10.0  1  10.0<BR>";
//                    content += "备注：";
//                    content += this.orderMasterService.queryOrderById(orderId).getMsg();
//                    content += "<BR>";
//                    //content += orderDetails.toString();
//                    content += "--------------------------------<BR>";
//                    content += "合计：" + this.orderMasterService.queryOrderById(orderId).getOrderAmount() + "元<BR>";
////        content += "送货地点：";
////        content +=  address;
////        content +=  " <BR>";
////        content += "联系电话：";
////        content +=  phone;
////        content +=  "<BR>";
//                    content += "订餐时间：";
//                    content += this.orderMasterService.queryOrderById(orderId).getCreateTime();
//                    content += "<BR>";
////                    content += "<QR>http://www.dzist.com</QR>";
//
//                    PrintService.print(printAccountConfig.getSn(), printAccountConfig.getUrl(), printAccountConfig.getUser(), printAccountConfig.getUkey(), content);
//
//                    log.info("【SN】={}", printAccountConfig.getSn());
//                    log.info("【URL】={}", printAccountConfig.getUrl());
//                    log.info("【USER】={}", printAccountConfig.getUser());
//                    log.info("【UKEY】={}", printAccountConfig.getUkey());
//                    log.info("【订单详情】={}", orderDetails);
//
//                    List<SellerInfo> sellers = this.sellerInfoMapper.select(sellerInfo);
//                    System.out.println("支付成功");
//                }
//            } else {
//                System.out.println("业务结果失败");
////                return WxPayNotifyResponse.success("code:" + 9999 + "微信回调结果异常,异常原因:");
//            }
//
//        } else {
//            resPrint.put("return_code", "FAIL");
//            resPrint.put("return_msg", "签名失败");
//            resXml = WXPayUtil.mapToXml(resPrint);
//        }
//        log.info(resXml);
//        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//        out.write(resXml.getBytes());
//        out.flush();
//        out.close();
//        br.close();
//        return null;
//    }
//}
