package com.example.demo.controller;

import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Role;
import com.example.demo.pojo.User;
import com.example.demo.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {
    private static final Logger logger = LoggerFactory
            .getLogger(PermissionController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("/getListRole")
    public Object getListRole() throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<Role> list=this.roleService.getListByObject("getListRole",null);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException d){
            logger.error("查询角色列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询角色列表异常！", e);
        }
        return map;
    }


    @RequestMapping("/getListSelect")
    public Object getListSelect() throws Exception{
        List<Role> list=null;
        try {
            list=roleService.getListByObject("getListRole",null);

        }catch (DataAccessException dae){
            logger.error("查询用户列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询用户列表异常！", e);
        }
        return list;
    }


    @RequestMapping("/addRole")
    public Object addRole(Role role) throws Exception{
        try {
            Date date = new Date();//当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            role.setCreateTime(df.format(date));
            role.setUpdateTime(df.format(date));
            String addRole=this.roleService.addByObject("addRole",role,true);
            if (addRole==null || "".equals(addRole)){
                logger.debug("设置角色[新增]，结果=新增失败！");
                throw new RuntimeException("新增失败!");
            }else {
                return "success";
            }
        }catch (DataAccessException d){
            logger.error("新增角色数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("新增角色异常！", e);
            return "操作异常，请您稍后再试!";
        }
    }


    @RequestMapping("/editRole")
    public Object editRole(Role role) throws Exception{
        try {
            if(role!=null){
                this.roleService.updateByObject("editRole",role);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("修改角色数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改角色列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }

    }

    @RequestMapping("/del")
    public Object del(Integer id) throws Exception{
        try {
            if (id!=null){
                this.roleService.deleteByObject("del",id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除角色列表数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除角色列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

}
