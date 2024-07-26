package com.svnlan.interceptor;

import com.svnlan.webdav.FileProperties;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求过滤 获取到用户Id
 *
 * @author lingxu 2023/04/15 10:27
 */
@Slf4j
//@WebFilter
public class RequestWebFilter implements Filter {

    @Resource
    private FileProperties fileProperties;

    public static final Pattern compile = Pattern.compile("^/webdav/(?<userId>\\d+)/.*");

    public static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = slash(req.getRequestURI(), SlashEnum.ADD);

        log.info("uri =>{}", uri);
        if (uri.startsWith(fileProperties.webdavPrefixPath())) {
            // 表示是 webdav 请求
            Matcher matcher = compile.matcher(uri);
            if (matcher.find()) {
                try {
                    String userId = matcher.group("userId");
//                    log.info("userId => {}", userId);
                    userThreadLocal.set(Long.parseLong(userId));
                    uri = slash(uri.replaceFirst("/" + userId, ""), SlashEnum.REMOVE);
                    log.info("replace uri => {}", uri);
                    req.getRequestDispatcher(uri).forward(request, response);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("用户id必须为数字类型");
                } finally {
                    userThreadLocal.remove();
                }
            } else {
                throw new IllegalArgumentException("必须要有userId");
            }
        } else
            chain.doFilter(request, response);
    }

    enum SlashEnum {
        ADD, REMOVE
    }

    public String slash(String uri, SlashEnum type) {
        if (type == SlashEnum.ADD && !uri.endsWith("/")) {
            return uri + "/";
        } else if (type == SlashEnum.REMOVE && uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        }
        return uri;
    }
}
