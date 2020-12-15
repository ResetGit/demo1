package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.Store;
import com.example.demo.pojo.User;
import com.example.demo.service.StoreService;
import com.example.demo.util.JwtHelper;
import com.example.demo.util.MD5Utils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private StoreService storeService;

    @Autowired
    Audience audience;


    @RequestMapping("/getListStoreByUserId")
    public Object getListStoreByUserId(HttpServletRequest request) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {

            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");

            List<Store> list=storeService.getListByObject("getListStoreByUserId",id);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException dae){
            logger.error("查询店铺列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询店铺列表异常！", e);
        }
        return map;
    }

    @RequestMapping("/add")
    public Object add(Store store,HttpServletRequest request) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {

            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");


            Date date = new Date();  //当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
            store.setUser_id(String.valueOf(id));
            store.setCreateTime(df.format(date));
            store.setUpdateTime(df.format(date));

            String addStore =this.storeService.addByObject("add",store,true);

            if (addStore==null || "".equals(addStore)){
                logger.debug("设置用户[新增]，结果=新增失败！");
                throw new RuntimeException("新增失败!");
            }else {
                map.put("ok","ok");
                return map;
            }
        }catch (DataAccessException dae){
            logger.error("设置店铺[新增]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置店铺[新增]异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }


    @RequestMapping("/editStore")
    public Object editStore(Store store) throws Exception{
        try {
            if (store!=null){
                Date date = new Date();  //当前时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //设置日期格式
                store.setUpdateTime(df.format(date));
                this.storeService.updateByObject("editStore",store);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("修改店铺数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("修改店铺列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }


    @RequestMapping("/del")
    public Object del(Integer id) throws Exception{
        try {
            if (id!=null){
                this.storeService.deleteByObject("del",id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除店铺数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除店铺列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }
}
