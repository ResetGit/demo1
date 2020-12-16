package com.example.demo.pojo;

import lombok.Data;

@Data
public class OrderMaster {
    private String orderId;
    private String buyerOpenid;
    private Float orderAmount;
    private Integer orderStatus;
    private Integer payStatus;
    private String createTime;
    private String updateTime;
    private String msg;
    private String zh;
    private String appid;
    private String storeId;
    private String shopname;

}
