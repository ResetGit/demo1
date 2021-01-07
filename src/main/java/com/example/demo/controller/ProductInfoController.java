package com.example.demo.controller;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.common.idworker.Sid;
import com.example.demo.pojo.Audience;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.pojo.ProductType;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductInfoService;
import com.example.demo.service.ProductTypeService;
import com.example.demo.util.IMoocJSONResult;
import com.example.demo.util.JwtHelper;
import io.jsonwebtoken.Claims;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/productInfo")
public class ProductInfoController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    Audience audience;

    /**
     * 根据用户id获取店铺列表
     * count 长度
     * data 列表
     * @return map
     */
    @RequestMapping("/getProductInfoListByUIdASId")
    public Object getProductInfoListByUIdASId(HttpServletRequest request, String productName) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            String token = request.getParameter("tokens");
            Claims listToken = JwtHelper.parseJWT(token,audience.getBase64Secret());

            JSONArray jsonArray = null;
            jsonArray = new JSONArray(Collections.singletonList(listToken.get("data")));
            Object id=jsonArray.getJSONObject(0).get("id");//id 为user_id
            map.put("userId",id);
            map.put("productName",productName);
            List<ProductInfo> list=this.productInfoService.getListByObject("getProductInfoListByUIdASId",map);

            for (int i=0;i<list.size();i++) {
                String storeid = list.get(i).getShopid();
                List<Object> list1 = new ArrayList<>();
                List<ProductCategory> categoryList = productCategoryService.getListByObject("gettype",storeid);
                for(int j=0;j<categoryList.size();j++) {
                    Map<String, Object> map2=new HashMap<>();
                    map2.put("categoryName", categoryList.get(j).getCategory_name().toString());
                    map2.put("categoryType", String.valueOf(categoryList.get(j).getCategory_type()));
                    map2.put("categoryId", categoryList.get(j).getCategory_id().toString());
                    list1.add(map2);
                }
                list.get(i).setData(list1);
            }
            map.put("count",list.size());
            map.put("data",list);
