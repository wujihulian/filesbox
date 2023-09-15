package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/6 13:49
 */
public interface RightsService {

    Map<String, Object> getUserOptions(LoginUser loginUser, HttpServletRequest request);
}
