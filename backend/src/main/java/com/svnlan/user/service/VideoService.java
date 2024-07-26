package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.CommonConvertDTO;
import com.svnlan.user.dto.ConvertListDTO;
import com.svnlan.utils.PageResult;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 15:00
 */
public interface VideoService {

    PageResult getConvertList(LoginUser loginUser, ConvertListDTO convertDTO);

    boolean removeConvert(String prefix, LoginUser loginUser, CommonConvertDTO convertDTO);
    boolean removeVideoFilePath(String prefix, LoginUser loginUser, CommonConvertDTO convertDTO);
}
