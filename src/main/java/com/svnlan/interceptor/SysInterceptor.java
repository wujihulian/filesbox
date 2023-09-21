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
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;

/**
 * @Author: @Description:用户权限校验
 */
@Slf4j
@Configuration
public class SysInterceptor implements HandlerInterceptor {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    UserAuthTool userAuthTool;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(SystemConstant.JWT_TOKEN);
        LogUtil.info("SysInterceptor getRequestURI=" + request.getRequestURI() + "  token=" + token + "，ua=" + request.getHeader("User-Agent"));
        if (StringUtils.isEmpty(token)) {
            print(response, CodeMessageEnum.bindSignError.getCode());
            return false;
        } else {
            // 验证JWT的签名，返回CheckResult对象
            CheckResult checkResult = JwtUtils.validateJWT(token, false);
            if (checkResult.isSuccess()) {
                // 校验管理后台部分权限
                //获取用户信息
                try {
                    LoginUser loginUser = loginUserUtil.getLoginUserByToken(request, token);

                    if (!ObjectUtils.isEmpty(loginUser)) {
                        LogUtil.info("SysInterceptor loginUser=" + JsonUtils.beanToJson(loginUser) + "，ua=" + request.getHeader("User-Agent"));
                        if (Objects.equals(loginUser.getUserType(), 1)) {
                            LoginUserUtil.threadLocal.set(loginUser);
                            return true;
                        } else {
                            boolean check = userAuthTool.checkManageAuth(loginUser, request.getRequestURI());
                            if (!check) {
                                LogUtil.error("权限不足, api: " + request.getRequestURI() + ".  user: " + JsonUtils.beanToJson(loginUser));
                                print(response, CodeMessageEnum.errorAdminAuth.getCode());
                                return false;
                            }
                            LoginUserUtil.threadLocal.set(loginUser);
                        }
                    }

                } catch (Exception e) {
                    LogUtil.error(e, "用户信息获取失败");
                    print(response, CodeMessageEnum.errorAdminAuth.getCode());
                    return false;
                }
                return true;
            }
            switch (checkResult.getErrCode()) {
                // 签名验证不通过
                case SystemConstant.JWT_ERRCODE_FAIL:
                    LogUtil.info("SysInterceptor 签名验证不通过 token=" + token);
                    print(response, CodeMessageEnum.bindSignError.getCode());
                    break;
                // 签名过期，返回过期提示码
                case SystemConstant.JWT_ERRCODE_EXPIRE:
                    LogUtil.info("SysInterceptor 签名过期 token=" + token);
                    print(response, CodeMessageEnum.bindSignError.getCode());
                    break;
                default:
                    break;
            }
            return false;
        }

    }

    private void print(HttpServletResponse response, String code) {
        print(response, code, null);
    }

    private void print(HttpServletResponse response, String code, Object data) {
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
            LogUtil.error("拦截器出错，" + request.getRequestURI());
        } else if (response.getStatus() == 404) {
            LogUtil.error("拦截器出错，" + request.getRequestURI());
        }
    }

    /**
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，
     * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LoginUserUtil.threadLocal.remove();
    }
}
