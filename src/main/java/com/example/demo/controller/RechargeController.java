package com.example.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.example.demo.common.idworker.Sid;
import com.example.demo.config.BossConfig;
import com.example.demo.service.OrderComboService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RechargeController {

    @Autowired
    private BossConfig alipayConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderComboService orderComboService;

    @RequestMapping("testprint")
    public Object print(){

        return "测试";
    }

    @CrossOrigin
    @RequestMapping("orderh5")
    public Object doPost(HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) throws ServletException, IOException {
        String order = Sid.next();      //随机生成订单
        String money = httpRequest.getParameter("money");             //总价
        String user = httpRequest.getParameter("user");
        String[] map = money.split(":");
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid(), alipayConfig.getApp_private_key(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getAlipay_public_key(), alipayConfig.getSign_type());
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("https://www.lssell.cn/tt3/diancan/login.html");
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyurl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\""+order+"\"," +
                " \"total_amount\":\""+map[1]+"\"," +
                " \"subject\":\""+user+":"+map[0]+":"+map[2]+"\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"," +
                "      \"goods_detail\":[{" +
                "\"goods_name\":\""+user+"\"," +
                "        }]" +
                " }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayConfig.getCharset());
//        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();
        System.out.println(form);
        return form;
    }

    /**
     * 支付宝服务器异步通知url
     * @throws Exception
     */
    @RequestMapping(value="/notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取支付宝发送过来的信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        //循环获取到所有的值
        for(String str:requestParams.keySet()) {
            String name =str;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipay_public_key(), alipayConfig.getCharset(), alipayConfig.getSign_type());
        //boolean类型signVerified为true时 则验证成功
        if(signVerified) {
            //获取到支付的状态 TRADE_SUCCESS则支付成功
            String trade_status =request.getParameter("trade_status");
            if (trade_status.equals("TRADE_SUCCESS")){
                System.out.println("支付成功");
                String user = request.getParameter("subject");
                String gmtcreate = request.getParameter("gmt_create");
                String out_trade_no = request.getParameter("out_trade_no");
                String buyer_pay_amount = request.getParameter("buyer_pay_amount");
                String[] username = user.split(":");
                Map kh = new HashMap();
                kh.put("username",username[0]);
                kh.put("day",username[2]);
                userService.updateByObject("editiscombo",kh);
                Map map = new HashMap();
                map.put("comboname",username[1]);
                map.put("username",username[0]);
                map.put("gmtcreate",gmtcreate);
                map.put("outtradeno",out_trade_no);
                map.put("comboprice",buyer_pay_amount);
                orderComboService.addOrderCombo(map);

            }else {
                System.out.println("支付失败");
            }
        }
        //签名验证失败
        else {
            System.out.println(AlipaySignature.getSignCheckContentV1(params));
        }
    }
}
