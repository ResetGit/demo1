package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class RoleService extends BaseService<Role,String> {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public BaseMapper<Role, String> getMapper() {
        return roleMapper;
    }


}
