package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.mapper.OrderMasterAliMapper;
import com.example.demo.mapper.ZfbMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.OrderMasterAliService;
import com.example.demo.service.StoreService;
import com.example.demo.service.ZfbService;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;



@RestController
@RequestMapping("/orderMasterAli")
public class OrderMasterAliController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private OrderMasterAliService orderMasterAliService;

    @Autowired
    private OrderMasterAliMapper orderMasterAliMapper;

    @Autowired
    private ZfbService zfbService;

    @Autowired
    private ZfbMapper zfbMapper;

    @Autowired
    private StoreService storeService;


    @Autowired
    Audience audience;



    @PostMapping("/OrderMasterByUserIdAli")
    public Object OrderMasterByUserIdAli(HttpServletRequest request,String start,String end){
        String token = request.getParameter("tokens");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        String id= String.valueOf(jsonArray.getJSONObject(0).get("id"));//id 为user_id
        List<OrderMasterAli> list=null;
        if(start==null || start=="" || end==null || end==""){
             list = this.orderMasterAliMapper.OrderMasterByUserIdAli(id);
        }else {
             list = this.orderMasterAliMapper.OrderMasterByUserIdAliLike(id,start,end);
        }

        Map<String, Object> map=new HashMap<>();
        map.put("count",list.size());
        map.put("data",list);

        return map;
    }

    @RequestMapping("/weekDate")
    public Object weekDate(HttpServletRequest request){

        String token = request.getParameter("tokens");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        String id= String.valueOf(jsonArray.getJSONObject(0).get("id"));//id 为user_id

        Map<String, Object> map=new HashMap<>();
        List<Object> listTime = new ArrayList<Object>();
        List<Object> sum= new ArrayList<Object>();
        List<Object> sum1= new ArrayList<Object>();
        double sumWeek = 0.0;


        //获取本周周一到周日的日期
        List<Object> listDataTime=this.orderMasterAliMapper.weekDate();

        //根据用户id来获取店铺id(storeId)
        List<Store> store=this.storeService.getListByObject("getByStoreId",id);



        List<Zfb> zfb=null;
        for (int i=0;i<listDataTime.size();i++){
            //把日期存在list
            listTime.add(listDataTime.get(i).toString());
            System.out.println("Time:=========================="+listDataTime.get(i).toString());

            for(int y=0;y<store.size();y++){
                //店铺id
                String storeId=store.get(y).getId();

                System.out.println("storeId:---"+storeId);


                map.put("storeId",storeId);
                map.put("pay_status","1");
                map.put("create_time",listDataTime.get(i).toString());
                zfb=this.zfbService.getListByObject("getListByStoreIdSum",map);

                if(zfb.size()>0){
                    for(int j=0;j<zfb.size();j++){
//                        if(zfb.get(j).getOrder_amount()!=null){
//                            sumWeek= zfb.stream().mapToDouble(Zfb::getOrder_amount).sum();
//                        }
                    }
                }else {
                    sumWeek=0.0;
                }
                System.out.println("sum:"+sumWeek);
                sum.add(sumWeek);

                System.out.println(sum);
//                map.put("data",listTime);
//                map.put("sum",sumWeek);
            }


        }



        return map;
    }

    @RequestMapping("/DateTime")
    public Object DateTime(){
        //获取本周周一到周日的日期
        List<Object> listDataTime=this.orderMasterAliMapper.weekDate();

        return listDataTime;
    }

    @RequestMapping("/getDatList")
    public Object getDatLists(String storeId){
        Map<String, Object> map=new HashMap<>();
        List<Object> name=new ArrayList<Object>();
        List<Object> time=new ArrayList<Object>();
        List<Object> seriesData=new ArrayList<Object>();
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        Double sumWeek=0.0;

        //根据用户id 获取店铺name
        Store stores= (Store) this.storeService.getByObject("getByStoreName",storeId);

        //获取本周周一到周日的日期
        List<Object> listDataTime=this.orderMasterAliMapper.weekDate();
        for (int i=0;i<listDataTime.size();i++) {
            //把日期存在list
            time.add(listDataTime.get(i).toString());
        }


        name.add(stores.getName());
        time.add(listDataTime);


        /**
         * 柱状图上的具体数据1
         */
        for (int i=0;i<listDataTime.size();i++){

            map.put("storeId",storeId);
            map.put("pay_status","1");
            map.put("create_time",listDataTime.get(i).toString());
            List<Zfb> zfb=this.zfbService.getListByObject("getListByStoreId",map);
            if(zfb.size()>0){
                for(int x=0;x<zfb.size();x++){
                    sumWeek= zfb.stream().mapToDouble(Zfb::getOrder_amount).sum();
                }
            }else {
                sumWeek=0.0;
            }
            seriesData.add(decimalFormat.format(sumWeek));
        }
        map.put("name",name);
        map.put("time",time);
        map.put("seriesData",seriesData);
        return map;
    }

    @RequestMapping("/getDatListByTime")
    public Object getDatListByTime(String time,String storeId){
        Map<String, Object> map=new HashMap<>();
        map.put("storeId",storeId);
        map.put("time",time);

        List<Zfb> zfb=this.zfbService.getListByObject("getDatListByTime",map);

        return zfb;
    }

    @RequestMapping("/OrderMasterToday")
    public Object OrderMasterToday(String storeId){


        Date date = new Date();  //当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  //设置日期格式
        System.out.println(df.format(date));
        Map<String, Object> map=new HashMap<>();

        map.put("storeId",storeId);
        map.put("pay_status","1");
        map.put("create_time",df.format(date));

        List<Zfb> list=this.zfbService.getListByObject("getListByStoreIdToday",map);
        if(list.size()>0){
            map.put("count",list.size());
            map.put("data",list);

        }else {
            map.put("count",list.size());
            map.put("data",list);
        }
        return map;
    }


    @RequestMapping("/OrderMasterAli")
    public Object OrderMasterAli(String storeId,String start,String end){
        Map<String, Object> map=new HashMap<>();

        map.put("storeId",storeId);
        map.put("start",start);
        map.put("end",end);

        List<Zfb> list=this.zfbService.getListByObject("OrderMasterAli",map);
        if(list.size()>0){
            map.put("count",list.size());
            map.put("data",list);

        }else {
            map.put("count",list.size());
            map.put("data",list);
        }
        return map;
    }
}
