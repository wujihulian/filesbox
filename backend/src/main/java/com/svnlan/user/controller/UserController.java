package com.svnlan.user.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.enums.FindTypeEnum;
import com.svnlan.enums.SendOperateTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.LoginParamDTO;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.UserService;
import com.svnlan.user.vo.LoginQRVO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 11:10
 */
@RestController
public class UserController {
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    UserService userService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
       * @Description: 个人中心设置
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/13 11:26
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/user/setOption", method = RequestMethod.POST)
    public Result setUser(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            userService.setUserOption(prefix,loginUser, optionDTO);
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
     * 修改用户信息
     */
    @RequestMapping(value = "/api/disk/user/setUserInfo", method = RequestMethod.POST)
    public Result setUserInfo(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            userService.setUserInfo(prefix,loginUser, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }
    @RequestMapping(value = "/api/disk/check/email", method = RequestMethod.POST)
    public Result check(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        try {
            optionDTO.setOpType(SendOperateTypeEnum.BINDING.getCode());
            userService.checkEmailCode(optionDTO);
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
       * @Description: 发送邮箱
       * @params:  [userCenterDTO, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/20 13:22
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/email/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendBindEmailCode(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        Boolean check;
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            //发送验证码
            check = userService.sendBindEmailCode(userDTO.getValue(), loginUser, request);
        } catch (SvnlanRuntimeException e) {
            //处理异常
            result = new Result(Boolean.FALSE, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception ex) {
            //处理异常
            result = new Result(Boolean.FALSE, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        if (!check) {
            result = new Result(Boolean.FALSE, CodeMessageEnum.bindError.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回操作成功信息
        result = new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), check);
        return JsonUtils.beanToJson(result);
    }

    /**
     * 绑定微信/企业微信
     */
    @GetMapping("/api/wechat/bind")
    public Result wechatBind(String code, String state, String sig, String type) {
        return Result.returnSuccess(userService.bindWechat(code, state, sig, type));
    }

    /**
     * 解绑微信/企业微信
     */
    @GetMapping("/api/wechat/unbind")
    public Result wechatUnbind(String type) {
        userService.wechatUnbind(type);
        return Result.returnSuccess();
    }

    /**
     * @description: 获取扫码登录二维码URL
     * @param request
     * @param paramDTO
     * @return java.lang.String
     */
    @PostMapping(value = "/api/disk/findLoginAuthUrl")
    public String findLoginAuthUrl(HttpServletRequest request, @RequestBody LoginParamDTO paramDTO) {
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> paramDTO:%s", JsonUtils.beanToJson(paramDTO));
            //获取扫码登录二维码URL
            LoginQRVO loginQRVO = this.userService.findLoginAuthUrl(paramDTO);
            result = Result.returnSuccess(loginQRVO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "SvnlanRuntimeException" + paramTip);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "Exception" + paramTip);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }

    @ResponseBody
    @PostMapping(value = "/api/disk/scanLogin")
    public String scanLogin(@RequestBody LoginParamDTO paramDTO, HttpServletRequest request, HttpServletResponse response) {
        Result result;
        Map<String, Object> resultMap;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        String lockRedisKey = "";
        try {
            paramTip = String.format(" >>> paramDTO:%s", JsonUtils.beanToJson(paramDTO));

            String tempAuth = paramDTO.getTempAuth();
            if(StringUtil.isEmpty(tempAuth)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode(), "tempAuth不能为空");
            }
            ValueOperations valueOperations = this.stringRedisTemplate.opsForValue();
            lockRedisKey = String.format(GlobalConfig.ScanLoginAuth, tempAuth);
            Boolean isSuccess = valueOperations.setIfAbsent(lockRedisKey, "1");
            if(isSuccess) { //若成功，则置1秒过期
                //
                this.stringRedisTemplate.expire(lockRedisKey, 1, TimeUnit.SECONDS);
            } else {
                throw new SvnlanRuntimeException(CodeMessageEnum.codeErrorFreq.getCode());
            }

            //（TV、WEB端）临时授权登录（扫码登录）
            LoginQRVO tokenDTO = this.userService.scanLogin(prefix, paramDTO, request, response);
            result = Result.returnSuccess(tokenDTO);

            //移除并发锁
            this.stringRedisTemplate.delete(lockRedisKey);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, paramTip + "SvnlanRuntimeException");
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, paramTip + "Exception");
            result = Result.returnError(CodeMessageEnum.system_error);

            //移除并发锁
            this.stringRedisTemplate.delete(lockRedisKey);
        }
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/email/safeSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String safeSend(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        Boolean check;
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            //发送验证码
            check = userService.sendVerificationCode(FindTypeEnum.EMAIL.getCode(), userDTO.getValue(), loginUser, SendOperateTypeEnum.SAFE.getCode(), null, request);

        } catch (SvnlanRuntimeException e) {
            //处理异常
            result = new Result(Boolean.FALSE, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception ex) {
            //处理异常
            result = new Result(Boolean.FALSE, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        if (!check) {
            result = new Result(Boolean.FALSE, CodeMessageEnum.bindError.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回操作成功信息
        result = new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), check);
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/check/safeEmail", method = RequestMethod.POST)
    public Result safeEmail(@RequestBody UserDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        try {
            optionDTO.setOpType(SendOperateTypeEnum.SAFE.getCode());
            userService.checkEmailCode(optionDTO);
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
