package com.example.demo.service;

import com.example.demo.pojo.Combo;

import java.util.List;
import java.util.Map;

public interface ComboService {
    List<Combo> ComboList();
    void addCombo(Map map);
    String editCombo(String id);
    String delCombo(String id);
}
