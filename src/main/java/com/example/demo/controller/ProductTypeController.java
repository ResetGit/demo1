package com.example.demo.controller;

import com.example.demo.mapper.ProductTypeMapper;
import com.example.demo.pojo.ProductType;
import com.example.demo.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private ProductTypeMapper productTypeMapper;

    //查询商品属性
    @PostMapping("/productTypeList")
    public Object productTypeList(String proTypeName){
        Map<String, Object> map=new HashMap<>();
        List<ProductType> list=productTypeService.productTypeList(proTypeName);
        map.put("count",list.size());
        map.put("data",list);
        return map;
    }

    @PostMapping("/insertProductType")
    @ResponseBody
    public Object insertProductType(ProductType productType){
        Map<String, Object> map=new HashMap<>();
        Example example = new Example(ProductType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proType",productType.getProType());
        List<ProductType> list = productTypeMapper.selectByExample(example);
        if(list.size()>0){
            map.put("error","error");
            return map;
        }else {
            this.productTypeService.insertProductType(productType);
            map.put("success","success");
            return map;
        }
    }

    //修改菜类
    @PostMapping("/editProductType")
    @ResponseBody
    public Object editProductType(ProductType productType){
        Map<String, Object> map=new HashMap<>();
        Example example = new Example(ProductType.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proType",productType.getProType());
        List<ProductType> list = productTypeMapper.selectByExample(example);

        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                if(list.get(i).getId()!=productType.getId()){
                    map.put("error","error");
                }else {
                    this.productTypeService.editProductType(productType);
                    map.put("success","success");
                }
            }
        }else{
            this.productTypeService.editProductType(productType);
            map.put("success","success");
        }

        return map;
    }

    //删除菜类
    @PostMapping("/delProductType")
    @ResponseBody
    public String delProductType(ProductType productType) {
        this.productTypeService.delProductType(productType.getId());

        return "success";
    }

}
