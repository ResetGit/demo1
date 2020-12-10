package com.example.demo.controller;

import com.example.demo.pojo.OrderDetail;
import com.example.demo.service.OrderDetailService;
import com.example.demo.util.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
    //生成订单详情
    @PostMapping("/creatOrderDetail")
    public IMoocJSONResult createDetail(@RequestBody Map<String,String>map){

        //获取参数信息
        String orderId = map.get("orderId");
        String productId = map.get("productId");
        String productName = map.get("productName");
        float productPrice = Float.parseFloat(map.get("productPrice"));
        int num = Integer.parseInt(map.get("num"));//商品数量

        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(productId) || StringUtils.isBlank(productName)
            || productPrice < 0.0 || num<0){

            return IMoocJSONResult.errorMsg("参数不合法");
        }

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setProductId(productId);
        orderDetail.setProductName(productName);
        orderDetail.setProductPrice(productPrice);
        orderDetail.setProductQuantity(num);

        //保存到详情表中
        this.orderDetailService.saveOrderDetail(orderDetail);


        return IMoocJSONResult.ok();
    }


    @PostMapping("/creatOrderDetailByOrderId")
    public Object creatOrderDetailByOrderId(String orderId){
        List<OrderDetail> orderDetail = this.orderDetailService.creatOrderDetailByOrderId(orderId);
        Map<String, Object> map=new HashMap<>();
        map.put("count",orderDetail.size());
        map.put("data",orderDetail);

        return map;
    }

    //根据订单号查询订单详情(查询订单详情)
    @PostMapping("/queryOrderDatailByOrderId")
    public IMoocJSONResult queryOrderDatailByOrderId(@RequestBody Map<String,String>map){

        String orderId = map.get("orderId");
        List<OrderDetail> orderDetails = this.orderDetailService.queryOrderDetailByOrderId(orderId);

        if (CollectionUtils.isEmpty(orderDetails)){

            return IMoocJSONResult.errorMsg("订单为空");
        }


        //合计总价
        double num;
        for (OrderDetail o: orderDetails) {



        }


        return IMoocJSONResult.ok(orderDetails);
    }


}
