package com.example.demo.service.Imp;

import com.example.demo.common.idworker.Sid;
import com.example.demo.config.WeChatConfig;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.pojo.OrderMasterAli;
import com.example.demo.service.OrderMasterAliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class OrderMasterAllServiceImp implements OrderMasterAliService {
    @Autowired
    private Sid sid;
    @Autowired
    private OrderMasterAliMapper orderMasterMapper;
    @Autowired
    private WeChatConfig weChatConfig;

    //保存支付宝订单
    @Override
    public void saveOrder(OrderMasterAli orderMaster) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        orderMaster.setCreateTime(date);
        orderMaster.setUpdateTime(date);
        this.orderMasterMapper.insert(orderMaster);

    }

    //根据订单编号来查询订单
    @Override
    public OrderMasterAli queryOrderById(@RequestBody String orderId) {
        Example example = new Example(OrderMasterAli.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        return this.orderMasterMapper.selectOneByExample(example);
    }

    //查询订单列表
    @Override
    public List<OrderMasterAli> orderMasterList(String start, String end) {
        Example example = new Example(OrderMasterAli.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("appid",weChatConfig.getWECHAT_APPID());
        //模糊查询
        if(start==null || start=="" || end==null || end==""){
            List<OrderMasterAli>  orderMasters = this.orderMasterMapper.selectByExample(example);
            return orderMasters;
        }else{
//            criteria.andLike("buyerOpenid", "%" + buyerOpenid + "%");
            criteria.andBetween("createTime",start,end);
            List<OrderMasterAli>  orderMasters = this.orderMasterMapper.selectByExample(example);
            return orderMasters;
        }
    }

    @Override
    public List<OrderMasterAli>  orderWeedKMasterList(Date start, Date end) {
        Example example = new Example(OrderMasterAli.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andBetween("createTime",start,end);
        List<OrderMasterAli>  orderMasters = this.orderMasterMapper.selectByExample(example);
        return orderMasters;
    }

    //通过订单id删除订单
    @Override
    public void deleteOrderById(String orderId) {

    }

    //根据openid获取oderlist
    @Override
    public List<OrderMasterAli> getListByOpenid(String openId) {

        Example example = new Example(OrderMasterAli.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buyerId",openId);

        List<OrderMasterAli> orderMasters = this.orderMasterMapper.selectByExample(example);

        return orderMasters;
    }

    @Override
    public void updateOrderById(String orderId) {
        OrderMasterAli orderMaster1 = new OrderMasterAli();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        s.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String date = s.format(new Date());
        orderMaster1.setOrderId(orderId);
        orderMaster1.setPayStatus(1);
        orderMaster1.setOrderStatus(1);
        orderMaster1.setUpdateTime(date);
        orderMasterMapper.updateByPrimaryKeySelective(orderMaster1);
    }




    @Override
    public List<OrderMasterAli> DataOrder(String create_time) {
        List<OrderMasterAli>  orderMasters = this.orderMasterMapper.selectAll();
        return orderMasters;
    }


}
