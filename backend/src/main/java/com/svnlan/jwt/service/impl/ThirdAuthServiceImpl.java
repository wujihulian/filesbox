package com.svnlan.jwt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncCubeFileUtil;
import com.svnlan.jwt.dto.AutoLoginDto;
import com.svnlan.jwt.dto.CubeLogin;
import com.svnlan.jwt.dto.CubeResult;
import com.svnlan.jwt.service.AuthService;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.jwt.tool.RestRequestUtil;
import com.svnlan.user.dao.Impl.GroupDaoImpl;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.CubeConfigVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.TenantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 认证实现
 *
 */
@Slf4j
@Service
public class ThirdAuthServiceImpl implements AuthService {

    @Resource
    AsyncCubeFileUtil asyncCubeFileUtil;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    UserDao userDao;
    @Resource
    GroupDaoImpl groupDaoImpl;
    @Resource
    JWTService jwtService;
    @Resource
    JWTTool jwtTool;
    @Resource
    UserManageService userManageService;

    private static String autoLoginUrl = "https://cube.zjedu.com/user/api/v1/login/autoLogin";

    @Override
    public Map<String, String>  autoLogin(AutoLoginDto dto) {

        Long tenantId = tenantUtil.getTenantIdByServerName();
        CubeConfigVo cubeConfigVo = asyncCubeFileUtil.getCubeConfig(tenantId);
        String accessKey = cubeConfigVo.getAccessKey();

        // 请求 cube 方 获取用户基础信息
        HashMap<String, Object> extraMap = new HashMap<>();
        extraMap.put("accessKey", accessKey);
        ParameterizedTypeReference<CubeResult<CubeLogin>> reference = new ParameterizedTypeReference<CubeResult<CubeLogin>>() {
        };
        log.info("开始请求三方平台登录接口");
        StopWatch autoLogin = new StopWatch("autoLogin");
        autoLogin.start();
        CubeResult<CubeLogin> cubeResult = RestRequestUtil.executePost(restTemplate, autoLoginUrl, dto.toHashMap(extraMap), null, reference);
        autoLogin.stop();
        log.info("autoLoginStatistics =>{}", autoLogin.prettyPrint());
        Assert.notNull(cubeResult, "cubeResult 为 null");
        Assert.isTrue(cubeResult.getSuccess() && Objects.isNull(cubeResult.getErrMessage()), cubeResult.getErrMessage());

        // 获取返回的信息
        CubeLogin cubeLogin = cubeResult.getData();
        log.info("CubeLogin cubeLogin => {}", JsonUtils.beanToJson(cubeLogin));
        Assert.isTrue((Objects.isNull(cubeLogin.getIsStudent()) || Boolean.TRUE.equals(cubeLogin.getIsStudent())), "学生账号请联系管理员");
//        boolean isParent = cubeLogin.getIsStudent() != null && !cubeLogin.getIsStudent();
        cubeLogin.setIsStudent(Objects.isNull(cubeLogin.getIsStudent()) || cubeLogin.getIsStudent());
//        if (1 == 1) {
//            return "测试测试 天天搞测试\n 代码代码 天天写代码";
//        }
        // 进行必要的检查和同步工作
        List<JSONObject> imList =  asyncCubeFileUtil.queryImUserIdList(cubeConfigVo.getCubeOrgId(), Arrays.asList(cubeLogin.getCubeUserId()), accessKey, cubeConfigVo.getSecretKey());
        if (!CollectionUtils.isEmpty(imList)){
            String dingUserId = imList.get(0).getString("imUserId");
            if (!ObjectUtils.isEmpty(dingUserId)){
                cubeLogin.setDingUserId(dingUserId);
            }
        }
        return executeUser(SecurityTypeEnum.CUBE, cubeLogin, tenantId);
    }


