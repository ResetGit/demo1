package com.example.demo.controller;

import com.example.demo.pojo.OrderCombo;
import com.example.demo.service.OrderComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderComboController {

    @Autowired
    OrderComboService orderComboService;

    @RequestMapping("ordercombo")
    public List<OrderCombo> list(){
        List list = orderComboService.orderComboList();
        return list;
    }

    @RequestMapping("addordercombo")
    public void add(@RequestBody  Map map){
        System.out.println(map);
        orderComboService.addOrderCombo(map);
    }
}
