package com.example.demo.service;

import com.example.demo.pojo.OrderDetailAli;

import java.util.List;

public interface OrderDetailAliService {

    void saveOrderDetail(OrderDetailAli orderDetail);

    //根据订单号查询订单详情
    List<OrderDetailAli>queryOrderDetailByOrderId(String orderId);

    List<OrderDetailAli>creatOrderDetailByOrderId(String orderId);

    List<OrderDetailAli>findbyorderidali(String orderId);
}
