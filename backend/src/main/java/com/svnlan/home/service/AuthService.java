package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.vo.IoSourceAuthVo;
import com.svnlan.jwt.domain.LoginUser;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/11 17:16
 */
public interface AuthService {

    void setAuth(CheckFileDTO checkFileDTO, LoginUser loginUser);
    List<IoSourceAuthVo> getSourceAuth(CheckFileDTO checkFileDTO, LoginUser loginUser);
}
