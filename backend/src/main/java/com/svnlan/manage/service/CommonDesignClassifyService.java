package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dto.DesignClassifyDto;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/19 17:17
 */
public interface CommonDesignClassifyService {

    List getDesignClassifyList(DesignClassifyDto designClassifyDto, LoginUser loginUser);

    int saveDesignClassify(DesignClassifyDto designClassifyDto, LoginUser loginUser);
    int editDesignClassify(DesignClassifyDto designClassifyDto, LoginUser loginUser);
    void deleteDesignClassify(DesignClassifyDto designClassifyDto, LoginUser loginUser);
}
