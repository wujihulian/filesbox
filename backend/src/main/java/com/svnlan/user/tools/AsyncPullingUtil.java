package com.svnlan.user.tools;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncCubeFileUtil;
import com.svnlan.home.utils.AsyncDingFileUtil;
import com.svnlan.home.utils.AsyncEnWebChatFileUtil;
import com.svnlan.home.utils.DingUtil;
import com.svnlan.jwt.dao.SystemLogDao;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.DingService;
import com.svnlan.jwt.third.dingding.UserInfo;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.Impl.UserDaoImpl;
import com.svnlan.user.dao.RoleDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.Role;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.GroupService;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.*;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AsyncPullingUtil {
    @Resource
    StorageService storageService;
    @Resource
    GroupService groupService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    GroupDao groupDaoImpl;
    @Resource
    AsyncDingFileUtil asyncDingFileUtil;
    @Resource
    UserDaoImpl userDaoImpl;
    @Resource
    RoleDao roleDao;
    @Resource
    UserManageService userManageService;
    @Resource
    AsyncEnWebChatFileUtil asyncEnWebChatFileUtil;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    private SystemLogDao systemLogDaoImpl;
    @Resource
    AsyncCubeFileUtil asyncCubeFileUtil;
    @Resource
    DingService dingService;

    @Async(value = "asyncTaskExecutor")
    public void asyncPullDingGroup(List<Long> deptList, List<JSONObject> list, String taskId, Long tenantId, String enterpriseType
    , LoginUser loginUser){

        Integer storageId = storageService.getDefaultStorageDeviceId();
        // 不存在则创建部门
        Long parentID = getDingParentGroupId(storageId, tenantId);
        Integer sort = groupDaoImpl.getMaxSort(parentID);
        sort = ObjectUtils.isEmpty(sort) ? 0 : sort;
        GroupVo parentGroup = groupDaoImpl.getGroupAndSourceByID(parentID);
        // 获取已存在的用户
        List<String> userUnionIdList = userDaoImpl.getAllUserByDingUnionId(tenantId);
        ThirdUserInitializeConfig initializeConfig = asyncDingFileUtil.getInitializeConfig(SecurityTypeEnum.DING_DING);
        Integer roleId = getDefaultSetRoleId(tenantId);
        String accessToken = asyncDingFileUtil.getAccessToken(tenantId);
        String status = "1";

        pullDingUser(parentGroup.getGroupID(), tenantId, 1L, parentGroup.getName(), enterpriseType, userUnionIdList, roleId
                , accessToken, loginUser, taskId, initializeConfig);


        try {
            execSaveGroupDing(deptList, list, sort,parentGroup, parentID, tenantId, storageId, taskId, enterpriseType, userUnionIdList
                    , roleId, accessToken, loginUser, initializeConfig);
        }catch (Exception e){
            status = "2";
            LogUtil.error(e, "asyncPullDingGroup 拉取钉钉组织、用户失败 taskId= " + taskId);
        }

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_ding + taskId, "1", 10, TimeUnit.MINUTES);

        try {
            JSONObject logJson = systemLogDaoImpl.getSystemLogExportList(taskId, LogTypeEnum.pullDing.getCode());
            if (!ObjectUtils.isEmpty(logJson)) {
                logJson.put("execStatus", status);
                systemLogDaoImpl.updateSystemLogDetail (JsonUtils.beanToJson(logJson), logJson.getLong("id"));
            }
        }catch (Exception e){
            LogUtil.error(e, "asyncPullDingGroup 拉取钉钉组织、用户失败 最后修改日志状态 taskId= " + taskId);
        }



    }


    private void execSaveGroupDing(List<Long> deptList, List<JSONObject> list,int sort,GroupVo parentGroup, Long parentID, Long tenantId, Integer storageId, String taskId
            , String enterpriseType, List<String> userUnionIdList, Integer roleId, String accessToken, LoginUser loginUser, ThirdUserInitializeConfig initializeConfig){
        if (ObjectUtils.isEmpty(parentGroup)){
            LogUtil.info(taskId + "asyncPullDingGroup parentGroup is null");
        }
        Group group = null;
        Integer subSort = 0;
        GroupVo parentSubGroup = null;
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (JSONObject dept : list){
            String deptName = dept.getString("name");
            long deptId = dept.getLong("dept_id");
            if (deptId <= 0){
                continue;
            }
            boolean exist = false;
            if (!CollectionUtils.isEmpty(deptList) && deptList.contains(deptId)) {
                exist = true;
                parentSubGroup = groupDaoImpl.getGroupAndSourceByDeptId(tenantId,deptId,"ding");
            }else {
                group = new Group();
                group.setGroupID(null);
                group.setName(deptName);
                group.setExtraField("ding");
                group.setSizeMax(0.0);
                group.setDingDeptId(deptId);
                sort = sort + 1;
                parentSubGroup = groupService.addGroupAll("asyncPullDingGroup", group, parentID, 0L, tenantId, storageId, sort, parentGroup);

                /** 操作日志*/
                reMap = new HashMap<>(4);
                reMap.put("name", deptName);
                reMap.put("groupId", parentSubGroup.getGroupID());
                reMap.put("groupParentId", parentGroup.getGroupID());
                reMap.put("groupParentName", parentGroup.getName());
                paramList.add(reMap);
            }
            // pull user
            pullDingUser(parentSubGroup.getGroupID(), tenantId, deptId, deptName, enterpriseType, userUnionIdList, roleId
                    , accessToken, loginUser, taskId, initializeConfig);

            List<JSONObject> subList = null;
            //if (!ObjectUtils.isEmpty(enterpriseType) && "2".equals(enterpriseType)){
                // 家校通
                //subList = asyncDingFileUtil.getSchoolDepartmentForeachList(deptId, tenantId, 1L, 30L);
           // }else {
                // 企业类型
                subList =  asyncDingFileUtil.getDepartmentList(deptId, tenantId);
           // }
            if (CollectionUtils.isEmpty(subList)){
                LogUtil.info("没有下一级部门 dept=" + dept);
                continue;
            }
            if (exist){
                subSort = groupDaoImpl.getMaxSort(parentSubGroup.getGroupID());
                subSort = ObjectUtils.isEmpty(subSort) ? 0 : subSort;
            }
            execSaveGroupDing(deptList, subList, subSort,parentSubGroup, parentSubGroup.getGroupID(), tenantId, storageId, taskId, enterpriseType
                    , userUnionIdList, roleId, accessToken, loginUser, initializeConfig);
        }


        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullDingGroup.getCode(), paramList, null, taskId);
    }

    private void pullDingUser(Long groupId, Long tenantId, Long deptId, String deptName, String enterpriseType, List<String> userUnionIdList, Integer roleId
    , String accessToken, LoginUser loginUser, String taskId, ThirdUserInitializeConfig initializeConfig){

        List<JSONObject> userList = null;
        boolean isEnterprise = false;
        //if (!ObjectUtils.isEmpty(enterpriseType) && "2".equals(enterpriseType)){
            // 家校通
            //userList = asyncDingFileUtil.getSchoolDeptUserForeachList(deptId, tenantId, 1L, 30L);
        //}else {
            // 企业类型
            isEnterprise = true;
            userList = asyncDingFileUtil.getDepartmentUserForeachList(deptId, tenantId, 0L, 30L, taskId);
        //}
        if (CollectionUtils.isEmpty(userList)){
            LogUtil.info(deptName + " 部门下没有user信息 deptId=" + deptId);
            return;
        }
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();

        for (JSONObject user : userList){
            String unionid = user.getString("unionid");
            String userid = user.getString("userid");
            if (!CollectionUtils.isEmpty(userUnionIdList) && userUnionIdList.contains(unionid)) {

                List<UserGroupVo> userGroupVoList = new ArrayList<>();
                userGroupVoList.add(new UserGroupVo(groupId, roleId));
                userManageService.userGroupRelation("pull 钉钉已存在用户则关联 ", "8", unionid, "",  userGroupVoList, tenantId);
                continue;
            }else {
                UserDTO userDTO = null;
                if (isEnterprise) {
                    userDTO = setUserByEnterprise(user, unionid);
                }else {
                    userDTO = asyncDingFileUtil.getDingUserInfoByuserid(userid, accessToken, unionid);
                }
                if (ObjectUtils.isEmpty(userDTO)){
                    LogUtil.info("该用户获取为空，请联系管理员查看是否限制了此用户权限 dingUserId=" + userid + "，unionId=" + unionid);
                    continue;
                }
                userDTO.setDingUserId(userid);
                userDTO.setTaskId(taskId);
                createThirdUser(tenantId, groupId, roleId, userDTO, SecurityTypeEnum.DING_DING, " pull钉盘同步创建用户", initializeConfig);
                userUnionIdList.add(unionid);
                /** 操作日志*/

                reMap = new HashMap<>(3);
                reMap.put("name", userDTO.getName());
                reMap.put("nickname", userDTO.getNickname());
                reMap.put("userId", userDTO.getUserID());
                paramList.add(reMap);
            }
        }
        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullDingUser.getCode(), paramList, null, taskId);
    }

    private UserDTO setUserByEnterprise(JSONObject user, String unionid){
        UserDTO userDTO = new UserDTO();
        userDTO.setUnionId(unionid);
        userDTO.setNickname(user.getString("name"));
        userDTO.setAvatar(user.getString("avatar"));
        userDTO.setOpenId(null);
        userDTO.setName(user.getString("mobile"));
        userDTO.setPhone(user.getString("mobile"));
        return userDTO;
    }

    private UserDTO setUserByCube(JSONObject user, String cubeUserId){
        UserDTO userDTO = new UserDTO();
        userDTO.setUnionId(null);
        userDTO.setNickname(user.getString("name"));
        userDTO.setAvatar("");
        userDTO.setOpenId(cubeUserId);
        String py = ChinesUtil.getPingYin(userDTO.getNickname());
        String phone = user.getString("mobile");
        if (ObjectUtils.isEmpty(phone)){
            phone = user.getString("migrateStaffId");
        }else {
            phone = phone.substring(phone.length() - 4);
        }
        userDTO.setName(py + "_" + phone);
        userDTO.setPhone("");
        return userDTO;
    }

    private Map<String, String> getImUserIdByCubeUserIds(String cubeOrgId, List<String> cubeUserIds, String ak, String sk){
        List<JSONObject> imList =  asyncCubeFileUtil.queryImUserIdList(cubeOrgId, cubeUserIds, ak, sk);
        Map<String, String> userInfoMap = null;
        if (!CollectionUtils.isEmpty(imList)){

            userInfoMap = new HashMap<>();
            for (JSONObject n : imList){
                userInfoMap.put(n.getString("cubeUserId"), n.getString("imUserId"));
            }
        }
        return userInfoMap;
    }

    private Map<String, UserInfo> getPhoneByCubeUserIds(String cubeOrgId, List<String> cubeUserIds, String ak, String sk, String coreAccessToken){
        List<JSONObject> imList =  asyncCubeFileUtil.queryImUserIdList(cubeOrgId, cubeUserIds, ak, sk);
        if (!CollectionUtils.isEmpty(imList)){

            Map<String, UserInfo> userInfoMap = new HashMap<>();
            for (JSONObject o : imList){

            }
            List<String> imIds = imList.stream().map(n-> n.getString("imUserId")).collect(Collectors.toList());
            UserInfo userInfo = null;
            for (String imUserId : imIds){
                userInfo = dingService.getUserInfo(imUserId, coreAccessToken);
                if (!ObjectUtils.isEmpty(userInfo)){
                    userInfoMap.put(imUserId, userInfo);
                }
            }

        }
        return null;
    }
    private UserDTO setUserByEnWebChat(JSONObject user, String corpId, String userid){
        UserDTO userDTO = new UserDTO();
        userDTO.setUnionId(null);
        userDTO.setNickname(user.getString("name"));
        userDTO.setAvatar(user.getString("avatar"));
        userDTO.setOpenId(corpId+"_"+userid);
        String phone = user.getString("mobile");
        userDTO.setName("en_"+userid);
        userDTO.setPhone(phone);
        return userDTO;
    }

    private Long getDingParentGroupId(Integer storageId, Long tenantId){

        Long parentID = 0L;
        Group dingGroup = null;
        Group dingNameGroup = null;
        List<Group> dingParentList =  groupDaoImpl.getThirdGroupParent("ding", 1L,"钉盘");
        if (!CollectionUtils.isEmpty(dingParentList)){
            for (Group g : dingParentList){
                if (!ObjectUtils.isEmpty(g.getExtraField()) && g.getExtraField().equals("ding")){
                    dingGroup = g;
                    parentID = g.getGroupID();
                }else if(g.getName().equals("钉盘")){
                    dingNameGroup = g;
                }
            }
        }
        if (ObjectUtils.isEmpty(dingGroup)){
            if (ObjectUtils.isEmpty(dingNameGroup)){
                Group group = new Group();
                group.setGroupID(null);
                group.setName("钉盘");
                group.setSizeMax(0.0);
                group.setDingDeptId(0L);
                group.setExtraField("ding");
                GroupVo parentGroup = groupDaoImpl.getTopGroupByTenantId(tenantId);
                Integer sort = groupDaoImpl.getMaxSort(parentGroup.getGroupID());
                parentID = parentGroup.getGroupID();
                sort = ObjectUtils.isEmpty(sort) ? 0 : sort;
                groupService.addGroupAll("asyncPullDingGroup", group, parentID, 0L, tenantId, storageId, sort + 1, parentGroup);
                parentID = group.getGroupID();
            }else {
                groupDaoImpl.updateGroupThirdTag("ding", dingNameGroup.getGroupID());
                parentID = dingNameGroup.getGroupID();
            }
        }
        return parentID;
    }

    /**
     * 异步拉取企业微信
     * @param deptList
     * @param list
     * @param taskId
     * @param tenantId
     */
    @Async(value = "asyncTaskExecutor")
    public void asyncPullEnWebChatGroup(List<Long> deptList, List<JSONObject> list, String taskId, Long tenantId
            , EnWebChatConfigVo dingConfigVo, String accessToken, LoginUser loginUser){
        Integer storageId = storageService.getDefaultStorageDeviceId();
        // 不存在则创建部门
        Long parentID = getEnWebChatParentGroupId(storageId, tenantId);
        Integer sort = groupDaoImpl.getMaxSort(parentID);
        sort = ObjectUtils.isEmpty(sort) ? 0 : sort;
        GroupVo parentGroup = groupDaoImpl.getGroupAndSourceByID(parentID);
        // 获取已存在的用户
        List<String> userIdList = userDaoImpl.getAllUserByEnWebChat(tenantId);
        Integer roleId = getDefaultSetRoleId(tenantId);
        String status = "1";
        ThirdUserInitializeConfig initializeConfig = asyncDingFileUtil.getInitializeConfig(SecurityTypeEnum.EN_WECHAT);
        try {
            execSaveGroupEnWebChat(deptList, list, sort,parentGroup, parentID, tenantId, storageId, taskId, userIdList
                    , dingConfigVo.getCorpId(), accessToken, roleId, loginUser, initializeConfig);
        }catch (Exception e){
            status = "2";
            LogUtil.error(e, "asyncPullEnWebChatGroup 拉取企业微信组织、用户失败 最后修改日志状态 taskId= " + taskId);
        }

        try {
            JSONObject logJson = systemLogDaoImpl.getSystemLogExportList(taskId, LogTypeEnum.pullEnWebchat.getCode());
            if (!ObjectUtils.isEmpty(logJson)) {
                logJson.put("execStatus", status);
                systemLogDaoImpl.updateSystemLogDetail (JsonUtils.beanToJson(logJson), logJson.getLong("id"));
            }
        }catch (Exception e){
            LogUtil.error(e, "asyncPullEnWebChatGroup 拉取钉钉组织、用户失败 最后修改日志状态 taskId= " + taskId);
        }

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_enwebchat + taskId, "1", 10, TimeUnit.MINUTES);
    }

    private void execSaveGroupEnWebChat(List<Long> deptList, List<JSONObject> list,int sort,GroupVo parentGroup, Long parentID, Long tenantId, Integer storageId
            , String taskId, List<String> userIdList, String corpId, String accessToken, Integer roleId, LoginUser loginUser, ThirdUserInitializeConfig initializeConfig){
        if (ObjectUtils.isEmpty(parentGroup)){
            LogUtil.info(taskId + "execSaveGroupEnWebChat parentGroup is null");
        }
        Group group = null;
        // Integer subSort = 0;
        GroupVo parentSubGroup = null;
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();

        Map<Long, GroupVo> parentMap = new HashMap<>();

        for (JSONObject dept : list){
            LogUtil.info("微信部门 dept=" + JsonUtils.beanToJson(dept));
            long parentid = dept.getLong("parentid");
            if (parentid <= 0){
                continue;
            }
            long deptId = dept.getLong("id");
            String deptName = dept.getString("name");
            if (!CollectionUtils.isEmpty(deptList) && deptList.contains(deptId)) {
                parentSubGroup = groupDaoImpl.getGroupAndSourceByDeptId(tenantId, deptId, "enWechat");
                parentMap.put(deptId, parentSubGroup);
            }else {
                group = new Group();
                group.setGroupID(null);
                group.setName(deptName);
                group.setExtraField("enWechat");
                group.setSizeMax(0.0);
                group.setDingDeptId(deptId);
                sort = sort + 1;
                parentSubGroup = groupService.addGroupAll("execSaveGroupEnWebChat", group, parentID, 0L, tenantId, storageId
                        , sort, (ObjectUtils.isEmpty(parentMap) || !parentMap.containsKey(parentid)) ? parentGroup : parentMap.get(parentid));
                parentMap.put(deptId, parentSubGroup);
                /** 操作日志*/
                reMap = new HashMap<>(4);
                reMap.put("name", deptName);
                reMap.put("groupId", parentSubGroup.getGroupID());
                reMap.put("groupParentId", parentGroup.getGroupID());
                reMap.put("groupParentName", parentGroup.getName());
                paramList.add(reMap);
            }
            // pull user
            pullEnWebChatUser(parentSubGroup.getGroupID(), tenantId, deptId, deptName, userIdList, roleId, accessToken
                    , corpId, loginUser, taskId, initializeConfig);
 
        }

        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullEnWebchatGroup.getCode(), paramList, null, taskId);
    }
    private Long getEnWebChatParentGroupId(Integer storageId, Long tenantId){

        Long parentID = 0L;
        Group dingGroup = null;
        Group dingNameGroup = null;
        List<Group> dingParentList =  groupDaoImpl.getThirdGroupParent("enWechat", 1L,"企业微信");
        if (!CollectionUtils.isEmpty(dingParentList)){
            for (Group g : dingParentList){
                if (!ObjectUtils.isEmpty(g.getExtraField()) && g.getExtraField().equals("enWechat")){
                    dingGroup = g;
                    parentID = g.getGroupID();
                }else if(g.getName().equals("企业微信")){
                    dingNameGroup = g;
                }
            }
        }
        if (ObjectUtils.isEmpty(dingGroup)){
            if (ObjectUtils.isEmpty(dingNameGroup)){
                Group group = new Group();
                group.setGroupID(null);
                group.setName("企业微信");
                group.setSizeMax(0.0);
                group.setDingDeptId(0L);
                group.setExtraField("enWechat");

                GroupVo parentGroup = groupDaoImpl.getTopGroupByTenantId(tenantId);
                Integer sort = groupDaoImpl.getMaxSort(parentGroup.getGroupID());
                sort = ObjectUtils.isEmpty(sort) ? 0 : sort;

                parentID = parentGroup.getGroupID();
                groupService.addGroupAll("asyncPullEnWebChatGroup", group, parentID, 0L, tenantId, storageId, sort + 1, parentGroup);
                parentID = group.getGroupID();
            }else {
                groupDaoImpl.updateGroupThirdTag("enWechat", dingNameGroup.getGroupID());
                parentID = dingNameGroup.getGroupID();
            }
        }
        return parentID;
    }

    private Integer getDefaultSetRoleId(Long tenantId){
        Role role = roleDao.getRoleByKey("admin.auth.editUploader", tenantId);
        return role.getRoleID();
    }

    private UserVo createThirdUser(Long tenantId, Long groupId, Integer roleId, UserDTO userDTO, SecurityTypeEnum thirdNameEnum, String prefix
            , ThirdUserInitializeConfig initializeConfig){
        if (ObjectUtils.isEmpty(userDTO) || ObjectUtils.isEmpty(userDTO.getName())) {
            LogUtil.error("用户信息为空 ！userDTO=" + (ObjectUtils.isEmpty(userDTO) ? "" : JsonUtils.beanToJson(userDTO)));
            return null;
        }
        UserVo userVo = null;
        try {
            userVo = new UserVo();
            if (ObjectUtils.isEmpty(initializeConfig)) {
                initializeConfig = asyncDingFileUtil.getInitializeConfig(thirdNameEnum);
            }

            // {"groupID":1,"name":"sdfsdfsdf","nickname":"asdfadsf","password":"asdf23","sizeMax":1,"roleID":3,"groupInfo":[]}
            userDTO.setGroupID(groupId);
            String pwd = RandomStringUtils.randomAlphanumeric(6);
            /*if (!ObjectUtils.isEmpty(userDTO.getPhone())){
                pwd = userDTO.getPhone().substring(userDTO.getPhone().length() - 6);
            }else {
                pwd = RandomStringUtils.randomAlphanumeric(6);
            }*/
            // 明文密码， 后面会加密
            userDTO.setPassword(pwd);
            userDTO.setPwdState("0");
            // 用户的配额
            userDTO.setSizeMax(initializeConfig.getSizeMax());
            // 用户角色
            userDTO.setRoleID(initializeConfig.getRoleID());
            userDTO.setSex(1);
            // 代表管理员
            userDTO.setCreateUser(0L);
            List<UserGroupVo> userGroupVoList = new ArrayList<>();
            userGroupVoList.add(new UserGroupVo(groupId, roleId));
            userDTO.setGroupInfo(userGroupVoList);
            userDTO.setTenantId(tenantId);
            userDTO.setOpenIdType(Integer.parseInt(thirdNameEnum.getCode()));
            Long userId = null;
            // 判断手机号是否存在，存在则关联，不存在则创建
            if (!ObjectUtils.isEmpty(userDTO.getPhone())){
                userId = userManageService.checkPhoneUserIsExist("第三方同步用户：校验手机号是否已存在用户",
                        userDTO.getPhone(), tenantId, userDTO, thirdNameEnum);
            }else if (!ObjectUtils.isEmpty(userDTO.getDingUserId())){
                userId = userManageService.checkDingUserIsExist("第三方同步用户：校验dingUserId是否已存在用户",
                        userDTO.getDingUserId(), tenantId, userDTO, thirdNameEnum);
            }
            if (ObjectUtils.isEmpty(userId) || userId <= 0) {
                userId = userManageService.addThirdUser(prefix, userDTO);
            }
            userDTO.setUserID(userId);
            userVo.setUserID(userId);
            userVo.setName(ObjectUtils.isEmpty(userDTO.getPhone()) ? userDTO.getName() : userDTO.getPhone());
            userVo.setNickname(userDTO.getNickname());
            userVo.setAvatar(userDTO.getAvatar());
            userVo.setSex(userDTO.getSex());
        }catch (Exception e){
            LogUtil.error(e, " pullDing createDingUser 创建用户失败");
        }

        return userVo;
    }


    private void pullEnWebChatUser(Long groupId, Long tenantId, Long deptId, String deptName, List<String> userIdList, Integer roleId
            , String accessToken, String corpId, LoginUser loginUser, String taskId, ThirdUserInitializeConfig initializeConfig){
        List<JSONObject> userList = asyncEnWebChatFileUtil.getDepartmentUserList(deptId, accessToken);
        if (CollectionUtils.isEmpty(userList)){
            LogUtil.info(deptName + " 部门下没有user信息 pullEnWebChatUser deptId=" + deptId);
            return;
        }
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (JSONObject user : userList){
            String userid = user.getString("userid");
            String openId = corpId+"_"+userid;
            if (!CollectionUtils.isEmpty(userIdList) && userIdList.contains(openId)) {

                List<UserGroupVo> userGroupVoList = new ArrayList<>();
                userGroupVoList.add(new UserGroupVo(groupId, roleId));
                userManageService.userGroupRelation("pull 企业微信已存在用户则关联 ", "7", "", openId,  userGroupVoList, tenantId);

                continue;
            }else {
                UserDTO userDTO =  setUserByEnWebChat(user, corpId, userid);
                createThirdUser(tenantId, groupId, roleId, userDTO,  SecurityTypeEnum.EN_WECHAT," 企业微信同步创建用户", initializeConfig);
                userIdList.add(corpId+"_"+userid);

                /** 操作日志*/
                reMap = new HashMap<>(3);
                reMap.put("name", userDTO.getName());
                reMap.put("nickname", userDTO.getNickname());
                reMap.put("userId", userDTO.getUserID());
                paramList.add(reMap);
            }
        }

        systemLogTool.setSysLog(loginUser, LogTypeEnum.pullEnWebchatUser.getCode(), paramList, null, taskId);
    }


    @Async(value = "asyncTaskExecutor")
    public void asyncPullCubeGroup(List<String> deptList, List<JSONObject> list, String taskId, Long tenantId
            , LoginUser loginUser, CubeConfigVo cubeConfigVo){

        Integer storageId = storageService.getDefaultStorageDeviceId();
        // 不存在则创建部门
        Long parentID = getCubeParentGroupId(storageId, tenantId);
        Integer sort = groupDaoImpl.getMaxSort(parentID);
        sort = ObjectUtils.isEmpty(sort) ? 0 : sort;
        GroupVo parentGroup = groupDaoImpl.getGroupAndSourceByID(parentID);
        // 获取已存在的用户
        List<String> userUnionIdList = userDaoImpl.getAllUserByCube(tenantId);
        ThirdUserInitializeConfig initializeConfig = asyncDingFileUtil.getInitializeConfig(SecurityTypeEnum.CUBE);
        Integer roleId = getDefaultSetRoleId(tenantId);
        String accessToken = asyncCubeFileUtil.getCorpToken(cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey());

        if (ObjectUtils.isEmpty(accessToken)){
            LogUtil.error("getPhoneByCubeUserIds 服务商获取第三方应用授权企业的access_token失败");

        }
        String status = "1";
        try {
            execSaveGroupCube(deptList, list, sort,parentGroup, parentID, tenantId, storageId, taskId, userUnionIdList
                    , roleId, loginUser, cubeConfigVo, accessToken, initializeConfig);
        }catch (Exception e){
            status = "2";
            LogUtil.error(e, "asyncPullCubeGroup 拉取钉钉组织、用户失败 taskId= " + taskId);
        }

        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_pull_cube + taskId, "1", 10, TimeUnit.MINUTES);

        try {
            JSONObject logJson = systemLogDaoImpl.getSystemLogExportList(taskId, LogTypeEnum.pullCube.getCode());
            if (!ObjectUtils.isEmpty(logJson)) {
                logJson.put("execStatus", status);
                systemLogDaoImpl.updateSystemLogDetail (JsonUtils.beanToJson(logJson), logJson.getLong("id"));
            }
        }catch (Exception e){
            LogUtil.error(e, "asyncPullCubeGroup 拉取魔方组织、用户失败 最后修改日志状态 taskId= " + taskId);
        }

    }

    private void execSaveGroupCube(List<String> deptList, List<JSONObject> selectList,int sort,GroupVo parentGroup, Long parentID, Long tenantId, Integer storageId, String taskId
            ,  List<String> userUnionIdList, Integer roleId, LoginUser loginUser, CubeConfigVo cubeConfigVo, String coreAccessToken, ThirdUserInitializeConfig initializeConfig){
        if (ObjectUtils.isEmpty(parentGroup)){
            LogUtil.info(taskId + "asyncPullCubeGroup parentGroup is null");
        }

        // 拉取deptId=1下的用户
        pullCubeUser(parentGroup.getGroupID(), tenantId, "1", parentGroup.getName(), userUnionIdList, roleId
                , loginUser, taskId, cubeConfigVo, coreAccessToken, initializeConfig);


        Group group = null;
        Integer subSort = 0;
        GroupVo parentSubGroup = null;
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (JSONObject dept : selectList){
            String deptName = dept.getString("name");
            String imDeptId = dept.getString("deptId");
            boolean exist = false;
            if (!CollectionUtils.isEmpty(deptList) && deptList.contains(imDeptId)) {
                exist = true;
                parentSubGroup = groupDaoImpl.getGroupAndSourceByDingDeptId(tenantId,Long.parseLong(imDeptId),"cube");
            }else {
                group = new Group();
                group.setGroupID(null);
                group.setName(deptName);
                group.setExtraField("cube");
                group.setSizeMax(0.0);
                group.setDingDeptId(Long.parseLong(imDeptId));
                group.setThirdDeptId(imDeptId);
                String dataV2 = dept.getString("dataV2");
                if (!ObjectUtils.isEmpty(dataV2)){
                    JSONObject data = JSONObject.parseObject(dataV2);
                    group.setThirdDeptId(data.getString("deptId"));
                }
                sort = sort + 1;
                parentSubGroup = groupService.addGroupAll("asyncPullCubeGroup", group, parentID, 0L, tenantId, storageId, sort, parentGroup);

                /** 操作日志*/
                reMap = new HashMap<>(4);
                reMap.put("name", deptName);
                reMap.put("groupId", parentSubGroup.getGroupID());
                reMap.put("groupParentId", parentGroup.getGroupID());
                reMap.put("groupParentName", parentGroup.getName());
                paramList.add(reMap);
            }
            // pull user
            pullCubeUser(parentSubGroup.getGroupID(), tenantId, imDeptId, deptName, userUnionIdList, roleId
                    , loginUser, taskId, cubeConfigVo, coreAccessToken, initializeConfig);

            List<JSONObject> subList = asyncCubeFileUtil.queryImDeptListByParentId(cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey()
                    , cubeConfigVo.getCubeOrgId(), imDeptId);
            if (CollectionUtils.isEmpty(subList)){
                LogUtil.info("没有下一级部门 dept=" + dept);
                continue;
            }
            if (exist){
                subSort = groupDaoImpl.getMaxSort(parentSubGroup.getGroupID());
                subSort = ObjectUtils.isEmpty(subSort) ? 0 : subSort;
            }
            execSaveGroupCube(deptList, subList, subSort,parentSubGroup, parentSubGroup.getGroupID(), tenantId, storageId, taskId
                    , userUnionIdList, roleId, loginUser, cubeConfigVo, coreAccessToken, initializeConfig);
        }
        if (!CollectionUtils.isEmpty(paramList)) {
            systemLogTool.setSysLog(loginUser, LogTypeEnum.pullCubeGroup.getCode(), paramList, null, taskId);
        }
    }

    private void pullCubeUser(Long groupId, Long tenantId, String parentDeptId, String deptName, List<String> userUnionIdList, Integer roleId
            , LoginUser loginUser, String taskId, CubeConfigVo cubeConfigVo, String coreAccessToken, ThirdUserInitializeConfig initializeConfig){

        List<JSONObject> userList = asyncCubeFileUtil.getDepartmentUserForeachList(cubeConfigVo.getCubeOrgId(), parentDeptId
                ,cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey(), 1, 200);

        if (CollectionUtils.isEmpty(userList)){
            LogUtil.info(deptName + " 部门下没有user信息 pullCubeUser parentDeptId=" + parentDeptId);
            return;
        }
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        List<JSONObject> addUserList = new ArrayList<>();
        List<String> cubeUserIds = new ArrayList<>();
        for (JSONObject user : userList){
            int status = user.getInteger("status");
            if (status != 3){
                continue;
            }
            String cubeUserId = user.getString("cubeUserId");
            if (!CollectionUtils.isEmpty(userUnionIdList) && userUnionIdList.contains(cubeUserId)) {

                List<UserGroupVo> userGroupVoList = new ArrayList<>();
                userGroupVoList.add(new UserGroupVo(groupId, roleId));
                userManageService.userGroupRelation("pull 魔方已存在用户则关联 ", "13", "", cubeUserId,  userGroupVoList, tenantId);

                continue;
            }else {
                addUserList.add(user);
                cubeUserIds.add(cubeUserId);
            }
        }
        if (CollectionUtils.isEmpty(addUserList)){
            return;
        }

        Map<String , String> imUserIdMap = this.getImUserIdByCubeUserIds(cubeConfigVo.getCubeOrgId(), cubeUserIds
                , cubeConfigVo.getAccessKey(), cubeConfigVo.getSecretKey());

        for (JSONObject user : addUserList){
            String cubeUserId = user.getString("cubeUserId");
            UserDTO userDTO = null;

            userDTO = setUserByCube(user, cubeUserId);
            if (!ObjectUtils.isEmpty(imUserIdMap) && imUserIdMap.containsKey(cubeUserId)){
                userDTO.setDingUserId(imUserIdMap.get(cubeUserId));
            }

            if (!ObjectUtils.isEmpty(userDTO.getDingUserId()) && "1".equals(parentDeptId)){
                LogUtil.info("查询用户是否是教师");
               // JSONObject a = DingUtil.getEduUserInfo(Long deptId, userDTO.getDingUserId() , String access_token)

            }

            createThirdUser(tenantId, groupId, roleId, userDTO, SecurityTypeEnum.CUBE, " 魔方同步创建用户", initializeConfig);
            userUnionIdList.add(cubeUserId);
            /** 操作日志*/

            reMap = new HashMap<>(3);
            reMap.put("name", userDTO.getName());
            reMap.put("nickname", userDTO.getNickname());
            reMap.put("userId", userDTO.getUserID());
            paramList.add(reMap);
        }
        if (!CollectionUtils.isEmpty(paramList)) {
            systemLogTool.setSysLog(loginUser, LogTypeEnum.pullCubeUser.getCode(), paramList, null, taskId);
        }
    }

    private Long getCubeParentGroupId(Integer storageId, Long tenantId){

        Long parentID = 0L;
        Group dingGroup = null;
        Group dingNameGroup = null;
        List<Group> dingParentList =  groupDaoImpl.getThirdGroupParent("cube", 1L,"魔方");
        if (!CollectionUtils.isEmpty(dingParentList)){
            for (Group g : dingParentList){
                if (!ObjectUtils.isEmpty(g.getExtraField()) && g.getExtraField().equals("cube")){
                    dingGroup = g;
                    parentID = g.getGroupID();
                }else if(g.getName().equals("魔方")){
                    dingNameGroup = g;
                }
            }
        }
        if (ObjectUtils.isEmpty(dingGroup)){
            if (ObjectUtils.isEmpty(dingNameGroup)){
                Group group = new Group();
                group.setGroupID(null);
                group.setName("魔方");
                group.setSizeMax(0.0);
                group.setDingDeptId(0L);
                group.setExtraField("cube");

                GroupVo parentGroup = groupDaoImpl.getTopGroupByTenantId(tenantId);
                Integer sort = groupDaoImpl.getMaxSort(parentGroup.getGroupID());
                sort = ObjectUtils.isEmpty(sort) ? 0 : sort;

                parentID = parentGroup.getGroupID();
                groupService.addGroupAll("asyncPullCubeGroup", group, parentID, 0L, tenantId, storageId, sort + 1, parentGroup);
                parentID = group.getGroupID();
            }else {
                groupDaoImpl.updateGroupThirdTag("cube", dingNameGroup.getGroupID());
                parentID = dingNameGroup.getGroupID();
            }
        }
        return parentID;
    }
}
