package com.example.demo.controller;

import com.example.demo.config.WeChatConfig;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.pojo.OrderMaster;
import com.example.demo.service.OrderMasterService;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.IMoocJSONResult;
import com.example.demo.util.OpenIdJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
public class OrderController {

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private WeChatConfig weChatConfig;

    //生成订单
    @PostMapping("/creat")
    public IMoocJSONResult creatOrder(@RequestBody Map<String,Object>map){

        String openid = map.get("openid").toString();
        //String amount = map.get("amount").toString();
        float orderAmount = (float) map.get("orderAmount");
        //int orderStatus = (int) map.get("orderStatus");
       // int payStatus = (int) map.get("payStatus");

        if (StringUtils.isBlank(openid) ||  orderAmount <0){
            return IMoocJSONResult.errorMsg("参数不合法");
        }

        //生成订单，保存
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerOpenid(openid);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.getOrderStatus();
        orderMaster.getPayStatus();

        this.orderMasterService.saveOrder(orderMaster);

        return IMoocJSONResult.ok();
    }


    //查询订单列表
    @PostMapping("/orderList")
    public Object orderList(String start,String end){
        Map<String, Object> map=new HashMap<>();
        List<OrderMaster> orderMasters = this.orderMasterService.orderMasterList(start,end);
        map.put("count",orderMasters.size());
        map.put("data",orderMasters);
        return map;
    }

    //根据oppenid获取订单
    @PostMapping("/getListByOpenid")
    public IMoocJSONResult getListByOpenid(@RequestBody Map<String,String>map) throws Exception {

        String code = map.get("code");
        String result = "";
        result = HttpUtil.doGet(
                "https://api.weixin.qq.com/sns/jscode2session?appid="
                        + weChatConfig.getWECHAT_APPID() + "&secret="
                        + weChatConfig.getAppsecret() + "&js_code="
                        + code
                        + "&grant_type=authorization_code", null);
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result,OpenIdJson.class);
        if (StringUtils.isBlank(openIdJson.getOpenid())){
            return IMoocJSONResult.errorMsg("参数不合法");
        }

       List<OrderMaster> orderMasters =  this.orderMasterService.getListByOpenid(openIdJson.getOpenid());

        if (CollectionUtils.isEmpty(orderMasters)){
            return IMoocJSONResult.errorMsg("该用户没有订单");
        }

