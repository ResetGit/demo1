package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.AlipayConfig;
import com.example.demo.config.WeChatConfig;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.pojo.OrderMasterAli;
import com.example.demo.service.OrderMasterAliService;
import com.example.demo.util.IMoocJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderAliController {

    @Autowired
    private OrderMasterAliService orderMasterService;
    @Autowired
    private OrderMasterAliMapper orderMasterMapper;
    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private AlipayConfig alipayConfig;

//    //生成订单
//    @PostMapping("/creat")
//    public IMoocJSONResult creatOrder(@RequestBody Map<String,Object>map){
//
//        String openid = map.get("openid").toString();
//        //String amount = map.get("amount").toString();
//        float orderAmount = (float) map.get("orderAmount");
//        //int orderStatus = (int) map.get("orderStatus");
//       // int payStatus = (int) map.get("payStatus");
//
//        if (StringUtils.isBlank(openid) ||  orderAmount <0){
//            return IMoocJSONResult.errorMsg("参数不合法");
//        }
//
//        //生成订单，保存
//        OrderMaster orderMaster = new OrderMaster();
//        orderMaster.setBuyerOpenid(openid);
//        orderMaster.setOrderAmount(orderAmount);
//        orderMaster.getOrderStatus();
//        orderMaster.getPayStatus();
//
//        this.orderMasterService.saveOrder(orderMaster);
//
//        return IMoocJSONResult.ok();
//    }


    //查询订单列表
    @PostMapping("/orderListAli")
    public Object orderList(String start,String end){
        Map<String, Object> map=new HashMap<>();
        List<OrderMasterAli> orderMasters = this.orderMasterService.orderMasterList(start,end);
        map.put("count",orderMasters.size());
        map.put("data",orderMasters);
        return map;
    }

    //根据oppenid获取订单
    @PostMapping("/getListByOpenidAli")
    public IMoocJSONResult getListByOpenid(@RequestBody JSONObject auth_code) throws Exception {
        String code = (String) auth_code.get("auth_code");
        System.out.println("code=" + code);
//        //String serverUrl, String appId, String privateKey, String format,String charset, String alipayPublicKey, String signType
//        //实例化客户端 参数：正式环境URL,Appid,商户私钥 PKCS8格式,字符编码格式,字符格式,支付宝公钥,签名方式
//        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
//        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
//        // 值为authorization_code时，代表用code换取
//        request.setGrantType("authorization_code");
//        //授权码，用户对应用授权后得到的
//        request.setCode(code);
//        //这里使用execute方法
//        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
//        System.out.println("userid="+response.getUserId());
//        //刷新令牌，上次换取访问令牌时得到。见出参的refresh_token字段
//        request.setRefreshToken(response.getAccessToken());
        //返回成功时 就将唯一标识返回
        List<OrderMasterAli> orderMasters = this.orderMasterService.getListByOpenid(code);
        if (CollectionUtils.isEmpty(orderMasters)) {
            return IMoocJSONResult.errorMsg("该用户没有订单");
        }
        return  IMoocJSONResult.ok(orderMasters);

//        return IMoocJSONResult.ok(orderMasters);
//        if(response.isSuccess()){
//            System.out.println("调用成功");
//
//        } else {
//            return IMoocJSONResult.errorMsg("调用失败");
//        }
    }


//    @PostMapping("/DataOrder")
//    public Object DataOrder(){
//        Map<String, Object> map=new HashMap<>();
//        List<Double> list = new ArrayList<Double>();
//        double sumOne = 0;
//        double sumTwo = 0;
//        double sumThree = 0;
//        double sumFour = 0;
//        double sumFive = 0;
//        double sumSix = 0;
//        double sumSeven = 0;
//        double sumEight = 0;
//        double sumNine = 0;
//        double sumTen = 0;
//        double sumEleven = 0;
//        double sumTwelve = 0;
//
//        //获取1份的订单信息
//        List<OrderMaster> orderMastersOne = this.orderMasterService.orderMasterList("2020-01-01","2020-01-31");
//        //循环1月份的订单信息获取总金额
//        for(int i=0;i<orderMastersOne.size();i++){
//            //求和总金额
//            sumOne = orderMastersOne.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取2份的订单信息
//        List<OrderMaster> orderMastersTwo = this.orderMasterService.orderMasterList("2020-02-01","2020-02-31");
//        //循环2月份的订单信息获取总金额
//        for(int i=0;i<orderMastersTwo.size();i++){
//            //求和总金额
//            sumTwo = orderMastersTwo.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//
//        //获取3份的订单信息
//        List<OrderMaster> orderMastersThree = this.orderMasterService.orderMasterList("2020-03-01","2020-03-31");
//        //循环3月份的订单信息获取总金额
//        for(int i=0;i<orderMastersThree.size();i++){
//            //求和总金额
//            sumThree = orderMastersThree.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取4份的订单信息
//        List<OrderMaster> orderMastersFour = this.orderMasterService.orderMasterList("2020-04-01","2020-04-31");
//        //循环4月份的订单信息获取总金额
//        for(int i=0;i<orderMastersFour.size();i++){
//            //求和总金额
//            sumFour = orderMastersFour.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取5份的订单信息
//        List<OrderMaster> orderMastersFive = this.orderMasterService.orderMasterList("2020-05-01","2020-05-31");
//        //循环5月份的订单信息获取总金额
//        for(int i=0;i<orderMastersFive.size();i++){
//            //求和总金额
//            sumFive = orderMastersFive.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取6份的订单信息
//        List<OrderMaster> orderMastersSix = this.orderMasterService.orderMasterList("2020-06-01","2020-06-31");
//        //循环6月份的订单信息获取总金额
//        for(int i=0;i<orderMastersSix.size();i++){
//            //求和总金额
//            sumSix = orderMastersSix.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取7份的订单信息
//        List<OrderMaster> orderMastersSeven = this.orderMasterService.orderMasterList("2020-07-01","2020-07-31");
//        //循环7月份的订单信息获取总金额
//        for(int i=0;i<orderMastersSeven.size();i++){
//            //求和总金额
//            sumSeven = orderMastersSeven.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取8份的订单信息
//        List<OrderMaster> orderMastersEight = this.orderMasterService.orderMasterList("2020-08-01","2020-08-31");
//        //循环8月份的订单信息获取总金额
//        for(int i=0;i<orderMastersEight.size();i++){
//            //求和总金额
//            sumEight = orderMastersEight.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取9份的订单信息
//        List<OrderMaster> orderMastersNine = this.orderMasterService.orderMasterList("2020-09-01","2020-09-31");
//        //循环9月份的订单信息获取总金额
//        for(int i=0;i<orderMastersNine.size();i++){
//            //求和总金额
//            sumNine = orderMastersNine.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取9份的订单信息
//        List<OrderMaster> orderMastersTen = this.orderMasterService.orderMasterList("2020-10-01","2020-10-31");
//        //循环9月份的订单信息获取总金额
//        for(int i=0;i<orderMastersTen.size();i++){
//            //求和总金额
//            sumTen = orderMastersTen.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取11份的订单信息
//        List<OrderMaster> orderMastersEleven = this.orderMasterService.orderMasterList("2020-11-01","2020-11-31");
//        //循环11月份的订单信息获取总金额
//        for(int i=0;i<orderMastersEleven.size();i++){
//            //求和总金额
//             sumEleven = orderMastersEleven.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取12份的订单信息
//        List<OrderMaster> orderMastersTwelve = this.orderMasterService.orderMasterList("2020-12-01","2020-12-31");
//        //循环12月份的订单信息获取总金额
//        for(int i=0;i<orderMastersTwelve.size();i++){
//            //求和总金额
//            sumTwelve = orderMastersTwelve.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//
//        list.add(sumOne);
//        list.add(sumTwo);
//        list.add(sumThree);
//        list.add(sumFour);
//        list.add(sumFive);
//        list.add(sumSix);
//        list.add(sumSeven);
//        list.add(sumEight);
//        list.add(sumNine);
//        list.add(sumTen);
//        list.add(sumEleven);
//        list.add(sumTwelve);
//        map.put("data",list);
//
//
////
////        Calendar c = Calendar.getInstance();
////        for(int i = 0; i < 12; i ++){
////            int j = c.get(Calendar.MONTH) + 1 - i;
////            String date = "";
////            if(j >= 1){
////                date = (j >= 10 ? "" : "0") + j;
////            } else {
////                int p = 11 - i;//剩余循环次数
////                int n = c.get(Calendar.MONTH) + 2 + p;
////                date =(n >= 10 ? "" : "0") + n;
////            }
////            list.add(date);
////        }
////        map.put("data",orderMasters1);
//        return map;
//    }

//    @GetMapping("/DataOrderAll_Ali")
//    public Object DataOrderAll(){
//        Map<String, Object> map=new HashMap<>();
//        List<Double> list = new ArrayList<Double>();
//        DecimalFormat decimalFormat=new DecimalFormat("0.0");
//        //获取今日已支付的订单
//        List<OrderMasterAli> orderMastersToday=orderMasterMapper.TodayDataOrder();
//        System.out.println(orderMastersToday);
//        double sumToday = 0;
//        for (int i=0;i<orderMastersToday.size();i++){
//            sumToday= orderMastersToday.stream().mapToDouble(OrderMasterAli::getOrderAmount).sum();
//        }
//
//        //获取本周已支付的订单
//        List<OrderMasterAli> orderMastersWeek=orderMasterMapper.weekDataOrder();
//        double sumWeek = 0.0;
//        for (int i=0;i<orderMastersWeek.size();i++){
//            sumWeek= orderMastersWeek.stream().mapToDouble(OrderMasterAli::getOrderAmount).sum();
//        }
//
//        //获取本月已支付的订单
//        List<OrderMasterAli> orderMastersMonth=orderMasterMapper.monthDataOrder();
//        double sumMonth = 0.0;
//        for (int i=0;i<orderMastersMonth.size();i++){
//            sumMonth= orderMastersMonth.stream().mapToDouble(OrderMasterAli::getOrderAmount).sum();
//        }
//        list.add(Double.valueOf(decimalFormat.format(sumToday)));
//        list.add(Double.valueOf(decimalFormat.format(sumWeek)));
//        list.add(Double.valueOf(decimalFormat.format(sumMonth)));
//        map.put("data",list);
//        System.out.println(map);
//        return map;
//    }
//
//    //今日已支付的订单的收入
//    @PostMapping("/TodayDataOrderAli")
//    public Object TodayDataOrder(){
//        Map<String, Object> map=new HashMap<>();
//        List<OrderMasterAli> orderMasters=orderMasterMapper.TodayDataOrder();
//        double sum = 0;
//        for (int i=0;i<orderMasters.size();i++){
//            sum= orderMasters.stream().mapToDouble(OrderMasterAli::getOrderAmount).sum();
//        }
//
//
//
//        map.put("data",sum);
//        return map;
//    }
//
//    //本周已支付的订单的收入
//    @PostMapping("/weekDataOrder_Ali")
//    public Object weekDataOrder(){
//        Map<String, Object> map=new HashMap<>();
//        List<OrderMasterAli> orderMasters=orderMasterMapper.weekDataOrder();
//        double sum = 0;
//        for (int i=0;i<orderMasters.size();i++){
//            sum= orderMasters.stream().mapToDouble(OrderMasterAli::getOrderAmount).sum();
//        }
//        map.put("data",sum);
//        return map;
//    }

}
