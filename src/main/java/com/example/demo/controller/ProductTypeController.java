package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductType;
import com.example.demo.service.ProductTypeService;
import com.example.demo.util.IMoocJSONResult;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    Audience audience;

    /**
     * 根据用户id获取店铺列表
     * count 长度
     * data 列表
     * @return map
     */
    @RequestMapping("/getTypeListByUIdASId")
    public Object getTypeListByUIdASId(HttpServletRequest request, String proTypeName) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id

            map.put("userId",id);
            map.put("proTypeName",proTypeName);

            List<ProductType> list=this.productTypeService.getListByObject("getTypeListByUIdASId",map);
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
    @RequestMapping(value = "/getTypeListByStore", produces = "application/json; charset=utf-8")
    public IMoocJSONResult getTypeListByStore(@RequestBody Map<String, String> map1) throws Exception{
        Map<String, Object> map=new HashMap<>();
        List<ProductType> list=null;
        try {
            String storeId = map1.get("storeId");
            list= this.productTypeService.getListByObject("getTypeListByStore",storeId);
        }catch (DataAccessException dae){
            logger.error("查询商品属性列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询商品属性列表异常！", e);
        }
        return IMoocJSONResult.ok(list);
    }

    /**
     * 根据属性编号查找列表，如果有则返回error,则新增
     * @return map
     */
    @RequestMapping("/add")
    public Object add(ProductType productType) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<ProductType> list=this.productTypeService.getListByObject("getByProType",productType.getPro_type());
            if(list.size()>0){
                map.put("error","error");
            }else {
                Date date = new Date();  //当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
                productType.setCreate_time(df.format(date));
                productType.setUpdate_time(df.format(date));

                String addStore =this.productTypeService.addByObject("add",productType,true);
                if (addStore==null || "".equals(addStore)){
                    logger.debug("设置商品属性[新增]，结果=新增失败！");
                    throw new RuntimeException("新增失败!");
                }else {
                    map.put("ok","ok");
                }
            }
            return map;
        }catch (DataAccessException dae){
            logger.error("设置商品属性[新增]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置商品属性[新增]异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }


    /**
     * 根据商品编号查找列表，如果有,循环查询除this记录,则返回error,则修改
     * @return map
     */
    @RequestMapping("/edit")
    public Object edit(ProductType productType) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            Date date = new Date();  //当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
            productType.setUpdate_time(df.format(date));


            List<ProductType> list=this.productTypeService.getListByObject("getByProType",productType.getPro_type());
            if(list.size()>0){
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getId() == productType.getId() || list.get(i).getId().equals(productType.getId())){
                        this.productTypeService.updateByObject("edit",productType);
                        map.put("ok","ok");
                    }else{
                        map.put("error","error");
                    }
                }
            }else {
                this.productTypeService.updateByObject("edit",productType);
                map.put("ok","ok");
            }
            return map;
        }catch (DataAccessException dae){
            logger.error("设置商品属性[修改]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("商品属性[修改]异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }


    @RequestMapping("/del")
    public Object del(String id) throws Exception{
        try {
            if (id!=null){
                this.productTypeService.deleteByObject("del",id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除商品属性数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除商品属性列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }


}
