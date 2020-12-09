package com.example.demo.service;

import com.example.demo.pojo.OrderMasterAli;

import java.util.Date;
import java.util.List;

public interface OrderMasterAliService {
    //保存订单
    void saveOrder(OrderMasterAli orderMaster);

    //根据订单编号查询订单
    OrderMasterAli queryOrderById(String orderId);

    List<OrderMasterAli> orderMasterList(String start, String end);

    List<OrderMasterAli> DataOrder(String create_time);

    List<OrderMasterAli> orderWeedKMasterList(Date start, Date end);

    void deleteOrderById(String orderId);

    //根据openid获取order
    List<OrderMasterAli> getListByOpenid(String openId);

    void updateOrderById(String orderId);

}
