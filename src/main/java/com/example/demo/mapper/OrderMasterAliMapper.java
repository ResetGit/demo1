package com.example.demo.mapper;

import com.example.demo.pojo.OrderMasterAli;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface OrderMasterAliMapper extends Mapper<OrderMasterAli> {

    //根据userid和stroeid来查找订单
    @Select("select\n" +
            "o.order_id as orderId,\n" +
            "s.`name` as shopname,\n" +
            "o.order_amount as orderAmount,\n" +
            "o.order_status as orderStatus,\n" +
            "o.pay_status as payStatus,\n" +
            "o.msg,\n" +
            "o.create_time as createTime,\n" +
            "o.update_time as updateTime\n" +
            "from order_master_ali o\n" +
            "LEFT JOIN store s ON s.id=o.store_id\n" +
            "LEFT JOIN user u ON u.id=s.user_id\n" +
            "where u.id=#{id,jdbcType=INTEGER}")
    List<OrderMasterAli> OrderMasterByUserIdAli(String id);

    @Select("select\n" +
            "o.order_id as orderId,\n" +
            "s.`name` as storeName,\n" +
            "o.order_amount as orderAmount,\n" +
            "o.order_status as orderStatus,\n" +
            "o.pay_status as payStatus,\n" +
            "o.msg,\n" +
            "o.create_time as createTime,\n" +
            "o.update_time as updateTime\n" +
            "from order_master_ali o\n" +
            "LEFT JOIN store s ON s.id=o.store_id\n" +
            "LEFT JOIN user u ON u.id=s.user_id\n" +
            "where u.id=#{id,jdbcType=INTEGER}\n" +
            "AND o.create_time>= #{start}\n" +
            "AND o.create_time< #{end}")
    List<OrderMasterAli> OrderMasterByUserIdAliLike(String id,String start,String end);


    @Select("SELECT * FROM (\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-1) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-2) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-3) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-4) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-5) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-6) as data\n" +
            "union all\n" +
            "select subdate(curdate(),date_format(curdate(),'%w')-7) as data) a")
    List<Object> weekDate();


    @Select("SELECT SUM(order_amount) as sum  from order_master_ali WHERE create_time like '%${create_time}%' and pay_status=#{pay_status} and store_id=#{store_id}")
    List<Object> getByTimeData(String create_time,String pay_status,String store_id);

    @Select("SELECT SUM(order_amount) from order_master_ali WHERE create_time like '%${create_time}%' and pay_status='1'")
    List<Object> getByTimeData1(String create_time);
//    //查询今日已支付的订单
//    @Select("select * from order_master_ali where to_days(create_time) = to_days(now()) and pay_status='1'")
//    List<OrderMasterAli> TodayDataOrder();
//
//    //查询本周已支付的订单
//    @Select("SELECT * FROM order_master_ali WHERE YEARWEEK(date_format(create_time,'%Y-%m-%d'),1) = YEARWEEK(now(),1) and pay_status='1' ORDER BY create_time")
//    List<OrderMasterAli> weekDataOrder();
//
//    //查询本月已支付的订单
//    @Select("SELECT *FROM order_master_ali WHERE DATE_FORMAT(create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) and pay_status='1' ORDER BY create_time")
//    List<OrderMasterAli> monthDataOrder();

}