        return IMoocJSONResult.ok(orderMasters);
    }


    @PostMapping("/DataOrder")
    public Object DataOrder(){
        Map<String, Object> map=new HashMap<>();
        List<Double> list = new ArrayList<Double>();
        double sumOne = 0;
        double sumTwo = 0;
        double sumThree = 0;
        double sumFour = 0;
        double sumFive = 0;
        double sumSix = 0;
        double sumSeven = 0;
        double sumEight = 0;
        double sumNine = 0;
        double sumTen = 0;
        double sumEleven = 0;
        double sumTwelve = 0;

        //获取1份的订单信息
        List<OrderMaster> orderMastersOne = this.orderMasterService.orderMasterList("2020-01-01","2020-01-31");
        //循环1月份的订单信息获取总金额
        for(int i=0;i<orderMastersOne.size();i++){
            //求和总金额
            sumOne = orderMastersOne.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取2份的订单信息
        List<OrderMaster> orderMastersTwo = this.orderMasterService.orderMasterList("2020-02-01","2020-02-31");
        //循环2月份的订单信息获取总金额
        for(int i=0;i<orderMastersTwo.size();i++){
            //求和总金额
            sumTwo = orderMastersTwo.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }


        //获取3份的订单信息
        List<OrderMaster> orderMastersThree = this.orderMasterService.orderMasterList("2020-03-01","2020-03-31");
        //循环3月份的订单信息获取总金额
        for(int i=0;i<orderMastersThree.size();i++){
            //求和总金额
            sumThree = orderMastersThree.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取4份的订单信息
        List<OrderMaster> orderMastersFour = this.orderMasterService.orderMasterList("2020-04-01","2020-04-31");
        //循环4月份的订单信息获取总金额
        for(int i=0;i<orderMastersFour.size();i++){
            //求和总金额
            sumFour = orderMastersFour.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取5份的订单信息
        List<OrderMaster> orderMastersFive = this.orderMasterService.orderMasterList("2020-05-01","2020-05-31");
        //循环5月份的订单信息获取总金额
        for(int i=0;i<orderMastersFive.size();i++){
            //求和总金额
            sumFive = orderMastersFive.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取6份的订单信息
        List<OrderMaster> orderMastersSix = this.orderMasterService.orderMasterList("2020-06-01","2020-06-31");
        //循环6月份的订单信息获取总金额
        for(int i=0;i<orderMastersSix.size();i++){
            //求和总金额
            sumSix = orderMastersSix.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取7份的订单信息
        List<OrderMaster> orderMastersSeven = this.orderMasterService.orderMasterList("2020-07-01","2020-07-31");
        //循环7月份的订单信息获取总金额
        for(int i=0;i<orderMastersSeven.size();i++){
            //求和总金额
            sumSeven = orderMastersSeven.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取8份的订单信息
        List<OrderMaster> orderMastersEight = this.orderMasterService.orderMasterList("2020-08-01","2020-08-31");
        //循环8月份的订单信息获取总金额
        for(int i=0;i<orderMastersEight.size();i++){
            //求和总金额
            sumEight = orderMastersEight.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取9份的订单信息
        List<OrderMaster> orderMastersNine = this.orderMasterService.orderMasterList("2020-09-01","2020-09-31");
        //循环9月份的订单信息获取总金额
        for(int i=0;i<orderMastersNine.size();i++){
            //求和总金额
            sumNine = orderMastersNine.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取9份的订单信息
        List<OrderMaster> orderMastersTen = this.orderMasterService.orderMasterList("2020-10-01","2020-10-31");
        //循环9月份的订单信息获取总金额
        for(int i=0;i<orderMastersTen.size();i++){
            //求和总金额
            sumTen = orderMastersTen.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取11份的订单信息
        List<OrderMaster> orderMastersEleven = this.orderMasterService.orderMasterList("2020-11-01","2020-11-31");
        //循环11月份的订单信息获取总金额
        for(int i=0;i<orderMastersEleven.size();i++){
            //求和总金额
             sumEleven = orderMastersEleven.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }

        //获取12份的订单信息
        List<OrderMaster> orderMastersTwelve = this.orderMasterService.orderMasterList("2020-12-01","2020-12-31");
        //循环12月份的订单信息获取总金额
        for(int i=0;i<orderMastersTwelve.size();i++){
            //求和总金额
            sumTwelve = orderMastersTwelve.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
        }


        list.add(sumOne);
        list.add(sumTwo);
        list.add(sumThree);
        list.add(sumFour);
        list.add(sumFive);
        list.add(sumSix);
        list.add(sumSeven);
        list.add(sumEight);
        list.add(sumNine);
        list.add(sumTen);
        list.add(sumEleven);
        list.add(sumTwelve);
        map.put("data",list);


//
//        Calendar c = Calendar.getInstance();
//        for(int i = 0; i < 12; i ++){
//            int j = c.get(Calendar.MONTH) + 1 - i;
//            String date = "";
//            if(j >= 1){
//                date = (j >= 10 ? "" : "0") + j;
//            } else {
//                int p = 11 - i;//剩余循环次数
//                int n = c.get(Calendar.MONTH) + 2 + p;
//                date =(n >= 10 ? "" : "0") + n;
//            }
//            list.add(date);
//        }
//        map.put("data",orderMasters1);
        return map;
    }

//    @GetMapping("/DataOrderAll")
//    public Object DataOrderAll(){
//        Map<String, Object> map=new HashMap<>();
//        List<Double> list = new ArrayList<Double>();
//        DecimalFormat decimalFormat=new DecimalFormat("0.0");
//        //获取今日已支付的订单
//        List<OrderMaster> orderMastersToday=orderMasterMapper.TodayDataOrder();
//        double sumToday = 0;
//        for (int i=0;i<orderMastersToday.size();i++){
//            sumToday= orderMastersToday.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取本周已支付的订单
//        List<OrderMaster> orderMastersWeek=orderMasterMapper.weekDataOrder();
//        double sumWeek = 0.0;
//        for (int i=0;i<orderMastersWeek.size();i++){
//            sumWeek= orderMastersWeek.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//        //获取本月已支付的订单
//        List<OrderMaster> orderMastersMonth=orderMasterMapper.monthDataOrder();
//        double sumMonth = 0.0;
//        for (int i=0;i<orderMastersMonth.size();i++){
//            sumMonth= orderMastersMonth.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//        list.add(Double.valueOf(decimalFormat.format(sumToday)));
//        list.add(Double.valueOf(decimalFormat.format(sumWeek)));
//        list.add(Double.valueOf(decimalFormat.format(sumMonth)));
//        map.put("data",list);
//        return map;
//    }
//
//    //今日已支付的订单的收入
//    @PostMapping("/TodayDataOrder")
//    public Object TodayDataOrder(){
//        Map<String, Object> map=new HashMap<>();
//        List<OrderMaster> orderMasters=orderMasterMapper.TodayDataOrder();
//        double sum = 0;
//        for (int i=0;i<orderMasters.size();i++){
//            sum= orderMasters.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//
//
//
//        map.put("data",sum);
//        return map;
//    }
//
//    //本周已支付的订单的收入
//    @PostMapping("/weekDataOrder")
//    public Object weekDataOrder(){
//        Map<String, Object> map=new HashMap<>();
//        List<OrderMaster> orderMasters=orderMasterMapper.weekDataOrder();
//        double sum = 0;
//        for (int i=0;i<orderMasters.size();i++){
//            sum= orderMasters.stream().mapToDouble(OrderMaster::getOrderAmount).sum();
//        }
//        map.put("data",sum);
//        return map;
//    }
}
