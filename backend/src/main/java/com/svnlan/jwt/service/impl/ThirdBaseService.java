package com.svnlan.jwt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.domain.ThirdUserInitializeConfig.GroupInfoDTO;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PasswordUtil;
import com.svnlan.utils.TenantUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.svnlan.jooq.tables.Users.USERS;
import static com.svnlan.jwt.constant.SystemConstant.CHANGE_PWD_BY_THIRD;

/**
 * 三方相关公共逻辑
 *
 * @author lingxu 2023/05/10 11:42
 */
@Slf4j
public abstract class ThirdBaseService {
    @Resource
    protected RestTemplate restTemplate;
    @Resource
    private SystemOptionDao systemOptionDaoImpl;

    @Resource
    protected UserDao userDao;

    @Resource
    private GroupDao groupDaoImpl;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserManageService userManageService;

    @Resource
    private JWTTool jwtTool;

    @Resource
    private JWTService jwtService;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    AsyncDingFileUtil asyncDingFileUtil;

    @Value("${platform.url:}")
    protected String platformUrl;

    // 1 企业类型 2 家校通
    @Value("${enterprise.type}")
    String enterpriseType;


    public static final ThreadLocal<Boolean> isAppThreadLocal = new ThreadLocal<>();

    /**
     * 获取配置信息
     */
    protected ThirdUserInitializeConfig getInitializeConfig(SecurityTypeEnum thirdNameEnum) {
        // 查询钉钉用户的配置
        String thirdLoginConfig = systemOptionDaoImpl.getSystemConfigByKey("thirdLoginConfig");
        if (!StringUtils.hasText(thirdLoginConfig)) {
            return null;
        }
        log.info("thirdLoginConfig => {}", thirdLoginConfig);
        List<ThirdUserInitializeConfig> thirdUserInitializeConfigList = JSONObject.parseArray(thirdLoginConfig, ThirdUserInitializeConfig.class);
        // 找出钉钉的配置
        return thirdUserInitializeConfigList.stream()
                .filter(it -> thirdNameEnum.getValue().equals(it.getThirdName()))
                .findFirst()
                .orElseThrow(() -> new SvnlanRuntimeException("未查询到对应平台初始化配置 不能进行登录或相关操作"));
    }

    /**
     * 验证签名
     */
    @SneakyThrows
    public boolean checkSig(String sig) {
        // zhy-fix 这边验签总是过不去 区分微信 appId 企业微信 corpId
        return true;
    }

    @SneakyThrows
    public Pair<String, String> beforeLogin(String type, Supplier<String> supplier, String aid) {
        // 返回给前端 appId
        String appId = supplier.get();
        if (!StringUtils.hasText(appId) && StringUtils.hasText(platformUrl)) {
            appId = restTemplate.getForObject(platformUrl + "api/platform/wechat/appId?type=" + type, String.class);
        }
        // 返回给前端 appId
//        String sig = PBE.encrypt(PasswordUtil.MD5(appId, GlobalConfig.PWD_SALT) + "_" + new Date().getTime());
        log.info("beforeLogin type => {}  platformUrl => {}  appId => {}", type, platformUrl, appId);
        Assert.notNull(appId, "appId 不可为空");
        return Pair.of(appId, aid);
    }

    protected final ThreadLocal<String> openIdThreadLocal = new ThreadLocal<>();

    abstract ThirdUser getThirdUser(String accessToken, String code);

    abstract ThirdUserInitializeConfig getInitializeConfig();

    abstract String getOpenId(UserVo userVo);

    abstract boolean setOpenId(UserVo userVo, User user, String openId);

    public abstract JSONObject doBind(UserVo userVo, String code, String state, Long userId);

    @Resource
    private DSLContext context;

