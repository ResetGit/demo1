package com.example.demo.service;

import com.example.demo.pojo.SellerInfo;

import java.util.List;


public interface SellerInfoService {

    //查询顾客列表
    List<SellerInfo> sellerList();

    SellerInfo querySellerByOppenid(String openid);

    void saveSeller(SellerInfo seller);
}
