package com.example.demo.service;

import com.example.demo.pojo.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    void saveOrderDetail(OrderDetail orderDetail);

    //根据订单号查询订单详情
    List<OrderDetail>queryOrderDetailByOrderId(String orderId);

    List<OrderDetail>creatOrderDetailByOrderId(String orderId);
}