    protected Map<String, String> executeUser(SecurityTypeEnum typeEnum, CubeLogin cubeLogin, Long tenantId) {
        // 获取三方用户信息
        String cubeUserId = cubeLogin.getCubeUserId();

        List<UserVo> userVoList = userDao.queryUserInfoByOpenIdOrMobile(cubeUserId, cubeLogin.getMobile(), typeEnum.getCode(),
                null,cubeLogin.getDingUserId());
        UserVo userVo = null;
        if (!CollectionUtils.isEmpty(userVoList)) {
            if (userVoList.size() == 1) {
                userVo = userVoList.get(0);
            } else {
                // 正常情况下， 这里的 userVoList size 为2 即 手机号 openId 分别查询出了一条
                // 这种情况下属于 手机号是一个账号， openId 被绑定到另一个账号上的情况
                // 找出这个 openId 所在的账号
                userVo = userVoList.stream().filter(it -> Objects.equals(cubeUserId, it.getThirdOpenId())).findAny()
                        .orElseThrow(() -> new SvnlanRuntimeException("未找到 魔方用户  =>" + cubeUserId + "的账号"));
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Long userId;
        if (Objects.nonNull(userVo)) {
            log.info("userVo => {}", userVo);
            Assert.isTrue(Objects.equals(userVo.getStatus(), 1), "该账号未启用");

            // 判断用户是否被禁用
            Assert.isTrue(Objects.equals(userVo.getStatus(), 1), "账号被禁用或尚未启用！请联系管理员");
            userId = userVo.getUserID();
            // 如果用户有些信息没有， 则用返回的信息更新
        } else {
            ThirdUserInitializeConfig initializeConfig = asyncCubeFileUtil.getInitializeConfig(SecurityTypeEnum.CUBE);

            UserDTO userDTO = new UserDTO();
            // {"groupID":1,"name":"sdfsdfsdf","nickname":"asdfadsf","password":"asdf23","sizeMax":1,"roleID":3,"groupInfo":[]}
            userDTO.setGroupID(1L);
            String mobile = cubeLogin.getMobile();
            userDTO.setName(mobile);
            userDTO.setPhone(mobile);
            userDTO.setNickname(cubeLogin.getName());
            // 明文密码， 后面会加密
            userDTO.setPassword(mobile.substring(mobile.length() - 6));
            // 用户的配额
            userDTO.setSizeMax(initializeConfig.getSizeMax());
            // 用户角色
            userDTO.setRoleID(initializeConfig.getRoleID());
            userDTO.setAvatar(cubeLogin.getAvatar());
            // 代表管理员
            userDTO.setCreateUser(0L);
            userDTO.setOpenId(cubeLogin.getUserId());
            userDTO.setUnionId(cubeLogin.getUserId());
            // 从配置里读取需要关联的用户部门
            List<Long> groupIds = initializeConfig.getGroupInfo().stream().map(ThirdUserInitializeConfig.GroupInfoDTO::getGroupID).collect(Collectors.toList());
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
            userDTO.setTenantId(tenantId);
            userDTO.setOpenIdType(Integer.parseInt(typeEnum.getCode()));
            userId = userManageService.addThirdUser(jwtTool.findApiPrefix(request), userDTO);
        }
        JSONObject jsonObj = afterExecuteUser(userId);
        Map<String, String> result = jwtService.doThirdLogin(userId, request);
        if (Objects.nonNull(jsonObj)) {
            result.put("nickname", jsonObj.getString("nickname"));
            result.put("avatar", jsonObj.getString("avatar"));
        }
        return result;
    }

    protected JSONObject afterExecuteUser(Long userId) {
        // 根据 userId 查询昵称和头像
        User user = userDao.selectAvatarAndNickName(userId);
        io.jsonwebtoken.lang.Assert.notNull(user, "未查询到该用户 userID => " + userId);
        return new JSONObject().fluentPut("avatar", user.getAvatar())
                .fluentPut("nickname", user.getNickname());
    }

}
