package com.example.demo.service;

import com.example.demo.pojo.OrderCombo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface OrderComboService {
    List<OrderCombo> orderComboList();
    void addOrderCombo(Map map);
}
