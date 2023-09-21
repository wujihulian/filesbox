package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;

import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/28 13:54
 */
public interface FileDocConvertService {

    Map<String, Object> doc2Convert(CheckFileDTO updateFileDTO, LoginUser loginUser);
    boolean taskAction(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
}
