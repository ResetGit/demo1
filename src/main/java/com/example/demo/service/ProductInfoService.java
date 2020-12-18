package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.ProductInfoMapper;
import com.example.demo.mapper.StoreMapper;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.pojo.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class ProductInfoService extends BaseService<ProductInfo,String> {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public BaseMapper<ProductInfo, String> getMapper() {
        return productInfoMapper;
    }


}
