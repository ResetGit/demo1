package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.Permission_RoleMapper;
import com.example.demo.pojo.Permission_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class Permission_RoleService extends BaseService<Permission_Role,String> {

    @Autowired
    private Permission_RoleMapper permission_roleMapper;

    @Override
    public BaseMapper<Permission_Role, String> getMapper() {
        return permission_roleMapper;
    }


}
