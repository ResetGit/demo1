package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.UserRole;
import com.example.demo.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userRole")
public class UserRoleController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping("/setUserRole")
    public Object setUserRole(String user_id,String role_id) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<UserRole> list=this.userRoleService.getListByObject("getByUserId",user_id);
            if(list.size()>0){
                map.put("ex","ex");
                return map;
            }else {
                map.put("user_id",user_id);
                map.put("role_id",role_id);
                this.userRoleService.addByObject("setUserRole",map,true);
                map.put("success","success");
                return map;
            }
        }catch (DataAccessException d){
            logger.error("插入数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("插入异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }

    @RequestMapping("/getUserAndRoles")
    public Object getUserAndRoles(String user_id) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<UserRole> list=userRoleService.getListByObject("getUserAndRoles",user_id);
//            map.put("data",list);
//            map.put("count",list.size());
            return list;
        }catch (DataAccessException dae){
            logger.error("查询用户列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询用户列表异常！", e);
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/delCheck")
    public Object delCheck(String id) throws Exception{
        try {
            if (id!=null){
                this.userRoleService.deleteByObject("delCheck",id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }
}
