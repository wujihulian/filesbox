package com.svnlan.jwt.tool;

import com.svnlan.utils.RandomUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 业务工具组件
 */
@Component
public class JWTTool {
    /**
     * @description: 得API前缀
     * @param request
     * @return java.lang.String
     */
    public String findApiPrefix(HttpServletRequest request) {
        String prefix = "";
        String requestUri = request.getRequestURI();
        prefix = String.format("@日志@%s(%s) >>> ", requestUri , RandomUtil.getuuid());
        return prefix;
    }


}
