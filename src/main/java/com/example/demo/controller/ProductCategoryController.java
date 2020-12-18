package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.Store;
import com.example.demo.service.ProductCategoryService;
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
@RequestMapping("/productCategory")
public class ProductCategoryController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    Audience audience;

    /**
     * 根据用户id获取菜类列表
     * count 长度
     * data 列表
     * @return map
     */
    @RequestMapping("/getCatListByUIdASId")
    public Object getCatListByUIdASId(HttpServletRequest request,String categoryName) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id

            map.put("userId",id);
            map.put("category_name",categoryName);

            List<ProductCategory> list=this.productCategoryService.getListByObject("getCatListByUIdASId",map);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException dae){
            logger.error("查询菜类列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询菜类列表异常！", e);
        }
        return map;
    }

    /**
     * 根据店铺id获取菜类列表，h5页面要用
     * @return list
     */
    @RequestMapping(value = "/getCatListByStore", produces = "application/json; charset=utf-8")
    public IMoocJSONResult getCatListByStore(@RequestBody Map<String, String> map1) throws Exception{
        Map<String, Object> map=new HashMap<>();
        List<ProductCategory> list=null;
        try {
            //接收传入参数
            String storeId = map1.get("storeId");
            list= this.productCategoryService.getListByObject("getCatListByStore",storeId);
        }catch (DataAccessException dae){
            logger.error("查询菜类列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询菜类列表异常！", e);
        }
        return IMoocJSONResult.ok(list);
    }


    /**
     * 根据菜类编号查找列表，如果有则返回error,则新增
     * @return map
     */
    @RequestMapping("/add")
    public Object add(ProductCategory productCategory) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<ProductCategory> list=this.productCategoryService.getListByObject("getByCategoryType",productCategory.getCategory_type());
            if(list.size()>0){
                map.put("error","error");
            }else {
                Date date = new Date();  //当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
                productCategory.setCreate_time(df.format(date));
                productCategory.setUpdate_time(df.format(date));

                String addStore =this.productCategoryService.addByObject("add",productCategory,true);
                if (addStore==null || "".equals(addStore)){
                    logger.debug("设置菜类[新增]，结果=新增失败！");
                    throw new RuntimeException("新增失败!");
                }else {
                    map.put("ok","ok");
                }
            }
            return map;
        }catch (DataAccessException dae){
            logger.error("设置菜类[新增]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置菜类[新增]异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }


    /**
     * 根据菜类编号查找列表，如果有,循环查询除this记录,则返回error,则修改
     * @return map
     */
    @RequestMapping("/edit")
    public Object edit(ProductCategory productCategory) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            Date date = new Date();  //当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
            productCategory.setUpdate_time(df.format(date));


            List<ProductCategory> list=this.productCategoryService.getListByObject("getByCategoryType",productCategory.getCategory_type());
            if(list.size()>0){
                for (int i=0;i<list.size();i++){
                    if (list.get(i).getCategory_id() == productCategory.getCategory_id() || list.get(i).getCategory_id().equals(productCategory.getCategory_id())){
                        this.productCategoryService.updateByObject("edit",productCategory);
                        map.put("ok","ok");
                    }else{
                        map.put("error","error");

                    }
                }
            }else {
                this.productCategoryService.updateByObject("edit",productCategory);
                map.put("ok","ok");
            }
            return map;
        }catch (DataAccessException dae){
            logger.error("设置菜类[修改]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置菜类[修改]异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }

    @RequestMapping("/del")
    public Object del(String category_id) throws Exception{
        try {
            if (category_id!=null){
                this.productCategoryService.deleteByObject("del",category_id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除菜类数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除菜类列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

}
