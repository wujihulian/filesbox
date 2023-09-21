package com.svnlan.user.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.RoleDTO;
import com.svnlan.user.service.RoleService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:12
 */
@RestController
public class RoleController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    RoleService roleService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
       * @Description: 获取角色权限列表
       * @params:  [paramDto, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/10 13:35
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/role/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRoleList(RoleDTO paramDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        List data = null;
        try {
            // 角色类型，1用户角色，2文档角色
            paramDto.setRoleType("1");
            data = roleService.getRoleList(loginUser, paramDto);
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
       * @Description: 删除角色权限
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 13:36
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/role/remove", method = RequestMethod.POST)
    public Result setRoleStatus(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setRoleType("1");
            optionDTO.setStatus(2);
            roleService.setRoleStatus(prefix,loginUser, optionDTO);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.userRoleAuth_key, 1, TimeUnit.SECONDS);
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
       * @Description: 新增编辑角色权限
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 14:06
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/role/edit", method = RequestMethod.POST)
    public Result addAndEditRole(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setRoleType("1");
            optionDTO.setStatus(1);
            roleService.editRole(prefix,loginUser, optionDTO);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.userRoleAuth_key, 1, TimeUnit.SECONDS);
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
       * @Description: 角色权限排序
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 14:38
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/role/sort", method = RequestMethod.POST)
    public Result setRoleSort(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setRoleType("1");
            roleService.setRoleSort(prefix,loginUser, optionDTO);
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
     * @Description: 获取文档权限列表
     * @params:  [paramDto, request]
     * @Return:  java.lang.String
     * @Author:  sulijuan
     * @Date:  2023/2/10 13:35
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/auth/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAuthList(RoleDTO paramDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        List data = null;
        try {
            // 角色类型，1用户角色，2文档角色
            paramDto.setRoleType("2");
            data = roleService.getRoleList(loginUser, paramDto);
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
     * @Description: 删除文档权限
     * @params:  [optionDTO, request]
     * @Return:  com.svnlan.common.Result
     * @Author:  sulijuan
     * @Date:  2023/2/10 13:39
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/auth/remove", method = RequestMethod.POST)
    public Result setDeleteRole(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setStatus(2);
            optionDTO.setRoleType("2");
            roleService.setRoleStatus(prefix,loginUser, optionDTO);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.REDIS_KEY_USER_GROUP_AUTH, 1, TimeUnit.SECONDS);
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
     * @Description: 新增编辑文档权限
     * @params:  [optionDTO, request]
     * @Return:  com.svnlan.common.Result
     * @Author:  sulijuan
     * @Date:  2023/2/10 14:06
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/auth/edit", method = RequestMethod.POST)
    public Result addAndEditAuth(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setRoleType("2");
            roleService.editRole(prefix,loginUser, optionDTO);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.REDIS_KEY_USER_GROUP_AUTH, 1, TimeUnit.SECONDS);
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
     * @Description: 文档权限排序
     * @params:  [optionDTO, request]
     * @Return:  com.svnlan.common.Result
     * @Author:  sulijuan
     * @Date:  2023/2/10 14:38
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/auth/sort", method = RequestMethod.POST)
    public Result setAuthSort(@RequestBody RoleDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setRoleType("2");
            roleService.setRoleSort(prefix,loginUser, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }
}
