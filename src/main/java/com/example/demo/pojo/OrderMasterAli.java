package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class OrderMasterAli {
    @Id
    private String orderId;
    private String buyerId;
    private Float orderAmount;
    private Integer orderStatus;
    private Integer payStatus;
    private String createTime;
    private String updateTime;
    private String msg;
    private String zh;
    private String appId;
    private String storeId;
}
