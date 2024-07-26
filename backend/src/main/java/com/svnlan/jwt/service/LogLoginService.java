package com.svnlan.jwt.service;

import com.svnlan.jwt.domain.LogLogin;

/**
 * @Author:
 * @Description:
 */
public interface LogLoginService {

    void setLogLogin(String value);
    void setLogLogin(LogLogin logLogin);
}
