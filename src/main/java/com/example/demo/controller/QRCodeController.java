package com.example.demo.controller;

import com.example.demo.config.PrintAccountConfig;
import com.example.demo.util.HttpClientUtils;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.QRCodeUtil;
import com.example.demo.util.QRCodeUtilEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码调用前端控制器
 */
@RestController
public class QRCodeController {

    @Autowired
    private PrintAccountConfig printAccountConfig;

    /**
     * 根据 url 生成 普通二维码
     */
    @RequestMapping(value = "/createCommonQRCode")
    public void createCommonQRCode(HttpServletResponse response,
                                   String url, String text,
                                   String zh, String storeId,
                                   String storeName, String userName,
                                   String key, String sn
                                                        ) throws Exception {

        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            url="https://www.lssell.cn/tt3/h5/?zh="+zh+"&storeId="+storeId+"&storeName="+storeName+"&userName="+userName+"&key=" +key+"&sn="+sn;
//            text="桌号:"+zh;
            //使用工具类生成二维码
//            QRCodeUtil.encode(url,stream);

            QRCodeUtilEx.encode(url, zh, stream);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    /**
     * 根据 url 生成 带有logo二维码
     */
    @RequestMapping(value = "/createLogoQRCode")
    public void createLogoQRCode(HttpServletResponse response, String url,String text) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            String logoPath = Thread.currentThread().getContextClassLoader().getResource("https://cli.im/weapp").getPath()
                    + "static/img" + File.separator + "1.jpg";
            //使用工具类生成二维码
            QRCodeUtil.encode(url,logoPath, stream, true);
//            QRCodeUtilEx.encode(url,logoPath, stream, true);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    /**
     * 详情看官方文档 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/qr-code.html
     * 此处以官方接口B为例
     * 生成小程序码，可接受页面参数较短，生成个数不受限
     * param :参数 例如：123    page：需要跳转的页面地址 例如：pages/index
     */
    @RequestMapping("/getCode")
    public void smallProgramCode(String scene, String page, HttpServletResponse response) {
        OutputStream stream = null;
        try {
            //获取AccessToken
            String accessToken = getAccessToken();
            //设置响应类型
            response.setContentType("image/png");
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            //组装参数
            Map<String, Object> paraMap = new HashMap<>();
            Map<String, Object> Map = new HashMap<>();
            //二维码携带参数 不超过32位 参数类型必须是字符串
            paraMap.put("scene",scene);
//            paraMap.put("number", param);
            //二维码跳转页面
            paraMap.put("page", "pages/buy/buy");
            //二维码的宽度
            paraMap.put("width", 200);
            //自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
            paraMap.put("auto_color", false);
            //是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码
            paraMap.put("is_hyaline", false);



            //执行post 获取数据流
            byte[] result = HttpClientUtils.doImgPost(url, paraMap);

            //输出图片到页面
            response.setContentType("image/jpg");
            stream = response.getOutputStream();
            stream.write(result);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取ACCESS_TOKEN
     * 官方文档地址:https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     * <p>
     * 需要正确的 appid  和 secret  查看自己的微信开放平台
     */
    public String getAccessToken() {
        //这里需要换成你d的小程序appid
        String appid = "wx66045ae0272bfb91";
//        //这里需要换成你d的小程序secret
        String appSecret = "e1ac7aa5111e02079ade61c8bf9db9f5";

        //获取微信ACCESS_TOKEN 的Url
        String accent_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String url = accent_token_url.replace("APPID", appid).replace("APPSECRET", appSecret);
        //发送请求
        String result = HttpClientUtils.doGet(url);
        Map<String, Object> resultMap = (Map<String, Object>) JsonUtil.jsonToMap(result);
//        System.out.println("access_token------>" + resultMap.get("access_token").toString());
        return resultMap.get("access_token").toString();
    }


}
