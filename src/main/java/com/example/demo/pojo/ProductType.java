package com.example.demo.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ProductType {

    //商品属性 id
    private Integer id;

    //属性编号
    private String proType;

    //商品价格
    private String proTypePrice;

    //属性名称
    private String proTypeName;

    private Date createTime;

    private Date updateTime;
}
