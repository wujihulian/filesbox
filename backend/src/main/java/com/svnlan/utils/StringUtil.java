package com.svnlan.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:常用的字符串相关的工具类
 * @Modified:
 */
public final class StringUtil {
    /**
     * 将指定位数的数字不足的位数用0补充
     *
     * @param Number 传入的数字
     * @return 返回一个字符串, 如输入1 返回00001,输入11 返回 00011,以此类推,如果超出长度,则显示真正的整数值
     */
    public static String stringTo0(int Number) {
        String str = String.format("%05d", Number);//字符串格式化.
        //格式化的规则是 0-->不足时,前面补充0  4-->长度为4  d-->正整数
        return str;
    }

    public static String stringTo4(int Number) {
        String str = String.format("%04d", Number);//字符串格式化.
        //格式化的规则是 0-->不足时,前面补充0  4-->长度为4  d-->正整数
        return str;
    }

    /**
     * 判断是否是空字符串 null和"" 都返回 true
     *
     * @param str 判断的字符串
     * @return 是否有效
     */
    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param list   需要处理的列表
     * @param symbol 链接的符号
     * @return 处理后的字符串
     */
    public static String joinString(List list, String symbol) {
        String result = "";
        if (list != null) {
            for (Object o : list) {
                String temp = o.toString();
                if (temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 判定第一个字符串是否等于的第二个字符串中的某一个值
     *
     * @param str1 测试的字符串
     * @param str2 字符串数组(用,分割)
     * @return 是否包含
     */
    public static boolean requals(String str1, String str2) {
        if (str1 != null && str2 != null) {
            str2 = str2.replaceAll("\\s*", "");
            String[] arr = str2.split(",");
            for (String t : arr) {
                if (t.equals(str1.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判定第一个字符串是否等于的第二个字符串中的某一个值
     *
     * @param str1  测试的字符串
     * @param str2  字符串数组
     * @param split str2字符串的分隔符
     * @return 是否包含
     */
    public static boolean requals(String str1, String str2, String split) {
        if (str1 != null && str2 != null) {
            str2 = str2.replaceAll("\\s*", "");
            String[] arr = str2.split(split);
            for (String t : arr) {
                if (t.equals(str1.trim())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 字符串省略截取
     * 字符串截取到指定长度size+...的形式
     *
     * @param subject 需要处理的字符串
     * @param size    截取的长度
     * @return 处理后的字符串
     */
    public static String subStringOmit(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }


    /**
     * 截取字符串　超出的字符用symbol代替
     *
     * @param str    需要处理的字符串
     * @param len    字符串长度
     * @param symbol 最后拼接的字符串
     * @return 测试后的字符串
     */
    public static String subStringSymbol(String str, int len, String symbol) {
        String temp = "";
        if (str != null && str.length() > len) {
            temp = str.substring(0, len) + symbol;
        }
        return temp;
    }


    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array  需要处理的字符串数组
     * @param symbol 链接的符号
     * @return 处理后的字符串
     */
    public static String joinString(String[] array, String symbol) {
        String result = "";
        if (array != null) {
            for (String temp : array) {
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1 && CheckUtil.valid(symbol)) {
                result = result.substring(0, result.length() - symbol.length());
            }
        }
        return result;
    }

    /**
     * 将一组字符才以指定的字符链接起来
     *
     * @param linkStr 链接字符
     * @param strs    需要连接的动态参数
     * @return
     */
    public static String join(String linkStr, String... strs) {
        StringBuffer result = new StringBuffer();
        for (String temp : strs) {
            if (temp != null && temp.trim().length() > 0)
                result.append(temp + linkStr);
        }
        if (result.length() > 1 && CheckUtil.valid(linkStr)) {
            return result.substring(0, result.length() - linkStr.length());
        }
        return result.toString();
    }


    /**
     * 隐藏邮件地址前缀。
     *
     * @param email - EMail邮箱地址 例如: ssss@koubei.com 等等...
     * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
     */
    public static String getHideEmailPrefix(String email) {
        if (null != email) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }

    /**
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     *
     * @param src - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num - 重复生成次数
     * @return 返回已生成的重复字符串
     */
    public static String repeat(String src, int num) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++)
            s.append(src);
        return s.toString();
    }

    /**
     * 截取字符串左侧的Num位截取到末尾
     *
     * @param str1 处理的字符串
     * @param num  开始位置
     * @return 截取后的字符串
     */
    public static String ltrim(String str1, int num) {
        String tt = "";
        if (!isEmpty(str1) && str1.length() >= num) {
            tt = str1.substring(num, str1.length());
        }
        return tt;

    }

    /**
     * 截取字符串右侧第0位到第Num位
     *
     * @param str 处理的字符串
     * @param num 截取的位置
     * @return 截取后的字符串
     */
    public static String rtrim(String str, int num) {
        if (!isEmpty(str) && str.length() > num) {
            str = str.substring(0, str.length() - num);
        }
        return str;
    }

    /**
     * 根据指定的字符把源字符串分割成一个list
     *
     * @param src     处理的字符串
     * @param pattern 分割字符串
     * @return 处理后的list
     */
    public static List<String> parseString2List(String src, String pattern) {
        List<String> list = new ArrayList<>();
        if (src != null) {
            String[] tt = src.split(pattern);
            list.addAll(Arrays.asList(tt));
        }
        return list;
    }

    /**
     * 格式化一个float
     *
     * @param format 要格式化成的格式 such as #.00, #.#
     * @return 格式化后的字符串
     */
    public static String formatDouble(double f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }


    /**
     * 截取字符串左侧指定长度的字符串
     *
     * @param input 输入字符串
     * @param count 截取长度
     * @return 截取字符串
     */
    public static String left(String input, int count) {
        if (isEmpty(input)) {
            return "";
        }
        count = (count > input.length()) ? input.length() : count;
        return input.substring(0, count);
    }

    /**
     * 截取字符串右侧指定长度的字符串
     *
     * @param input 输入字符串
     * @param count 截取长度
     * @return 截取字符串
     * Summary 其他编码的有待测试
     */
    public static String right(String input, int count) {
        if (isEmpty(input)) {
            return "";
        }
        count = (count > input.length()) ? input.length() : count;
        return input.substring(input.length() - count, input.length());
    }


    /**
     * 全角字符变半角字符
     *
     * @param str 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String full2Half(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return BCConvert.qj2bj(str);
    }

    /**
     * 半角字符变全角字符
     *
     * @param str 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String getHalf2Full(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return BCConvert.bj2qj(str);
    }


    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     *
     * @param str 需要处理的字符串
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }


    /**
     * 判断字符串数组中是否包含某字符串元素
     *
     * @param substring 某字符串
     * @param source    源字符串数组
     * @return 包含则返回true，否则返回false
     */
    public static boolean isIn(String substring, String[] source) {
        if (isEmpty(substring) || !CheckUtil.valid(source)) {
            return false;
        }
        for (String t : source) {
            if (substring.equals(t)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 字符串转换unicode.实现native2ascii.exe类似的功能
     *
     * @param string 需要处理的字符串
     */
    public static String string2Unicode(String string) {
        StringBuilder uni = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            String temp = "\\u" + String.valueOf(Integer.toHexString(string.charAt(i)));
            uni.append(temp);
        }
        return uni.toString();
    }

    /**
     * 转字符串 实现native2ascii.exe类似的功能
     *
     * @param unicode 需要处理的字符串
     */
    public static String unicode2String(String unicode) {
        StringBuilder str = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            str.append((char) data);
        }
        return str.toString();
    }


    /**
     * 删除所有的标点符号
     *
     * @param str 处理的字符串
     */
    public static String trimPunct(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.replaceAll("[\\pP\\p{Punct}]", "");
    }

    /**
     * 字符串相似度比较(速度较快)
     */
    public static double SimilarityRatio(String str1, String str2) {
        str1 = StringUtil.trimPunct(str1);
        str2 = StringUtil.trimPunct(str2);
        if (str1.length() > str2.length()) {
            return StringImpl.getSimilarityRatio(str1, str2);
        } else {
            return StringImpl.getSimilarityRatio(str2, str1);
        }

    }

    /**
     * 字符串相似度比较(速度较快)
     */
    public static double SimilarDegree(String str1, String str2) {
        str1 = StringUtil.trimPunct(str1);
        str2 = StringUtil.trimPunct(str2);
        if (str1.length() > str2.length()) {
            return StringImpl.getSimilarDegree(str1, str2);
        } else {
            return StringImpl.getSimilarDegree(str2, str1);
        }
    }


    /**
     * 获取字符串str在String中出现的次数
     *
     * @param string 处理的字符串
     * @param str    子字符串
     */
    public static int countSubStr(String string, String str) {
        if ((str == null) || (str.length() == 0) || (string == null) || (string.length() == 0)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = string.indexOf(str, index)) != -1) {
            count++;
            index += str.length();
        }
        return count;
    }


    /**
     * 替换一个出现的子串
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceFirst(String s, String sub, String with) {
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }


    /**
     * 替换最后一次出现的字串
     * Replaces the very last occurrence of a substring with supplied string.
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceLast(String s, String sub, String with) {
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }


    /**
     * 删除所有的子串
     * Removes all substring occurrences from the string.
     *
     * @param s   source string
     * @param sub substring to remove
     */
    public static String remove(String s, String sub) {
        int c = 0;
        int sublen = sub.length();
        if (sublen == 0) {
            return s;
        }
        int i = s.indexOf(sub, c);
        if (i == -1) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        do {
            sb.append(s.substring(c, i));
            c = i + sublen;
        } while ((i = s.indexOf(sub, c)) != -1);
        if (c < s.length()) {
            sb.append(s.substring(c, s.length()));
        }
        return sb.toString();
    }

    /**
     * 将字符串首字母转大写
     *
     * @param str 需要处理的字符串
     */
    public static String upperFirstChar(String str) {
        if (isEmpty(str)) {
            return "";
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'a') && (cs[0] <= 'z')) {
            cs[0] -= (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 将字符串首字母转小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstChar(String str) {
        if (isEmpty(str)) {
            return "";
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'A') && (cs[0] <= 'Z')) {
            cs[0] += (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 判读俩个字符串右侧的length个字符串是否一样
     *
     * @param str1
     * @param str2
     * @param length
     * @return
     */
    public static boolean rigthEquals(String str1, String str2, int length) {
        return right(str1, length).equals(right(str2, length));
    }

    /**
     * 判读俩个字符串左侧的length个字符串是否一样
     *
     * @param str1
     * @param str2
     * @param length
     * @return
     */
    public static boolean leftEquals(String str1, String str2, int length) {
        return left(str1, length).equals(left(str2, length));
    }

    /**
       * @Description: 判读两个字符串是否一样
       * @params:  [str1, str2]
       * @Return:  boolean
       * @Modified:
       */
    public static boolean equals (String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        } else {
            return str1.equals(str2);
        }
    }

    /**
       * @Description: 判断两个整数是否一样
       * @params:  [int1, int2]
       * @Return:  boolean
       * @Modified:
       */
    public static boolean equals (Integer int1, Integer int2) {
        if (int1 == null) {
            return int2 == null;
        } else {
            return int1.equals(int2);
        }
    }

    /**
     * @Description: 判断两个整数是否一样
     * @params:  [int1, int2]
     * @Return:  boolean
     * @Modified:
     */
    public static boolean equals (Long long1, Long long2) {
        if (long1 == null) {
            return long2 == null;
        } else {
            return long1.equals(long2);
        }
    }

    /**
     * @description: null转空处理
     * @param obj
     * @return java.lang.String
     */
    public static String changeNullToEmpty(Object obj) {
        if(null == obj) {
            return "";
        }
        return obj.toString();
    }

    /**
     * @description: 字符串去左右空格
     * @param obj
     * @return java.lang.String
     */
    public static String trim(Object obj) {
        String str = changeNullToEmpty(obj);
        str = str.trim();
        return str;
    }
    /*
     * 中文转unicode编码英文不转
     */
    public static String encode(String str) {
        char[] chars = str.toCharArray();
        StringBuilder unicodeStr = new StringBuilder("");
        for (int i = 0; i < chars.length; i++) {
            if (String.valueOf(chars[i]).matches("[\u4e00-\u9fa5]")) {
                String hexB = Integer.toHexString(chars[i]);
                if (hexB.length() <= 2) {
                    hexB = "00" + hexB;
                }
                unicodeStr.append("\\u");
                unicodeStr.append(hexB);
            } else {
                unicodeStr.append(chars[i]);
            }
        }
        return unicodeStr.toString();
    }
    /**
     * @Description: 获取随机 6 位验证码
     * @params:  []
     * @Return:  java.lang.String
     * @Author:  sulijuan
     * @Modified:
     */
    public static String verifyCode(){
        //生成短信验证码
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
    /**
     * @Description: 验证是否是数字
     * @params:  [str]
     * @Return:  boolean
     * @Modified:
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumericDecimal(String str){
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "^[^*&%\\s]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String unicodetoString(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        return sb.toString();
    }

    public static String subStringLimit(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size);
        }
        return subject;
    }
    public static String getMaxString(List<String> list) {
        String pLevel = null;
        int num = 0;
        for (String p : list){
            if (p.length() > num){
                pLevel = p;
                num = pLevel.length();
            }
        }
        return pLevel;
    }
}
