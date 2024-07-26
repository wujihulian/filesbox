package com.svnlan.jwt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.DingUtil;
import com.svnlan.jwt.service.DingService;
import com.svnlan.jwt.third.dingding.DingTalkResult;
import com.svnlan.jwt.third.dingding.UserInfo;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.taobao.api.ApiException;
import com.alibaba.fastjson.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.svnlan.jwt.constant.SystemConstant.ACCESS_TOKEN_KEY;

/**
 * 钉钉相关
 *
 * @author lingxu 2023/04/20 15:57
 */
@Slf4j
@Service
public class DingServiceImpl extends ThirdBaseService implements DingService {
    @Autowired
    OptionTool optionTool;
    @Resource
    SystemOptionDao systemOptionDao;

    public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }

    public static com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public static final String REQUEST_HOST = "https://oapi.dingtalk.com/topapi";

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @SneakyThrows
    public Map<String, String> checkAndExecuteUserLogin(String accessToken, String state) {
        return executeUser(accessToken, SecurityTypeEnum.DING_DING, state, null);
    }

    @SneakyThrows
    @Override
    ThirdUser getThirdUser(String accessToken, String code) {
        if (StringUtils.hasText(code)) {
            // 企业内部应用免登
            // 获取用户id
            String userId = getUserId(code, accessToken);
            log.info("userId => {}", userId);
            // 获取用户信息
            UserInfo userInfo = getUserInfo(userId, accessToken);
            log.info("userInfo => {}", userInfo);
            return new ThirdUser.ThirdUserBuilder()
                    .avatar(userInfo.getAvatar())
                    .mobile(userInfo.getMobile())
                    .email(userInfo.getEmail())
                    .unionId(userInfo.getUnionId())
                    .unionId(userId)
                    .nick(userInfo.getName()).build();

        } else {
            // 三方登录
            com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
            GetUserHeaders getUserHeaders = new GetUserHeaders();
            getUserHeaders.xAcsDingtalkAccessToken = accessToken;
            //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
            GetUserResponseBody dingUserInfo = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody();

            return new ThirdUser.ThirdUserBuilder()
                    .avatar(dingUserInfo.avatarUrl)
                    .openId(dingUserInfo.openId)
                    .mobile(dingUserInfo.mobile)
                    .email(dingUserInfo.email)
                    .unionId(dingUserInfo.unionId)
                    .nick(dingUserInfo.nick).build();
        }

    }

    @Override
    public ThirdUserInitializeConfig getInitializeConfig() {
        return super.getInitializeConfig(SecurityTypeEnum.DING_DING);
    }

    /**
     * 获取登录用户 userId
     *
     * @param code 授权码
     * @return
     */
    public String getUserId(String code, String accessToken) {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(REQUEST_HOST + "/v2/user/getuserinfo");
            OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
            req.setCode(code);
            OapiV2UserGetuserinfoResponse rsp = client.execute(req, accessToken);
            if (Objects.equals(rsp.getErrcode(), 0L) && Objects.equals(rsp.getErrmsg(), "ok")) {
                return rsp.getResult().getUserid();
            }
            System.out.println(JSONObject.toJSONString(rsp));
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户id查询用户详情
     *
     * @param userId 用户id
     * @return 用户详情信息
     */
    @Override
    public UserInfo getUserInfo(String userId, String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)){
            LogUtil.info("钉钉 根据用户id查询用户详情 accessToken 为空  userId=" + userId);
            return  null;
        }
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(REQUEST_HOST + "/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, accessToken);
//            log.info("rsp => {}", JSONObject.toJSONString(rsp));
            DingTalkResult<UserInfo> dingTalkResult = JSON.parseObject(rsp.getBody(), new TypeReference<DingTalkResult<UserInfo>>() {
            });
            Assert.isTrue(dingTalkResult.getErrCode() == 0, "根据用户id查询用户详情失败 " + dingTalkResult.getErrMsg());
            return dingTalkResult.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> checkAndExecuteInnerUserLogin(String accessToken, String code) {
        Map<String, String> map = executeUser(accessToken, SecurityTypeEnum.DING_DING, null, code);
        try {
            Long tenantId = tenantUtil.getTenantIdByServerName();

            String check = systemOptionDao.getSystemConfigByKey("enableSubscription", tenantId);
            if (ObjectUtils.isEmpty(check) || !"1".equals(check)) {
                String dingUnionId = map.get("dingUnionId");
                if (!ObjectUtils.isEmpty(dingUnionId)){
                    Map<Long,List<UserVo>> tenantUsers = new HashMap<>();
                    UserVo vo = new UserVo();
                    vo.setDingUnionId(dingUnionId);
                    tenantUsers.put(tenantId, Arrays.asList(vo));
                    asyncDingFileUtil.enableSubscription(tenantId, tenantUsers);

                    OptionVo optionVo = new OptionVo();
                    optionVo.setType("");
                    optionVo.setKeyString("enableSubscription");
                    optionVo.setValueText("1");
                    optionVo.setCreateTime(LocalDateTime.now());
                    optionVo.setModifyTime(LocalDateTime.now());
                    optionVo.setTenantId(tenantId);
                    systemOptionDao.insert(optionVo);
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "订阅事件配置失败");
        }
        return map;
    }

    @Override
    String getOpenId(UserVo userVo) {
        return userVo.getDingOpenId();
    }

    @Override
    boolean setOpenId(UserVo userVo, User user, String openId) {
        if (StringUtils.hasText(userVo.getDingOpenId())) {
            return false;
        }
        if (StringUtils.hasText(openId)) {
            // 企业内部应用，用户免登 是没有openId的
            user.setDingOpenId(openId);
            return true;
        }
        return false;
    }

    @Override
    protected void beforeExecuteUser(UserVo userVo, ThirdUser thirdUser) {
        LogUtil.info("ding service beforeExecuteUser enterpriseType=" + enterpriseType);
        // 20240222 家校通判断是否是教师
        if (!ObjectUtils.isEmpty(enterpriseType) && "2".equals(enterpriseType)){
            if (ObjectUtils.isEmpty(thirdUser) || ObjectUtils.isEmpty(thirdUser.unionId)){
                return;
            }
            Long tenantId = tenantUtil.getTenantIdByServerName();
            if (ObjectUtils.isEmpty(tenantId) || tenantId <= 0){
                tenantId = 1L;
            }
            String accessToken = this.getAccessToken(tenantId);
            String dingUserId = DingUtil.getUserIdByUnionid(thirdUser.unionId, accessToken);
            if (ObjectUtils.isEmpty(dingUserId)){
                return;
            }
            UserDTO userDTO = asyncDingFileUtil.getDingUserInfoByuserid(dingUserId, accessToken, thirdUser.unionId);
            if (ObjectUtils.isEmpty(userDTO)){
                LogUtil.info("该用户获取为空，请联系管理员查看是否限制了此用户权限 dingUserId=" + dingUserId + "，unionId=" + thirdUser.unionId);
                throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), "用户限制权限！");
            }
            // 家校通
            LogUtil.info("家校通校验用户是否是教师 userDTO=" + JsonUtils.beanToJson(userDTO));
            // TODO 判断是否是老师，如果是学生则返回
            if (!CollectionUtils.isEmpty(userDTO.getDeptIdList()) && userDTO.getDeptIdList().size() >= 1){
                boolean isTeacher = false;
                try {
                    isTeacher = DingUtil.checkDingUserIsTeacher(userDTO.getDeptIdList(), accessToken);
                }catch (Exception e){
                    LogUtil.error(e, "获取家校通是否是老师失败");
                }
                if (!isTeacher){
                    LogUtil.info("该用户非教师 userDTO=" + JsonUtils.beanToJson(userDTO));
                    // 非教师的用户放入redis排除
                    asyncDingFileUtil.putDingUserErrRedis(thirdUser.unionId);
                    throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), "非教师用户！");
                }
            }
        }


    }

    @Override
    public JSONObject doBind(UserVo userVo, String code, String state, Long userId) {
        log.info("need not to implement!");
        return null;
    }



    @SneakyThrows
    @Override
    public String getAccessToken(String authCode,Long tenantId) {
        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();
        GetUserTokenRequest request = new GetUserTokenRequest()
                //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
                .setClientId(appKey)
                //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
                .setClientSecret(appSecret)
                .setCode(authCode)
                .setGrantType("authorization_code");
        GetUserTokenResponse response = client.getUserToken(request);
        //获取用户个人token
        return response.getBody().getAccessToken();
    }

    public static final String PLATFORM = "dingding";

    @Override
    public String getAccessToken(Long tenantId) {
        String accessToken = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + PLATFORM);
        if (StringUtils.hasText(accessToken)) {
            return accessToken;
        }
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();
        synchronized (this) {
            accessToken = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + PLATFORM);
            if (StringUtils.hasText(accessToken)) {
                return accessToken;
            }
            try {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
                OapiGettokenRequest req = new OapiGettokenRequest();
                req.setAppkey(appKey);
                req.setAppsecret(appSecret);
                req.setHttpMethod("GET");
                OapiGettokenResponse rsp = client.execute(req);
                accessToken = rsp.getAccessToken();
                if (StringUtils.hasText(accessToken)) {
                    stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_KEY + PLATFORM, accessToken, rsp.getExpiresIn(), TimeUnit.SECONDS);
                    return accessToken;
                }
            } catch (ApiException e) {
                log.error("_err => {}", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }

}