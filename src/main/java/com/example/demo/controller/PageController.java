package com.example.demo.controller;

import com.example.demo.pojo.Combo;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;
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

    @RequestMapping("/product_info_add")
    public ModelAndView product_info_add(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("product_info_add");
        return mv;
    }

    @RequestMapping("/product_info_edit")
    public ModelAndView product_info_edit(@Validate String id){
        ModelAndView mv = new ModelAndView();
        List<ProductInfo> list = productInfoService.getListByObject("findyid",id);
        String storeid = list.get(0).getShopid();
        List<ProductCategory> list2 = productCategoryService.getListByObject("gettype",storeid);
        mv.addObject("list",list);
        mv.addObject("type",list2);
        mv.setViewName("product_info_edit");
        return mv;
    }

//    @RequestMapping("/login")
//    public String loginuser(){
//        return "login";
//    }

    @RequestMapping("/product_catrgory_list")
    public String product_catrgory(){
        return "product_category";
    }

    @RequestMapping("/recharge")
    public ModelAndView recharge(){
        ModelAndView mv = new ModelAndView();
        List<Combo> list = comboService.ComboList(null);
        mv.addObject("list",list);
        mv.setViewName("recharge");
        return mv;
    }


}
