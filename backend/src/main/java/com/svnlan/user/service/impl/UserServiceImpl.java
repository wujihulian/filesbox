package com.svnlan.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.*;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.constant.SystemConstant;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.domain.SystemLog;
import com.svnlan.jwt.dto.ClearTokenDTO;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.jwt.service.impl.ThirdBaseService;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserOptionDao;
import com.svnlan.user.domain.Email;
import com.svnlan.user.domain.UserOption;
import com.svnlan.user.dto.LoginParamDTO;
import com.svnlan.user.dto.ScanLoginQRDTO;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.MailService;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.service.UserService;
import com.svnlan.user.tools.UserTool;
import com.svnlan.user.vo.LoginQRVO;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.svnlan.enums.SecurityTypeEnum.deriveEnum;
import static com.svnlan.jwt.constant.SystemConstant.CHANGE_PWD_BY_THIRD;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 11:11
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService, ApplicationContextAware {
    @Resource
    UserOptionDao userOptionDaoImpl;
    @Resource
    UserOptionDao userOptionDao;
    @Resource
    UserDao userDao;
    @Resource
    UserDao userDaoImpl;
    @Resource
    UserTool userTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemOptionDao systemOptionDaoImpl;
    @Resource
    MailService mailService;
    @Resource
    JWTService jwtService;
    @Resource
    TenantUtil tenantUtil;

    @Resource
    private UserManageService userManageService;

    @Value("${verification.expiried}")
    private int expiriedTime;

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = applicationContext;
    }

    @Override
    public void setUserOption(String prefix, LoginUser loginUser, UserDTO userOption) {
        if (ObjectUtils.isEmpty(userOption.getKey())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!UserOptionEnum.contains(userOption.getKey())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), userOption.getKey(), ObjectUtils.isEmpty(userOption.getValue()) ? "" : userOption.getValue());
        } catch (Exception e) {
            LogUtil.error(e, prefix + " setUserOption error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }

    @Override
    public void setUserInfo(String prefix, LoginUser loginUser, UserDTO userOption) {
        if (ObjectUtils.isEmpty(userOption.getKey())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long tenantId = null;
        switch (userOption.getKey()) {
            case "nickname":
                if (ObjectUtils.isEmpty(userOption.getValue())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (userOption.getValue().trim().length() > 255) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                int n = userDao.findByNickname(userOption.getValue().trim());
                if (n > 0) {
                    LogUtil.error("setUserInfo nickname : 昵称已存在 ： >> " + userOption.getValue());
                    throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_NICKNAME.getCode());
                }
                try {
                    userDao.updateNickname(userOption.getValue(), loginUser.getUserID());
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updateNickname error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "sex":
                if (ObjectUtils.isEmpty(userOption.getValue())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (!Arrays.asList("0", "1").contains(userOption.getValue())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                try {
                    userDao.updateSex(userOption.getValue(), loginUser.getUserID());
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updateSex error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "avatar":
                if (ObjectUtils.isEmpty(userOption.getValue())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (userOption.getValue().trim().length() > 255) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                try {
                    userDao.updateAvatar(userOption.getValue(), loginUser.getUserID());
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updateAvatar error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "email":
                if (ObjectUtils.isEmpty(userOption.getValue())) { // || ObjectUtils.isEmpty(userOption.getCode())
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (userOption.getValue().trim().length() > 255) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                /*if (!this.checkCode(FindTypeEnum.EMAIL.getCode() + "_" + SendOperateTypeEnum.BINDING.getCode(), userOption.getCode(), userOption.getValue(),  true)) {
                    LogUtil.error("setUserEmail: 绑定邮箱 >> value=" + JsonUtils.beanToJson(userOption));
                    //判断参数是否传入
                    throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
                }*/
                try {
                    userDao.updateEmail(userOption.getValue(), loginUser.getUserID());
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updateEmail error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "password":

                if (userOption.getValue().trim().length() > 100) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }

                boolean updatePwdState = false;
                if (StringUtils.hasText(userOption.getSig())) {
                    // 是否三方扫码修改密码的
                    checkThirdScanSig(userOption.getOpenIdType(), userOption.getSig(), loginUser.getUserID());
                } else {
                    if (ObjectUtils.isEmpty(userOption.getValue()) ) {
                        throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                    }
                    // 随机密码提示修改密码
                    OptionVo passwordState = userOptionDao.getUserConfigVoByKey(loginUser.getUserID(),"passwordState");
                    if (ObjectUtils.isEmpty(passwordState) || ObjectUtils.isEmpty(passwordState.getValueText())
                            || "1".equals(passwordState.getValueText())){
                        // 普通修改密码方式
                        if (ObjectUtils.isEmpty(userOption.getPassword())) {
                            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                        }
                        // 解析出原密码
                        String originPassword = decodePassword(userOption.getPassword(), loginUser.getUserID());
                        // 原先加过密的密码
                        String oldCryptoPassword = PasswordUtil.passwordEncode(originPassword, GlobalConfig.PWD_SALT);
                        UserVo userVo = userDao.getUserSimpleInfo(loginUser.getUserID());
                        if (ObjectUtils.isEmpty(userVo)) {
                            log.warn("userId => {} 不存在", loginUser.getUserID());
                        }
                        if (ObjectUtils.isEmpty(userVo) || !oldCryptoPassword.equals(userVo.getPassword())) {
                            throw new SvnlanRuntimeException(CodeMessageEnum.oldPwdError.getCode());
                        }
                    }else {
                        updatePwdState = true;
                    }
                }
                // 验证密码强度
                userManageService.checkPasswordSecureLevel(userOption.getValue());
                try {
                    // 解密前端传递过来的密码
                    String newPassword = decodePassword(userOption.getValue(), loginUser.getUserID());
                    userDao.updatePassword(PasswordUtil.passwordEncode(newPassword, GlobalConfig.PWD_SALT), loginUser.getUserID());
                    if (updatePwdState) {
                        userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), "passwordState", "1");
                    }
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updateEmail error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                // clear token 清除了Redis中被修改密码用户的所有token.
                ClearTokenDTO clearTokenDTO = new ClearTokenDTO();
                clearTokenDTO.setUserID(loginUser.getUserID());
                jwtService.clearToken(clearTokenDTO);
                break;
            case "phone":
                if (ObjectUtils.isEmpty(userOption.getValue())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (userOption.getValue().trim().length() > 20) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                List<UserVo> list = userDao.findByPhone(userOption.getValue().trim());
                if (!CollectionUtils.isEmpty(list) && list.size() > 0) {
                    LogUtil.error("setUserInfo nickname : 手机号已存在 ： >> " + userOption.getValue());
                    throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_PHONE.getCode());
                }
                try {
                    userDao.updatePhone(userOption.getValue(), loginUser.getUserID());
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " setUserInfo updatePhone error " + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "desktopBackgroundImg":

                if (!ObjectUtils.isEmpty(userOption.getValue()) && userOption.getValue().trim().length() > 1000) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
                userOption.setValue(ObjectUtils.isEmpty(userOption.getValue()) ? "" : userOption.getValue());
                tenantId = tenantUtil.getTenantIdByServerName();
                OptionVo backImgVo = userOptionDaoImpl.getUserConfigVoByKey(loginUser.getUserID(), "desktopBackgroundImg");
                try {
                    if (ObjectUtils.isEmpty(backImgVo)) {
                        List<UserOption> paramList = new ArrayList<>();
                        paramList.add(new UserOption(loginUser.getUserID(), "", "desktopBackgroundImg", userOption.getValue(),tenantId));
                        userOptionDao.batchInsert(paramList);
                    } else {
                        userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), "desktopBackgroundImg", userOption.getValue());
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " setUserInfo desktopBackgroundImg "+ (ObjectUtils.isEmpty(backImgVo) ? "insert" : "update" ) +" error userOption=" + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            case "fileRepeat":
                if (!ObjectUtils.isEmpty(userOption.getValue()) && userOption.getValue().trim().length() > 1000) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }

                tenantId = tenantUtil.getTenantIdByServerName();
                OptionVo fileRepeat = userOptionDaoImpl.getUserConfigVoByKey(loginUser.getUserID(), "fileRepeat");
                try {
                    if (ObjectUtils.isEmpty(fileRepeat)) {
                        List<UserOption> paramList = new ArrayList<>();
                        paramList.add(new UserOption(loginUser.getUserID(), "", "fileRepeat", userOption.getValue(),tenantId));
                        userOptionDao.batchInsert(paramList);
                    } else {
                        userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), "fileRepeat", userOption.getValue());
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " setUserInfo fileRepeat " + (ObjectUtils.isEmpty(fileRepeat) ? "insert" : "update" ) + "error userOption=" + JsonUtils.beanToJson(userOption) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                    throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
                }
                break;
            default:
                break;
        }
    }

    private String decodePassword(String password, Long userId) {
        com.svnlan.jwt.dto.UserDTO userDTO = new com.svnlan.jwt.dto.UserDTO();
        userDTO.setPassword(password);
        userDTO.setName(userId.toString());
        return jwtService.checkPassword(userDTO);
    }

    @Resource
    private Environment environment;

    private void checkThirdScanSig(Integer openIdType, String sig, Long userId) {
        SecurityTypeEnum securityTypeEnum = deriveEnum(openIdType);
        Assert.notNull(securityTypeEnum, "openIdType 不合法");
        // 扫码修改密码
        String key = String.format(CHANGE_PWD_BY_THIRD, securityTypeEnum.getValue(), userId);
        String sigStr = stringRedisTemplate.opsForValue().get(key);
        Assert.notNull(sigStr, "请先进行扫码确认");
        Assert.isTrue(Objects.equals(sigStr, sig), "扫码修改密码验证失败");
        if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            // 不是我本地环境的话，就直接删除掉
            stringRedisTemplate.delete(key);
        }
    }


    @Override
    public Boolean checkEmailCode(UserDTO optionDTO) {
        return userTool.checkEmailCode(optionDTO, false);
    }

    public Boolean checkCode(String type, String code, String detail, boolean check) {

        return userTool.checkCode(type, code, detail, check);
    }

    /**
     * @Description: 登录用户绑定邮箱发送验证码
     * @params: [email]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    @Override
    public Boolean sendBindEmailCode(String email, LoginUser loginUser, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(email)) {
            LogUtil.error("sendBindEmailCode() : 参数错误 >> " + email);
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        int n = userDao.findByEmail(email);
        if (n > 0) {
            //邮箱已存在，不能再次绑定
            LogUtil.error("sendBindEmailCode() : 邮箱已存在，不能再次绑定： >> " + email);
            throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_EMAIL.getCode());
        }

        //发送绑定邮件频率限制
        String prefix = String.format("@日志@/sendBindEmailCode(%s)>>>", RandomUtil.getuuid());
        this.userTool.validateBindEmailSendLimit(prefix, loginUser.getUserID(), email);

        return this.sendVerificationCode(FindTypeEnum.EMAIL.getCode(), email, loginUser, SendOperateTypeEnum.BINDING.getCode(), null, request);
    }

    @Override
    public Boolean sendVerificationCode(String type, String detail, LoginUser loginUser, String operateType, String module, HttpServletRequest request) {
        String code = StringUtil.verifyCode();
        String key = getKey(type + "_" + operateType, detail);

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String token = operations.get(key);
        LogUtil.info("sendVerificationCode key=" + key + ", code=" + code + "，token=" + token);

        // 邮箱
        if ("2".equals(type)) {
            if (StringUtil.isEmpty(token)) {
                //新增验证码记录
                token = addRecord(type, detail, code, operateType, loginUser);
            }
            // 发送短息功能
            if (StringUtil.isEmpty(token)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.sendFail.getCode());
            }
        }

        // 手机
        if ("1".equals(type)) {
            // return sendPhoneCode(detail, loginUser, code, module, request);
        } else if ("2".equals(type)) {
            // 邮箱
            return sendEmailCode(detail, code);
        }
        return false;
    }

    private String addRecord(String type, String detail, String code, String operateType, LoginUser loginUser) {
        String key = getKey(type + "_" + operateType, detail);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        // 插入缓存
        operations.set(key, code, expiriedTime, TimeUnit.MINUTES);
        return code;
    }

    /**
     * @Description: 生成key
     * @params: [type (1手机，2邮箱), detail, platformId]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Modified:
     */
    private String getKey(String type, String detail) {
        return userTool.getKey(type,detail);
    }


    private Boolean sendEmailCode(String detail, String code) {
        try {
            String emailTitle = "【" + systemOptionDaoImpl.getSystemConfigByKey("systemName") + "】";
            String emailContent = I18nUtils.tryI18n(CodeMessageEnum.emailCodeText.getCode()) + code;
            Email email = new Email();
            email.setEmail(detail);
            email.setSubject(emailTitle);
            email.setContent(emailTitle + emailContent);
            mailService.send(email);
        } catch (Exception e) {
            LogUtil.error(e, "sendVerificationCode sendEmailCode 发送邮箱error");
        }
        return true;
    }

    /**
     * 获取用户使用的总空间大小
     */
    @Override
    public Long sumUserSpaceUsed() {
        // 单位是 byte
        return userDao.sumUserSpaceUsed();
    }

    @Override
    public Long selectUserCount() {
        return userDao.selectUserCount();
    }

    @Override
    public String getPasswordByUserName(String username) {
        return userDao.getPasswordByUserName(username);
    }

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private SystemLogDao systemLogDao;

    /**
     * 微信绑定
     *
     * @param code  授权码
     * @param state
     * @param sig   签名
     * @param type  2 微信 7 企业微信 @see {@link SecurityTypeEnum}
     * @return
     */
    @Override
    public JSONObject bindWechat(String code, String state, String sig, String type) {
        String value = SecurityTypeEnum.getValue(type);
        // 是否为移动端
        boolean isApp = false;
        if (Objects.nonNull(value) && value.endsWith("App")) {
            value = value.replace("App", "");
            isApp = true;
        }
        ThirdBaseService.isAppThreadLocal.set(isApp);
        ThirdBaseService thirdBaseService = (ThirdBaseService) appContext.getBean(String.format("%sServiceImpl", value));
        Assert.isTrue(thirdBaseService.checkSig(sig), "签名不正确");
        // 登录用户信息
        LoginUser loginUser = loginUserUtil.getLoginUser();
        // 当前登录id
        Long userId = loginUser.getUserID();
        // 判断用户是否已经绑定了
        UserVo userVo = userDao.queryUserByUserId(userId);
        Assert.notNull(userVo, "未查询到当前用户");
        Assert.isTrue(Objects.equals(userVo.getStatus(), 1), "当前用户未启用");

        try {
            return thirdBaseService.doBind(userVo, code, state, userId);
        } finally {
            ThirdBaseService.isAppThreadLocal.remove();
        }
    }

    public void wechatUnbind(String type) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        log.info("开始解绑微信 ....");
        String value = SecurityTypeEnum.getValue(type);
        Assert.hasText(value, "未查询到具体平台，请确认 type 值");
        if (value.endsWith("App")) {
            value = value.replace("App", "");
        }
        Assert.isTrue(Objects.equals(userDao.removeOpenId(loginUser.getUserID(), value), 1), "微信解绑失败");
        // 写操作日志
//        logWithBind(loginUser, LogTypeEnum.wechatUnbind);
    }

    private void logWithBind(LoginUser loginUser, LogTypeEnum logTypeEnum) {
        SystemLog record = new SystemLog();
        record.setCreateTime(System.currentTimeMillis());
        record.setSessionID("");
        record.setUserID(loginUser.getUserID());
        record.setType(logTypeEnum.getCode());

        Map<String, Object> rMap = new HashMap<>(6);
        rMap.put("ua", loginUser.getUserAgent());
        rMap.put("ip", loginUser.getIp());
        //0 其他 1 电信，2 移动，3 联通，4 网通，5 铁通，6 华数，7 教育网
        rMap.put("network", "");
        rMap.put("country", "");
        rMap.put("osName", "");
        record.setDesc(JsonUtils.beanToJson(rMap));
        record.setVisitDate(new Date());

        LogUtil.info("systemLogDao insert 插入数据信息:" + JsonUtils.beanToJson(record));
        systemLogDao.insert(record);
    }

    @Override
    public LoginQRVO findLoginAuthUrl(LoginParamDTO paramDTO) {
        // /pages/download.html
        String qrUrl = HttpUtil.getRequestRootUrl(null) + GlobalConfig.logAuthUrl;
        Long timestamp = paramDTO.getTimestamp();
        String signature = paramDTO.getSignature();
        String clientType = paramDTO.getClientType();
        if (StringUtil.isEmpty(clientType)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ScanLoginMsgTypeEnum.TV_SCAN_LOGIN.getCode().equals(clientType)
                && !ScanLoginMsgTypeEnum.WEB_SCAN_LOGIN.getCode().equals(clientType)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        if (ObjectUtils.isEmpty(timestamp) || StringUtil.isEmpty(signature)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //解密
        String decryptJson = AESUtil.aes(signature, GlobalConfig.ScanLoginSalt, Cipher.DECRYPT_MODE);
        if (StringUtil.isEmpty(decryptJson)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        //校验签名
        LoginParamDTO loginParamDTO = null;
        try {
            loginParamDTO = JsonUtils.jsonToBean(decryptJson, LoginParamDTO.class);
        } catch (Exception e) {
            throw new SvnlanRuntimeException(CodeMessageEnum.SING_INVALID.getCode());
        }
        Long decryptTimestamp = loginParamDTO.getTimestamp();
        if (!timestamp.equals(decryptTimestamp)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.SING_INVALID.getCode());
        }
        long currentTimestamp = System.currentTimeMillis();
        long diff = Math.abs(currentTimestamp - timestamp);
        if (diff > GlobalConfig.scanLoginSignatureExpire * 60 * 1000) {
            throw new SvnlanRuntimeException(CodeMessageEnum.SING_INVALID.getCode());
        }

        String key = RandomUtil.getuuid();
        //TV
        if (ScanLoginMsgTypeEnum.TV_SCAN_LOGIN.getCode().equals(clientType)) {
            key += "t";
        } else { //web
            key += "w";
        }
        qrUrl += "?key=" + key;

        String roomName = key;
        String redisKey = GlobalConfig.SCAN_LOGIN_CODE_REDIS_KEY + roomName;
        ValueOperations valueOperations = this.stringRedisTemplate.opsForValue();
        ScanLoginQRDTO scanLoginQRDTO = new ScanLoginQRDTO();
        scanLoginQRDTO.setRoomName(roomName);
        scanLoginQRDTO.setState("0");
        scanLoginQRDTO.setGmtCreate(new Date());
        valueOperations.set(redisKey, JsonUtils.beanToJson(scanLoginQRDTO), GlobalConfig.scanLoginCodeExpire, TimeUnit.MINUTES);

        LoginQRVO loginQRVO = new LoginQRVO();
        loginQRVO.setQrUrl(qrUrl);
        return loginQRVO;
    }

    @Override
    public LoginQRVO scanLogin(String prefix, LoginParamDTO paramDTO, HttpServletRequest request, HttpServletResponse response) {
        String tempAuth = paramDTO.getTempAuth();
        ValueOperations valueOperations = this.stringRedisTemplate.opsForValue();

        paramDTO.setSourceDomain(HttpUtil.getRequestRootUrl(null));
        Object tokenObj = valueOperations.get(tempAuth);
        if (null == tokenObj) {
            throw new SvnlanRuntimeException(CodeMessageEnum.TEMP_AUTH_INVALID.getCode(),
                    CodeMessageEnum.TEMP_AUTH_INVALID.getMsg());
        }
        LoginQRVO tempAuthDTO = JsonUtils.jsonToBean(StringUtil.changeNullToEmpty(tokenObj), LoginQRVO.class);
        String token = tempAuthDTO.getToken();
        //由token拿对应用户信息
        LoginUser loginUser = this.loginUserUtil.getLoginUserByToken(request, token);
        if (null == loginUser || loginUser.getUserID().longValue() == 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.loginError.getCode());
        }

        Long targetUserId = loginUser.getUserID();

        //
        UserVo user = this.userDaoImpl.getUserInfo(targetUserId);

        //校验临时授权码
        String roomName = tempAuthDTO.getRoomName();
        String redisKey = GlobalConfig.SCAN_LOGIN_CODE_REDIS_KEY + roomName;
        Object qrCodeObj = valueOperations.get(redisKey);
        if (null == qrCodeObj) {
            throw new SvnlanRuntimeException(CodeMessageEnum.TEMP_AUTH_INVALID.getCode(), "临时授权码失效");
        }
        ScanLoginQRDTO scanLoginQRDTO = JsonUtils.jsonToBean(StringUtil.changeNullToEmpty(qrCodeObj), ScanLoginQRDTO.class);
        String useState = scanLoginQRDTO.getState();
        //校验同一时间同一二维码（房间号）是否正在被使用，后一个先置失败
        //若有账号登录成功过，则此次置为失败
        if ("1".equals(useState)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.QR_INVALID.getCode(), CodeMessageEnum.QR_INVALID.getMsg());
        }

        //调用登录服务接口进行登录
        Map<String, String> tokenMap = jwtService.doLoginByService(loginUser);
        if (ObjectUtils.isEmpty(tokenMap) || !tokenMap.containsKey("token") || ObjectUtils.isEmpty(tokenMap.get("token"))) {
            throw new SvnlanRuntimeException(CodeMessageEnum.TEMP_AUTH_INVALID.getCode(), "临时授权码失效");
        }
        String tokenDto = tokenMap.get("token");

        tempAuthDTO.setToken(tokenDto);
        Cookie sCookie = new Cookie("token", tokenDto);
        sCookie.setPath("/");
        sCookie.setMaxAge(-1);
        response.addCookie(sCookie);

        //更新二维码对应内容信息
        Date gmtCreate = scanLoginQRDTO.getGmtCreate();
        scanLoginQRDTO.setState("1");
        scanLoginQRDTO.setUserId(user.getUserID());
        Long currentML = System.currentTimeMillis();
        //剩余有效期
        Long remainExpire = gmtCreate.getTime() + GlobalConfig.scanLoginCodeExpire * 60 * 1000 - currentML;
        valueOperations.set(redisKey, JsonUtils.beanToJson(scanLoginQRDTO), remainExpire, TimeUnit.MILLISECONDS);

        return tempAuthDTO;
    }

    public String loginByService(UserVo user, HttpServletRequest request, HttpServletResponse response) {

        com.svnlan.jwt.dto.UserDTO userDTO = new com.svnlan.jwt.dto.UserDTO();
        userDTO.setIsGraphicCode(Short.valueOf("0"));
        userDTO.setName(user.getName());
        userDTO.setPassword(PasswordUtil.passwordEncode(user.getPassword(), GlobalConfig.PWD_SALT));
        Map<String, String> tokenMap = jwtService.doLogin(userDTO, request);

        if (tokenMap != null && tokenMap.containsKey("token")) {
            // 成功
        } else if (tokenMap != null && tokenMap.containsKey("errCode")) {
            throw new SvnlanRuntimeException(tokenMap.get("errCode"));
        } else if (tokenMap != null && tokenMap.containsKey("wrongCount")) {
            throw new SvnlanRuntimeException(tokenMap.get("wrongCount"));
        } else {
            LogUtil.error("loginByService 登录失败 else, " + JsonUtils.beanToJson(userDTO) );
            throw new SvnlanRuntimeException(CodeMessageEnum.pwdError.getCode());
        }
        Cookie cookie = new Cookie(SystemConstant.LOGIN_COOKIE_KEY, String.valueOf(System.currentTimeMillis()));
        cookie.setPath("/");
        response.addCookie(cookie);
        return tokenMap.get("token");
    }

}
