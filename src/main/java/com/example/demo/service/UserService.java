package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class UserService extends BaseService<User,String> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseMapper<User, String> getMapper() {
        return userMapper;
    }


}
