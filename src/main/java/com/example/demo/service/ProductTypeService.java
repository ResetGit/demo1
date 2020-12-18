package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.pojo.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductTypeService extends BaseService<ProductType,String>{

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public BaseMapper<ProductType, String> getMapper() {
        return productTypeMapper;
    }
}
