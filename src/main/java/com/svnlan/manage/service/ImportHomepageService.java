package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.domain.LogImportArticle;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.vo.CommonInfoVo;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public interface ImportHomepageService {

    LogImportArticle importHomepage(CommonInfoDto infoDto, LoginUser loginUser);
}
