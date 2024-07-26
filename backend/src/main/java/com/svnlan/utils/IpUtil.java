package com.svnlan.utils;


import com.ithit.webdav.server.DavRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;

/**
 * @Description:获取客户端IP地址
 * @Param:
 * @return:
 */
public class IpUtil {
    /**
     * @Description:获取ip
     * @Param: [request]
     * @return: java.lang.String
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static String getIp(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (request == null) {
            return null;
        }
        String ip = request.headers().get("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.headers().get("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            return ip;
        }
        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().remoteAddress();
        ip = insocket.getAddress().getHostAddress();
        return ip;
    }

    public static String getDavIp(DavRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip.toLowerCase())) {
            return ip;
        }
        HttpServletRequest request1 = (HttpServletRequest)request.getOriginalRequest();
        return request1.getRemoteAddr();
    }
}
