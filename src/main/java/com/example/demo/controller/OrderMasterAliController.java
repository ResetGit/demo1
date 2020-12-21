package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.OrderDetailAli;
import com.example.demo.pojo.OrderMaster;
import com.example.demo.pojo.OrderMasterAli;
import com.example.demo.service.OrderMasterAliService;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/orderMasterAli")
public class OrderMasterAliController {

    @Autowired
    private OrderMasterAliService orderMasterAliService;

    @Autowired
    private OrderMasterAliMapper orderMasterAliMapper;


    @Autowired
    Audience audience;



    @PostMapping("/OrderMasterByUserIdAli")
    public Object OrderMasterByUserIdAli(HttpServletRequest request,String start,String end){
        String token = request.getParameter("tokens");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        String id= String.valueOf(jsonArray.getJSONObject(0).get("id"));//id 为user_id
        List<OrderMasterAli> list=null;
        if(start==null || start=="" || end==null || end==""){
             list = this.orderMasterAliMapper.OrderMasterByUserIdAli(id);
        }else {
             list = this.orderMasterAliMapper.OrderMasterByUserIdAliLike(id,start,end);
        }

        Map<String, Object> map=new HashMap<>();
        map.put("count",list.size());
        map.put("data",list);

        return map;
    }

    @RequestMapping("/weekDate")
    public Object weekDate(){
        List<Object> list=this.orderMasterAliMapper.weekDate();
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i).toString());
        }
        return "";
    }

}
