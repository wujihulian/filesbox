package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dto.CommonInfoTypeDto;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:27
 */
public interface CommonInfoTypeService {

    List getInfoTypeList(CommonInfoTypeDto infoTypeDto, LoginUser loginUser);

    int saveInfoType(CommonInfoTypeDto infoTypeDto, LoginUser loginUser);
    int editInfoType(CommonInfoTypeDto infoTypeDto, LoginUser loginUser);
    void deleteInfoType(CommonInfoTypeDto infoTypeDto, LoginUser loginUser);
}
