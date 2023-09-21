package com.svnlan.home.service;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/9 15:16
 */
public interface UploadZipService {

    boolean zipFile(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
    boolean taskAction(CheckFileDTO checkFileDTO, Map<String, Object> resultMap);
    List<CommonSource> unZip(CheckFileDTO checkFileDTO, LoginUser loginUser);
    boolean taskActionUnZip(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
}
