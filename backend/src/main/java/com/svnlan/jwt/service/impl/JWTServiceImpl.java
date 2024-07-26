package com.svnlan.jwt.service.impl;

import com.svnlan.common.CheckResult;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.ClientTypeEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.dao.UserJwtDao;
import com.svnlan.jwt.domain.CommonUserClient;
import com.svnlan.jwt.domain.LogLogin;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.UserRole;
import com.svnlan.jwt.dto.ClearTokenDTO;
import com.svnlan.jwt.dto.UserDTO;
import com.svnlan.jwt.enums.VerificationCodeType;
import com.svnlan.jwt.service.CaptchaService;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.jwt.service.LogLoginService;
import com.svnlan.jwt.tool.CaptchaCacheTool;
import com.svnlan.jwt.vo.LoginUserVO;
import com.svnlan.jwt.vo.RegisterConfigVo;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.User;
import com.svnlan.user.service.NoticeUserService;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:
 */
@Service
public class JWTServiceImpl implements JWTService {
    @Resource
    private UserJwtDao userJwtDao;

    @Resource
    private UserJwtDao userJwtDaoImpl;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private LogLoginService logLoginService;
    @Resource
    SystemLogTool systemLogTool;

    @Resource
    SystemOptionDao systemOptionDaoImpl;
    @Resource
    UserManageService userManageService;

    @Value("${user.default.headPic}")
    private String defaultAvatar;

    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private TenantUtil tenantUtil;
    @Resource
    CaptchaCacheTool captchaCacheTool;
    @Resource
    CaptchaService captchaService;
    @Resource
    UserDao userDao;

    /**
     * @Description: 登录
     * @params: [userDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @Override
    public Map<String, String> doLogin(UserDTO userDTO, HttpServletRequest request) {
        String username = userDTO.getName().trim();
        String password = userDTO.getPassword();
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.namePwdNotNull.getCode());
        }
        password = checkPassword(userDTO, request);

        boolean checkPwdErrLock = false;
        String passwordErrorLock = systemOptionDaoImpl.getSystemConfigByKey("passwordErrorLock");


        if (!ObjectUtils.isEmpty(passwordErrorLock) && "1".equals(passwordErrorLock)){
            checkPwdErrLock = true;
        }
        if (checkPwdErrLock) {
            //验证错误次数
            Map<String, String> checkMap = null;
            try {
                checkMap = loginUserUtil.checkAccountWrong(request, userDTO);
            } catch (Exception e) {
                LogUtil.error(e, "验证失败登录次数失败");
            }
            if (checkMap != null && checkMap.containsKey("errCode")) {
                return checkMap;
            }
        }
        try {
            //验证短时间内重复登录
            Map<String, String> checkFastLogin = loginUserUtil.checkFastLogin(request, username, password);
            if (checkFastLogin != null) {
                return checkFastLogin;
            }
        } catch (Exception e) {
            LogUtil.error(e, "短时间内重复登录验证失败");
        }
        String userAgent = UserAgentUtils.getUserAgent(request);
        //若是安卓或IOS APP
        /*if(VersionUtil.isAndroidApp(userAgent) || VersionUtil.isIOSApp(userAgent)) {

        }*/

