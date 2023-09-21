package com.svnlan.user.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.dto.ClearTokenDTO;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.UserManageService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 10:47
 */
@RestController
public class UserManageController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    UserManageService userManageService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    JWTService jwtService;

    /**
       * @Description:
       * @params:  [request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/9 10:50
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/user/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserList(UserDTO userDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        PageResult data = null;
        try {
            data = userManageService.getUserList(loginUser, userDTO);
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
        result = new Result(CodeMessageEnum.success.getCode(), data);
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 启用、禁用、删除用户
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/9 15:36
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/user/set", method = RequestMethod.POST)
    public Result setUser(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            if (!ObjectUtils.isEmpty(optionDTO.getOpType()) && "avatar".equals(optionDTO.getOpType())){
                userManageService.setUserAvatar(prefix,loginUser, optionDTO);
            }else {
                userManageService.setUserStatus(prefix,loginUser, optionDTO);
            }
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    /**
       * @Description: 新增、编辑用户
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/9 15:48
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/user/edit", method = RequestMethod.POST)
    public Result editUser(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setCreateUser(loginUser.getUserID());
            userManageService.editUser(prefix,loginUser, optionDTO);
            // 修改密码后清除token
            if (!ObjectUtils.isEmpty(optionDTO.getPassword())) {
                // clear token 清除了Redis中被修改密码用户的所有token.
                ClearTokenDTO clearTokenDTO = new ClearTokenDTO();
                clearTokenDTO.setUserID(optionDTO.getUserID());
                jwtService.clearToken(clearTokenDTO);
            }
            // 删除编辑的用户的相关redis
            stringRedisTemplate.opsForHash().delete(GlobalConfig.REDIS_KEY_USER_GROUP_AUTH, optionDTO.getUserID()+"");
            // 删除编辑的用户的相关redis
            stringRedisTemplate.opsForHash().delete(GlobalConfig.userRoleAuth_key, optionDTO.getUserID()+"");
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    /**
     * 删除缓存
     * @param userDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/redis/del", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String delRedis(UserDTO userDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        if (loginUser.getUserType().intValue() != 1){
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        PageResult data = null;
        try {
            stringRedisTemplate.delete(userDTO.getKey());
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
        result = new Result(CodeMessageEnum.success.getCode(), data);
        return JsonUtils.beanToJson(result);
    }
}
