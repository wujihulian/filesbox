package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.vo.ConvertToPicResultDTO;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/30 13:38
 */
public interface ImportConvertPicService {

    /**
     * @description: 发送将PPT、PDF、WORD转图片的指令
     * @param prefix
     * @param sourceID
     * @param loginUser
     * @return void
     */
    void convertToPic(String prefix, Long sourceID, LoginUser loginUser);

    /**
     * @description: 查PPT、PDF、WORD转图片的结果
     * @param prefix
     * @param sourceID
     * @param loginUser
     * @return com.svnlan.common.dto.ConvertToPicResultDTO
     */
    ConvertToPicResultDTO getConvertPicResult(String prefix, Long sourceID, LoginUser loginUser);

    List<String> checkBanWord(CommonInfoDto infoDto);
}
