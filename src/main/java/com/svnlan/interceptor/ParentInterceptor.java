package com.svnlan.interceptor;

import com.svnlan.common.CheckResult;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.JwtUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description:
 */
@Configuration
public class ParentInterceptor implements HandlerInterceptor {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    UserAuthTool userAuthTool;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(SystemConstant.JWT_TOKEN);
        LogUtil.info("ParentInterceptor getRequestURI=" + request.getRequestURI() + "  token=" + token);
        if (StringUtils.isEmpty(token)) {
            print(response, CodeMessageEnum.loginFirst.getCode());
            return false;
        } else {
            // 验证JWT的签名，返回CheckResult对象
            CheckResult checkResult = JwtUtils.validateJWT(token, false);
            if (checkResult.isSuccess()) {
                return true;
            }
            switch (checkResult.getErrCode()) {
                // 签名验证不通过
                case SystemConstant.JWT_ERRCODE_FAIL:
                    LogUtil.info("ParentInterceptor 签名验证不通过 token=" + token);
                    print(response, CodeMessageEnum.bindSignError.getCode());
                    break;
                // 签名过期，返回过期提示码
                case SystemConstant.JWT_ERRCODE_EXPIRE:
                    /* 签名过期 */
                    LogUtil.info("ParentInterceptor 签名过期 token=" + token);
                    print(response, CodeMessageEnum.bindSignError.getCode());
                    break;
                default:
                    break;
            }
            return false;
        }

    }
    private void print(HttpServletResponse response, String code){
        print(response, code, null);
    }
    private void print(HttpServletResponse response,String code, Object data) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            PrintWriter writer = response.getWriter();
            Result result = new Result(code.equals(CodeMessageEnum.success.getCode()), code, data);
            writer.write(JsonUtils.beanToJson(result));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LogUtil.error(e, "拦截器print");
        }
    }

    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
        if (response.getStatus() == 500) {
            LogUtil.error("拦截器出错，"+ request.getRequestURI());
        } else if (response.getStatus() == 404) {
            LogUtil.error("拦截器出错，"+ request.getRequestURI());
        }
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
