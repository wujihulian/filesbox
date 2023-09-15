package com.svnlan.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class CookieUtil {
    public static String getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies =  request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
