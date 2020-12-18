package com.example.demo.service;


import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.ProductInfoMapper;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.pojo.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoService extends BaseService<ProductInfo,String>{
    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public BaseMapper<ProductInfo, String> getMapper() {
        return productInfoMapper;
    }

}
