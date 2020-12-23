package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.mapper.ZfbMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.OrderMasterAliService;
import com.example.demo.service.ZfbService;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    private ZfbService zfbService;


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
    public Object weekDate(HttpServletRequest request){




        Map<String, Object> map=new HashMap<>();
        List<Object> list2 = new ArrayList<Object>();
        List<Double> list3 = new ArrayList<Double>();
        List<Object> list=this.orderMasterAliMapper.weekDate();

        String data=null;
        double sum=0;

        for (int i=0;i<list.size();i++){
//            System.out.println("data:"+list.get(i).toString());
            data=list.get(i).toString();
            list2.add(data);

//            String token = request.getParameter("tokens");
//            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
//
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
//            String id= String.valueOf(jsonArray.getJSONObject(0).get("id"));//id 为user_id

//            map.put("id",id);
//            map.put("pay_status","1");
//            map.put("create_time",list.get(i).toString());

//            List<Zfb> list1= this.zfbService.getListByObject("WeekData", map);

            List<Object> list1=this.orderMasterAliMapper.getByTimeData1(list.get(i).toString());

            for(int j=0;j<list1.size();j++){
//                System.out.println("sum:"+list1.get(j).toString());
                sum= Double.parseDouble(list1.get(j).toString());
                list3.add(sum);
            }
        }

        map.put("data",list2);
        map.put("sum",list3);
        return map;
    }


}
