package com.example.demo.controller;

import com.example.demo.pojo.OrderCombo;
import com.example.demo.service.OrderComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderComboController {

    @Autowired
    OrderComboService orderComboService;

    @RequestMapping("ordercombo")
    public Map list(String createtime,String orderid){
        Map map1 = new HashMap();
        List<OrderCombo> list = orderComboService.orderComboList(createtime,orderid);
        Map map2 = new HashMap();
        map1.put("data",list);
        BigDecimal price = new BigDecimal("0");
        for(int i=0;i<list.size();i++){
            String money = list.get(i).getComboprice();
            BigDecimal money1 = new BigDecimal(money);
            price = price.add(money1);
        }
        map1.put("msg",price);
        map1.put("count",list.size());
        System.out.println(map1);
        return map1;
    }

    @RequestMapping("addordercombo")
    public void add(@RequestBody  Map map){
        System.out.println(map);
        orderComboService.addOrderCombo(map);
    }
}
