package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class OrderMaster {
    @Id
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
    private String sn;
    private String userKey;
    private String userName;
    private String printertoken;

}
