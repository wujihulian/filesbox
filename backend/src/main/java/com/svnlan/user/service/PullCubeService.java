package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.SystemOptionDTO;

import java.util.Map;

public interface PullCubeService {

    void pullAboutInfo(String prefix, LoginUser loginUser, SystemOptionDTO optionDTO, Map<String, Object> resultMap);
    boolean taskAction(String prefix, SystemOptionDTO optionDTO, Map<String, Object> resultMap, LoginUser loginUser);
}