        //根据前端传递的标志位，决定是否校验图形验证码
        if (userDTO.getIsGraphicCode() != null && userDTO.getIsGraphicCode() == 1) {
            if (userDTO.getCaptchaInput() == null || userDTO.getCaptchaInput().trim().isEmpty()) {
                throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
            }
            boolean captcha = false;
            String type = null;
            if (ObjectUtils.isEmpty(userDTO.getType())) {
                type = VerificationCodeType.SLIDE.name();
            } else {
                type = userDTO.getType();
            }
            VerificationCodeType verificationCodeType = Enum.valueOf(VerificationCodeType.class, type.toUpperCase());
            switch (verificationCodeType) {
                //  获取运算验证码
                case OPERATION:
                    break;
                //  获取字符验证码
                case CHAR:
                    // 校验图形验证码是否正确
                    captcha = captchaCacheTool.checkCaptchaCodeAlphabets(request, userDTO.getCaptchaInput());
                    break;
                //  获取滑动验证码
                case SLIDE:
                    captcha = captchaService.checkVerificationResult(userDTO.getX(), userDTO.getY());
                    break;
                default:
                    throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
            }

            if (!captcha) {
                LogUtil.error("hpSearchSchoolUser captchaError , dto : {}", JsonUtils.beanToJson(userDTO));
                throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
            }
        }
        LoginUser loginUser = null;
        //从数据库查找用户
        List<LoginUser> user = null;
        if (user == null) {
            user = userJwtDaoImpl.findByUserName(username);
        }
        if (CollectionUtils.isEmpty(user)){
            LogUtil.info("doLogin  findByUserName is null 搜索用户为空");
        }else {
            LogUtil.info("doLogin  findByUserName  搜索用户 userList=" + JsonUtils.beanToJson(user) +
                    "，password=" + password + "，加密：" + PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT));
        }
        //账号是否正确
        boolean accountRight = false;
        if (user.size() == 1) {
            loginUser = user.get(0);
        } else if (user.size() > 1) {
            for (LoginUser u : user) {
                //前端传过来的密码加密
                String givenPwd = PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT);
                boolean isRight = (loginUser.getPassword()).equals(givenPwd);
                if (isRight) {
                    loginUser = u;
                    accountRight = true;
                    break;
                }
            }
            if (ObjectUtils.isEmpty(loginUser)) {
                LogUtil.error("doLogin 登录结果有多个" + JsonUtils.beanToJson(userDTO));
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }
        }

        if (!accountRight && loginUser != null) {
            //前端传过来的密码加密
            String givenPwd = PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT);
            accountRight = (loginUser.getPassword()).equals(givenPwd);
        }
        if (accountRight) {

            if (loginUser.getStatus().intValue() != 1) {
                throw new SvnlanRuntimeException(CodeMessageEnum.userEnabled.getCode());
            }
            loginUser.setUserType(3);
            // 获取权限
            UserRole val = userJwtDaoImpl.getUserIdentity(loginUser.getUserID());
            if (!ObjectUtils.isEmpty(val)) {
                if (!ObjectUtils.isEmpty(val.getAdministrator()) && 1 == val.getAdministrator()) {
                    loginUser.setUserType(1);
                } else if (!ObjectUtils.isEmpty(val.getAuth()) && val.getAuth().indexOf("admin") >= 0) {
                    loginUser.setUserType(2);
                }
            }
            // 把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
            Boolean saveCookie = false;
            if (userDTO.getSaveCookie() != null && userDTO.getSaveCookie()) {
                saveCookie = true;
            }
            Map<String, String> tokenMap = this.saveInfos(loginUser, false, saveCookie, false);
            //保存token 账号, 以供判断短时重复登录
            loginUserUtil.addLoginInfo(request, username, password, tokenMap);
//            // 建立登录用户与通知的关联关系
//            final Long userId = loginUser.getUserID();
//            asyncTaskExecutor.execute(()->noticeUserService.buildRelationAfterLogin(userId, true));
            return tokenMap;
        }
        if (checkPwdErrLock) {
            return loginUserUtil.doAccountWrong(request, userDTO);
        } else {
            return null;
        }
    }

    /**
     * 设置用户 userType
     */
    public void setUserType(LoginUser loginUser) {
        loginUser.setUserType(3);
        // 获取权限
        UserRole val = userJwtDao.getUserIdentity(loginUser.getUserID());
        if (!ObjectUtils.isEmpty(val)) {
            if (!ObjectUtils.isEmpty(val.getAdministrator()) && 1 == val.getAdministrator()) {
                loginUser.setUserType(1);
            } else if (!ObjectUtils.isEmpty(val.getAuth()) && val.getAuth().contains("admin")) {
                loginUser.setUserType(2);
            }
        }
    }

    /**
     * 三方登录
     */
    @Override
    public Map<String, String> doThirdLogin(Long userId, HttpServletRequest request) {

        //从数据库查找用户
        LoginUser loginUser = userJwtDao.findByUserId(userId);
        Assert.notNull(loginUser, "未查询到用户 userId =>" + userId);


        if (loginUser.getStatus() != 1) {
            throw new SvnlanRuntimeException(CodeMessageEnum.userEnabled.getCode());
        }
        loginUser.setUserType(3);
        // 把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数

        Map<String, String> tokenMap = this.saveInfos(loginUser, false, true, true);
        //保存token 账号, 以供判断短时重复登录
        loginUserUtil.addLoginInfo(request, loginUser.getName(), loginUser.getPassword(), tokenMap);
        return tokenMap;
    }

    public String checkPassword(UserDTO userDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return checkPassword(userDTO, request);
    }

    private String checkPassword(UserDTO userDTO, HttpServletRequest request) {
        String password = userDTO.getPassword();
        if (userDTO.getEncrypted() != null && userDTO.getEncrypted()) {
            password = AESUtil.decrypt(password, GlobalConfig.LOGIN_PASSWORD_AES_KEY, true);
            if (password == null) {
                throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
            } else {
                LogUtil.info("解密成功");
            }
        } else {
            LogUtil.info("未加密 " + userDTO.getName() + ", ua: " + request.getHeader("User-Agent")
                    + ", ip: " + IpUtil.getIp(request) + ", referer: " + request.getHeader("Referer") + ", uri: " + request.getRequestURI());

        }
        return password;
    }


    /**
     * @Description: 保存各种信息
     * @params: [loginUser, isService]
     * @Return: java.lang.String
     * @Modified:
     */
    private Map<String, String> saveInfos(LoginUser loginUser, boolean isService, boolean saveCookie, boolean isAsync) {
        Boolean isParent = false;
        Map<String, Object> map = new HashMap<>();
        map.put("userID", loginUser.getUserID());
        map.put("uuid", UUID.randomUUID());
        String token = JwtUtils.createJWT("user_" + loginUser.getUserID(), loginUser.getName(), map, null, isParent);

        HttpServletRequest request = this.getRequest();
        //保存token到redis
        if (request.getAttribute("isTest") == null || !"1".equals(request.getAttribute("isTest").toString())) {
            this.saveTokenInfo(token, loginUser, saveCookie);
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("userID", loginUser.getUserID().toString());
        if (!ObjectUtils.isEmpty(loginUser.getTenantId())) {
            tokenMap.put("tenantId", loginUser.getTenantId().toString());
        }
        //保存用户设备到数据库
        int loginLimit = 0;
        //login_limit   设置受限制登陆的客户端数量，0表示不受限制
        this.saveClient(token, loginUser, isService, loginLimit);
        //更新登录信息
        loginUser.setLastLogin(System.currentTimeMillis());
        if (request.getAttribute("isTest") == null || !"1".equals(request.getAttribute("isTest").toString())) {
            try {
                //保存到登录日志
                this.saveLoginLog(loginUser, isService, isAsync);
            } catch (Exception e) {
                LogUtil.error(e, "保存登录日志失败");
            }
        } else {
            loginUser.setIp(IpUtil.getIp(request));
        }
        //todo 添加user_school数据
        //保存cookie
        if (!isService) {
            this.saveCookie(token, saveCookie, isParent);
        }
        return tokenMap;
    }


    /**
     * @Description: 服务调用登录
     * @params: [loginUser]
     * @Return: java.lang.String
     * @Modified:
     */
    @Override
    public Map<String, String> doLoginByService(LoginUser loginUser) {
        //保存登录状态
        return this.saveInfos(loginUser, true, loginUser.getSaveCookie() != null && loginUser.getSaveCookie(), false);
    }

    /**
     * @Description: 刷新token
     * @params: [request]
     * @Return: java.util.Map<java.lang.String
     */
    @Override
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            return null;
        }
        //验证refresh token
        CheckResult checkResult = JwtUtils.validateJWT(token);
        if (checkResult.getErrCode() != 0 && checkResult.getErrCode() != SystemConstant.JWT_ERRCODE_EXPIRE) {
            return null;
        }
        Claims claims = checkResult.getClaims();
        Long userID = Integer.toUnsignedLong((Integer) claims.get("userID"));
        String keyRefresh = GlobalConfig.JWT_REDIS_PREFIX + "_"
                + userID + "_"
                + token;
        //从redis取出存在refresh token key中的token值
        Long ttl = redisTemplate.getExpire(keyRefresh);

        return null;
    }


    /**
     * @Description: 获取用户信息
     * @params: []
     * @Return: com.svnlan.jwt.vo.LoginUserVO
     * @Modified:
     */
    @Override
    public LoginUserVO getLoginUserApi() {
        HttpServletRequest request = this.getRequest();
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        if (loginUser == null || loginUser.getUserID() == null) {
            return null;
        }

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setIp(IpUtil.getIp(request));

        loginUserVO.setLastLogin(loginUser.getLastLogin());
        loginUserVO.setName(loginUser.getName());
        loginUserVO.setSex(loginUser.getSex());
        loginUserVO.setStatus(loginUser.getStatus());
        loginUserVO.setUserType(loginUser.getUserType());
        loginUserVO.setUserID(loginUser.getUserID());
        loginUserVO.setLanguageVersion(loginUser.getLanguageVersion());
        loginUserVO.setNickname(loginUser.getNickname());
        loginUserVO.setAvatar(loginUser.getAvatar());
        //设置默认头像
        if (ObjectUtils.isEmpty(loginUserVO.getAvatar())) {
            loginUserVO.setAvatar(defaultAvatar);
        }
        return loginUserVO;
    }

    /**
     * @Description: 清除token
     * @params: [clearTokenDTO]
     * @Return: int
     * @Modified:
     */
    @Override
    public long clearToken(ClearTokenDTO clearTokenDTO) {
        List<Long> userIdList = new ArrayList<>();
        //单个用户的
        if (clearTokenDTO.getUserID() != null) {
            userIdList.add(clearTokenDTO.getUserID());
        } else if (!CollectionUtils.isEmpty(clearTokenDTO.getUserIdList())) {//批量的
            userIdList = clearTokenDTO.getUserIdList();
        }
        if (CollectionUtils.isEmpty(userIdList)) {
            return 0;
        }
        //循环删除
        for (Long userID : userIdList) {
            String hashKey = GlobalConfig.JWT_TOKEN_HASH_PRE + userID;
            Set<Object> tokenList = stringRedisTemplate.opsForHash().keys(hashKey);
            if (CollectionUtils.isEmpty(tokenList)) {
                continue;
            }
            //删除key todo 处理数据库
            stringRedisTemplate.delete(hashKey);
            redisTemplate.delete(tokenList);
        }
        return userIdList.size();
    }

    /**
     * @Description: 保存cookie
     * @params: [token]
     * @Return: void
     * @Modified:
     */
    private void saveCookie(String token, Boolean saveCookie, Boolean isParent) {
        HttpServletRequest request = this.getRequest();
        HttpServletResponse response = this.getResponse();
        //不保存cookie
        if (request.getAttribute("noCookie") != null && request.getAttribute("noCookie").toString().equals("1")) {
            return;
        }

//        String tokenKey = "token";
        List<String> tokenList = new ArrayList<>();
        tokenList.add(isParent ? "ptoken" : "token");
        //存到stoken上, 从平台 进入 网校教师
        if (request.getAttribute("saveAsSToken") != null && "1".equals(request.getAttribute("saveAsSToken").toString())) {
            tokenList.add("stoken");
        }
        for (String tokenKey : tokenList) {
            Cookie cookie = new Cookie(tokenKey, token);
            cookie.setPath("/");
            if (saveCookie) {
                cookie.setMaxAge(SystemConstant.JWT_LONG_COOKIE_TIME);
             } else {
               cookie.setMaxAge(-1);
             }
            if (token != null) {
                response.addCookie(cookie);
            }
        }
        Object sTokenObj = request.getAttribute("sToken");
        if (sTokenObj != null) {
            Cookie sCookie = new Cookie("stoken", sTokenObj.toString());
            sCookie.setPath("/");
            sCookie.setMaxAge(-1);
            response.addCookie(sCookie);
        }
    }

    private void saveLoginLog(LoginUser loginUser, boolean isService, boolean isAsync) {
        HttpServletResponse response = this.getResponse();
        if (isAsync) {
            HttpServletRequest request = this.getRequest();
            asyncTaskExecutor.execute(() -> saveLoginLog(loginUser, isService, request, response));
        } else {
            saveLoginLog(loginUser, isService);
        }
        setCookie(response);
    }

    private void saveLoginLog(LoginUser loginUser, boolean isService) {
        saveLoginLog(loginUser, isService, null, null);
        setCookie(null);
    }

    private void setCookie(HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(SystemConstant.LOGIN_COOKIE_KEY, String.valueOf(System.currentTimeMillis()));
        cookie.setPath("/");
        HttpServletResponse response = Optional.ofNullable(servletResponse).orElseGet(this::getResponse);
        response.addCookie(cookie);
    }

    /**
     * @Description: 保存登录日志
     * @params: [loginUser, isService]
     * @Return: void
     * @Modified:
     */
    private void saveLoginLog(LoginUser loginUser, boolean isService, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            String ua;
            String serverName;
            String ip;
            HttpServletRequest request = Optional.ofNullable(servletRequest).orElseGet(this::getRequest);
            if (isService) {
                //服务间调用的
                ua = loginUser.getUserAgent();
                serverName = loginUser.getServerName();
                ip = loginUser.getIp();
            } else {
                ua = request.getHeader("User-Agent");
                serverName = Objects.nonNull(servletRequest) ? loginUserUtil.getServerName(servletRequest) : loginUserUtil.getServerName();
                ip = IpUtil.getIp(request);
            }
            loginUser.setIp(ip);

            LogLogin logLogin = new LogLogin();

            logLogin.setUserIp(ip);
            logLogin.setDomain(serverName);
            logLogin.setGmtLogin(System.currentTimeMillis());
            logLogin.setName(loginUser.getName());
            logLogin.setNickname(ObjectUtils.isEmpty(loginUser.getNickname()) ? loginUser.getName() : loginUser.getNickname());
            logLogin.setUserID(loginUser.getUserID());
            logLogin.setAvatar(loginUser.getAvatar());
            logLogin.setSex(loginUser.getSex().toString());
            //todo ua处理
            String resolution = request.getHeader("resolution");
            if (resolution != null) {
                logLogin.setResolution(resolution);
            } else {
                logLogin.setResolution("");
            }
            if (ua == null) {
                logLogin.setBrowser("");
                logLogin.setNetworkOperator("");
                logLogin.setOperatingSystem("");
                logLogin.setClientType(ClientTypeEnum.other.getCode());
            } else {
                logLogin.setClientType(ClientTypeEnum.pc.getCode());
                String browserName = request.getHeader("browsername");
                LogUtil.info("获取登录设备信息:" + ua + "browsername" + browserName);
                if (browserName != null) {
                    logLogin.setBrowser(browserName);
                } else if (ua.toLowerCase().contains("ios")) {
                    logLogin.setBrowser("App");
                } else if (ua.toLowerCase().contains("xxbrowser")) {
                    logLogin.setBrowser("xxBrowser");
                } else if (UserAgentUtils.getBorderGroup(ua) != null) {
                    logLogin.setBrowser(UserAgentUtils.getBorderGroup(ua));
                } else {
                    logLogin.setClientType(ClientTypeEnum.other.getCode());
                    logLogin.setBrowser("otherBrowser");
                }

                if (ObjectUtils.isEmpty(logLogin.getBrowser())) {
                    if (ua.toLowerCase().contains("ios")) {
                        logLogin.setBrowser("App");
                    }
                }
                String osName = UserAgentUtils.getOsName(ua);
                if ("App".equals(logLogin.getBrowser())) {
                    //判断是不是安卓
                    if (osName.toLowerCase().contains("android")) {
                        osName = "Android";
                        logLogin.setClientType(ClientTypeEnum.android.getCode());
                    } else {
                        osName = "iOS";
                    }
                } else if ("App(TV)".equals(logLogin.getBrowser())) {
                    logLogin.setClientType(ClientTypeEnum.ios.getCode());
                    osName = "Android";
                }
                // 判断来源端
                if (ua.toLowerCase().contains("wechat")) {
                    logLogin.setClientType(ClientTypeEnum.mini.getCode());
                }
                //操作系统的名字
                logLogin.setOperatingSystem(osName);
                //保存ua
                if (ua.length() > 512) {
                    String string = ua.subSequence(0, 512).toString();
                    logLogin.setUserAgent(string);
                } else {
                    logLogin.setUserAgent(ua);
                }
            }
            //kafkaTemplate.send("logLoginConsumer", JsonUtils.beanToJson(logLogin));
            /** 添加登录日志*/
            logLoginService.setLogLogin(logLogin);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e, "写登录日志失败");
        }
    }

    /**
     * @Description: 登出
     * @params: []
     * @Return: void
     * @Modified:
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        String token = request.getHeader("token");
        this.delToken(token, response);
        stringRedisTemplate.delete(GlobalConfig.user_safe_redis_key+loginUser.getUserID());
        // 移除登录状态缓存
        CaffeineUtil.CURRENT_ONLINE_USER.invalidate(loginUser.getUserID());
        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, LogTypeEnum.logout.getCode(), request);
    }

    /**
     * @Description: 将stoken的值覆盖到token
     * @params: [sToken, response]
     * @Return: void
     * @Modified:
     */
    private void copySTokenToToken(String sToken, String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", sToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        //并且清除redis中的token
        this.delTokenRedis(token);
    }

    /**
     * @Description: 清除token
     * @params: [token, response]
     * @Return: void
     * @Modified:
     */
    private void delToken(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        response.addCookie(cookie);
        this.delTokenRedis(token);
    }


    private void delTokenRedis(String token) {
        CheckResult checkResult = JwtUtils.validateJWT(token, false);
        if (checkResult.isSuccess()) {
            Claims claims = checkResult.getClaims();
            Long userID = Long.valueOf(claims.get("userID").toString());
            String key = GlobalConfig.JWT_REDIS_PREFIX + "_" + userID + "_" + token;
            redisTemplate.delete(key);
        }
    }

    /**
     * @Description: 保存token对应的用户信息
     * @params: [token, loginUser]
     * @Return: void
     * @Modified:
     */
    private void saveTokenInfo(String token, LoginUser loginUser, boolean saveCookie) {
        ValueOperations<String, LoginUser> operations = redisTemplate.opsForValue();
        //jwt_10086_10000_dasdpaskdpas.asdas.asdas

        String key = GlobalConfig.JWT_REDIS_PREFIX + "_"
                + loginUser.getUserID() + "_"
                + token;
        if (StringUtil.isEmpty(loginUser.getAvatar())) {
            loginUser.setAvatar(defaultAvatar);
        }
        //redis jwt存loginUser
        loginUser.setSaveCookie(saveCookie);
        Long ttl = saveCookie ? SystemConstant.JWT_REDIS_LONG_TTL : SystemConstant.JWT_REDIS_TTL;
        //Long ttl = SystemConstant.JWT_REDIS_LONG_TTL ;
        operations.set(key, loginUser, ttl, TimeUnit.MILLISECONDS);
        stringRedisTemplate.opsForHash().put(GlobalConfig.JWT_TOKEN_HASH_PRE + loginUser.getUserID(), key, String.valueOf(System.currentTimeMillis()));
    }


    /**
     * @Description: 保存设备记录
     * @params: [token, loginUser]
     * @Return: void
     * @Modified:
     */
    private void saveClient(String token, LoginUser loginUser, boolean isService, int count) {
        CommonUserClient commonUserClient = new CommonUserClient();
        HttpServletRequest request = this.getRequest();
        String agentString = request.getHeader("User-Agent");
        String resolution = request.getHeader("resolution");
        String borderGroup = UserAgentUtils.getBorderGroup(agentString);
        if (request.getHeader("browsername") != null) {
            borderGroup = request.getHeader("browsername");
            commonUserClient.setBrowser(borderGroup);
        } else if (agentString.toLowerCase().contains("ios")) {
            commonUserClient.setBrowser("App");
        } else if (borderGroup != null && agentString.toLowerCase().contains("xxbrowser")) {
            commonUserClient.setBrowser("xxBrowser");
            borderGroup = "xxBrowser";
        } else if (borderGroup != null) {
            commonUserClient.setBrowser(borderGroup);
        } else {
            commonUserClient.setBrowser("otherBrowser");
        }
        String osName = UserAgentUtils.getOsName(agentString);
        if ("App(TV)".equals(commonUserClient.getBrowser())) {
            osName = "Android";
        } else if ("App".equals(commonUserClient.getBrowser())) {
            //判断是不是安卓
            if (osName.toLowerCase().contains("android")) {
                osName = "Android";
            } else {
                osName = "iOS";
            }
        }
        //操作系统的名字
        commonUserClient.setOperatingSystem(osName);
        //浏览器分辨率
        if (resolution != null) {
            commonUserClient.setResolution(resolution);
        } else {
            commonUserClient.setResolution("");
        }
        //根据用户操作系统和分辨率去查,判断count 如果=1 不处理
        Map<String, Object> map = new HashMap<>();
        map.put("osName", osName);
        map.put("resolution", resolution);
        map.put("borderGroup", borderGroup);
        map.put("userID", loginUser.getUserID());
        //状态过滤
        map.put("status", 1);
        /*int sameNum = commonUserClientDao.selectCountBy(map);
        if (sameNum == 0) {
            //如果等于0,这是个新设备,根据用户id和网校id去查总数,如果这个总数已经小于限制数,添加记录,否则报错
            int totalCount = commonUserClientDao.selectByUserIdAndSchoolId(loginUser.getUserId(), loginUser.getSchoolId());
            //当前设备记录数小于限制数
            if (totalCount < count) {
                Long currentTime = System.currentTimeMillis();
                Date now = new Date(currentTime);
                Date endDate = new Date(currentTime + SystemConstant.JWT_REDIS_TTL);
                commonUserClient.setGmtLogin(now);
                commonUserClient.setLoginName(loginUser.getLoginName());
                commonUserClient.setRealName(loginUser.getRealName());
                commonUserClient.setPlatformId(loginUser.getPlatformId());
                commonUserClient.setSex(loginUser.getSex());
                commonUserClient.setState(loginUser.getState());
                commonUserClient.setUserId(loginUser.getUserId());
                PlatformAndSchool platformAndSchool = loginUserUtil.getPlatformAndSchoolId(isService ? loginUser.getServerName() : null);
                commonUserClient.setSchoolId(platformAndSchool.getSchoolId());
                commonUserClient.setToken(token);
                commonUserClient.setGmtTokenCreate(now);
                commonUserClient.setGmtTokenGone(endDate);
                if (isService) {//服务间调用的
                    commonUserClient.setUserIp(loginUser.getIp());
                } else {
                    commonUserClient.setUserIp(IpUtil.getIp(request));
                }
                //获取请求内容
                try {
                    LogUtil.info("登陆限制设备信息：" + commonUserClient.toString());
                    commonUserClientDao.insert(commonUserClient);
                } catch (Exception e) {
                    LogUtil.error(e, "设备信息入库失败");
                }
            } else {
                String redisKey = GlobalConfig.SEVEN_DAY_CLIENT_COUNT + loginUser.getSchoolId() + "_" + loginUser.getUserId();
                //添加到redis中
                ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
                String s = stringStringValueOperations.get(redisKey);
                List<Long> longs;
                if (s == null) {
                    longs = new ArrayList<>();
                } else {
                    longs = JsonUtils.stringToList(s, long.class);
                }
                longs.add(System.currentTimeMillis());
                stringStringValueOperations.set(redisKey, JsonUtils.beanToJson(longs), 7, TimeUnit.DAYS);
                throw new SvnlanRuntimeException(CodeMessageEnum.LOGIN_LIMIT.getCode(), "您的账号已在多处登录，此设备已被限制，请尽快修改密码或者联系客服人员。");
            }
        }*/
    }


    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    @Override
    public Map<String, Object> getAccessToken(UserDTO dto) {
        String userName = dto.getName().trim();
        String password = dto.getPassword().trim();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.namePwdNotNull.getCode());
        }
        password = AESUtil.decrypt(password, GlobalConfig.OPEN_LOGIN_PASSWORD_AES_KEY, true);
        password = PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT);

        LoginUser loginUser = userJwtDao.findByLoginName(userName);
        if (ObjectUtils.isEmpty(loginUser) || !password.equals(loginUser.getPassword())) {
            LogUtil.error("用户名或者密码错误  getAccessToken getPassword" + dto.getPassword().trim());
            throw new SvnlanRuntimeException(CodeMessageEnum.pwdError.getCode());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userID", loginUser.getUserID());
        String userType = loginUser.getRoleID() > 1 ? "2" : "1";
        map.put("userType", userType);
        map.put("uuid", UUID.randomUUID());
        String acccessToken = JwtUtils.createJWT(String.valueOf(loginUser.getUserID()), String.valueOf(loginUser.getLoginType()), map, null);
        setRedisKey(loginUser, acccessToken);
        map.clear();
        map.put("accessToken", acccessToken);
        LogUtil.info("获取accesstoken：" + acccessToken);
        return map;
    }

    private void setRedisKey(LoginUser loginUser, String token) {
        String key = GlobalConfig.JWT_API_REDIS_PREFIX + loginUser.getUserID() + "_" + token;
        ValueOperations<String, LoginUser> operations = redisTemplate.opsForValue();
        operations.set(key, loginUser);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    private LoginUser getRedisKey(Long userID, String token) {
        String key = GlobalConfig.JWT_API_REDIS_PREFIX + userID + "_" + token;
        ValueOperations<String, LoginUser> operations = redisTemplate.opsForValue();
        LoginUser loginUser = operations.get(key);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return loginUser;
    }


    @Override
    public void logoutByToken(String prefix, String token) {
        if (StringUtil.isEmpty(token)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.bindSignError.getCode());
        }
        //
        delTokenRedis(token);
    }


    @Override
    public Map<String, String> register(UserDTO userDTO, HttpServletRequest request) {

        String username = userDTO.getName().trim();
        String password = userDTO.getPassword();
        String passwordTwo = userDTO.getPasswordTwo();
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(passwordTwo)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.namePwdNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(userDTO.getName()) && userDTO.getName().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!password.equals(passwordTwo)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rootPwdEqual.getCode());
        }



        /** 校验登录名重复*/
        if (!ObjectUtils.isEmpty(userDTO.getName())) {
            List<UserVo> nameList = userDao.findByName(userDTO.getName());
            if (!CollectionUtils.isEmpty(nameList)) {
                int size = nameList.size();
                if (size > 0) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_NAME.getCode());
                }
            }
        }
        Long tenantId = tenantUtil.getTenantIdByServerName();
        // 注册用户是否超出限制判断
        userManageService.checkTotalUsersLimit(tenantId);

        password = checkPassword(userDTO, request);

        String registerConfig = systemOptionDaoImpl.getSystemConfigByKey("registerConfig");

        if (ObjectUtils.isEmpty(registerConfig)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        RegisterConfigVo vo = JsonUtils.jsonToBean(registerConfig, RegisterConfigVo.class);
        if (ObjectUtils.isEmpty(vo) || !vo.getEnableRegister()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        int status = 1;
        if (vo.getNeedReview()) {
            status = 0;
        }

        com.svnlan.user.dto.UserDTO uDTO = new com.svnlan.user.dto.UserDTO();
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(PasswordUtil.passwordEncode(password, GlobalConfig.PWD_SALT));
        user.setRoleID(vo.getRoleID());
        user.setNickname("");
        user.setPhone("");
        user.setEmail("");
        user.setSizeMax(vo.getSizeMax() < 0 ? 2 : vo.getSizeMax());
        user.setSizeUse(0L);
        user.setSex(1);
        user.setDingOpenId("");
        user.setWechatOpenId("");
        user.setAlipayOpenId("");
        user.setStatus(status);
        uDTO.setTenantId(tenantId);
        uDTO.setGroupInfo(vo.getGroupInfo());
        userManageService.addUser("user register", user, uDTO);
        userManageService.doUserAdditional("user register", user, uDTO);

        if (vo.getNeedReview()) {
            Map<String, String> reMap = new HashMap<>(2);
            reMap.put("needReview", "true");
            return reMap;
        } else {
            userDTO.setIsGraphicCode(Short.valueOf("0"));
            return this.doLogin(userDTO, request);
        }
    }
}
