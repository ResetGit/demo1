package com.example.demo.controller;

import com.example.demo.service.ComboService;
import com.example.demo.pojo.Combo;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ComboController {

    @Autowired
    private ComboService comboService;

    @RequestMapping("listCombo")
    private Map listCombo(String name){
        System.out.println(name);
        Map map = new HashMap();
            List list = comboService.ComboList(name);
            map.put("data",list);
            map.put("count",list.size());
            return map;
    }

    @RequestMapping("addcombo")
    public Integer addcombo(@RequestBody Map<String,String> map){
        int i = comboService.addCombo(map);
        return i;
    }

    @RequestMapping("delcombo")
    public String delcombo(String id){
        String i =comboService.delCombo(id);
        return i;
    }

    @RequestMapping("editcombo")
    public String editcombo(Combo combo){
        String i = comboService.editCombo(combo);
        return i;
    }
}
