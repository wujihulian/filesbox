package com.svnlan.user.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.service.GroupService;
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

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:12
 */
@RestController
public class GroupController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    GroupService groupService;


    /**
       * @Description: 获取部门信息列表
       * @params:  [paramDto, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/10 15:38
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGroup(GroupDTO paramDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        List data = null;
        try {
            data = groupService.getGroupList(loginUser, paramDto);
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
       * @Description: 部门搜索
       * @params:  [paramDto, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/10 16:39
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchGroup(GroupDTO paramDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        List data = null;
        try {
            data = groupService.searchGroupList(loginUser, paramDto);
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
       * @Description: 删除部门
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 15:39
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/remove", method = RequestMethod.POST)
    public Result deleteGroup(@RequestBody GroupDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setStatus(2);
            groupService.setGroupStatus(prefix,loginUser, optionDTO);
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
       * @Description: 禁用、启用部门
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 16:41
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/set", method = RequestMethod.POST)
    public Result setGroupStatus(@RequestBody GroupDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            groupService.setGroupStatus(prefix,loginUser, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "  Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    /**
       * @Description:新增编辑部门
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 15:41
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/edit", method = RequestMethod.POST)
    public Result addAndEditGroup(@RequestBody GroupDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            groupService.editGroup(prefix,loginUser, optionDTO);
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
       * @Description: 部门排序
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/10 15:41
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/sort", method = RequestMethod.POST)
    public Result setGroupSort(@RequestBody GroupDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            groupService.setGroupSort(prefix,loginUser, optionDTO);
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
       * @Description: 同步组织空间使用大小
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/group/syncGroupSize", method = RequestMethod.GET)
    public Result syncGroupSize(GroupDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            groupService.syncGroupSize(prefix,loginUser, optionDTO);
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
