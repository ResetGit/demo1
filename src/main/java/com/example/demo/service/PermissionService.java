package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class PermissionService extends BaseService<Permission,String> {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public BaseMapper<Permission, String> getMapper() {
        return permissionMapper;
    }


}
