//package com.example.demo.service.Imp;
//
//import com.example.demo.common.idworker.Sid;
//import com.example.demo.service.OrderMasterService;
//import com.linmao.common.idworker.Sid;
//import com.linmao.config.WeChatConfig;
//import com.linmao.mapper.OrderMasterMapper;
//import com.linmao.pojo.OrderMaster;
//import com.linmao.service.OrderMasterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//import tk.mybatis.mapper.entity.Example;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.TimeZone;
//
//@Service
//public class OrderMasterServiceImp implements OrderMasterService {
//
//    @Autowired
//    private Sid sid;
//    @Autowired
//    private OrderMasterMapper orderMasterMapper;
//    @Autowired
//    private WeChatConfig weChatConfig;
//
//    //保存订单
//    @Override
//    public void saveOrder(OrderMaster orderMaster) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String date = df.format(new Date());
//        orderMaster.setCreateTime(date);
//        orderMaster.setUpdateTime(date);
//        this.orderMasterMapper.insert(orderMaster);
//
//    }
//
//    //根据订单编号来查询订单
//    @Override
//    public OrderMaster queryOrderById(@RequestBody String orderId) {
//        Example example = new Example(OrderMaster.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("orderId",orderId);
//        return this.orderMasterMapper.selectOneByExample(example);
//    }
//
//    //查询订单列表
//    @Override
//    public List<OrderMaster> orderMasterList(String start,String end) {
//        Example example = new Example(OrderMaster.class);
//        example.orderBy("createTime").desc();
//        Example.Criteria criteria = example.createCriteria();
////        criteria.andEqualTo("appid",weChatConfig.getWECHAT_APPID());
//        //模糊查询
//        if(start==null || start=="" || end==null || end==""){
//            List<OrderMaster>  orderMasters = this.orderMasterMapper.selectByExample(example);
//            return orderMasters;
//        }else{
////            criteria.andLike("buyerOpenid", "%" + buyerOpenid + "%");
//            criteria.andBetween("createTime",start,end);
//            List<OrderMaster>  orderMasters = this.orderMasterMapper.selectByExample(example);
//            return orderMasters;
//        }
//    }
//
//    @Override
//    public List<OrderMaster>  orderWeedKMasterList(Date start, Date end) {
//        Example example = new Example(OrderMaster.class);
//        Example.Criteria criteria = example.createCriteria();
//
//        criteria.andBetween("createTime",start,end);
//        List<OrderMaster>  orderMasters = this.orderMasterMapper.selectByExample(example);
//        return orderMasters;
//    }
//
//    //通过订单id删除订单
//    @Override
//    public void deleteOrderById(String orderId) {
//
//    }
//
//    //根据openid获取oderlist
//    @Override
//    public List<OrderMaster> getListByOpenid(String openId) {
//
//        Example example = new Example(OrderMaster.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("buyerOpenid",openId);
//
//        List<OrderMaster> orderMasters = this.orderMasterMapper.selectByExample(example);
//
//        return orderMasters;
//    }
//
//    @Override
//    public void updateOrderById(String orderId) {
//        OrderMaster orderMaster1 = new OrderMaster();
//        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        s.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        String date = s.format(new Date());
//        orderMaster1.setOrderId(orderId);
//        orderMaster1.setPayStatus(1);
//        orderMaster1.setOrderStatus(1);
//        orderMaster1.setUpdateTime(date);
//        orderMasterMapper.updateByPrimaryKeySelective(orderMaster1);
//    }
//
//
//
//
//    @Override
//    public List<OrderMaster> DataOrder(String create_time) {
//        List<OrderMaster>  orderMasters = this.orderMasterMapper.selectAll();
//        return orderMasters;
//    }
//
//
//
//}
