package com.svnlan.jwt.service;

import com.svnlan.jwt.dto.AutoLoginDto;

import java.util.Map;

/**
 * 认证服务层
 *
 */
public interface AuthService {

    Map<String, String> autoLogin(AutoLoginDto dto);
}
