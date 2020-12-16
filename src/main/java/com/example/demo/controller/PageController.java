package com.example.demo.controller;

import com.example.demo.pojo.Combo;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.service.ComboService;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductInfoService;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ComboService comboService;

//    @RequestMapping("/product_info_add")
//    public ModelAndView product_info_add(String categoryName){
//        List<ProductCategory> categoryList =  productCategoryService.categoryList(categoryName);
//        List<Map> categoryName1 = new ArrayList<>();
//        for (ProductCategory p : categoryList){
//            Map<String,String> map = new HashMap<>();
//            map.put("CategoryName",p.getCategoryName());
//            map.put("CategoryType",p.getCategoryType().toString());
//            categoryName1.add(map);
//        }
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("categoryName",categoryName1);
//        mv.setViewName("product_info_add");
//        return mv;
//    }
//
//    @RequestMapping("/product_info_edit")
//    public ModelAndView product_info_edit(@Validate String id, String categoryName){
//        ModelAndView mv = new ModelAndView();
//        productInfoService.queryProductInfoById(id);
//        List<ProductCategory> categoryList =  productCategoryService.categoryList(categoryName);
//        List<Map> categoryName1 = new ArrayList<>();
//        for (ProductCategory p : categoryList){
//            Map<String,String> map = new HashMap<>();
//            map.put("CategoryName",p.getCategoryName());
//            map.put("CategoryType",p.getCategoryType().toString());
//            categoryName1.add(map);
//        }
//        mv.addObject("categoryName",categoryName1);
//        mv.addObject("list", productInfoService.queryProductInfoById(id));
//        mv.setViewName("product_info_edit");
//        return mv;
//    }

//    @RequestMapping("/login")
//    public String loginuser(){
//        return "login/login";
//    }

    @RequestMapping("/product_catrgory_list")
    public String product_catrgory(){
        return "product_category";
    }

    @RequestMapping("/recharge")
    public ModelAndView recharge(){
        ModelAndView mv = new ModelAndView();
        List<Combo> list = comboService.ComboList();
        mv.addObject("list",list);
        mv.setViewName("recharge");
        return mv;
    }


}
