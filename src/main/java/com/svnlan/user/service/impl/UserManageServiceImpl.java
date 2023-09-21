package com.svnlan.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.*;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.impl.DingServiceImpl;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.user.dao.*;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.User;
import com.svnlan.user.domain.UserMeta;
import com.svnlan.user.domain.UserOption;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 10:47
 */
@Service
public class UserManageServiceImpl implements UserManageService {


    @Value("${user.default.headPic}")
    private String defaultAvatar;

    @Resource
    UserDao userDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    GroupDao groupDao;
    @Resource
    SystemOptionDao systemOptionDao;
    @Resource
    UserMetaDao userMetaDao;
    @Resource
    UserOptionDao userOptionDao;
    @Resource
    SourceOperateTool sourceOperateTool;

    @Override
    public PageResult getUserList(LoginUser loginUser, UserDTO userDTO) {

        Map<String, Object> map = new HashMap<>(2);
        if (!ObjectUtils.isEmpty(userDTO.getKeyword())) {
            map.put("keyword", userDTO.getKeyword().toLowerCase());
        }
        if (!ObjectUtils.isEmpty(userDTO.getStatus())) {
            map.put("status", userDTO.getStatus());
        }
        // 判断是否是顶层group
        Group group = groupDao.getTopGroup();
        if (!ObjectUtils.isEmpty(userDTO.getGroupID())
                && !group.getGroupID().equals(userDTO.getGroupID())) {
            map.put("groupID", userDTO.getGroupID());
        }
        if (!ObjectUtils.isEmpty(userDTO.getGroupLevelList())) {
            map.put("groupLevelList", userDTO.getGroupLevelList());
        }
        map.put("sortType", SortEnum.getSortType(userDTO.getSortType()));
        map.put("sortField", "u." + SortFieldEnum.getSortField(userDTO.getSortField()));
        //
        PageHelper.startPage(userDTO.getCurrentPage(), userDTO.getPageSize());
        List<UserVo> list = this.userDao.getUserListByParam(map);

        if (CollectionUtils.isEmpty(list)) {
            return new PageResult(0L, new ArrayList());
        }

        List<Long> userIdList = list.stream().map(UserVo::getUserID).collect(Collectors.toList());
        List<UserGroupVo> userGroupList = CollectionUtils.isEmpty(userIdList) ? null : userGroupDao.getUserGroupInfoList(userIdList);
        Map<Long, List<UserGroupVo>> userGroupMap = new HashMap<>(userIdList.size());
        List<UserGroupVo> ugList = null;
        if (!CollectionUtils.isEmpty(userGroupList)) {
            Set<Long> parentIdList = userGroupList.stream().filter(n -> n.getParentID() > 0).map(UserGroupVo::getParentID).collect(Collectors.toSet());
            Map<Long, String> parentMap = null;
            if (!CollectionUtils.isEmpty(parentIdList)) {
                List<Group> parentList = groupDao.getGroupInfoList(new ArrayList<>(parentIdList));
                parentMap = CollectionUtils.isEmpty(parentList) ? null : parentList.stream().collect(Collectors.toMap(Group::getGroupID, Group::getName, (v1, v2) -> v2));
            }

            for (UserGroupVo ug : userGroupList) {
                if (!ObjectUtils.isEmpty(userGroupMap) && userGroupMap.containsKey(ug.getUserID())) {
                    ugList = userGroupMap.get(ug.getUserID());
                } else {
                    ugList = new ArrayList<>();
                }
                ug.setParentName((!ObjectUtils.isEmpty(parentMap) && parentMap.containsKey(ug.getParentID())) ? parentMap.get(ug.getParentID()) : "");
                ugList.add(ug);
                userGroupMap.put(ug.getUserID(), ugList);
            }
        }

        for (UserVo user : list) {
            user.setGroupInfo(new ArrayList<>());
            if (!ObjectUtils.isEmpty(userGroupMap) && userGroupMap.containsKey(user.getUserID())) {
                user.setGroupInfo(userGroupMap.get(user.getUserID()));
            }

            if (!ObjectUtils.isEmpty(user.getAvatar())){
                user.setAvatar(FileUtil.getShowAvatarUrl(user.getAvatar(), user.getName()));
            }
        }

        PageInfo<UserVo> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setList(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Resource
    private JWTTool jwtTool;

    /**
     * 添加三方登录的用户
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addThirdUser(String prefix, UserDTO userDTO) {
        User user = changeUserParam(userDTO);
        // 3 表示普通用户角色
        user.setRoleID(userDTO.getRoleID());
        // 设置头像
        user.setAvatar(userDTO.getAvatar());
        // 状态为 启用状态
        user.setStatus(1);
        addUser(prefix, user, userDTO);
        doUserAdditional(prefix, user, userDTO);

        return user.getUserID();
    }

    @Override
    public void editUser(String prefix, LoginUser loginUser, UserDTO userDTO) {

        /** 校验参数 */
        checkUserParam(userDTO);

        /** 转换数据*/
        User user = changeUserParam(userDTO);

        /** 添加用户 */
        if (ObjectUtils.isEmpty(userDTO.getUserID()) || userDTO.getUserID() <= 0) {
            addUser(prefix, user, userDTO);
        } else {
            updateUser(prefix, user, userDTO);

            /** 群组 先删除后添加*/
            userGroupDao.delByUserID(user.getUserID());
            // user meta 先删除后添加
            userMetaDao.delMetaByUserID(user.getUserID(), UserMetaEnum.delKeyList());

        }

        doUserAdditional(prefix, user, userDTO);
    }

    @Override
    public void doUserAdditional(String prefix, User user, UserDTO userDTO) {
        if (!ObjectUtils.isEmpty(user.getUserID()) && !CollectionUtils.isEmpty(userDTO.getGroupInfo())) {
            int sort = 1;
            List<UserGroupVo> ugList = userDTO.getGroupInfo();
            for (UserGroupVo ugVo : ugList) {
                ugVo.setUserID(user.getUserID());
                ugVo.setSort(sort);
                sort++;
            }
            try {
                userGroupDao.batchInsert(ugList);
            } catch (Exception e) {
                LogUtil.error(e, prefix + " batchInsert error " + JsonUtils.beanToJson(userDTO));
            }

            if (!ObjectUtils.isEmpty(user.getNickname())) {
                // 删除原有的meta
                List<UserMeta> paramList = new ArrayList<>();
                paramList.add(new UserMeta(user.getUserID(), UserMetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(user.getNickname())));
                paramList.add(new UserMeta(user.getUserID(), UserMetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(user.getNickname())));

                try {
                    userMetaDao.batchInsert(paramList);
                } catch (Exception e) {
                    LogUtil.error(e, prefix + " userMeta batchInsert error " + JsonUtils.beanToJson(userDTO));
                }
            }
        }
    }

    public void updateUser(String prefix, User user, UserDTO userDTO) {

        try {
            user.setNickname(ObjectUtils.isEmpty(user.getNickname()) ? "" : user.getNickname());
            userDao.update(user);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " updateUser error " + JsonUtils.beanToJson(userDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }

    @Override
    public void addUser(String prefix, User user, UserDTO userDTO) {
        try {
            userDao.insert(user);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " addUser error " + JsonUtils.beanToJson(userDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }

        // 添加默认配置
        if (!ObjectUtils.isEmpty(user.getUserID()) && user.getUserID() > 0) {

            userDTO.setCreateUser(ObjectUtils.isEmpty(userDTO.getCreateUser()) ? user.getUserID() : userDTO.getCreateUser());
            addDefaultSetting(user.getUserID());
            // 默认创建目录
            String folders = systemOptionDao.getSystemConfigByKey("newUserFolder");
            // 创建用户默认source
            sourceOperateTool.setUserDefaultSource(user, userDTO.getCreateUser(), folders);

        }
    }

    private void addDefaultSetting(Long userID) {
        // 添加默认配置
        List<UserOption> paramList = UserOptionEnum.setDefaultOptionList(userID);
        try {
            userOptionDao.batchInsert(paramList);
        } catch (Exception e) {
            LogUtil.error(e, " addDefaultSetting batchInsert error userID=" + userID);
        }
    }

    /**
     * 转换数据
     */
    private User changeUserParam(UserDTO userDTO) {
        User user = new User();
        user.setUserID(!ObjectUtils.isEmpty(userDTO.getUserID()) ? userDTO.getUserID() : null);
        user.setName(userDTO.getName());
        user.setRoleID(userDTO.getRoleID());
        if (!ObjectUtils.isEmpty(userDTO.getPassword())) {
            user.setPassword(PasswordUtil.passwordEncode(userDTO.getPassword(), GlobalConfig.PWD_SALT));
        }
        user.setNickname(!ObjectUtils.isEmpty(userDTO.getNickname()) ? userDTO.getNickname() : "");
        user.setPhone(!ObjectUtils.isEmpty(userDTO.getPhone()) ? userDTO.getPhone() : "");
        user.setEmail(!ObjectUtils.isEmpty(userDTO.getEmail()) ? userDTO.getEmail() : "");
        user.setSizeMax(!ObjectUtils.isEmpty(userDTO.getSizeMax()) ? userDTO.getSizeMax() : 2);
        user.setSizeMax(user.getSizeMax() < 0 ? 2 : user.getSizeMax());
        user.setSex(!ObjectUtils.isEmpty(userDTO.getSex()) ? userDTO.getSex() : 1);

        if (Objects.nonNull(userDTO.getOpenIdType())) {
            // openId
            if (SecurityTypeEnum.DING_DING.getCode().equals(userDTO.getOpenIdType().toString())) {
                user.setDingOpenId(userDTO.getOpenId());
            } else if (SecurityTypeEnum.WECHAT.getCode().equals(userDTO.getOpenIdType().toString())) {
                user.setWechatOpenId(userDTO.getOpenId());
            } else if (SecurityTypeEnum.ALIPAY.getCode().equals(userDTO.getOpenIdType().toString())) {
                user.setAlipayOpenId(userDTO.getOpenId());
            }else if (SecurityTypeEnum.EN_WECHAT.getCode().equals(userDTO.getOpenIdType().toString())) {
                user.setEnWechatOpenId(userDTO.getOpenId());
            }
        }

        return user;
    }

    /**
     * 校验参数
     */
    private void checkUserParam(UserDTO userDTO) {
        if (ObjectUtils.isEmpty(userDTO.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(userDTO.getName()) && userDTO.getName().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限必选
        if (ObjectUtils.isEmpty(userDTO.getRoleID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        /** 添加时验证 */
        if (ObjectUtils.isEmpty(userDTO.getUserID()) || userDTO.getUserID() <= 0) {
            if (ObjectUtils.isEmpty(userDTO.getPassword())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.inputPwd.getCode());
            }
        }
        if (!ObjectUtils.isEmpty(userDTO.getNickname()) && userDTO.getNickname().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(userDTO.getPhone()) && userDTO.getPhone().length() > 20) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(userDTO.getEmail()) && userDTO.getEmail().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        /** 校验密码 */
        checkPasswordSecureLevel(userDTO.getPassword());

        /** 校验登录名重复*/
        if (!ObjectUtils.isEmpty(userDTO.getName())) {
            List<UserVo> nameList = userDao.findByName(userDTO.getName());
            if (!CollectionUtils.isEmpty(nameList)) {
                int size = nameList.size();
                if (ObjectUtils.isEmpty(userDTO.getUserID()) || userDTO.getUserID() <= 0) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_NAME.getCode());
                }
                if (size > 1 || !userDTO.getUserID().equals(nameList.get(0).getUserID())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.ERROR_USER_EXIST_NAME.getCode());
                }
            }
        }
    }

    /**
     * 根据安全级别 校验密码强度
     */
    public void checkPasswordSecureLevel(String password) {
        if (!ObjectUtils.isEmpty(password)) {
            String value = systemOptionDao.getSystemConfigByKey("passwordRule");
            /** none不限制 ，strong 中强，strongMore高强 不限制 /长度大于6且必须同时包含英文和数字 / 长度大于6且必须同时包含数字大写*/
            switch (value) {
                case "none":
                    break;
                case "strong":
                    if (!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,100}$")) {
                        throw new SvnlanRuntimeException(CodeMessageEnum.passwordCheckError.getCode());
                    }
                    break;
                case "strongMore":
                    // String pattern = "^.*(?=.{6,16})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*?\\(\\)]).*$";
                    // String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$";

                    if (!password.matches("^.*(?=.{6,16})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).*$")) {
                        throw new SvnlanRuntimeException(CodeMessageEnum.passwordCheckError.getCode());
                    }
                    break;
            }
        }
    }

    @Override
    public void setUserStatus(String prefix, LoginUser loginUser, UserDTO userDTO) {

        if (ObjectUtils.isEmpty(userDTO.getUserID()) && ObjectUtils.isEmpty(userDTO.getUserIDStr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(userDTO.getStatus())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(0, 1, 2).contains(userDTO.getStatus())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<Long> userIdList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(userDTO.getUserID())) {
            userIdList.add(userDTO.getUserID());
        } else {
            userIdList = Arrays.asList(userDTO.getUserIDStr().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        try {
            userDao.updateUserState(userIdList, userDTO.getStatus());
        } catch (Exception e) {
            LogUtil.error(e, prefix + " 设置用户状态失败" + JsonUtils.beanToJson(userDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }


    @Override
    public void setUserAvatar(String prefix, LoginUser loginUser, UserDTO userDTO) {
        if (ObjectUtils.isEmpty(userDTO.getUserID()) && ObjectUtils.isEmpty(userDTO.getUserIDStr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(userDTO.getAvatar()) && userDTO.getAvatar().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        List<Long> userIdList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(userDTO.getUserID())) {
            userIdList.add(userDTO.getUserID());
        } else {
            userIdList = Arrays.asList(userDTO.getUserIDStr().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        userDTO.setAvatar(ObjectUtils.isEmpty(userDTO.getAvatar()) ? "" : userDTO.getAvatar());
        try {
            userDao.updateAvatarList(userIdList, userDTO.getAvatar());
        } catch (Exception e) {
            LogUtil.error(e, prefix + " setUserAvatar error " + JsonUtils.beanToJson(userDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }
}
