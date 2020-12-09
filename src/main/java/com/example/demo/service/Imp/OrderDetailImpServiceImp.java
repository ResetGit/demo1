package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.OrderDetailAliMapper;
import com.example.demo.pojo.OrderDetailAli;
import com.example.demo.service.OrderDetailAliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailImpServiceImp implements OrderDetailAliService {

    @Autowired
    private OrderDetailAliMapper orderDetailMapper;

    @Override
    public void saveOrderDetail(OrderDetailAli orderDetail) {
        //设置id
        orderDetail.setDetailId(Sid.next());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        orderDetail.setCreateTime(date);
        orderDetail.setUpdateTime(date);
        this.orderDetailMapper.insert(orderDetail);

    }

    @Override
    public List<OrderDetailAli> creatOrderDetailByOrderId(String orderId){
        Example example = new Example(OrderDetailAli.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        List<OrderDetailAli> orderDetails = this.orderDetailMapper.selectByExample(example);
        return orderDetails;
    }

    @Override
    public List<OrderDetailAli> findbyorderidali(String orderId) {
        Example example = new Example(OrderDetailAli.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        List<OrderDetailAli> list = orderDetailMapper.selectByExample(example);
        try {
            return list;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<OrderDetailAli> queryOrderDetailByOrderId(String orderId) {
        Example example = new Example(OrderDetailAli.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        List<OrderDetailAli> orderDetails = this.orderDetailMapper.selectByExample(example);
        return orderDetails;
    }
}
