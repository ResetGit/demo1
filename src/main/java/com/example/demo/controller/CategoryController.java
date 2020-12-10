package com.example.demo.controller;

import com.example.demo.common.idworker.Sid;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.mapper.ProductCategoryMapper;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    //查询菜品分类的列表
    @PostMapping("/categoryList")
    public Object categoryList(String categoryName){

        List<ProductCategory> productCategories = this.categoryService.categoryList(categoryName);
        Map<String, Object> map=new HashMap<>();
        map.put("count",productCategories.size());
        map.put("data",productCategories);
        if (CollectionUtils.isEmpty(productCategories)){
            map.put("msg","菜品列表为空！");
            return map;
        }

        return map;
    }

    //新增菜类
    @PostMapping("/insertCategory")
    @ResponseBody
    public Object insertCategory(ProductCategory productCategory) {
        System.out.println(productCategory.getCategoryId());
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryType",productCategory.getCategoryType());
        List<ProductCategory> list = productCategoryMapper.selectByExample(example);

        Map<String, Object> map=new HashMap<>();
        if(list.size()>0){
            map.put("error","error");
            return map;
        }else {
            map.put("success","success");
            productCategory.setCategoryId(Sid.next());
            productCategory.setCreateTime(new Date());
            this.categoryService.insertCategory(productCategory);
            return map;
        }
    }

    //删除菜类
    @PostMapping("/delCategory")
    @ResponseBody
    public String delCategory(ProductCategory productCategory) {
        this.categoryService.delCategory(String.valueOf(productCategory.getCategoryId()));

        return "success";
    }

    //修改菜类
    @PostMapping("/editCategory")
    @ResponseBody
    public Object editCategory(ProductCategory productCategory) {
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryType",productCategory.getCategoryType());
        List<ProductCategory> list = productCategoryMapper.selectByExample(example);

        Map<String, Object> map=new HashMap<>();
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCategoryId()!=productCategory.getCategoryId()){
                    map.put("error","error");
                }else{
                    this.categoryService.editCategory(productCategory);
                    map.put("success","success");
                }
            }
        }else{
            this.categoryService.editCategory(productCategory);
            map.put("success","success");
        }
        return map;
    }

}
