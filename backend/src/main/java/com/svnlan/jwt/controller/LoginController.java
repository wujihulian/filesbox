package com.svnlan.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.I18nUtils;
import com.svnlan.common.Result;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.dto.CaptchaCreateDTO;
import com.svnlan.jwt.dto.ClearTokenDTO;
import com.svnlan.jwt.dto.ImageVerificationDto;
import com.svnlan.jwt.dto.UserDTO;
import com.svnlan.jwt.service.*;
import com.svnlan.jwt.service.impl.ThirdBaseService;
import com.svnlan.jwt.tool.CaptchaCacheTool;
import com.svnlan.jwt.vo.ImageVerificationVo;
import com.svnlan.jwt.vo.LoginUserVO;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.captcha.utils.CaptchaCodeUtil;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:用户登录
 */
@Slf4j
@RestController
@Api(tags = "登录模块")
public class LoginController {

    @Resource
    private JWTService jwtService;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private DingService dingService;

    @Resource
    private EnWechatService enWechatService;
    @Resource
    private AsyncDingFileUtil asyncDingFileUtil;
    @Resource
    private WechatService wechatService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger("error");


    /**
     * @Description: 登录
     * @params: [userDTO, response]
     * @Return: com.svnlan.common.Result
     * @Modified:
     */
//    @LockAction
    @ApiOperation("登录")
    @RequestMapping(value = "/api/disk/login", method = RequestMethod.POST)
    public Result login(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request) {
        Map<String, String> tokenMap = null;
        try {
            tokenMap = jwtService.doLogin(userDTO, request);
            if (tokenMap != null && tokenMap.containsKey("token")) {
                return new Result(CodeMessageEnum.success.getCode(), tokenMap);
            } else if (tokenMap != null && tokenMap.containsKey("errCode")) {
                return new Result(false, tokenMap.get("errCode"), tokenMap);
            } else if (tokenMap != null && tokenMap.containsKey("wrongCount")) {
                return new Result(false, tokenMap.get("wrongCount"), tokenMap);
            } else if (tokenMap != null ) {
                LogUtil.error("/api/disk/login 登录失败 else1, " + JsonUtils.beanToJson(userDTO) + "，tokenMap=" + JsonUtils.beanToJson(tokenMap) );
                return new Result(false, CodeMessageEnum.pwdError.getCode(), null);
            } else {
                LogUtil.error("/api/disk/login 登录失败 else, " + JsonUtils.beanToJson(userDTO) );
                return new Result(false, CodeMessageEnum.pwdError.getCode(), null);
            }
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, "/api/disk/login 登录失败, svn" + JsonUtils.beanToJson(userDTO));
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/login 登录失败,e " + JsonUtils.beanToJson(userDTO));
        }
        return new Result(false, CodeMessageEnum.pwdError.getCode(), null);
    }

    /**
     * @Description: 服务间登录
     * @params: [loginUser]
     * @Return: com.svnlan.common.Result
     * @Modified:
     */
    @RequestMapping(value = "/service/disk/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result login(@RequestBody LoginUser loginUser) {
        try {
            Map<String, String> tokenMap = jwtService.doLoginByService(loginUser);
            if (tokenMap != null) {
                return new Result(true, CodeMessageEnum.success.getCode(), tokenMap);
            } else {
                LogUtil.error("/service/disk/login 用户名或者密码错误 else getAccessToken loginUser=" + JsonUtils.beanToJson(loginUser));
                return new Result(false, CodeMessageEnum.pwdError.getCode(), null);
            }
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e,"/service/disk/login 用户名或者密码错误 svn loginUser=" + JsonUtils.beanToJson(loginUser));

            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/service/disk/login e 登录失败" + JsonUtils.beanToJson(loginUser));
        }
        return new Result(false, CodeMessageEnum.pwdError.getCode(), null);
    }

    /**
     * @Description:服务间获取用户信息
     * @params: [map]
     * @Return: com.svnlan.jwt.domain.LoginUser
     * @Modified:
     */
    @RequestMapping(value = "/service/getLoginUser", method = RequestMethod.POST)
    public LoginUser getLoginUser(@RequestBody Map<String, String> map) {
        LoginUser loginUser;
        try {
            loginUser = loginUserUtil.getLoginUser(map);
        } catch (Exception e) {
            loginUser = null;
            LogUtil.error(e, "登录失败: " + JsonUtils.beanToJson(map));
        }
        return loginUser;
    }


    /**
     * @Description: 用户登录信息
     * @params: []
     * @Return: com.svnlan.common.Result
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/getLoginUser", method = RequestMethod.GET)
    public Result getLoginUserApi() {
        try {
            LoginUserVO loginUser = jwtService.getLoginUserApi();
            boolean isSuccess = loginUser != null && loginUser.getUserID() != null;
            if (isSuccess) {
                return new Result(CodeMessageEnum.success.getCode(), loginUser);
            }
        } catch (SvnlanRuntimeException e) {
            return new Result(e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "获取用户信息失败");
        }
        return new Result(false, CodeMessageEnum.loginTokenError.getCode(), null);
    }

    /**
     * @Description:登出
     * @params: []
     * @Return: com.svnlan.common.Result
     * @Author:
     * @Modified:
     */
    @VisitRecord(isAsync = true)
    @RequestMapping(value = "/api/disk/logout", method = RequestMethod.GET)
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            jwtService.logout(request, response, loginUser);
        } catch (Exception e) {
            LogUtil.error(e, "登出出错");
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    @ApiOperation("刷新token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", paramType = "header", value = "student")
    })
    @RequestMapping(value = "/api/disk/refreshToken", method = RequestMethod.GET)
    public Result refreshToken(HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = jwtService.refreshToken(request);
            if (tokenMap != null) {
                return new Result(CodeMessageEnum.success.getCode(), tokenMap);
            }
        } catch (Exception e) {
            LOGGER.error("刷新token失败, e:" + e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
     * @Description: 清除缓存的token
     * @params: []
     * @Return: com.svnlan.common.Result
     * @Modified:
     */
    @RequestMapping(value = "/service/disk/clearToken", method = RequestMethod.POST)
    public Result clearToken(@RequestBody @Valid ClearTokenDTO clearTokenDTO) {
        try {
            long count = jwtService.clearToken(clearTokenDTO);
            return new Result(CodeMessageEnum.success.getCode(), count);
        } catch (SvnlanRuntimeException e) {
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "清除token失败, " + JsonUtils.beanToJson(clearTokenDTO));
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
     * @Description: 获得系统时间
     * @params: [userDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/getSystemTime", method = RequestMethod.GET)
    public Long getSystemTime() {
        return System.currentTimeMillis();
    }

    @RequestMapping(value = "/api/disk/appErrLogs", method = RequestMethod.POST)
    public String appErrLogs(String logs) {
        LogUtil.error("appErrLogs, " + logs);
        return "x";
    }

    @Resource
    private OtherSettingService otherSettingService;
    @Autowired
    CaptchaCacheTool captchaCacheTool;
    @Resource
    CaptchaService captchaService;

    /**
     * @Description: 验证密码
     * @params: []
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/checkPwd", method = RequestMethod.POST)
    public String checkPwd(@RequestBody UserDTO userDTO) {
        Result result;
        try {
            Boolean b = otherSettingService.checkPassword(userDTO.getPassword());
            result = new Result(true, CodeMessageEnum.success.getCode(), b);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "验证密码失败");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    private String getRequestToken(String token, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(token)) {
            token = request.getHeader(SystemConstant.JWT_TOKEN);
        }
        return token;
    }

    /**
     * @param request  : http请求结果
     * @param response : http响应结果
     * @Description:获取图片验证码
     * @Return: void
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/getVerifyImageCode", method = RequestMethod.GET)
    public void getVerifyImageCode(@Valid CaptchaCreateDTO dto, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = null;
        try {
            if (ObjectUtils.isEmpty(dto.getWidth()) || dto.getWidth() > 400) {
                dto.setWidth(400);
            }
            if (ObjectUtils.isEmpty(dto.getHeight()) || dto.getHeight() > 300) {
                dto.setHeight(300);
            }
            if (ObjectUtils.isEmpty(dto.getLen())) {
                dto.setLen(4);
            }
            if (dto.getLen() > 6) {
                dto.setLen(6);
            }
            map = CaptchaCodeUtil.outPng(dto.getWidth(), dto.getHeight(), null, dto.getCType(), request, response);

            // 将验证码图片的字符存入Redis
            captchaCacheTool.setCaptchaCodeAlphabetsCache(map);
        } catch (IOException e) {
            LogUtil.error(e, "getVerifyImageCode IOException  ");
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, "getVerifyImageCode SvnlanRuntimeException  ");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getMsg());
        } catch (Exception e) {
            LogUtil.error(e, "getVerifyImageCode Exception ");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getMsg());
        }
        // exception occured.
//        return null;
    }

    /**
     * 获取验证码
     *
     * @param imageVerificationDto 验证码参数
     * @return 根据类型参数返回验证码
     */
    @RequestMapping("/api/disk/verification/image")
    @ResponseBody
    public ImageVerificationVo getVerificationImage(ImageVerificationDto imageVerificationDto, HttpServletResponse response) {
        ImageVerificationVo imageVerificationVo = null;
        try {
            imageVerificationVo = captchaService.selectImageVerificationCode(imageVerificationDto, response);
        } catch (Exception e) {
            LogUtil.error(e, "getVerificationImage error");
            return null;
        }
        return imageVerificationVo;
    }

    /**
     * 校验验证码
     *
     * @param x x轴坐标
     * @param y y轴坐标
     * @return 验证结果
     */
    @ResponseBody
    @RequestMapping("/api/disk/verification/result")
    public boolean checkVerificationResult(String x, String y) {
        boolean result = false;
        try {
            result = captchaService.checkVerificationResult(x, y);
        } catch (Exception e) {
            LogUtil.error(e, "checkVerificationResult error");
            return false;
        }
        return result;
    }

    /**
     * 钉钉登录获取授权码回调接口
     */
    @GetMapping("/api/ding/login/callback")
    public Result loginWithDingTalk(@RequestParam(value = "authCode") String authCode, @RequestParam(value = "state") String state) throws Exception {
        // 查询钉钉三方登录用户的配置
        ThirdUserInitializeConfig initializeConfig = dingService.getInitializeConfig();
        Assert.notNull(initializeConfig, "未配置钉钉扫码功能");
        LogUtil.info("state => {}"+ state);
        String accessToken;
        if ("innerLogin".equals(state)) {
            // 钉钉内部应用获取token
            accessToken = asyncDingFileUtil.getAccessToken(1L);
//            accessToken = dingService.getAccessToken();
            Assert.hasText(accessToken, "未获取到 accessToken");
            LogUtil.info("innerLogin accessToken => {}"+ accessToken);
            return Result.returnSuccess(dingService.checkAndExecuteInnerUserLogin(accessToken, authCode));
        } else {
            // 钉钉三方登录获取token
            accessToken = dingService.getAccessToken(authCode,1L);
            LogUtil.info("common accessToken => {}"+ accessToken);
            return Result.returnSuccess(dingService.checkAndExecuteUserLogin(accessToken, state));
        }
    }

    /**
     * 企业微信登录获取授权码回调接口
     */
    @GetMapping("/api/enwechat/login/callback")
    public Result loginWithEnWechat(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state) {
        // http://www.zdb7.com:8750/api/enwechat/login/callback
        log.info("code => {}  state => {}", code, state);
        ThirdUserInitializeConfig initializeConfig = enWechatService.getInitializeConfig();
        Assert.notNull(initializeConfig, "未配置企业微信扫码功能");
        String accessToken = enWechatService.getAccessToken(1L);
        log.info("accessToken => {}", accessToken);
        return Result.returnSuccess(enWechatService.checkAndExecuteUserLogin(accessToken, code, state));

    }

    /**
     * 微信登录获取授权码回调接口
     */
    @GetMapping("/api/wechat/login/callback")
    public Result loginWithWechat(@RequestParam(value = "code") String code, @RequestParam(value = "sig") String sig, @RequestParam(value = "state") String state) {
        log.info("code => {}  state => {} sig => {}", code, state, sig);
        ThirdUserInitializeConfig initializeConfig = wechatService.getInitializeConfig();
        Assert.notNull(initializeConfig, "未配置微信扫码功能");
        if (Objects.equals(SecurityTypeEnum.WECHAT_APP.getCode(), state)) {
            // 微信app端登陆回调
            ThirdBaseService.isAppThreadLocal.set(true);
        } else {
            // 微信网页端登陆回调
            ThirdBaseService.isAppThreadLocal.set(false);
        }
        Assert.isTrue(wechatService.checkSig(sig), "签名不正确");
        try {
            JSONObject jsonObj = wechatService.getAccessToken(code, state);
            log.info("loginWithWechat jsonObj => {}", jsonObj);
            return Result.returnSuccess(wechatService.checkAndExecuteUserLogin(jsonObj.getString("access_token"),
                    jsonObj.getString("openid"), code, state));
        } finally {
            ThirdBaseService.isAppThreadLocal.remove();
        }
    }

    @GetMapping("/api/wechat/data")
    public Result beforeLoginWithWechat(String type) {
        Pair<String, String> pair = null;
        if (Objects.equals(type, SecurityTypeEnum.WECHAT.getCode())
                || Objects.equals(type, SecurityTypeEnum.WECHAT_APP.getCode())) {
            pair = wechatService.beforeLoginWithWechat(type);
        } else if (Objects.equals(type, SecurityTypeEnum.EN_WECHAT.getCode())) {
            pair = enWechatService.beforeLoginWithWechat(type);
        }
        LogUtil.error("暂不支持的安全设置类型2type=" + type);
        Assert.notNull(pair, I18nUtils.tryI18n(CodeMessageEnum.shareErrorParam.getCode()));
        return Result.returnSuccess(new JSONObject().fluentPut("appId", pair.getFirst()).fluentPut("agentId", pair.getSecond()));
    }

    /**
     * @Description: 注册
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/register", method = RequestMethod.POST)
    public Result register(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request) {
        try {
            Map<String, String> tokenMap = jwtService.register(userDTO, request);
            if (tokenMap != null && tokenMap.containsKey("token")) {
                return new Result(CodeMessageEnum.success.getCode(), tokenMap);
            } else if (tokenMap != null && tokenMap.containsKey("needReview")) {
                // 注册成功，需要审核
                return new Result(CodeMessageEnum.success.getCode(), tokenMap);
            } else if (tokenMap != null && tokenMap.containsKey("errCode")) {
                return new Result(false, tokenMap.get("errCode"), tokenMap);
            } else if (tokenMap != null && tokenMap.containsKey("wrongCount")) {
                return new Result(false, tokenMap.get("wrongCount"), tokenMap);
            } else {
                LogUtil.error("/api/disk/register  注册失败 else, " + JsonUtils.beanToJson(userDTO));
                return new Result(false, CodeMessageEnum.explorerError.getCode(), null);
            }
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, "/api/disk/register 注册失败, svn" + JsonUtils.beanToJson(userDTO));
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/register 注册失败,e " + JsonUtils.beanToJson(userDTO));
        }
        return new Result(false, CodeMessageEnum.explorerError.getCode(), null);
    }

    @RequestMapping(value = "/api/disk/clearRedis", method = RequestMethod.GET)
    public Result clearRedis(String key) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        if (loginUser.getUserType() != 1){
            return new Result(false, CodeMessageEnum.system_error.getCode(), key);
        }
        stringRedisTemplate.delete(key);

        return new Result(true, CodeMessageEnum.success.getCode(), key);
    }
}
