package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.pojo.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailServiceImp implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        //设置id
        orderDetail.setDetailId(Sid.next());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        orderDetail.setCreateTime(date);
        orderDetail.setUpdateTime(date);
        this.orderDetailMapper.insert(orderDetail);

    }

    @Override
    public List<OrderDetail> creatOrderDetailByOrderId(String orderId){
        Example example = new Example(OrderDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        List<OrderDetail> orderDetails = this.orderDetailMapper.selectByExample(example);
        return orderDetails;
    }

    @Override
    public List<OrderDetail> queryOrderDetailByOrderId(String orderId) {

        Example example = new Example(OrderDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);

        List<OrderDetail> orderDetails = this.orderDetailMapper.selectByExample(example);

        return orderDetails;
    }
}
