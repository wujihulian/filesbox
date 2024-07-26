package com.svnlan.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.service.ServerAboutService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ServerController {

    @Resource
    JWTTool jwtTool;
    @Resource
    ServerAboutService serverAboutService;

    @RequestMapping(value = "/api/disk/serverBasic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getServerBasicInfo(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        JSONObject map = null;
        try {
            map = serverAboutService.getServerInfoBasic(request);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回服务器信息
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/server", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getServerInfo(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        JSONObject map = null;
        try {
            map = serverAboutService.getServerInfo();
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回服务器信息
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }
}
