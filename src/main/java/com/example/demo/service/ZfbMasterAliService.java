package com.example.demo.service;

import com.example.demo.mapper.BaseMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.ZfbMasterAliMapper;
import com.example.demo.pojo.User;
import com.example.demo.pojo.ZfbMasterAli;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class ZfbMasterAliService extends BaseService<ZfbMasterAli,String> {

    @Autowired
    private ZfbMasterAliMapper zfbMasterAliMapper;

    @Override
    public BaseMapper<ZfbMasterAli, String> getMapper() {
        return zfbMasterAliMapper;
    }


}
