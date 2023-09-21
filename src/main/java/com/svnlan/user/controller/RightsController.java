package com.svnlan.user.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.jwt.vo.LoginUserVO;
import com.svnlan.user.service.RightsService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 13:48
 */
@RestController
public class RightsController {

    @Resource
    JWTTool jwtTool;
    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    RightsService rightsService;

    /**
       * @Description: 获取用户信息及权限信息及设置信息
       * @params:  [request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/9 10:48
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserOptions(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        Map<String, Object> map = null;
        try {
            map = rightsService.getUserOptions(loginUser, request);
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
        // 返回平台信息
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }
}
