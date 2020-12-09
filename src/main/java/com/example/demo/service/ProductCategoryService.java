package com.example.demo.service;


import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;

import java.util.List;

public interface ProductCategoryService {

    //菜品类目列表
    List<ProductCategory> categoryList(String categoryName);

    //根据类目录名称查询菜品
    List<ProductInfo> queryProductInfoByCategory(int categoryType);

    //添加菜品分类
    void insertCategory(ProductCategory productCategory);
    //删除
    void delCategory(String categoryId);
    //修改
    void editCategory(ProductCategory productCategory);
}
