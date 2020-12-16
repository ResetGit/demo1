package com.example.demo.service;

import com.example.demo.pojo.Combo;

import java.util.List;
import java.util.Map;

public interface ComboService {
    List<Combo> ComboList(String name);
    Integer addCombo(Map map);
    String editCombo(Combo combo);
    String delCombo(String id);
}
