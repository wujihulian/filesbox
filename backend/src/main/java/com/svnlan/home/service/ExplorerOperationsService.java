package com.svnlan.home.service;


import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.UserDTO;

import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
public interface ExplorerOperationsService {


    void initSourcePathLevel(Long sourceID);

    void countSize(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);

    void setSafePwd(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
    void checkSafePwd(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);

    JSONObject sendEmailFile(UserDTO userDTO, LoginUser loginUser);
    JSONObject sendEmailCheck(UserDTO userDTO, LoginUser loginUser);

}
