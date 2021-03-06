package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.User;
import com.example.demo.service.PermissionService;
import com.example.demo.service.UserService;
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
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private static final Logger logger = LoggerFactory
            .getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    @Autowired
    Audience audience;

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
    *获取权限列表,左侧菜单
     *  return list
    * */
    @RequestMapping("/getListPermissionUl")
    public Object getListPermissionUl(HttpServletRequest request) throws Exception{
        List<Permission> list=null;
        try {

            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());


            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));

            Object id=jsonArray.getJSONObject(0).get("id");
            list=this.permissionService.getListByObject("getListPermissionUl",id);

//            System.out.println(jsonArray.getJSONObject(0).get("id"));
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

    @RequestMapping("/addPermission")
    public Object addPermission(Permission permission) throws Exception{
        try {
            Date date = new Date();//当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            permission.setCreateTime(df.format(date));
            permission.setUpdateTime(df.format(date));
            String addPermission=this.permissionService.addByObject("addPermission",permission,true);
            if (addPermission==null || "".equals(addPermission)){
                logger.debug("设置权限[新增]，结果=新增失败！");
                throw new RuntimeException("新增失败!");
            }else {
                return "success";
            }
        }catch (DataAccessException d){
            logger.error("添加权限列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("添加权限列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/editPermList")
    public Object editPermList(Permission permission) throws Exception{
        try {
            if(permission!=null){
                this.permissionService.updateByObject("editPermList",permission);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("修改权限列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改权限列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }

    }
    //111
    @RequestMapping("/del")
    public Object dels(Integer id) throws Exception{
        try {
            String keys="";
            if (id!=null){
                keys +=id;
                List<Permission> list=this.permissionService.getListByObject("getListPermission",null);
                for(int i=0;i<list.size();i++){
                    if(id==list.get(i).getPid()){
                        keys += ",";
                        keys +=  list.get(i).getId();
                    }
                }
                this.permissionService.deleteByObject("delKeys",keys);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除权限列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除权限列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }


}
