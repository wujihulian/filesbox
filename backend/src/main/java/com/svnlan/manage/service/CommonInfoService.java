package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.domain.LogImportArticle;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.manage.vo.FirstpageInfoIdVO;
import com.svnlan.utils.PageResult;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:27
 */
public interface CommonInfoService {

    PageResult getInfoList(CommonInfoDto infoDto, LoginUser loginUser);
    Long saveCommonInfo(CommonInfoDto infoDto, LoginUser loginUser);
    Long editCommonInfo(CommonInfoDto infoDto, LoginUser loginUser);
    void deleteInfo(CommonInfoDto infoDto, LoginUser loginUser);
    CommonInfoVo getCommonInfo(CommonInfoDto infoDto, LoginUser loginUser);

    CommonInfoVo getHomepageDetail(CommonInfoDto infoDto, LoginUser loginUser);

    CommonInfoVo homePageInfoToTop(CommonInfoDto saveHomepageInfoDTO);

    void modifyPageinfoSort(LoginUser loginUser, CommonInfoDto commonInfoDto);
    List<Integer> getHomepagePageSort(CommonInfoDto dto, LoginUser loginUser);
    CommonInfoVo verifyHomepageInfo(CommonInfoDto dto, LoginUser loginUser);

    void operateLike(Long id, Integer isLike);
    List<CommonInfoVo> firstHomepageDetail(FirstpageInfoIdVO firstpageInfoIdVO);
}
