package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.OrderComboMapper;
import com.example.demo.pojo.OrderCombo;
import com.example.demo.service.OrderComboService;
import org.simpleframework.xml.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderComboServiceImp implements OrderComboService {

    @Autowired
    OrderComboMapper orderComboMapper;

    @Override
    public List<OrderCombo> orderComboList(String createtime,String orderid) {
        createtime+="%";
        orderid+="%";
        Example example = new Example(OrderCombo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("createtime", createtime);
        criteria.andLike("orderid", orderid);
        if (createtime.equals("null%") && orderid.equals("null%")) {
            List<OrderCombo> list = orderComboMapper.selectAll();
            return list;
        }else {
            List<OrderCombo> list = orderComboMapper.selectByExample(example);
            return list;
        }
    }

    @Override
    public List<OrderCombo> StaffList(String date) {
        date+="%";
        Example example = new Example(OrderCombo.class);
        Example.Criteria criteria = example.createCriteria();
        if(date.equals("null%")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM%");
            Date newDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newDate);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
            newDate = calendar.getTime();
            String StaffDate = simpleDateFormat.format(newDate);
            criteria.andLike("createtime",StaffDate);
            List<OrderCombo> list = orderComboMapper.selectByExample(example);
            return list;
        }else {
            criteria.andLike("createtime",date);
            List<OrderCombo> list = orderComboMapper.selectByExample(example);
            return list;
        }
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
        orderCombo.setShopnumber((String) map.get("shopnumber"));
        orderComboMapper.insert(orderCombo);
    }
}
