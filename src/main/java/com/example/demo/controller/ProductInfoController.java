package com.example.demo.controller;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.pojo.ProductType;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductInfoService;
import com.example.demo.service.ProductTypeService;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@RequestMapping("/productInfo")
public class ProductInfoController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    Audience audience;

    /**
     * 根据用户id获取店铺列表
     * count 长度
     * data 列表
     * @return map
     */
    @RequestMapping("/getProductInfoListByUIdASId")
    public Object getProductInfoListByUIdASId(HttpServletRequest request, String productName) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id

            map.put("userId",id);
            map.put("productName",productName);
            List<ProductCategory> categoryList =  productCategoryService.getListByObject("getListCategory",null);
            List<ProductInfo> list=this.productInfoService.getListByObject("getProductInfoListByUIdASId",map);

            List<Map> list1 = new ArrayList<>();
            for (int j=0;j<categoryList.size();j++){
                map.put("categoryName",categoryList.get(j).getCategory_name());
                map.put("categoryType", String.valueOf(categoryList.get(j).getCategory_type()));
                map.put("categoryId",categoryList.get(j).getCategory_id());
                list1.add(map);
            }
            for (int i=0;i<list.size();i++) {
                list.get(i).setData(list1);
            }

            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException dae){
            logger.error("查询小品列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询小品列表异常！", e);
        }
        return map;
    }


    /**
     * 根据店铺id获取商品属性列表，h5页面要用
     * @return list
     */
    @RequestMapping("/getProductInfoListByStore")
    public Object getProductInfoListByStore(String storeId) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<ProductInfo> list= this.productInfoService.getListByObject("getProductInfoListByStore",storeId);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException dae){
            logger.error("查询商品属性列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询商品属性列表异常！", e);
        }
        return map;
    }

    @RequestMapping("/updateByZt")
    public Object updateByZt(String productId,String productStatus) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            map.put("product_id",productId);
            map.put("product_status",productStatus);
            this.productInfoService.updateByObject("updateByZt",map);
            return "ok";
        }catch (DataAccessException dae){
            logger.error("修改商品状态数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改商品状态异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }



}