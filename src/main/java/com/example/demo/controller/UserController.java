package com.example.demo.controller;


import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtHelper;
import com.example.demo.util.MD5Utils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    Audience audience;


    /**
     * 获取用户列表
     * count 长度
     * data 列表
     * @return map
     */
    @RequestMapping("/getListUser")
    public Object getListUser() throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            List<User> list=userService.getListByObject("getListUser",null);
            map.put("count",list.size());
            map.put("data",list);
        }catch (DataAccessException dae){
            logger.error("查询用户列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询用户列表异常！", e);
        }
        return map;
    }

    /**
     * 设置用户[新增]
     * @return ok/map
     */
    @RequestMapping("/register")
    public Object register(User user) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            if (user ==null) {
                return "请您填写用户信息!";
            }
            //根据用户名查询数据库，如果存在，则从新输入！
            Object name= this.userService.getByObject("getByName",user.getName());
            if(name!=null){
                map.put("exis","exis");
                return map;
            }

            Date date = new Date();//当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            user.setPassword(MD5Utils.md5(user.getPassword()));
            user.setCreateTime(df.format(date));
            user.setUpdateTime(df.format(date));
            String addUser =this.userService.addByObject("add",user,true);
            if (addUser==null || "".equals(addUser)){
                logger.debug("设置用户[新增]，结果=新增失败！");
                throw new RuntimeException("新增失败!");
            }else {
                map.put("ok","ok");
                return map;
            }
        }catch (DataAccessException dae){
            logger.error("设置用户[新增]数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("设置用户[新增]异常！", e);
            return "操作异常，请您稍后再试!";
        }
    }

    /**
     * 用户登录
     * name 用户名称
     * password 用户密码
     * @return ok/map
     */
    @RequestMapping("/loginUser")
    public Object loginUser(String name, String password, HttpServletResponse response) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            map.put("name",name);
            map.put("password",MD5Utils.md5(password));
            Object u=this.userService.getByObject("loginUser",map);
            if(u!=null){
                map.put("ok","ok");
                String token = JwtHelper.createJWT(u,audience.getName(),audience.getClientId(),audience.getExpiresSecond()*1000,audience.getBase64Secret());
                Cookie userCookie=new Cookie("login",token);
                userCookie.setMaxAge(-1);   //生命周期(365*24*60*60)一年
                userCookie.setPath("/");
                response.addCookie(userCookie);
                map.put("token",token);
                return map;
            }else {
                map.put("error","error");
                return map;
            }
        }catch (DataAccessException d){
            logger.error("登录数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("登录异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/loginGetSession")
    public Object loginGetSession (HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入token");
        String token = request.getParameter("tokens");
        Claims list = JwtHelper.parseJWT(token,audience.getBase64Secret());
        System.out.println(list);
        return list;
    }

    @RequestMapping("/quit")
    private void quit(HttpServletResponse response,HttpServletRequest request) throws IOException {
        Cookie[] cookies= request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
