package com.example.demo.config;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 易联云接口工具类
 */
public class Methods {
    /**
     * 易联云颁发给开发者的应用ID 非空值
     */
    public static String CLIENT_ID;

    /**
     * 易联云颁发给开发者的应用secret 非空值
     */
    public static String CLIENT_SECRET;

    /**
     * token
     */
    public static String token;

    /**
     * 刷新token需要的 refreshtoken
     */
    public static String refresh_token;

    /**
     * code
     */
    public static String CODE;

    private Methods() {
    }

    private static class SingleMethods {
        private static final Methods COCOS_MANGER = new Methods();
    }

    public static final Methods getInstance() {
        return SingleMethods.COCOS_MANGER;
    }

    /**
     * 开放式初始化
     *
     * @param client_id
     * @param client_secret
     * @param code
     */
    public void init(String client_id, String client_secret, String code) {
        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;
        CODE = code;
    }

    /**
     * 自有初始化
     *
     * @param client_id
     * @param client_secret
     */
    public void init(String client_id, String client_secret) {
        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;
    }

    /**
     * 开放应用
     */
    public String getToken() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String result = printjk.getToken(CLIENT_ID,
                "authorization_code",
                printjk.getSin(timestamp),
                CODE,
                "all",
                timestamp,
                printjk.getuuid());
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("getToken出现Json异常！" + e);
        }
        return result;
    }

    /**
     * 自有应用服务
     */
    public String getFreedomToken(String clientid) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String result = printjk.getToken(clientid,
                "client_credentials",
                printjk.getSin(timestamp),
                "all",
                timestamp,
                printjk.getuuid());
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            System.out.println(json);
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("getFreedomToken出现Json异常！" + e);
        }
        return refresh_token;
    }

    /**
     * 刷新token
     */
    public String refreshToken(String client_id,String token) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String result = printjk.refreshToken(client_id,
                "refresh_token",
                "all",
                printjk.getSin(timestamp),
                token,
                printjk.getuuid(),
                timestamp);
        try {
            JSONObject json = new JSONObject(result);
            JSONObject body = json.getJSONObject("body");
            token = body.getString("access_token");
            refresh_token = body.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("refreshToken出现Json异常！" + e);
        }
        return result;
    }

    /**
     * 添加终端授权 开放应用服务模式不需要此接口 ,自有应用服务模式所需参数
     */
    public String addPrinter(String machine_code, String msign) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.addPrinter(CLIENT_ID,
                machine_code,
                msign,
                token,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 极速授权
     */
    public String speedAu(String machine_code, String qr_key) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.speedAu(CLIENT_ID,
                machine_code,
                qr_key,
                "all",
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 打印
     */
    public String print(String machine_code, String content, String origin_id) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.print(CLIENT_ID,
                token,
                machine_code,
                content,
                origin_id,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 删除终端授权
     */
    public String deletePrinter(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.deletePrinter(CLIENT_ID,
                token,
                machine_code,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 添加应用菜单
     */
    public String addPrintMenu(String machine_code, String content) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.addPrintMenu(CLIENT_ID,
                token,
                machine_code,
                content,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 关机重启接口
     */
    public String shutDownRestart(String machine_code, String response_type) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.shutDownRestart(CLIENT_ID,
                token,
                machine_code,
                response_type,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 声音调节
     */
    public String setSound(String machine_code, String response_type, String voice) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.setSound(CLIENT_ID,
                token,
                machine_code,
                response_type,
                voice,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 获取机型打印宽度接口
     */
    public String getPrintInfo(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.getPrintInfo(CLIENT_ID,
                token,
                machine_code,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 获取机型软硬件版本接口
     */
    public String getVersion(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.getVersion(CLIENT_ID,
                token,
                machine_code,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 取消所有未打印订单
     */
    public String cancelAll(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.cancelAll(CLIENT_ID,
                token,
                machine_code,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 取消单条未打印订单
     */
    public String cancelOne(String machine_code, String order_id) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.cancelOne(CLIENT_ID,
                token,
                machine_code,
                order_id,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 设置logo
     */
    public String setIcon(String machine_code, String img_url) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.setIcon(CLIENT_ID,
                token,
                machine_code,
                img_url,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 删除logo
     */
    public String deleteIcon(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.deleteIcon(CLIENT_ID,
                token,
                machine_code,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 打印方式
     */
    public String btnPrint(String machine_code, String response_type) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.btnPrint(CLIENT_ID,
                token,
                machine_code,
                response_type,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

    /**
     * 接单拒单设置接口
     */
    public String getOrder(String machine_code, String response_type) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return printjk.getOrder(CLIENT_ID,
                token,
                machine_code,
                response_type,
                printjk.getSin(timestamp),
                printjk.getuuid(),
                timestamp);
    }

}