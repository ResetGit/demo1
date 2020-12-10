package com.example.demo.controller;

import com.example.demo.config.WeChatConfig;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.pojo.OrderMaster;
import com.example.demo.service.OrderMasterService;
import com.example.demo.util.HttpUtil;
import com.example.demo.util.IMoocJSONResult;
import com.example.demo.util.OpenIdJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private WeChatConfig weChatConfig;


}
