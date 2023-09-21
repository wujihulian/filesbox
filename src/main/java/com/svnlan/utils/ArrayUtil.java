package com.svnlan.utils;

/**
 * @Author:
 * @Description:数组相关的工具类
 * @Modified:
 */
public class ArrayUtil {

    /**
     * 传入一个double类型的数字，返回他的小数点后的位数
     * @param dd
     * @return
     */
    public static int doubleBitCount(double dd){
        String temp = String.valueOf(dd);
        int i = temp.indexOf(".");
        if(i > -1){
            return temp.length()-i -1;
        }
        return 0;

    }
    /**
     * 传入一个double类型的数组，返回这个数组内每个元素小数点后的位数
     * @param arr
     * @return
     */
    public static Integer[] doubleBitCount(double[] arr){
        Integer[] len = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            len[i] = doubleBitCount(arr[i]);
        }
        return len;
    }
}
