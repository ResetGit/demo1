//package com.example.demo;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//import org.apache.commons.lang3.StringUtils;
//
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * @author LiuNaiJie
// * on 2019-04-01
// */
//
//public class DemoApplicationTests {
//    /**
//     * 黑色
//     */
//
//    private static final int QRCOLOR = 0xFF000000;
//    /**
//     * 白色
//     */
//    private static final int BGWHITE = 0xFFFFFFFF;
//    /**
//     * 二维码宽
//     */
//    private static final int WIDTH = 400;
//    /**
//     * 二维码高
//     */
//    private static final int HEIGHT = 400;
//    /**
//     * 用于设置QR二维码参数
//     */
//    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
//        private static final long serialVersionUID = 1L;
//
//        {
//            // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
//            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            // 设置编码方式
//            put(EncodeHintType.CHARACTER_SET, "utf-8");
//            put(EncodeHintType.MARGIN, 0);
//        }
//    };
//
//    /**
//     * @param logoFile logo图片
//     * @param codeFile 生成的二维码
//     * @param qrUrl    二维码内容
//     * @param note     二维码下方说明文字
//     */
//    public static void drawLogoQRCode(String qrUrl, String note) {
//        try {
//            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//            // 参数顺序分别为：二维码内容，编码类型，生成图片宽度，生成图片高度，设置参数
//            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
//            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
//            for (int x = 0; x < WIDTH; x++) {
//                for (int y = 0; y < HEIGHT; y++) {
//                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
//                }
//            }
//            int width = image.getWidth();
//            int height = image.getHeight();
//
//            // 添加下方说明文字
//            if (StringUtils.isNotEmpty(note)) {
//                // 新的图片，把带logo的二维码下面加上文字
//                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
//                Graphics2D outg = outImage.createGraphics();
//                // 画二维码到新的面板
//                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
//                // 画文字到新的面板
//                outg.setColor(Color.BLACK);
//                // 字体、字型、字号
//                outg.setFont(new Font("黑体", Font.PLAIN, 30));
//                int strWidth = outg.getFontMetrics().stringWidth(note);
//                // 画文字
//                outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12);
//                outg.dispose();
//                outImage.flush();
//                image = outImage;
//            }
//            image.flush();
//            ImageIO.write(image, "png", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        File logoFile = new File("D:\\project\\demo1\\src\\main\\resources\\static\\img\\1.jpg");
//        File codeFile = new File("D:\\project\\demo1\\src\\main\\resources\\static\\img\\qrcode.jpg");
//        drawLogoQRCode(null,null,"https://www.liunaijie.top","测试");
//    }
//
//
//}