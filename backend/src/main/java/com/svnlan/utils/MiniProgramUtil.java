package com.svnlan.utils;

import javax.servlet.http.HttpServletRequest;

public class MiniProgramUtil {
    //是否小程序
    public static boolean isMiniProgram(){
        HttpServletRequest request = HttpUtil.getRequest();
        String referer = request.getHeader("referer");
        referer = referer == null ? "" : referer;
        String ua = request.getHeader("user-agent").toLowerCase();
        //是否小程序
        boolean isMiniProgram = false;
        if (ua.contains("miniprogram") || referer.contains("servicewechat.com")){
            isMiniProgram = true;
        }
        return isMiniProgram;
    }
}

