package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class OrderCombo {
    @Id
    private String id;
    private String comboname;
    private String comboprice;
    private String orderid;
    private String createtime;
    private String username;
    private String shopnumber;
}
