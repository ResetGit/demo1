package com.example.demo.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public Utils() {
    }

    public static String getMD5Str(String str) {
        String re = null;

        try {
            byte[] tem = str.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.reset();
            md5.update(tem);
            byte[] encrypt = md5.digest();
            StringBuilder sb = new StringBuilder();
            byte[] var6 = encrypt;
            int var7 = encrypt.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                byte t = var6[var8];
                String s = Integer.toHexString(t & 255);
                if (s.length() == 1) {
                    s = "0" + s;
                }

                sb.append(s);
            }

            re = sb.toString();
        } catch (NoSuchAlgorithmException var11) {
            var11.printStackTrace();
        }

        return re.length() == 31 ? "0" + re : re;
    }

    public static boolean isNull(String content) {
        return content == null || content.equals("");
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }
}
