package com.svnlan.utils.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: huanghao
 * @Description:
 * @Date: 2019/6/20 11:46 AM
 */
public class CaptchaPng extends Captcha {

    public CaptchaPng(){}

    public CaptchaPng(int witdth, int height){
        this.setWidth(witdth);
        this.setHeight(height);
    }

    public CaptchaPng(int width, int height, int len){
        this.setWidth(width);
        this.setHeight(height);
        this.setLen(len);
    }

    public CaptchaPng(int width, int height, int len, Font font){
        this.setWidth(width);
        this.setHeight(height);
        this.setLen(len);
        this.setFont(font);
    }


    // 生成验证码,重写父类抽象方法
    @Override
    public boolean out(OutputStream os){
        checkAlphabets();
        // 生成png图片
        return this.graphicsImage(super.getAlphabets().toCharArray(), os);
    }


    /**
     * @Description:生成验证码图片
     * @param strs : 验证码字符
     * @param out : 输出流
     * @Return:  boolean
     * @Author:  huanghao
     * @Date:  2019/6/20 11:49 AM
     * @Modified:
     */
    private boolean graphicsImage(char[] strs, OutputStream out) {
        boolean ok;
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            // 随机画干扰线
            super.drawLine(3, g);
            // 随机画干扰圆
            super.drawOval(8, g);
            // 画字符串
            AlphaComposite ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);// 指定透明度
            g.setComposite(ac3);
            int hp = (height - font.getSize()) >> 1;
            int h = height - hp;
            int w = width / strs.length;
            int sp = (w - font.getSize()) / 2;
            for (int i = 0; i < strs.length; i++) {
                g.setColor(new Color(20 + num(110), 20 + num(110), 20 + num(110)));
                // 计算坐标
                int x = i * w + sp + num(3);
                int y = h - num(3, 6);
                if (x < 8) {
                    x = 8;
                }
                if (x + font.getSize() > width) {
                    x = width - font.getSize();
                }
                if (y > height) {
                    y = height;
                }
                if (y - font.getSize() < 0) {
                    y = font.getSize();
                }
                g.setFont(font.deriveFont(num(2) == 0 ? Font.PLAIN : Font.ITALIC));
                g.drawString(String.valueOf(strs[i]), x, y);
            }
            ImageIO.write(bi, "png", out);
            out.flush();
            ok = true;
        } catch (IOException e) {
            ok = false;
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }
}
