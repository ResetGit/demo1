package com.example.demo.controller;


import com.example.demo.pojo.Permission;
import com.example.demo.pojo.User;
import com.example.demo.service.PermissionService;
import com.example.demo.service.UserService;
import com.example.demo.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private static final Logger logger = LoggerFactory
            .getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    /**
    *获取权限列表,左侧菜单
     *  return list
    * */
    @RequestMapping("/getListPermission")
    public Object getListPermission() throws Exception{
        List<Permission> list=null;
        try {
            list=this.permissionService.getListByObject("getListPermission",null);

        }catch (DataAccessException d){
            logger.error("查询权限列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询权限列表异常！", e);
        }
        return list;
    }

    /**
     *获取权限列表,权限表树
     *  return map
     * */
    @RequestMapping("/getListPermissionTree")
    public Object getListPermissionTree() throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<Permission> list=this.permissionService.getListByObject("getListPermission",null);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException d){
            logger.error("查询权限列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询权限列表异常！", e);
        }
        return map;
    }


}
