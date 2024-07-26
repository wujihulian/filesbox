package com.svnlan.utils;

import java.util.*;

/**
 * @Author:
 * @Description:随机数相关的工具类
 * @Modified:
 */
public final class RandomUtil {
    public static final String ALLCHAR
            = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERCHAR
            = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERCHAR
            = "0123456789";
    public static final String NUMBERANDCHAR
            = "023456789abcdefghijklmnpqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";


    /**
     * 生成制定范围内的随机数
     *
     * @param scopeMin
     * @param scoeMax
     * @return
     */
    public static int integer(int scopeMin, int scoeMax) {
        Random random = new Random();
        return (random.nextInt(scoeMax) % (scoeMax - scopeMin + 1) + scopeMin);
    }

    /**
     * 返回固定长度的数字
     *
     * @param length
     * @return
     */
    public static String number(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getLowerString(int length) {
        return getMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getUpperString(int length) {
        return getMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    public static String getZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        return getString(num, fixdlenth, String.valueOf(num));
    }

    private static String getString(long num, int fixdlenth, String s) {
        StringBuffer sb = new StringBuffer();
        String strNum = s;
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(getZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" +
                    num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        return getString(num, fixdlenth, String.valueOf(num));
    }

    /**
     * 每次生成的len位数都不相同
     *
     * @param param
     * @return 定长的数字
     */
    public static int getNotSimple(int[] param, int len) {
        Random rand = new Random();
        for (int i = param.length; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = param[index];
            param[index] = param[i - 1];
            param[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = result * 10 + param[i];
        }
        return result;
    }

    /**
     * 从指定的数组中随机数组中的某个元素
     */
    public static <T> T randomItem(T[] param) {
        int index = integer(0, param.length);
        return param[index];
    }

    /**
     * 实现一个简单的字符串乘法
     * @param str
     * @param multiplication
     * @return
     */
    private static String strMultiplication(String str,int multiplication){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < multiplication; i++) {
            buffer.append(str);
        }
        return buffer.toString();
    }
    /**
     * 从指定的数组中按照指定比例返回指定的随机元素
     * @param param
     * @param percentum
     * @param <T>
     * @return
     */
    public static <T> T randomItem(T[] param,double[] percentum){
        int length = percentum.length;
        Integer[] ints = ArrayUtil.doubleBitCount(percentum);
        int max = Collections.max(Arrays.asList(ints));
        int[] arr = new int[length];
        int sum = 0;
        Map map = new HashMap(length);
        int multiple = Integer.parseInt("1"+strMultiplication("0",max));
        for (int i = 0; i < length; i++) {
            int temp = (int)(percentum[i] * multiple);
            arr[i] = temp;
            if(i == 0){
                map.put(i,new int[]{1,temp});
            }else{
                map.put(i,new int[]{sum,sum+temp});
            }
            sum += temp;
        }
        int indexSum = integer(1,sum);
        int index =-1;
        for (int i = 0; i < length; i++) {
            int[]  scope = (int[]) map.get(i);
            if(indexSum ==1 ){
                index = 0;
                break;
            }
            if(indexSum > scope[0] && indexSum <= scope[1]){
                index =i;
                break;
            }
        }
        if(index == -1){
            throw new RuntimeException("随机失败");
        }else{
            return param[index];
        }
    }
    /**
     * 返回一个UUID
     *
     * @return 小写的UUID
     */
    public static String getuuid() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        return s.substring(0, 8) + s.substring(9, 13) +
                s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 返回一个UUID
     *
     * @return 大写的UUID
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        String temp = s.substring(0, 8) + s.substring(9, 13) +
                s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
        return temp.toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(number(4));
        System.out.println(getStringAndNum(10));
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getStringAndNum(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERANDCHAR.charAt(random.nextInt(NUMBERANDCHAR.length())));
        }
        return sb.toString();
    }
}
