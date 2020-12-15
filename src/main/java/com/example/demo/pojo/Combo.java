package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Combo {
    @Id
    private String id;
    private String comboname;
    private String comboprice;
    private String createTime;
    private String updateTime;
}
