package com.svnlan.utils;

import com.svnlan.common.CheckResult;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.dao.UserJwtDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.UserRole;
import com.svnlan.jwt.dto.UserDTO;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author:
 * @Description:
 */
@Component
public class LoginUserUtil {
    private static final Logger log = LoggerFactory.getLogger("error");
    public static final ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    UserJwtDao userJwtDao;
    @Resource
    TenantUtil tenantUtil;


    @Value("${loginLimit.level2.count}")
    private Integer level2Count;

    @Value("${loginLimit.level2.interval}")
    private Integer level2Interval;

    @Value("${loginLimit.level2.denyInterval}")
    private Integer level2DenyInterval;

    //验证码有效期10分钟
    private final static long codeTTL = 600;

    public Long getLoginUserId() {
        return Optional.ofNullable(getLoginUser()).orElseThrow(() -> new SvnlanRuntimeException("未获取到登录用户")).getUserID();
    }

    public Long getLoginUserId(boolean required) {
        if (required) {
            return getLoginUserId();
        } else {
            return Optional.ofNullable(getLoginUser()).orElseGet(() -> {
                // 表示匿名用户
                LoginUser loginUser = new LoginUser();
                loginUser.setUserID(0L);
                return loginUser;
            }).getUserID();
        }
    }

    /**
     * @Description: 获取当前用户登录信息
     * @params: []
     * @Return: com.svnlan.jwt.domain.LoginUser
     * @Modified:
     */
    public LoginUser getLoginUser(Map<String, String> map) {
        //log.debug("getLoginUser map => {}", map);
        LoginUser loginUser = new LoginUser();
        String token = map.get("token");
        CheckResult checkResult = JwtUtils.validateJWT(token, false);
        //log.debug("checkResult => {}", JsonUtils.beanToJson(checkResult));
        if (checkResult.isSuccess()) {
            //取出userID
            Map<String, Object> claimsMap = checkResult.getClaims();
            if (claimsMap.get("userID") == null) {
                throw new SvnlanRuntimeException(CodeMessageEnum.bindSignError.getCode());
            }
            Long userID = Long.valueOf(claimsMap.get("userID").toString());
            //拼接key
            String key = GlobalConfig.JWT_REDIS_PREFIX + "_"
                    + userID + "_"
                    + token;
            //查询token记录
            ValueOperations<String, LoginUser> operations = redisTemplate.opsForValue();
            try {
                loginUser = operations.get(key);
            }catch (Exception e){
                loginUser = null;
                LogUtil.error(e, " 序列化出错");
            }
            if (loginUser == null) {
                //todo 查数据库
                loginUser = userJwtDao.findByUserId(userID);
                if (ObjectUtils.isEmpty(loginUser)){
                    LogUtil.error("getLoginUser is null userID is error map=" + JsonUtils.beanToJson(map) + "， checkResult=" + JsonUtils.beanToJson(checkResult));
                    return null;
                }
                loginUser.setUserType(3);
                // 获取权限
                UserRole val = userJwtDao.getUserIdentity(loginUser.getUserID());
                if (!ObjectUtils.isEmpty(val)) {
                    if (!ObjectUtils.isEmpty(val.getAdministrator()) && 1 == val.getAdministrator()) {
                        loginUser.setUserType(1);
                    } else if (!ObjectUtils.isEmpty(val.getAuth()) && val.getAuth().indexOf("admin") >= 0) {
                        loginUser.setUserType(2);
                    }
                }
                LogUtil.error("getLoginUser is null map=" + JsonUtils.beanToJson(map) + "， checkResult=" + JsonUtils.beanToJson(checkResult)
                        + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            }

            if (ObjectUtils.isEmpty(loginUser.getTenantId())){
                loginUser.setTenantId(tenantUtil.getTenantIdByUserId(userID));
            }

            log.info("key => {}", key + "，loginUser => {}", JsonUtils.beanToJson(loginUser));
            //续命
            Long ttl = Boolean.TRUE.equals(loginUser.getSaveCookie()) ? SystemConstant.JWT_REDIS_LONG_TTL : SystemConstant.JWT_REDIS_TTL;

            operations.set(key, loginUser, ttl, TimeUnit.MILLISECONDS);
            // Long ttl = SystemConstant.JWT_REDIS_LONG_TTL;
            //redisTemplate.expire(key, ttl, TimeUnit.MILLISECONDS);
        } else {
            LogUtil.error("getLoginUser not success  map=" + JsonUtils.beanToJson(map) + "， checkResult=" + JsonUtils.beanToJson(checkResult));
        }
        return loginUser;
    }

    public LoginUser getLoginUser() {
        LoginUser loginUser = threadLocal.get();
        if (Objects.isNull(loginUser)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return this.getLoginUser(request);
        }
        return loginUser;
    }

    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(SystemConstant.JWT_TOKEN);
        //log.debug("ua=" + request.getHeader("User-Agent") + "token => {}", token);
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("serverName", this.getServerName());

        LoginUser loginUser = getLoginUser(map);
        if (loginUser == null) {
            return null;
        }
        return loginUser;
    }

