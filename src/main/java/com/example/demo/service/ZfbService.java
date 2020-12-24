package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.ZfbMapper;
import com.example.demo.pojo.Zfb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class ZfbService extends BaseService<Zfb,String> {

    @Autowired
    private ZfbMapper zfbMapper;

    @Override
    public BaseMapper<Zfb, String> getMapper() {
        return zfbMapper;
    }


}
