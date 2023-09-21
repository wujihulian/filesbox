package com.svnlan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:正则相关的工具类
 * @Modified:
 */
public final class RegUtil {

    /**
     * Alphanumeric characters
     */
    public static final String  REG_ALNUM               = "\\p{Alnum}";
    /**
     * Alphabetic characters
     */
    public static final String  REG_ALPHA               = "\\p{Alpha}";
    /**
     * ASCII characters
     */
    public static final String  REG_ASCII               = "\\p{ASCII}";
    /**
     * Space and tab
     */
    public static final String  REG_BLANK               = "\\p{Blank}";
    /**
     * Control characters
     */
    public static final String  REG_CNTRL               = "\\p{Cntrl}";
    /**
     * Digits
     */
    public static final String  REG_DIGITS              = "\\p{Digit}";
    /**
     * SVisible characters (i.e. anything except spaces, control characters, etc.)
     */
    public static final String  REG_GRAPH               = "\\p{Graph}";
    /**
     * Lowercase letters
     */
    public static final String  REG_LOWER               = "\\p{Lower}";
    /**
     * Visible characters and spaces (i.e. anything except control characters, etc.)
     */
    public static final String  REG_PRINT               = "\\p{Print}";
    /**
     * Punctuation and symbols.
     */
    public static final String  REG_PUNCT               = "\\p{Punct}";
    /**
     * All whitespace characters, including line breaks
     */
    public static final String  REG_SPACE               = "\\p{Space}";
    /**
     * Uppercase letters
     */
    public static final String  REG_UPPER               = "\\p{Upper}";
    /**
     * Hexadecimal digits
     */
    public static final String  REG_XDIGIT              = "\\p{XDigit}";
    /**
     * 空白行
     */
    public static final String  REG_SPACE_LINE          = "\\n\\s*\\r";
    /**
     * 首尾空白字符
     */
    public static final String  REG_SPACE_POINT         = "^\\s*|\\s*$";
    /**
     * HTML
     */
    public static final String  REG_HTML                = "<(\\S*?)[^>]*>.*?</\\1>|<.*? />";
    /**
     * Email
     */
    public static final String  REG_EMAIL               = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 国内固定电话
     */
    public static final String  REG_FIXED_TELEPHONE     = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
    /**
     * 邮政编码
     */
    public static final String  REG_POSTALCODE          = "[1-9]\\d{5}(?!\\d)";
    /**
     * 身份证编码
     */
    public static final String  REG_IDENTIFICATION_CARD = "\\d{15}|\\d{18}";
    /**
     * URL地址
     */
    public static final String  REG_URL                 = "^http://([w-]+.)+[w-]+(/[w-./?%&=]*)?$";
    /**
     * 移动电话
     */
    public static final String  REG_MOBILE_TELEPHONE    = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    /**
     * 合法的名字（字母开头，允许5-16字节，允许字母数字下划线）
     */
    public static final String  REG_LEGAL_ACCOUNT       = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    /**
     * i地址
     */
    public static final String  REG_IP                  = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static      Pattern numericPattern          = Pattern.compile("^[0-9\\-]+$");
    private static      Pattern numericStringPattern    = Pattern.compile("^[0-9\\-\\-]+$");
    private static      Pattern floatNumericPattern     = Pattern.compile("^[0-9\\-\\.]+$");
    private static      Pattern abcPattern              = Pattern.compile("^[a-z|A-Z]+$");

    /**
     * 判断是否数字表示
     *
     * @param src 源字符串
     * @return 是否数字的标志
     */
    public final static boolean isNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 判断是否纯字母组合
     *
     * @param src 源字符串
     * @return 是否纯字母组合的标志
     */
    public final static boolean isABC(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }


    /**
     * 判断是否浮点数字表示
     *
     * @param src 源字符串
     * @return 是否数字的标志
     */
    public final static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 判断字符串str是否符合正则表达式reg
     *
     * @param str 需要处理的字符串
     * @param reg 正则
     * @return 是否匹配
     */
    public final static boolean isMatche(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher isNum   = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 获取符合reg正则表达式的字符串在String中出现的次数
     *
     * @param str 需要处理的字符串
     * @param reg 正则
     * @return 出现的次数
     */
    public final static int countSubStrReg(String str, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int     i = 0;
        while (m.find()) {
            i++;
        }
        return i;
    }


    /**
     * 判断是否是符合邮箱
     *
     * @param email 判断的字符串
     * @return 是否是符合的邮箱
     */
    public final static boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile(REG_EMAIL);
        return pattern.matcher(email).matches();
    }

    /**
       * @Description: 是否只包含数字英文
       * @params:  []
       * @Return:  boolean
       * @Modified:
       */
    public final static boolean onlyNumAndWord(String string){
        Pattern pattern = Pattern.compile("[^0-9A-Za-z]");
        Matcher m = pattern.matcher(string);
        return !m.find();
    }

    /**
     * @description: 是否是手机号
     * @param str
     * @return java.lang.Boolean
     */
    public final static Boolean isMobile(String str) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }

    /**
     * @description: 校验是否是http或https的合法链接
     * @param str
     * @return boolean
     */
    public static boolean isURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http)?://)"  //https、http
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return  str.matches(regex);
    }

}
