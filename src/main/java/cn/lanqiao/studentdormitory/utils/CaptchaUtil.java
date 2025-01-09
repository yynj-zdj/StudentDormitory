package cn.lanqiao.studentdormitory.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

public class CaptchaUtil {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random random = new Random();
    
    public static String generateCaptcha(int length) {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < length; i++) {
            captcha.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return captcha.toString();
    }
    
    public static BufferedImage createCaptchaImage(String captcha) {
        int width = 100;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        // 绘制验证码
        g.setColor(Color.BLACK);
        g.drawString(captcha, 20, 22);
        
        // 添加干扰线
        for (int i = 0; i < 4; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(random.nextInt(width), random.nextInt(height),
                    random.nextInt(width), random.nextInt(height));
        }
        
        g.dispose();
        return image;
    }
} 