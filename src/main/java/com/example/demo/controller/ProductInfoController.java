package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.mapper.ProductInfoMapper;
import com.example.demo.pojo.ProductCategory;
import com.example.demo.pojo.ProductInfo;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductInfoService;
import com.example.demo.util.IMoocJSONResult;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    //获取菜品列表
    @RequestMapping(value = "/productList", produces = "application/json; charset=utf-8")
    public IMoocJSONResult productList(String productName,String categoryName) {
        List<ProductInfo> productInfos = this.productInfoService.productInfoList(productName);
        List<ProductCategory> categoryList =  productCategoryService.categoryList(categoryName);
        List<Map> list = new ArrayList<>();
        for (int j=0;j<categoryList.size();j++){
            Map<String,String> map = new HashMap();
            map.put("categoryName",categoryList.get(j).getCategoryName());
            map.put("categoryType",categoryList.get(j).getCategoryType().toString());
            map.put("categoryId",categoryList.get(j).getCategoryId().toString());
            list.add(map);
        }
        for (int i=0;i<productInfos.size();i++) {
            productInfos.get(i).setData(list);
        }
        if (CollectionUtils.isEmpty(productInfos)) {
            return IMoocJSONResult.errorMsg("菜品为空");
        }
        return IMoocJSONResult.build(200,"ok",productInfos,0,productInfos.size());
    }

    //根据菜品目录查询菜品
    @PostMapping("/queryProductBycategory")
    public IMoocJSONResult queryProductByCategory(@RequestBody Map<String, String> map) {

        //接收传入参数
        String categoryType = map.get("categoryType");

        //传入参数不能为空
        if (StringUtils.isBlank(categoryType)) {

            return IMoocJSONResult.errorMsg("传入参数不能空");
        }

        int categoryNum = Integer.parseInt(categoryType);

        //校验参数是否合法
        if (categoryNum <= 0) {
            return IMoocJSONResult.errorMsg("参数不合法");
        }

        List<ProductInfo> productInfos = this.productCategoryService.queryProductInfoByCategory(categoryNum);

        return IMoocJSONResult.ok(productInfos);
    }

    //新增菜品
    @PostMapping("/insertProduct")
    public IMoocJSONResult insertProduct(@RequestBody Map<String, String> map) {
        //接受参数
        String productName = map.get("productName");
        String productPrice = map.get("productPrice");  //double 商品价格
        String productStock = map.get("productStock");  //int  商品库存
        String productDesc = map.get("productDesc");
        String productIcon = map.get("productIcon");
        String productStatus = map.get("productStatus");
        String categoryType = map.get("categoryType");  //int  菜品类
        if(productStock == "" || productStock == null){
            productStock = "0";
        }
        if(productStatus.equals("--请选择--")){
            productStatus = "1";
        }

        //校验参数是否合法
//        if (StringUtils.isBlank(productName) || StringUtils.isBlank(productDesc)
//                || StringUtils.isBlank(productPrice) || StringUtils.isBlank(productStock)
//                || StringUtils.isBlank(productIcon) || StringUtils.isBlank(productStatus) || StringUtils.isBlank(categoryType)) {
//
//            return IMoocJSONResult.errorMsg("参数不合法");
//
//        }

        //类型转换
        float price = Float.parseFloat(productPrice);
        int stock = Integer.parseInt(productStock);
        int productstatus = Integer.parseInt(productStatus);

        if (price < 0 || stock < 0 ) {

            return IMoocJSONResult.errorMsg("参数不合法");
        }
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductDescription(productDesc);
        productInfo.setProductPrice(price);
        productInfo.setCategoryType(categoryType);
        productInfo.setCreateTime(new Date());
        productInfo.setProductName(productName);
        productInfo.setProductStatus(productstatus);
//        productInfo.setProductStock(stock);
        productInfo.setProductIcon(productIcon);
        this.productInfoService.insertProduct(productInfo);

        return IMoocJSONResult.ok();
    }

    //删除菜品id
    @PostMapping("/deleteProduct")
    public IMoocJSONResult deleteProductInfo(@RequestBody JSONObject map) {
        //获取参数
        String productInfoId = (String) map.get("productId");
        if (StringUtils.isBlank(productInfoId)) {
            return IMoocJSONResult.errorMsg("参数不合法");
        }

        this.productInfoService.deleteProductInfo(productInfoId);

        return IMoocJSONResult.ok();

    }

    //修改菜品
    @PostMapping("/updateProduct")
    public IMoocJSONResult updateProduct(@RequestBody Map<String, String> map) {
        //接受参数
        String productId = map.get("productId");  //菜品id
        String productName = map.get("productName");
        String productPrice = map.get("productPrice");  //double 商品价格
        String productStock = map.get("productStock");  //int  商品库存
        String productDesc = map.get("productDesc");
        String productIcon = map.get("productIcon");
        String productStatus = map.get("productStatus");
        String categoryType = map.get("categoryType");  //int  菜品类

//        //校验参数是否合法
//        if (StringUtils.isBlank(productName) || StringUtils.isBlank(productDesc)
//                || StringUtils.isBlank(productPrice) || StringUtils.isBlank(productStock) || StringUtils.isBlank(productId)
//                || StringUtils.isBlank(productIcon) || StringUtils.isBlank(productStatus) || StringUtils.isBlank(categoryType)) {
//
//            return IMoocJSONResult.errorMsg("参数不合法");
//        }

        //类型转换
        float price = Float.parseFloat(productPrice);
//        int stock = Integer.parseInt(productStock);
        int productstatus = Integer.parseInt(productStatus);
//        if (price < 0 || stock < 0 ) {
//
//            return IMoocJSONResult.errorMsg("参数不合法");
//        }

        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId(productId);
        productInfo.setProductDescription(productDesc);
        productInfo.setProductIcon(productIcon);
        productInfo.setProductPrice(price);
        productInfo.setCategoryType(categoryType);
        productInfo.setCreateTime(null);
        productInfo.setUpdateTime(new Date());
        productInfo.setProductName(productName);
        productInfo.setProductStatus(productstatus);
//        productInfo.setProductStock(stock);
        this.productInfoService.updateProductInfo(productInfo);

        return IMoocJSONResult.ok();
    }

    @PostMapping("queryProductById")
    public IMoocJSONResult queryProductById(@RequestBody Map<String, String> map) {

        String productId = map.get("productId");

        if (StringUtils.isBlank(productId)) {

            return IMoocJSONResult.errorMsg("参数不合法");
        }

        ProductInfo productInfo = this.productInfoService.queryProductInfoById(productId);

        if (productInfo == null) {
            return IMoocJSONResult.errorMsg("不存在改商品");
        }


        return IMoocJSONResult.ok(productInfo);
    }

    //用于接收文件
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
                    System.out.println(path1);
                    outputStream = new FileOutputStream(outFile);
                    inputStream =  files[0].getInputStream();
                    //工具类进行拷贝
                    IOUtils.copy(inputStream,outputStream);
                    String str = "/img";
                    String slpath = path.replace(str,"/img/sl"+filename);
                    System.out.println("缩略图"+slpath);
                    Thumbnails.of(path1).scale(0.20f).toFile(slpath);
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



    //修改菜品状态
    @PostMapping("/updatebyzt")
    public IMoocJSONResult updatezt(@RequestBody Map<String, String> map) {
        //接受参数
        String productId = map.get("productId");  //菜品id
        String productStatus = map.get("productStatus");

        int productstatus = Integer.parseInt(productStatus);
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId(productId);
        productInfo.setUpdateTime(new Date());
        productInfo.setProductStatus(productstatus);

        this.productInfoService.updateProductInfo(productInfo);

        return IMoocJSONResult.ok();
    }

    //修改菜品状态
    @PostMapping("/updatebybh")
    public IMoocJSONResult updatebh(@RequestBody Map<String, String> map) {
        //接受参数
        System.out.println(map);
        String productId = map.get("productId");  //菜品id
        String categoryType = map.get("categoryType");
        String productStatus = map.get("productStatus");
        int productstatus = Integer.parseInt(productStatus);
        Example example = new Example(ProductInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId",productId);

        ProductInfo productInfo = new ProductInfo();
        productInfo.setUpdateTime(new Date());
        productInfo.setCategoryType(categoryType);
        productInfo.setProductStatus(productstatus);
        productInfoMapper.updateByExampleSelective(productInfo,example);
        return IMoocJSONResult.ok();
    }



}