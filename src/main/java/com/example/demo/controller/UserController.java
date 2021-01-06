package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.example.demo.common.idworker.Sid;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.*;
import com.example.demo.service.OrderComboService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserRoleService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtHelper;
import com.example.demo.util.MD5Utils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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

    @Autowired
    OrderComboService orderComboService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    private StoreService storeService;

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
        System.out.println(name);
        System.out.println(password);
        try {
            map.put("name",name);
            map.put("password",MD5Utils.md5(password));
            Object u=this.userService.getByObject("loginUser",map);
            System.out.println(u);
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


    /**
     *修改上架下架功能
     * 根据id 取获状态，修改状态
     * @return success
     */
    @RequestMapping("/setState")
    public Object setState(String id) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {

            String state=null;
            User user= (User) this.userService.getByObject("getByState",id);
            if(user.getState()==0){
                state="1";
            }else if(user.getState()==1){
                state="0";
            }

            map.put("id",id);
            map.put("state",state);
            this.userService.updateByObject("setState",map);
            return "success";
        }catch (DataAccessException d){
            logger.error("修改上架下架数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("修改上架下架列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

    /**
     *修改
     * @return success
     */
    @RequestMapping("/editUser")
    public Object editUser(User user) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            Date date = new Date();//当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            user.setUpdateTime(df.format(date));
            this.userService.updateByObject("editUser",user);
            return "success";
        }catch (DataAccessException d){
            logger.error("修改上架下架数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("修改上架下架列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/del")
    public Object del(Integer id) throws Exception{
        try {
            if (id!=null){
                this.userService.deleteByObject("del",id);
            }
            return "success";
        }catch (DataAccessException d){
            logger.error("删除用户数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("删除用户列表异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/getUserName")
    public Object getUserName(HttpServletRequest request) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id

            User user= (User) this.userService.getByObject("getUserName",id);
            return user;
        }catch (DataAccessException d){
            logger.error("登录数据库异常！", d.getMessage());
            throw new RuntimeException("数据库异常：" + d.getMessage());
        }catch (Exception e){
            logger.error("登录异常！", e);
            e.printStackTrace();
            return "操作异常，请您稍后再试!";
        }

    }

    @RequestMapping("/editMeans")
    public Object editMeans(@RequestBody Map map,HttpServletRequest request,HttpServletResponse response){
        int i = userService.updateByObject("editMeans",map);
        Cookie[] cookies= request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return i;
    }

    @RequestMapping("/editPassword")
    public Object editPassword(@RequestBody Map map,HttpServletRequest request,HttpServletResponse response){
        map.put("password",MD5Utils.md5((String) map.get("sh_password")));
        map.put("newpassword",MD5Utils.md5((String) map.get("new_password")));
        List<User> userList = userService.getListByObject("findbyPassword",map);
        if(userList.size() != 0){
            int i = userService.updateByObject("editPassword",map);
            Cookie[] cookies= request.getCookies();
            for(Cookie cookie: cookies){
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return i;
        }else {
            return 0;
        }
    }

    @RequestMapping("/ForgetPassword")
    public Object ForgetPassword(@RequestBody Map map) {
        map.put("password",MD5Utils.md5((String) map.get("sh_password")));
        List<User> userList = userService.getListByObject("findbyPhone", map);
        if (userList.size() != 0) {
            int i = userService.updateByObject("ForgetPassword", map);
            return i;
        } else {
            return 0;
        }
    }

    @RequestMapping("/StaffList")
    public Object StaffList(String date,HttpServletRequest request){
        String tcdate="";
        List<User> userList;
        if(date==null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Date date1 = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
            date1 = calendar.getTime();
            String StaffDate = simpleDateFormat.format(date1);
            tcdate = StaffDate;
        }else {
            tcdate = date;
        }
        Map map = new HashMap();
        //员工列表
        String token = request.getParameter("tokens");
        System.out.println("测试"+token);
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        Object id=jsonArray.getJSONObject(0).get("id");

        List<UserRole> list=userRoleService.getListByObject("getByUserId",id.toString());
        if(list.get(0).getRole_id().equals("1")) {
            userList = userService.getListByObject("getStaffList", null);
        }else {
            userList = userService.getListByObject("getUserName", id.toString());
        }
        //销售额列表
        List<OrderCombo> orderComboList = orderComboService.StaffList(date);

        //创建bigdecimal储存提成金额
        BigDecimal bigDecimal = new BigDecimal("0");

        //循环计算各月销售额的总金额
        for (int i = 0;i<orderComboList.size();i++){
            String combprice = orderComboList.get(i).getComboprice();   //订单金额combprice
            BigDecimal combbdc = new BigDecimal(combprice);             //用combbdc保存金额
            bigDecimal = bigDecimal.add(combbdc);                       //累加计算
        }

        //循环员工列表计算提成
        for (int j = 0;j<userList.size();j++){
            //new一个实体类保存总提成
            User user = new User();
            user.setAddress(userList.get(j).getAddress());
            user.setName(userList.get(j).getName());
            user.setId(userList.get(j).getId());
            user.setOccupation(userList.get(j).getOccupation());
            String tc = userList.get(j).getAddress();           //这个是提成系数

            //创建bigdecimal保存数据
            BigDecimal Tcbdc = new BigDecimal(tc);
            BigDecimal xs = new BigDecimal("100");

            //计算转换成百分比 tcbdc % 100
            Tcbdc = Tcbdc.divide(xs);

            //总提成
            Tcbdc = bigDecimal.multiply(Tcbdc).setScale(2,RoundingMode.CEILING);
            user.setTc(Tcbdc.toString());
            userList.set(j,user);
        }
        map.put("data",userList);
        map.put("count",userList.size());
        map.put("msg",tcdate+"月,本月总金额:"+bigDecimal);
        return map;
    }

    @RequestMapping("/delcombo")
    public Object delcombo(Integer id,String tokens){
        Claims listToken = JwtHelper.parseJWT(tokens,audience.getBase64Secret());
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        Object id2=jsonArray.getJSONObject(0).get("id");
        List<UserRole> list = userRoleService.getListByObject("getByUserId",id2.toString());
        if(list.get(0).getRole_id().equals("1")) {
            int i = userService.deleteByObject("del", id);
            int j = userRoleService.deleteByObject("deluserid", id.toString());
            int p = i + j;
            System.out.println(p);
            return p;
        }else {
            return "3";
        }
    }

    @RequestMapping("/addcombo")
    public Object delcombo(@RequestBody Map map){
        String token = (String) map.get("token");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        Object id=jsonArray.getJSONObject(0).get("id");
        List<UserRole> list = userRoleService.getListByObject("getByUserId",id.toString());
        if (list.get(0).getRole_id().equals("1")){
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            map.put("password",MD5Utils.md5((String) map.get("comboprice")));
            map.put("date",date);
        //根据用户名查询数据库，如果存在，则从新输入！
        Object name= this.userService.getByObject("getByName",map.get("comboname"));
        if(name!=null){
            return "0";
        }else {
            userService.addByObject("addcombo", map, true);
            List<User> userList = userService.getListByObject("getByName",map.get("comboname"));
            String userid = userList.get(0).getId();
            Map map1 = new HashMap( );
            map1.put("user_id",userid);
            map1.put("role_id",3);
            userRoleService.addByObject("addUserRole",map1,true);
            return "1";
        }
        }else {
            return "3";
        }
    }

    @RequestMapping("/editcombo")
    public Object editcombo(@RequestBody Map map){
        String token = (String) map.get("token");
        Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
        Object id=jsonArray.getJSONObject(0).get("id");
        List<UserRole> list = userRoleService.getListByObject("getByUserId",id.toString());
        if (list.get(0).getRole_id().equals("1")) {
            int i = userService.updateByObject("editcombo", map);
            return i;
        }else {
            return "3";
        }

    }

    @RequestMapping("/differentDays")
    public Object differentDays(HttpServletRequest request){
        try
        {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id


            User user=(User) this.userService.getByObject("getByUserId",id);

            Date date1 =new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//            String dateStr ="2021-12-20 17:29:20";
//            String dateStr ="2021-12-19 17:29:20";
            String dateStr = format1.format(date1);
//            String dateStr = user.getCreateTime();
            String dateStr2 = user.getEndTime();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println("ben:"+dateStr);
            System.out.println("end:"+dateStr2);


            Date date2 = format.parse(dateStr2);
            Date date = format.parse(dateStr);

            int days = (int) ((date2.getTime() - date.getTime()) / (1000*3600*24));

            if(days>=0 && days<=5){
                System.out.println("用户还有"+days+"天到期,请续费！");
                return days;
            }

//            System.out.println("两个日期的差距：" + days);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "";
    }



}
