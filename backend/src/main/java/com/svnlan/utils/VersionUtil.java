package com.svnlan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:
 */
public class VersionUtil {
    public static Pattern androidVersionPattern =  Pattern.compile("android version=(\\d+\\.\\d+[.]?[\\d+]?)+", Pattern.CASE_INSENSITIVE);
    public static int[] studentAppVersion = new int[]{7, 4};
    //验证app版本, 老师不允许登录
    public static boolean checkStudentApp(String ua) {
        if (isAndroidApp(ua)) {
            //大于该版本的不可用
            return !compareVersion(androidVersionPattern, studentAppVersion, ua);
        }
        return true;
    }

    //是否安卓app
    public static boolean isAndroidApp(String ua) {
        return ua.toLowerCase().contains("android version");
    }

    //是否iosapp
    public static boolean isIOSApp(String ua) {
        return ua.toLowerCase().contains("kong zhong xue tang");
    }

    private static Boolean compareVersion(Pattern pattern, int[] availableVersion, String ua) {
        Matcher matcher = pattern.matcher(ua);
        String versionString;
        if (matcher.find()){
            versionString = matcher.group(1);
        } else {
            return false;
        }
        String[] version = versionString.split("\\.");
        for (int i = 0 ; i< availableVersion.length; i++){
            //某位大于可用版本号, 返回true
            if (Integer.parseInt(version[i]) > availableVersion[i]){
                return true;
            }
            //某位大小于可用版本号, 返回false
            if (Integer.parseInt(version[i]) < availableVersion[i]){
                return false;
            }
        }
        return true;
    }

    public static boolean isMkzs(String ua) {
        return ua.toLowerCase().contains("android mkzs version=") || ua.toLowerCase().contains("mei ke zhu shou");
    }
}
