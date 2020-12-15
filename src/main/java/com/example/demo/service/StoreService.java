package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.StoreMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Store;
import com.example.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class StoreService extends BaseService<Store,String> {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public BaseMapper<Store, String> getMapper() {
        return storeMapper;
    }


}
