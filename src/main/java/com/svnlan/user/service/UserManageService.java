package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.domain.User;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.utils.PageResult;

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
}
