package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.pojo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class UserRoleService extends BaseService<UserRole,String> {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public BaseMapper<UserRole, String> getMapper() {
        return userRoleMapper;
    }


}
