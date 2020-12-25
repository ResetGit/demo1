package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.mapper.OrderDetailAliMapper;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.OrderDetailAliService;
import com.example.demo.service.StoreService;
import com.example.demo.service.WxService;
import com.example.demo.util.IMoocJSONResult;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/orderDetailAli")
public class OrderDetailAliController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private OrderDetailAliService orderDetailService;
    @Autowired
    private OrderDetailAliMapper aliMapper;

    @Autowired
    private WxService wxService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderMasterAliMapper orderMasterAliMapper;

    @Autowired
    Audience audience;

    @PostMapping("/OrderDetailByUserIdAli")
    public Object OrderDetailByUserIdAli(HttpServletRequest request, String start, String end){
        String token = request.getParameter("tokens");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        String id= String.valueOf(jsonArray.getJSONObject(0).get("id"));//id 为user_id
        Map<String, Object> map=new HashMap<>();

        map.put("id",id);
        map.put("start",start);
        map.put("end",end);
        List<Wx> list = this.wxService.getListByObject("OrderDetailByUserIdAli",map);

        map.put("count",list.size());
        map.put("data",list);
        return map;
    }


    @RequestMapping("/getDatList")
    public Object getDatLists(String storeId){
        Map<String, Object> map=new HashMap<>();
        List<Object> name=new ArrayList<Object>();
        List<Object> time=new ArrayList<Object>();
        List<Object> seriesData=new ArrayList<Object>();
        DecimalFormat decimalFormat=new DecimalFormat("0.0");

        Double sumWeek=0.0;

        //根据用户id 获取店铺name
        Store stores= (Store) this.storeService.getByObject("getByStoreName",storeId);

        //获取本周周一到周日的日期
        List<Object> listDataTime=this.orderMasterAliMapper.weekDate();
        for (int i=0;i<listDataTime.size();i++) {
            //把日期存在list
            time.add(listDataTime.get(i).toString());
        }


        name.add(stores.getName());
        time.add(listDataTime);


        /**
         * 柱状图上的具体数据1
         */
        for (int i=0;i<listDataTime.size();i++){

            map.put("storeId",storeId);
            map.put("pay_status","1");
            map.put("create_time",listDataTime.get(i).toString());
            List<Wx> wx=this.wxService.getListByObject("getListByStoreId",map);
            if(wx.size()>0){
                for(int x=0;x<wx.size();x++){
                    sumWeek= wx.stream().mapToDouble(Wx::getOrder_amount).sum();
                }
            }else {
                sumWeek=0.0;
            }
            seriesData.add(decimalFormat.format(sumWeek));
        }
        map.put("name",name);
        map.put("time",time);
        map.put("seriesData",seriesData);
        return map;
    }


    @PostMapping("/creatOrderDetailByOrderIdAli")
    public Object creatOrderDetailByOrderId(String orderId){
        List<OrderDetailAli> orderDetail = this.orderDetailService.creatOrderDetailByOrderId(orderId);
        Map<String, Object> map=new HashMap<>();
        map.put("count",orderDetail.size());
        map.put("data",orderDetail);
        return map;
    }

    //根据订单号查询订单详情(查询订单详情)
    @PostMapping("/queryOrderDatailByOrderIdAli")
    public IMoocJSONResult queryOrderDatailByOrderId(@RequestBody Map<String,String>map){

        String orderId = map.get("orderId");
        List<OrderDetailAli> orderDetails = this.orderDetailService.queryOrderDetailByOrderId(orderId);

        if (CollectionUtils.isEmpty(orderDetails)){

            return IMoocJSONResult.errorMsg("订单为空");
        }


        //合计总价
        double num;
        for (OrderDetailAli o: orderDetails) {



        }


        return IMoocJSONResult.ok(orderDetails);
    }


}
