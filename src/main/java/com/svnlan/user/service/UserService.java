package com.svnlan.user.service;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.LoginParamDTO;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.vo.LoginQRVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 11:10
 */
public interface UserService {

    void setUserOption(String prefix, LoginUser loginUser, UserDTO userOption);
    void setUserInfo(String prefix, LoginUser loginUser, UserDTO userOption);
    Boolean sendBindEmailCode(String email, LoginUser loginUser, HttpServletRequest request);
    Boolean checkEmailCode(UserDTO optionDTO);

    Long sumUserSpaceUsed();

    Long selectUserCount();

    String getPasswordByUserName(String username);

    JSONObject bindWechat(String code, String state, String sig, String type);

    void wechatUnbind(String type);

    LoginQRVO findLoginAuthUrl(LoginParamDTO paramDTO);
    LoginQRVO scanLogin(String prefix, LoginParamDTO paramDTO, HttpServletRequest request, HttpServletResponse response);

}