    public LoginUser getLoginUser(String serverName, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("serverName", serverName);
        LoginUser loginUser = getLoginUser(map);
        if (loginUser == null) {
            return null;
        }
        return loginUser;
    }

    public LoginUser getLoginUserByToken(HttpServletRequest request, String token) {
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("serverName", this.getServerName(request));
        LoginUser loginUser = getLoginUser(map);
        if (loginUser == null) {
            return null;
        }
        return loginUser;
    }


    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * @Description: 获取域名
     * @params: []
     * @Return: java.lang.String
     * @Modified:
     */
    public String getServerName(HttpServletRequest request) {

        //先从attribute取
        if (request.getAttribute("targetServerNameForOverride") != null) {
            return request.getAttribute("targetServerNameForOverride").toString();
        }
        //再从param取
        String targetServerName = request.getParameter("targetServerNameForOverride");
        //param没有才用host的值
        String serverName = StringUtil.isEmpty(targetServerName) ? request.getServerName() : targetServerName;
        //写到attribute
        request.setAttribute("targetServerNameForOverride", serverName);
        return serverName;
    }

    public String getServerName() {
        HttpServletRequest request = this.getRequest();
        return getServerName(request);
    }


    /**
     * @Description: 记录错误登录
     * @params: [request, userDTO]
     * @Return: java.util.Map<java.lang.String, java.lang.String>
     * @Modified:
     */
    public Map<String, String> doAccountWrong(HttpServletRequest request, UserDTO userDTO) {
        try {
            String ip = IpUtil.getIp(request);
            String key = getWrongCountKey(IpUtil.getIp(request), userDTO.getName());
            stringRedisTemplate.opsForZSet().add(key,
                    userDTO.getName() + "_" + userDTO.getPassword() + "_" + System.currentTimeMillis(),
                    (double) System.currentTimeMillis());
            stringRedisTemplate.expire(key, 86400, TimeUnit.SECONDS);
            Map<String, String> wrongMap = new HashMap<>();
            //错误历史
            Set<String> wrongList = getWrongList(ip, userDTO.getName());
            Integer wrongCount = CollectionUtils.isEmpty(wrongList) ? 1 : wrongList.size();
            wrongMap.put("limitCount", level2Count.toString());
            wrongMap.put("wrongCount", wrongCount.toString());
            //达到限制标准
            if (wrongCount.equals(level2Count)) {
                wrongMap.put("errCode", CodeMessageEnum.passwordErrorLock.getCode());
                wrongMap.put("denyTime", level2DenyInterval.toString());
                return wrongMap;
            }
            //错误的key管理列表
            stringRedisTemplate.opsForSet().add("loginWrongKeys", key);

            return wrongMap;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> checkAccountWrong(HttpServletRequest request, UserDTO userDTO) {
        Map<String, String> checkMap = new HashMap<>();
        String ip = IpUtil.getIp(request);

        //获取禁止登录情况
        String denyKey = getDenyKey(ip, userDTO.getName());
        String denyString = stringRedisTemplate.opsForValue().get(denyKey);
        //暂时禁止登录了, 返回4012
        if ("1".equals(denyString)) {
            checkMap.put("errCode", CodeMessageEnum.passwordErrorLock.getCode());
            checkMap.put("denyTime", level2DenyInterval.toString());
            checkMap.put("limitCount", level2Count.toString());
            return checkMap;
        }


        //查询级别2时间内的条数, 查询上限为level2的数量
        Set<String> recordList = getWrongList(ip, userDTO.getName());
        if (CollectionUtils.isEmpty(recordList)) {
            return null;
        }

        //达到了level2的数量, 直接限制登录
        if (level2Count.equals(recordList.size())) {
            stringRedisTemplate.opsForValue().set(denyKey, "1", level2DenyInterval, TimeUnit.SECONDS);
            checkMap.put("errCode", CodeMessageEnum.passwordErrorLock.getCode());
            checkMap.put("denyTime", level2DenyInterval.toString());
            checkMap.put("limitCount", level2Count.toString());
            return checkMap;
        }

        return null;
    }

    private Set<String> getWrongList(String ip, String username) {
        String key = getWrongCountKey(ip, username);
        Long currentTime = System.currentTimeMillis();
        Long compareTime = currentTime - level2Interval * 1000;
        Set<String> recordList = stringRedisTemplate.opsForZSet().rangeByScore(key, compareTime, currentTime, 0, level2Count);

        return recordList;
    }

    private String getVerifyCodeKey(String ip, String code) {
        return "loginVerify_" + ip + "_" + code;
    }


    private String getDenyKey(String ip, String username) {

        return "loginDenyIp_" + ip + "_" + username;
    }


    private String getWrongCountKey(String ip, String username) {
        return "loginWrongRecord_" + ip + "_" + username;
    }


    /**
     * @Description: 短时间内, 同ip, 同账号密码登录, 返回上一次登录的token
     * @params: [request, username, password]
     * @Return: java.util.Map<java.lang.String, java.lang.String>
     * @Modified:
     */
    public Map<String, String> checkFastLogin(HttpServletRequest request, String username, String password) {
        String key = getFastLoginKey(request, username, password, getServerName(request));
        //查找之前存的token , 带前缀
        String tokenKey = stringRedisTemplate.opsForValue().get(key);
        if (tokenKey == null) {
            return null;
        }
        //token 是否还在
        if (redisTemplate.opsForValue().get(tokenKey) != null) {
            String[] tokenArr = tokenKey.split("_");
            String token = tokenKey.replace(GlobalConfig.JWT_REDIS_PREFIX
                    + "_" + tokenArr[1] + "_", "");
            return new HashMap<String, String>() {{
                put("token", token);
                put("userID", tokenArr[1]);
                put("fl", "1");
            }};
        }
        return null;
    }

    /**
     * @Description: 拼接短时间重复登录的key, ip+账号+密码
     * @params: [request, username, password]
     * @Return: java.lang.String
     * @Modified:
     */
    private String getFastLoginKey(HttpServletRequest request, String username, String password, String serverName) {
        String ip = IpUtil.getIp(request);
        return GlobalConfig.JWT_FAST_LOGIN_KEY_PRE + ip + "_" + serverName + "_" + username + "_" + password;
    }

    /**
     * @Description: ip + 账号为key, 存储token
     * @params: [request, username, password, tokenMap]
     * @Return: void
     * @Modified:
     */
    public void addLoginInfo(HttpServletRequest request, String username, String password, Map<String, String> tokenMap) {
        try {
            //ip加账号
            String key = getFastLoginKey(request, username, password, getServerName(request));
            String token = tokenMap.get("token");
            String userIdStr = tokenMap.get("userID");
            //token, 带前缀
            String tokenKey = getTokenKey(token, userIdStr);
            stringRedisTemplate.opsForValue().set(key, tokenKey, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LogUtil.error(e, "添加token,账号信息失败");
        }
    }

    private String getTokenKey(String token, String userIdStr) {
        return GlobalConfig.JWT_REDIS_PREFIX + "_"
                + userIdStr + "_"
                + token;
    }

    public LoginUser getAccessToken(String accessToken) {
        CheckResult checkResult = JwtUtils.validateJWT(accessToken);
        Claims claims = checkResult.getClaims();
//        claims.get
//        LoginUser loginUser = JsonUtils.jsonToBean(json, LoginUser.class);
//        return loginUser;
        return null;
    }

    public void setLoginUserAboutUaInfo(HttpServletRequest request, LoginUser loginUser) {
        try {
            loginUser.setUserAgent(ObjectUtils.isEmpty(loginUser.getUserAgent()) ? request.getHeader("User-Agent") : loginUser.getUserAgent());
            loginUser.setServerName(ObjectUtils.isEmpty(loginUser.getServerName()) ? this.getServerName(request) : loginUser.getServerName());
            loginUser.setIp(ObjectUtils.isEmpty(loginUser.getIp()) ? IpUtil.getIp(request) : loginUser.getIp());
        }catch (Exception e){
            LogUtil.error(e, "获取ua、ip相关信息失败");
        }
    }

    public Long getTenantIdByServerName(){
        return this.getTenantIdByServerName(null);
    }
    public Long getTenantIdByServerName(String serverName){
        return tenantUtil.getTenantIdByServerName(serverName);
    }
}
