package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.OrderComboMapper;
import com.example.demo.pojo.OrderCombo;
import com.example.demo.service.OrderComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderComboServiceImp implements OrderComboService {

    @Autowired
    OrderComboMapper orderComboMapper;

    @Override
    public List<OrderCombo> orderComboList() {
        List<OrderCombo> list = orderComboMapper.selectAll();
        return list;
    }

    @Override
    public void addOrderCombo(Map map) {
        OrderCombo orderCombo = new OrderCombo();
        orderCombo.setId(Sid.next());
        orderCombo.setComboname((String) map.get("comboname"));
        orderCombo.setComboprice((String) map.get("comboprice"));
        orderCombo.setOrderid((String) map.get("outtradeno"));
        orderCombo.setCreatetime((String)map.get("gmtcreate"));
        orderCombo.setUsername((String)map.get("username"));
        orderComboMapper.insert(orderCombo);
    }
}
