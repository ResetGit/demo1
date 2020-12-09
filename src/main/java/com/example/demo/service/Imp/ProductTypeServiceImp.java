package com.example.demo.service.Imp;

import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.pojo.ProductType;
import com.example.demo.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProductTypeServiceImp implements ProductTypeService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> productTypeList(String proTypeName){
        Example example = new Example(ProductType.class);
        Example.Criteria criteria = example.createCriteria();
        if(proTypeName == null || proTypeName==""){
            List<ProductType> productTypes = this.productTypeMapper.selectAll();
            return productTypes;
        }else {
            criteria.andLike("proTypeName", "%" + proTypeName + "%");
            List<ProductType> productTypes = this.productTypeMapper.selectByExample(example);
            return productTypes;
        }


    }
    @Override
    public void insertProductType(ProductType productType){
        this.productTypeMapper.insert(productType);
    }


    @Override
    public void editProductType(ProductType productType){

        Example e = new Example(ProductType.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("id",productType.getId());

        this.productTypeMapper.updateByExample(productType,e);
    }

    //删除
    @Override
    public void delProductType(int id) {

        Example e = new Example(ProductType.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("id",id);

        this.productTypeMapper.deleteByExample(e);
    }
}