//            map.put("data1",categoryList);
        }catch (DataAccessException dae){
            logger.error("查询小品列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询小品列表异常！", e);
        }
        return map;
    }


    /**
     * 根据店铺id获取商品属性列表，h5页面要用
     * @return list
     */
    @RequestMapping(value = "/getProductInfoListByStore", produces = "application/json; charset=utf-8")
    public IMoocJSONResult getProductInfoListByStore(@RequestBody Map<String, String> map1) throws Exception{
        Map<String, Object> map=new HashMap<>();
        List<ProductInfo> list=null;
        try {
            String storeId = map1.get("storeId");
            String categoryType = map1.get("categoryType");
            map.put("storeId",storeId);
            map.put("categoryType",categoryType);
            list= this.productInfoService.getListByObject("getProductInfoListByStore",map);
        }catch (DataAccessException dae){
            logger.error("查询商品属性列表数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("查询商品属性列表异常！", e);
        }
        return IMoocJSONResult.ok(list);
    }

    @RequestMapping("/updateByZt")
    public Object updateByZt(String productId,String productStatus) throws Exception{
        Map<String, Object> map=new HashMap<>();
        try {
            map.put("product_id",productId);
            map.put("product_status",productStatus);
            this.productInfoService.updateByObject("updateByZt",map);
            return "ok";
        }catch (DataAccessException dae){
            logger.error("修改商品状态数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改商品状态异常！", e);
            return "操作异常，请您稍后再试!";
        }

    }

    //修改菜品状态
    @PostMapping("/updatebybh")
    public IMoocJSONResult updatebh(@RequestBody Map<String, String> map) {
        //接受参数
        System.out.println(map);
        try {
            String productId = map.get("productId");  //菜品id
            String categoryType = map.get("categoryType");
            String productStatus = map.get("productStatus");
            productInfoService.updateByObject("updatebybh", map);
            return IMoocJSONResult.ok();
        }catch (DataAccessException dae){
            logger.error("修改商品状态数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改商品状态异常！", e);
            return IMoocJSONResult.errorMsg("修改商品状态异常！");
        }
    }

    @RequestMapping("/gettype")
    public Object gettype(String storeid){
        System.out.println(storeid);
        try {
            List list = productCategoryService.getListByObject("gettype",storeid);
            return list;
        }catch (Exception e){
            return "失败";
        }
    }

    @RequestMapping("/insertProduct")
    public Object insertProduct(@RequestBody Map map){
        try {
            String data = new SimpleDateFormat("yy-MM-dd HH:mm:ss ").format(new Date());
            map.put("create_time",data);
            map.put("update_time",data);
            productInfoService.addByObject("insertinfo",map,true);
            return "成功";
        }catch (DataAccessException dae){
            logger.error("修改商品状态数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("状态异常！", e);
            return "操作异常，请您稍后再试!";
        }
    }

    //用于上传图片
    @RequestMapping(value="/productinfoupload", method = RequestMethod.POST)
    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String productId, @RequestParam("file") MultipartFile[] files) throws IOException {

        File file = new File("");
        System.out.println(files.toString());

        //文件保存路径,用于存储本地路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/img";


        //文件保存路径,用于存储服务器路径，暂时不用了
//        String path= file.getCanonicalPath()+"/usr/local/tomcat/WEB-INF/classes/static/img";

        FileOutputStream outputStream = null;

        InputStream inputStream = null;
        try {
            if (files != null || files.length >0){

                //获取file的名字
                String filename = files[0].getOriginalFilename();

                if (StringUtils.isNotBlank(filename)){
                    //文件最终上传的保存路径,本地的路径
                    String path1 = path+"/"+filename;
                    File outFile = new File(path1);
                    outputStream = new FileOutputStream(outFile);
                    inputStream =  files[0].getInputStream();
                    //工具类进行拷贝
                    IOUtils.copy(inputStream,outputStream);
                    String str = "/img";
                    String slpath = path.replace(str,"/img/sl"+filename);
                    Thumbnails.of(path1).scale(0.5f).toFile(slpath);
                }

            }else {
                return IMoocJSONResult.errorMsg("上传文件不能为空");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
        }
        return IMoocJSONResult.ok(path);
    }

    @RequestMapping("/deleteProduct")
    public IMoocJSONResult deleteProduct(@RequestBody Map map){
        try {
            productInfoService.deleteByObject("deleteProduct",map);
            return IMoocJSONResult.ok();
        }catch (Exception e){
            return IMoocJSONResult.errorMsg("测试");
        }

    }

    @RequestMapping("/updateProduct")
    public Object updateProduct(@RequestBody Map map){
        try {
        String date =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        map.put("updatedate",date);
        productInfoService.updateByObject("updateProduct",map);

        return "成功";
        }catch (DataAccessException dae){
            logger.error("修改状态数据库异常！", dae.getMessage());
            throw new RuntimeException("数据库异常：" + dae.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("状态异常！", e);
            return "操作异常，请您稍后再试!";
        }
    }

    @RequestMapping("/getlistproductlist")
    public Object getlistproductlist(@RequestBody Map map){
        List list= productInfoService.getListByObject("getlistproductlist",map);
        if(list.size()==0){
            Map map1 = new HashMap();
            map1.put("product_name","暂无数据");
            map1.put("product_price","暂无价格");
            map1.put("product_description","暂无描述");
            list.add(map1);
           return list;
        }
        return list;
    }

    @RequestMapping("/listfindbyid")
    public Object listfindbyid(String productId){
        List list= productInfoService.getListByObject("listfindbyid",productId);
        if(list.size()==0){
            Map map1 = new HashMap();
            map1.put("product_name","暂无数据");
            map1.put("product_price","暂无价格");
            map1.put("product_description","暂无描述");
            list.add(map1);
            return list;
        }
        return list;
    }
}