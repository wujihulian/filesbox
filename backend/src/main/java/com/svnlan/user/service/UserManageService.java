package com.svnlan.user.service;

import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.PageResult;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/9 10:47
 */
public interface UserManageService {

    PageResult getUserList(LoginUser loginUser, UserDTO userDTO);
    void editUser(String prefix, LoginUser loginUser, UserDTO userDTO);
    void setUserStatus(String prefix, LoginUser loginUser, UserDTO userDTO);
    void setUserAvatar(String prefix, LoginUser loginUser, UserDTO userDTO);
    Long addThirdUser(String prefix, UserDTO userDTO);
    void addUser(String prefix, User user, UserDTO userDTO);
    void doUserAdditional(String prefix, User user, UserDTO userDTO);
    void checkPasswordSecureLevel(String password);
    void associationUser(String prefix, UserVo userVo);
    Integer getAllUserCount(Long tenantId);
    void checkTotalUsersLimit(Long tenantId);
    void checkTotalUsersLimit(Long tenantId, String taskId);
    Long checkPhoneUserIsExist(String prefix, String phone, Long tenantId, UserDTO userDTO, SecurityTypeEnum thirdNameEnum);
    Long checkDingUserIsExist(String prefix, String imUserId, Long tenantId, UserDTO userDTO, SecurityTypeEnum thirdNameEnum);
    Long userGroupRelation(String prefix, String code, String unionId, String openId, List<UserGroupVo> userGroupVoList, Long tenantId);
}
