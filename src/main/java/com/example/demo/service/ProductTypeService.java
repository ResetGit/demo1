package com.example.demo.service;

import com.example.demo.pojo.ProductType;

import java.util.List;

public interface ProductTypeService {

    List<ProductType> productTypeList(String proTypeName);

   void insertProductType(ProductType productType);

    void editProductType(ProductType productType);

    void delProductType(int id);
}
