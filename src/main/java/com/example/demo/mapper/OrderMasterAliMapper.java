package com.example.demo.mapper;

import com.example.demo.pojo.OrderMasterAli;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface OrderMasterAliMapper extends Mapper<OrderMasterAli> {

    //查询今日已支付的订单
    @Select("select * from order_master_ali where to_days(create_time) = to_days(now()) and pay_status='1'")
    List<OrderMasterAli> TodayDataOrder();

    //查询本周已支付的订单
    @Select("SELECT * FROM order_master_ali WHERE YEARWEEK(date_format(create_time,'%Y-%m-%d'),1) = YEARWEEK(now(),1) and pay_status='1' ORDER BY create_time")
    List<OrderMasterAli> weekDataOrder();

    //查询本月已支付的订单
    @Select("SELECT *FROM order_master_ali WHERE DATE_FORMAT(create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) and pay_status='1' ORDER BY create_time")
    List<OrderMasterAli> monthDataOrder();

}
