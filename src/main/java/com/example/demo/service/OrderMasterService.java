package com.example.demo.service;

import com.example.demo.pojo.OrderMaster;
import java.util.Date;
import java.util.List;

public interface OrderMasterService {

    //保存订单
    void saveOrder(OrderMaster orderMaster);

    //根据订单编号查询订单
    OrderMaster queryOrderById(String orderId);

    List<OrderMaster> orderMasterList(String start, String end);

    List<OrderMaster> DataOrder(String create_time);

    List<OrderMaster> orderWeedKMasterList(Date start, Date end);

    void deleteOrderById(String orderId);

    //根据openid获取order
    List<OrderMaster> getListByOpenid(String openId);

    void updateOrderById(String orderId);
}
