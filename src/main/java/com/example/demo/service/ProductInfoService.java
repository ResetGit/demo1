package com.example.demo.service;


import com.example.demo.pojo.ProductInfo;

import java.util.List;

public interface ProductInfoService {

    //查询菜品列表
    List<ProductInfo> productInfoList(String productName);

    //添加菜品
    void insertProduct(ProductInfo productInfo);

    //修改菜品
    void updateProductInfo(ProductInfo productInfo);

    //删除菜品
    void deleteProductInfo(String productId);

    //根据菜品id查询菜品
    ProductInfo queryProductInfoById(String productId);

    //修改菜品状态
    void updatebyzt(ProductInfo productInfo);

    //修改编号
    void updatebybh(ProductInfo productInfo);



}
