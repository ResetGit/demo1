package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.ProductCategoryMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class ProductCategoryService extends BaseService<ProductCategory,String> {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public BaseMapper<ProductCategory, String> getMapper() {
        return productCategoryMapper;
    }


}