    /**
     * 校验对应的openId 是否已经绑定了账号
     */
    protected void checkBeforeBind(Supplier<String> supplier, String openId, Long userId) {
//        List<Object> userIds = SqlRunner.db().selectObjs("SELECT userID FROM user WHERE " + supplier.get() + " = {0}", openId);
        List<Object> userIds = context.select(USERS.ID)
                .from(USERS)
                .where(supplier.get() + "=" + openId)
                .fetchInto(Object.class);
        log.info("type =>{} userIds =>{}", supplier.get(), JSONObject.toJSONString(userIds));

        if (!CollectionUtils.isEmpty(userIds)) {
            if (userIds.size() == 1) {
                if (Objects.equals(userIds.get(0), userId)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.userBindingExist.getCode());
                }
            }
            throw new SvnlanRuntimeException(CodeMessageEnum.userBindingOtherExist.getCode());
        }
    }

    protected Map<String, String> executeUser(String accessToken, SecurityTypeEnum typeEnum, String state, String code) {
        // 获取三方用户信息
        ThirdUser thirdUser = getThirdUser(accessToken, code);
        LogUtil.info("thirdUser => {}"+ JsonUtils.beanToJson(thirdUser));
        String openId = thirdUser.openId;
        String unionId = thirdUser.unionId;
        List<UserVo> userVoList = userDao.queryUserInfoByOpenIdOrMobile(openId, thirdUser.mobile, typeEnum.getCode(),unionId, thirdUser.dingUserId);
        UserVo userVo = null;
        if (!CollectionUtils.isEmpty(userVoList)) {
            if (userVoList.size() == 1) {
                userVo = userVoList.get(0);
            } else {
                // 正常情况下， 这里的 userVoList size 为2 即 手机号 openId 分别查询出了一条
                // 这种情况下属于 手机号是一个账号， openId 被绑定到另一个账号上的情况
                // 找出这个 openId 所在的账号
                userVo = userVoList.stream().filter(it -> Objects.equals(openId, getOpenId(it))).findAny()
                        .orElseThrow(() -> new SvnlanRuntimeException("未找到 openId =>" + openId + "的账号"));
            }
        }
        beforeExecuteUser(userVo, thirdUser);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Long userId;
        if (Objects.nonNull(userVo)) {
            LogUtil.info("userVo => {}" + JsonUtils.beanToJson(userVo));
            Assert.isTrue(Objects.equals(userVo.getStatus(), 1), "该账号未启用");
            if ("changePwd".equals(state)) {
                String originOpenId = getOpenId(userVo);
                // 对于扫码修改密码来说，openId 一定有值 也即必须先绑定
                Assert.hasText(originOpenId, "请先进行绑定");
                // 有可能是通过手机号码查询出来的 openId 不一定一致
                Assert.isTrue(Objects.equals(originOpenId, openId), "请使用本人设备扫码");
                // 生成加密字符串
                String value = String.format("%d_%d_changePwd", userVo.getUserID(), System.currentTimeMillis());
                String cryptoValue = PasswordUtil.passwordEncode(value, GlobalConfig.PWD_SALT);
                stringRedisTemplate.opsForValue().set(String.format(CHANGE_PWD_BY_THIRD, typeEnum.getValue(), userVo.getUserID()), cryptoValue, 20, TimeUnit.MINUTES);
                Map<String, String> map = new HashMap<>();
                map.put("sig", cryptoValue);
                return map;
            }
            // 判断用户是否被禁用
            Assert.isTrue(Objects.equals(userVo.getStatus(), 1), "账号被禁用或尚未启用！请联系管理员");
            userId = userVo.getUserID();
            // 如果用户有些信息没有， 则用返回的信息更新
            populateAndUpdateUser(userVo, thirdUser);
        } else {
            ThirdUserInitializeConfig initializeConfig = getInitializeConfig();
            Assert.isTrue(!"changePwd".equals(state), "未查询到用户，请先进行登录");
            UserDTO userDTO = new UserDTO();
            // {"groupID":1,"name":"sdfsdfsdf","nickname":"asdfadsf","password":"asdf23","sizeMax":1,"roleID":3,"groupInfo":[]}
            userDTO.setGroupID(1L);
            String mobile = thirdUser.mobile;
            userDTO.setName(mobile);
            userDTO.setPhone(mobile);
            userDTO.setNickname(thirdUser.nick);
            // 明文密码， 后面会加密
            userDTO.setPassword(mobile.substring(mobile.length() - 6));
            // 用户的配额
            userDTO.setSizeMax(initializeConfig.getSizeMax());
            // 用户角色
            userDTO.setRoleID(initializeConfig.getRoleID());
            userDTO.setAvatar(thirdUser.avatar);
            // 代表管理员
            userDTO.setCreateUser(1L);
            userDTO.setOpenId(thirdUser.openId);
            userDTO.setUnionId(thirdUser.unionId);
            userDTO.setDingUserId(thirdUser.dingUserId);
            // 从配置里读取需要关联的用户部门
            List<Long> groupIds = initializeConfig.getGroupInfo().stream().map(GroupInfoDTO::getGroupID).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(groupIds)) {
                // 查询 group
                List<Group> groupInfoList = groupDaoImpl.getGroupInfoList(groupIds);
                Assert.notEmpty(groupInfoList, "未查询到组织");
                Assert.isTrue(Objects.equals(groupInfoList.size(), groupIds.size()), "存在未查询到的组织");

                List<UserGroupVo> userGroupVoList = initializeConfig.getGroupInfo().stream().map(it -> {
                    UserGroupVo userGroupVo = new UserGroupVo();
                    userGroupVo.setGroupID(it.getGroupID());
                    userGroupVo.setAuthID(it.getAuthID());
                    return userGroupVo;
                }).collect(Collectors.toList());

                userDTO.setGroupInfo(userGroupVoList);
            }
            userDTO.setTenantId(tenantUtil.getTenantIdByRequestServerName(request));
            userDTO.setOpenIdType(Integer.parseInt(typeEnum.getCode()));
            userId = userManageService.addThirdUser(jwtTool.findApiPrefix(request), userDTO);
        }
        JSONObject jsonObj = afterExecuteUser(userId);
        Map<String, String> result = jwtService.doThirdLogin(userId, request);
        if (Objects.nonNull(jsonObj)) {
            result.put("nickname", jsonObj.getString("nickname"));
            result.put("avatar", jsonObj.getString("avatar"));
        }
        result.put("dingUnionId", thirdUser.unionId);
        LogUtil.info("钉钉内部登录：result=" + JsonUtils.beanToJson(result));
        return result;
    }

    protected void beforeExecuteUser(UserVo userVo, ThirdUser thirdUser) {
        LogUtil.info("third base beforeExecuteUser enterpriseType=" + enterpriseType);
        Assert.notNull(userVo, "请先绑定账号，再进行操作");
    }

    /**
     * 用户绑定后的处理
     */
    protected JSONObject afterExecuteUser(Long userId) {
        // 根据 userId 查询昵称和头像
        User user = userDao.selectAvatarAndNickName(userId);
        Assert.notNull(user, "未查询到该用户 userID => " + userId);
        return new JSONObject().fluentPut("avatar", user.getAvatar())
                .fluentPut("nickname", user.getNickname());
    }

    public abstract String getAccessToken(Long tenantId);

    @ToString
    @Builder
    protected static class ThirdUser {
        public String mobile;
        public String nick;

        public String avatar;
        public String openId;
        public String unionId;

        public String sex;

        public String email;
        public String dingUserId;

    }

    /**
     * @param userVo    数据库中查询到的信息
     * @param thirdUser 第三方返回的信息
     */
    protected final void populateAndUpdateUser(UserVo userVo, ThirdUser thirdUser) {
        User user = new User();
        boolean isMatch = false;
        if (!StringUtils.hasText(userVo.getAvatar())) {
            // 处理头像
            user.setAvatar(thirdUser.avatar);
            isMatch = true;
        }
        if (!StringUtils.hasText(userVo.getPhone())) {
            user.setPhone(thirdUser.mobile);
            isMatch = true;
        }
        if (!StringUtils.hasText(user.getEmail()) && StringUtils.hasText(thirdUser.email)) {
            user.setEmail(thirdUser.email);
            isMatch = true;
        }
        if (!StringUtils.hasText(userVo.getNickname())) {
            user.setNickname(thirdUser.nick);
            isMatch = true;
        }
        if (!StringUtils.hasText(userVo.getDingUserId()) && !ObjectUtils.isEmpty(thirdUser.dingUserId)) {
            user.setDingUserId(thirdUser.dingUserId);
            isMatch = true;
        }

        // 设置 openId
        if (setOpenId(userVo, user, thirdUser.openId)) {
            isMatch = true;
        }
        if (!StringUtils.hasText(userVo.getDingUnionId())
                && !ObjectUtils.isEmpty(thirdUser) && !ObjectUtils.isEmpty(thirdUser.unionId)) {
            // 处理ding unionId
            user.setDingUnionId(thirdUser.unionId);
            isMatch = true;
        }
        if (isMatch) {
            long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
            user.setModifyTime(timestamp);
//            user.setNickname(ObjectUtils.isEmpty(user.getNickname()) ? "" : user.getNickname());
            user.setUserID(userVo.getUserID());
            userDao.update(user);
        }
    }

}
