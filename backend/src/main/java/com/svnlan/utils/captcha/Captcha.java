package com.svnlan.utils.captcha;

import java.awt.*;
import java.io.OutputStream;

/**
 * @Author: huanghao
 * @Description:
 * @Date: 2019/6/20 10:43 AM
 */
public abstract class Captcha extends CaptchaRandom {
    protected Font font = new Font("Arial", Font.BOLD, 32); // 字体Verdana
    protected int len = 4; // 验证码随机字符长度
    // TODO: 2019/6/20 宽高由前端传入参数
    protected int width = 130; // 验证码显示宽度
    protected int height = 48; // 验证码显示高度
    protected String chars = null; // 当前验证码
    protected int charType = TYPE_DEFAULT;  // 验证码类型，1字母数字混合，2纯数字，3纯字母
    public static final int TYPE_DEFAULT = 1;  // 字母数字混合
    public static final int TYPE_ONLY_NUMBER = 2;  // 纯数字
    public static final int TYPE_ONLY_CHAR = 3;  // 纯字母
    public static final int TYPE_ONLY_UPPER = 4;  // 纯大写字母
    public static final int TYPE_ONLY_LOWER = 5;  // 纯小写字母
    public static final int TYPE_NUM_AND_UPPER = 6;  // 数字大写字母
    // 常用颜色
    public static final int[][] COLOR = {{0, 135, 255}, {51, 153, 51}, {255, 102, 102}, {255, 153, 0}, {153, 102, 0}, {153, 102, 153}, {51, 153, 153}, {102, 102, 255}, {0, 102, 204}, {204, 51, 51}, {0, 153, 204}, {0, 51, 102}};


    /**
     * @Description:生成验证码的随机字符
     * @Return:  char[]
     * @Author:  huanghao
     * @Date:  2019/6/20 10:59 AM
     * @Modified:
     */
    protected char[] genAlphabets(){
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            switch (charType){
                case 2:  // 纯数字
                    cs[i] = alpha(numMaxIndex);
                    break;
                case 3: // 纯字母,大小写
                    cs[i] = alpha(charMinIndex, charMaxIndex);
                    break;
                case 4: // 大写字母
                    cs[i] = alpha(upperMinIndex, upperMaxIndex);
                    break;
                case 5: // 小写字母
                    cs[i] = alpha(lowerMinIndex, lowerMaxIndex);
                    break;
                case 6: // 大写字母和数字
                    cs[i] = alpha(upperMaxIndex);
                    break;
                default: // 大小写字母和数字
                    cs[i] = alpha();
                    break;
            }
        }
        chars = new String(cs);
        return cs;
    }

    /**
     * @Description:从常用的颜色中选取一种
     * @Return:  java.awt.Color
     * @Author:  huanghao
     * @Date:  2019/6/20 11:06 AM
     * @Modified:
     */
    protected Color getColor(){
        int[] common = COLOR[num(COLOR.length)];
        return new Color(common[0], common[1], common[2]);
    }

    /**
     * @Description:给定范围内获取随机颜色
     * @param fc : 颜色范围值
     * @param bc : 颜色范围值
     * @Return:  java.awt.Color
     * @Author:  huanghao
     * @Date:  2019/6/20 11:07 AM
     * @Modified:
     */
    protected Color getColor(int fc, int bc){
        if (fc > 255) fc = 255;
        if (fc < 0) fc = 1;
        if (bc > 255) bc = 255;
        if (bc < 0) bc = 0;

        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * @Description:抽象方法,用于子类实现
     * @param os : 输出流
     * @Return:  boolean
     * @Author:  huanghao
     * @Date:  2019/6/20 11:10 AM
     * @Modified:
     */
    public abstract boolean out(OutputStream os);

    /**
     * @Description:检查验证码,没有则生成.
     * @Return:  void
     * @Author:  huanghao
     * @Date:  2019/6/20 11:11 AM
     * @Modified:
     */
    public void checkAlphabets(){
        if (null == chars){
            this.genAlphabets();
        }
    }


    /**
     * @Description:获取生成的验证码
     * @Return:  java.lang.String
     * @Author:  huanghao
     * @Date:  2019/6/20 11:13 AM
     * @Modified:
     */
    public String getAlphabets(){
        this.checkAlphabets();
        return chars;
    }


    /**
     * @Description:生成干扰线
     * @param num : 干扰线的数量
     * @param g2 :
     * @Return:  void
     * @Author:  huanghao
     * @Date:  2019/6/20 11:23 AM
     * @Modified:
     */
    public void drawLine(int num, Graphics2D g2){
        this.drawLine(num, null, g2);
    }

    public void drawLine(int num, Color color, Graphics2D g2) {
        for (int i = 0; i < num; i++) {
            g2.setColor(color == null ? this.getColor(150, 250) : color);
            int x1 = num(-10, width - 10);
            int y1 = num(5, height - 5);
            int x2 = num(10, width + 10);
            int y2 = num(2, height - 2);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * @Description:画干扰图
     * @param number : 干扰图的个数
     * @param color : 颜色
     * @param g : Graphics2D对象
     * @Return:  void 
     * @Author:  huanghao
     * @Date:  2019/6/20 11:32 AM 
     * @Modified:
     */
    public void drawOval(int number, Color color, Graphics2D g) {
        for (int i = 0; i < number; i++) {
            g.setColor(color == null ? this.getColor(100, 250) : color);
            g.drawOval(num(width), num(height), 10 + num(20), 10 + num(20));
        }
    }

    public void drawOval(int num, Graphics2D g) {
        for (int i = 0; i < num; i++) {
            g.setColor(this.getColor(100, 250));
            g.drawOval(num(width), num(height), 10 + num(20), 10 + num(20));
        }
    }


    /** getter & setter **/
    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }
}
