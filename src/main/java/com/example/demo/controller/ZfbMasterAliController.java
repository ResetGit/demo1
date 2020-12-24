package com.example.demo.controller;


import com.example.demo.pojo.ZfbMasterAli;
import com.example.demo.service.ZfbMasterAliService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/zfbMasterAli")
public class ZfbMasterAliController {
    private static final Logger logger = LoggerFactory
            .getLogger(ZfbMasterAliController.class);

    @Autowired
    private ZfbMasterAliService zfbMasterAliService;

    @RequestMapping("/getList")
    public Object getList() throws Exception{
        List<ZfbMasterAli> list=this.zfbMasterAliService.getListByObject("getList",null);
        System.out.println(list.size());
        return list;
    }

}
