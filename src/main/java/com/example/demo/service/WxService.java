package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.WxMapper;
import com.example.demo.mapper.ZfbMapper;
import com.example.demo.pojo.Wx;
import com.example.demo.pojo.Zfb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class WxService extends BaseService<Wx,String> {

    @Autowired
    private WxMapper wxMapper;

    @Override
    public BaseMapper<Wx, String> getMapper() {
        return wxMapper;
    }


}
