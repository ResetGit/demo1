package com.example.demo.controller;

import com.example.demo.pojo.Permission_Role;
import com.example.demo.service.Permission_RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissionRole")
public class Permission_RoleController {
    private static final Logger logger = LoggerFactory
            .getLogger(PermissionController.class);

    @Autowired
    private Permission_RoleService permission_roleService;

    @RequestMapping("/editRolePermission")
    public Object editRolePermission(String permission_id,String role_id) throws Exception{
        try {

            //1.删除原权限
            this.permission_roleService.deleteByObject("delPermission",role_id);

            Map<String, Object> map=new HashMap<>();
            //2.添加权限
            if(permission_id!=null){
                String[] arrays =permission_id.split(",");
                for (String keys : arrays) {
                    map.put("permission_id",keys);
                    map.put("role_id",role_id);

                    this.permission_roleService.addByObject("addPermission",map,true);
                }
            }
        }catch (DataAccessException d){
            logger.error("插入数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("插入异常！", e);
        }
        return "success";
    }

    @RequestMapping("/getByRoleId")
    public Object getByRoleId(String role_id) throws Exception{
        List<Permission_Role> list=null;
        try {
            list=this.permission_roleService.getListByObject("getByRoleId",role_id);
        }catch (DataAccessException d){
            logger.error("查询角色数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询角色异常！", e);
        }
        return list;
    }


}
