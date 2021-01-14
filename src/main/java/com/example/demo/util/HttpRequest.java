package com.example.demo.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class HttpRequest {
    HttpRequest() {
    }

    public static String sendGet(String url, Map<String, String> paramMap) {
        String param = forMap(paramMap);
        String result = "";
        BufferedReader in = null;

        try {
            String urlNameString = url + "?" + param;
            URLConnection connection = getUrlConnection(urlNameString);
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            Iterator var8 = map.keySet().iterator();

            while(var8.hasNext()) {
                String key = (String)var8.next();
                System.out.println(key + "--->" + map.get(key));
            }

            String line;
            for(in = new BufferedReader(new InputStreamReader(connection.getInputStream())); (line = in.readLine()) != null; result = result + line) {
            }
        } catch (Exception var18) {
            System.out.println("发送GET请求出现异常！" + var18);
            var18.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }

        }

        return result;
    }

    public static String sendPost(String url, Map<String, String> paramMap) {
        String param = forMap(paramMap);
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URLConnection conn = getUrlConnection(url);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception var16) {
            System.out.println("发送 POST 请求出现异常！" + var16);
            var16.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return result.toString();
    }

    private static URLConnection getUrlConnection(String url) throws IOException {
        URL realUrl = new URL(url);
        URLConnection conn = realUrl.openConnection();
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        return conn;
    }

    private static String forMap(Map<String, String> paramMap) {
        String reqStr = "";
        if (null != paramMap && !paramMap.isEmpty()) {
            Entry entry;
            for(Iterator var2 = paramMap.entrySet().iterator(); var2.hasNext(); reqStr = (String)entry.getKey() + "=" + (String)entry.getValue() + "&" + reqStr) {
                entry = (Entry)var2.next();
                System.out.println("key = " + (String)entry.getKey() + ", value = " + (String)entry.getValue());
            }

            reqStr = reqStr.substring(0, reqStr.length() - 1);
        }

        return reqStr;
    }
}
