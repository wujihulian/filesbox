package com.svnlan.jwt.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.dto.*;
import com.svnlan.jwt.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author:
 * @Description:
 */
public interface JWTService {
    Map<String, String> doLogin(UserDTO userDTO, HttpServletRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser);


    Map<String, String> doLoginByService(LoginUser loginUser);

    Map<String, String> refreshToken(HttpServletRequest request);

    LoginUserVO getLoginUserApi();

    long clearToken(ClearTokenDTO clearTokenDTO);

    Map<String, Object> getAccessToken(UserDTO dto);

    Map<String, String> doThirdLogin(Long userId, HttpServletRequest request);

    /**
     * 设置用户userType 类型
     */
    void setUserType(LoginUser loginUser);
    /**
     * @description: 退出登录
     * @param prefix
     * @param token
     * @return void
     * @author
     */
    void logoutByToken(String prefix, String token);
    Map<String, String> register(UserDTO userDTO, HttpServletRequest request);

    String checkPassword(UserDTO userDTO);

}
