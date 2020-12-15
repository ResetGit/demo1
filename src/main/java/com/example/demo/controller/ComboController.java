package com.example.demo.controller;

import com.example.demo.service.ComboService;
import com.example.demo.pojo.Combo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ComboController {

    @Autowired
    private ComboService comboService;

    @RequestMapping("listCombo")
    private List<Combo> listCombo(){
            List list = comboService.ComboList();
            if(list.size()==0){
                List list1 = new ArrayList();
                list1.add("暂无数据");
                return list1;
            }
            return list;
    }
}
