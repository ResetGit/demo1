package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class OrderDetailAli {
    @Id
    private String detailId;
    private String orderId;
    private String productId;
    private String productName;
    private Float productPrice;
    private Integer productQuantity;
    private String createTime;
    private String updateTime;
    private String appid;
}
